package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import timber.log.Timber
import java.io.File

class ValidateSubmitFilePengajuan(
    private val validateFileUploadUseCase: ValidateFileUploadUseCase
) {
    operator fun invoke(persyaratanModelList: MutableList<PersyaratanPerizinanModel>,
                                 fileSelectModelList: List<FileSelectModel>): Result<List<FileSelectModel>>  {

        val filePersyaratanFilteredList = mutableListOf<FileSelectModel>()
        for (dataPersyaratan in persyaratanModelList) {
            if (dataPersyaratan.isRequired == 1) {
                val file = fileSelectModelList.find { it.key == dataPersyaratan.id.toString() }

                if (file != null) {
                    val validateResponse = validateFileUploadUseCase(
                        file.key,
                        file.filename,
                        file.format,
                        file.byteArray,
                        true
                    )

                    if (validateResponse.isSuccess) {
                        filePersyaratanFilteredList.add(file)
                    } else {
                        return Result.failure(
                            Exception("${dataPersyaratan.name}, ${validateResponse.exceptionOrNull()?.message}")
                        )
                    }
                } else {
                    return Result.failure(Exception("${dataPersyaratan.name}, tidak boleh kosong"))
                }
            }
        }
        return Result.success(filePersyaratanFilteredList)
    }
}