package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.domain.Entity.FileSelectModel

class ValidationReplyPengajuanUseCase {
    operator fun invoke(aempId: Int, userId: Int, pengajuanId: Int, role: String, balasanFile: FileSelectModel?, balasanText: String?, isApprove: Boolean) : Result<Unit> {
        if (aempId < 1) return Result.failure(Exception("User id tidak valid"))
        if (userId < 1) return Result.failure(Exception("User id tidak valid"))
        if (pengajuanId < 1) return Result.failure(Exception("Pengajuan id tidak valid"))
        if (role.isEmpty() && role != "admin" && role != "employee") return Result.failure(Exception("Role tidak valid"))
        if (isApprove) {
            if (balasanFile == null) return Result.failure(Exception("File persetujuan tidak boleh kosong"))
        }else {
            if (balasanText.isNullOrEmpty()) return Result.failure(Exception("Balasan tidak boleh kosong"))
        }
        return Result.success(Unit)
    }
}