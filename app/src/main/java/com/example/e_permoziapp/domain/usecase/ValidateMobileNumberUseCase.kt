package com.example.e_permoziapp.domain.usecase

class ValidateMobileNumberUseCase {
    operator fun invoke(mobileNumber: String?): Result<String> {
        if (mobileNumber.isNullOrEmpty()) {
            return Result.failure(Exception("Mobile number cannot be empty"))
        }
//        if(mobileNumber.length < 13) {
//            return Result.failure(Exception("Mobile number must be 10 digits"))
//        }
        if(!mobileNumber.all { it.isDigit() }) {
            return Result.failure(Exception("Mobile number must contain only digits"))
        }
        return Result.success(mobileNumber)
    }
}