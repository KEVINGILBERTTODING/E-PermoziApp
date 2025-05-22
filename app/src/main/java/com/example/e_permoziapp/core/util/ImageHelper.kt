package com.example.e_permoziapp.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

object ImageHelper {
    fun uriToBitmap(context: Context, uri: Uri?): ByteArray? {
        val uriClear = uri ?: return null
        var bitmap: Bitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uriClear)
            bitmap = ImageDecoder.decodeBitmap(source)
        }else {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uriClear)
        }
        return compressBitmapToByteArray(bitmap, 80)
    }

    fun compressBitmapToByteArray(bitmap: Bitmap?, quality: Int): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }
}