package com.example.e_permoziapp.domain.usecase.common

import com.example.e_permoziapp.core.util.FileHelper

class ValidateFileUploadUseCase {
    operator fun invoke(key: String?, fileName: String?, format: String?, byteArray: ByteArray?): Result<String> {
        if (key.isNullOrEmpty()) {
            return Result.failure(Exception("Nama file tidak valid"))
        }
        if (byteArray == null) {
            return Result.failure(Exception("File tidak ditemukan"))
        }
        if (FileHelper.isByteArraySizeValid(byteArray, 5).not()) {
            return Result.failure(Exception("Ukuran file tidak boleh lebih dari 5 MB"))
        }
        if (fileName.isNullOrEmpty()) {
            return Result.failure(Exception("Nama file tidak valid"))
        }
        if (format.isNullOrEmpty()) {
            return Result.failure(Exception("Format file tidak valid"))
        }
        return Result.success("success")
    }
}