package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateMobileNumberUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase

class ValidateUpdateUserProfileUseCase(
    private val validateFormTextUseCase: ValidateFormTextUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val validateMobileNumberUseCase: ValidateMobileNumberUseCase,
    private val validateNibUseCase: ValidateNibUseCase
) {
    operator fun invoke(name: String?, email: String?, password: String?, mobileNumber: String?, ktp: FileSelectModel?, nib: String?, npwp: FileSelectModel?,
                        nik: String?, placeOfBirth: String?, dateOfBirth: String?, gender: String?, religion: String?,
                        job: String?, region: String?, address: String?): Result<Unit> {
        val validateName = validateFormTextUseCase(name)
        val validateEmail = validateEmailUseCase(email)
        val validateNib = validateNibUseCase(nib)
        val validateMobileNumber = validateMobileNumberUseCase(mobileNumber)
        val validateKtp = validateFileUploadUseCase(ktp?.key, ktp?.filename, ktp?.format, ktp?.byteArray, ktp != null)
        val validateNpwp = validateFileUploadUseCase(npwp?.key, npwp?.filename, npwp?.format, npwp?.byteArray, npwp != null)
        val validateNik = validateFormTextUseCase(nik)
        val validatePlaceOfBirth = validateFormTextUseCase(placeOfBirth)
        val validateDateOfBirth = validateFormTextUseCase(dateOfBirth)
        val validateGender = validateFormTextUseCase(gender)
        val validateReligion = validateFormTextUseCase(religion)
        val validateJob = validateFormTextUseCase(job)
        val validateRegion = validateFormTextUseCase(region)
        val validateAddress = validateFormTextUseCase(address)

        if (validateName.not()) return Result.failure(Exception("Nama lengkap tidak boleh kosong"))
        if (validateEmail.isFailure) return Result.failure(Exception(validateEmail.exceptionOrNull()?.message))
        if (validateNib.isFailure) return Result.failure(Exception(validateNib.exceptionOrNull()?.message))
        if (validateMobileNumber.isFailure) return Result.failure(Exception(validateMobileNumber.exceptionOrNull()?.message))
        if (validateKtp.isFailure) return Result.failure(Exception(validateKtp.exceptionOrNull()?.message))
        if (validateNpwp.isFailure) return Result.failure(Exception(validateNpwp.exceptionOrNull()?.message))
        if (password.isNullOrEmpty().not()) {
            val validatePassword = validatePasswordUseCase(password)
            if (validatePassword.isFailure) return Result.failure(Exception(validatePassword.exceptionOrNull()?.message))
        }
        if (validateNik.not()) return Result.failure(Exception("NIK tidak boleh kosong"))
        if (validatePlaceOfBirth.not()) return Result.failure(Exception("Tempat lahir tidak boleh kosong"))
        if (validateDateOfBirth.not()) return Result.failure(Exception("Tanggal lahir tidak boleh kosong"))
        if (validateGender.not()) return Result.failure(Exception("Jenis kelamin tidak boleh kosong"))
        if (validateReligion.not()) return Result.failure(Exception("Agama tidak boleh kosong"))
        if (validateJob.not()) return Result.failure(Exception("Pekerjaan tidak boleh kosong"))
        if (validateRegion.not()) return Result.failure(Exception("Region tidak boleh kosong"))
        if (validateAddress.not()) return Result.failure(Exception("Alamat tidak boleh kosong"))
        return Result.success(Unit)
    }
}