package com.example.e_permoziapp.core.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import kotlin.Exception

object FileHelper {
    fun getFileNameFromUri(uri: Uri?, context: Context): String {
       return try {
           uri?.let {
               context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                   val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                   if (cursor.moveToFirst() && nameIndex != -1) {
                       return cursor.getString(nameIndex)
                   }
               }
           }
           ""
       }catch (e: Exception) {
           ""
       }
    }

    fun getMimeTypeFromUri(context: Context, uri: Uri?): String? {
        return if (uri?.scheme == ContentResolver.SCHEME_CONTENT) {
            context.contentResolver.getType(uri)
        } else {
            val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            if (extension != null) {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            } else {
                null
            }
        }
    }

    fun isByteArraySizeValid(byteArray: ByteArray, maxSizeMB: Int): Boolean {
        val maxSizeBytes = maxSizeMB * 1024 * 1024
        return byteArray.size <= maxSizeBytes
    }

}