package com.example.e_permoziapp.core.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.net.URI
import kotlin.Exception

object FileHelper {
    fun getFileNameFromUri(uri: Uri?, context: Context): String {
       return try {
           uri?.let {
               context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                   val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                   if (cursor.moveToFirst() && nameIndex != -1) {
                       return clearFileName(cursor.getString(nameIndex))
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

    fun openFile(context: Context, uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val mimeType = getMimeTypeFromUri(context, uri)
                ?: "*/*"
            intent.setDataAndType(uri, mimeType)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearFileName(filename: String?): String {
        if (filename.isNullOrEmpty()) return ""
        val cleaned = filename.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return cleaned.trim()
    }

}