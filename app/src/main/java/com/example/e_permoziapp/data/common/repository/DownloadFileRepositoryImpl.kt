package com.example.e_permoziapp.data.common.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
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

            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )

            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4096)
                while (!channel.isClosedForRead) {
                    val bytesRead = channel.readAvailable(buffer)
                    if (bytesRead == -1) break
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
            }

            suspendCancellableCoroutine<Result<Uri>> { cont ->
                android.media.MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    null
                ) { _, uri ->
                    if (uri != null) {
                        cont.resume(Result.success(uri)) { cause, _, _ -> }
                    } else {
                        cont.resume(Result.failure(Exception("File URI is null after scan."))) { cause, _, _ -> }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}