package com.example.admin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Base64
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val category: String? = null,
    val brand: String? = null,
    val discount: Int? = null,
    val description: String? = null,
    val images: List<String> = emptyList(), // Base64-encoded strings
    val status: String = "pending",
    val timestamp: Long = 0,
    var isFavorited: Boolean = false
) : Parcelable {
    /**
     * Convert the first Base64 string to a Bitmap.
     * Returns null if the list is empty or decoding fails.
     */
    fun getFirstImageBitmap(): Bitmap? {
        images.firstOrNull()?.let { base64 ->
            return try {
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        return null
    }
}

