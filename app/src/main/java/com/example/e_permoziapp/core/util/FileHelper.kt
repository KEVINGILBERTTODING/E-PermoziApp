package com.example.e_permoziapp.core.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
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

    fun getMimeTypeFromFilename(filename: String): String? {
        val extension = filename.substringAfterLast('.', "").lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
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
            val chooser = Intent.createChooser(intent, "Buka pakai...")
            context.startActivity(chooser)
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

    fun getFileExtension(filename: String): String {
        val dotIndex = filename.lastIndexOf('.')
        return if (dotIndex != -1 && dotIndex != filename.length - 1) {
            filename.substring(dotIndex + 1).lowercase()
        } else {
            ""
        }
    }

    fun openPdfFromLocalUri(uri: Uri, context: Context, fileName: String) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.getExternalFilesDir(null), fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val chooser = Intent.createChooser(intent, "Buka PDF pakai...")
        context.startActivity(chooser)
    }

}