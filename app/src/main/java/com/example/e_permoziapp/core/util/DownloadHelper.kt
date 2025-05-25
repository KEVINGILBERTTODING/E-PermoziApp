package com.example.e_permoziapp.core.util

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.webkit.MimeTypeMap
import com.example.e_permoziapp.core.constant.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DownloadHelper(
    private val context: Context,
    private val httpClient: HttpClient
) {

    suspend fun downloadFile(url: String, fileName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = httpClient.get(url)
                val channel: ByteReadChannel = response.body()

                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
                )
                val outputStream = FileOutputStream(file)

                val buffer = ByteArray(4096)
                while (!channel.isClosedForRead) {
                    val bytesRead = channel.readAvailable(buffer)
                    if (bytesRead == -1) break
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
                outputStream.close()

                android.media.MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    null
                ) { path, uri ->
                    if (uri != null) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            val mimeType = MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url))
                                ?: "*/*"
                            intent.setDataAndType(uri, mimeType)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                            Result.success("success download file and opened")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Result.failure(Exception("Could not open file: ${e.message}"))
                        }
                    } else {
                        Result.failure(Exception("File URI is null after scan."))
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}