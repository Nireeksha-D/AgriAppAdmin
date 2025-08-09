package com.example.admin.Video

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.AdminDashboardActivity
import com.example.admin.databinding.ActivityUploadVideoBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class UploadVideoActivity : AppCompatActivity() {

    private lateinit var b: ActivityUploadVideoBinding
    private var videoUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> if (result.resultCode == RESULT_OK) previewVideo() }

    private val pickLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { videoUri = it; previewVideo() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityUploadVideoBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.fabPick.setOnClickListener { showPickDialog() }
        b.btnSave.setOnClickListener { saveToRealtimeDb() }
    }

    private fun showPickDialog() {
        val opts = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Pick Video")
            .setItems(opts) { _, which ->
                if (which == 0) {
                    val values = ContentValues().apply {
                        put(MediaStore.Video.Media.TITLE, "cam_video")
                    }
                    videoUri = contentResolver.insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values
                    )
                    cameraLauncher.launch(
                        Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                            putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                        }
                    )
                } else {
                    pickLauncher.launch("video/*")
                }
            }
            .show()
    }

    private fun previewVideo() {
        videoUri?.let { uri ->
            b.videoPreview.apply {
                setZOrderOnTop(true)
                alpha = 0f
                setVideoURI(uri)
                setMediaController(MediaController(this@UploadVideoActivity).apply {
                    setAnchorView(this@apply)
                })
                setOnPreparedListener { mp ->
                    mp.setOnInfoListener { _, what, _ ->
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            alpha = 1f
                            return@setOnInfoListener true
                        }
                        false
                    }
                    start()
                }
            }
        }
    }

    private fun uriToBase64(uri: Uri): String? = try {
        contentResolver.openInputStream(uri)?.use { input ->
            ByteArrayOutputStream().use { baos ->
                val buf = ByteArray(4096)
                var len: Int
                while (input.read(buf).also { len = it } > 0) {
                    baos.write(buf, 0, len)
                }
                android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT)
            }
        }
    } catch (e: Exception) {
        Log.e("UploadDebug", "Base64 error: ${e.message}")
        null
    }

    private fun saveToRealtimeDb() {
        val titleText = b.etTitle.text.toString().trim()
        if (titleText.isEmpty()) {
            b.etTitle.error = "Title required"
            return
        }
        val title = titleText

        val uri = videoUri ?: return Toast.makeText(this, "Pick a video", Toast.LENGTH_SHORT).show()

        val base64 = uriToBase64(uri).orEmpty()
        Log.d("UploadDebug", "BASE64 length = ${base64.length}")
        if (base64.isEmpty()) return Toast.makeText(this, "Encoding failed", Toast.LENGTH_SHORT).show()

        progressDialog = ProgressDialog(this).apply {
            setMessage("Saving…"); setCancelable(false); show()
        }

        val id = System.currentTimeMillis().toString()
        uploadVideoInChunks(id, title, base64)
    }

    private fun uploadVideoInChunks(id: String, title: String, base64: String) {
        val ref = FirebaseDatabase.getInstance().getReference("LocalVideos").child(id)

        val chunkSize = 2 * 1024 * 1024
        val chunks = mutableMapOf<String, String>()
        var idx = 0
        var start = 0
        while (start < base64.length) {
            val end = minOf(base64.length, start + chunkSize)
            val part = base64.substring(start, end)
            chunks[idx.toString()] = part
            Log.d("UploadDebug", "Chunk $idx size: ${part.length}")
            idx++; start = end
        }
        Log.d("UploadDebug", "Chunks total: ${chunks.size}")

        val data = mapOf("id" to id, "title" to title, "timestamp" to id, "chunks" to chunks)

        ref.setValue(data)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Uploaded!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
