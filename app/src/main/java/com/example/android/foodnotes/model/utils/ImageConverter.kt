package com.example.android.foodnotes.model.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class ImageConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun byteArrayToBitmap(byteData: ByteArray) : Bitmap? = BitmapFactory.decodeByteArray(byteData, 0, byteData.size, BitmapFactory.Options())

        @TypeConverter
        @JvmStatic
        fun bitmapToString(bitmap: Bitmap) : String? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        @TypeConverter
        @JvmStatic
        fun stringToBitmap(encodedString: String) : Bitmap? {
            val byteData = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(byteData, 0, byteData.size, BitmapFactory.Options())
        }
    }
}