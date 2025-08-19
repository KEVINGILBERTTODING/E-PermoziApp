package com.example.e_permoziapp.data.common.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.domain.repository.DownloadFileRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class DownloadFileRepositoryImpl(
    private val httpClient: HttpClient,
    private val context: Context
): DownloadFileRepository {
    override suspend fun downloadFile(url: String, filename: String): Result<Uri> = withContext(
        Dispatchers.IO) {
        try {
            val response = httpClient.get(url)
            val channel: ByteReadChannel = response.body()

            val mimeType = FileHelper.getMimeTypeFromFilename(filename) ?: "application/octet-stream"

            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, filename)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Files.getContentUri("external")
            }

            val uri = resolver.insert(collection, contentValues)
                ?: return@withContext Result.failure(Exception("Failed to insert into MediaStore"))

            resolver.openOutputStream(uri)?.use { outputStream ->
                val buffer = ByteArray(4096)
                while (!channel.isClosedForRead) {
                    val bytesRead = channel.readAvailable(buffer)
                    if (bytesRead == -1) break
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
            } ?: return@withContext Result.failure(Exception("Failed to open output stream."))
            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            Result.success(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}