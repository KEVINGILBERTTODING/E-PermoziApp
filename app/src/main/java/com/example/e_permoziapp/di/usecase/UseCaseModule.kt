package com.example.e_permoziapp.di.usecase

import com.example.e_permoziapp.domain.usecase.auth.ClearAllUserInfoUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.LoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.LogoutUseCase
import com.example.e_permoziapp.domain.usecase.auth.RegisterUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateMobileNumberUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.domain.usecase.common.DownloadFileUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.DestroyPengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanByUserIdUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanDetailUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.SubmitPengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.UpdatePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.UserProfilePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidatePengajuanDate
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidatePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidateSubmitFilePengajuan
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidateSubmitPengajuan
import com.example.e_permoziapp.domain.usecase.perizinan.GetJenisPeriziananUseCase
import com.example.e_permoziapp.domain.usecase.persyaratan.GetPersyaratanByJenisIdUseCase
import com.example.e_permoziapp.domain.usecase.profile.UpdateUserPhotoUseCase
import com.example.e_permoziapp.domain.usecase.profile.UpdateUserProfileUseCase
import com.example.e_permoziapp.domain.usecase.profile.ValidateUpdateUserProfileUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { ValidatePasswordUseCase() }
    factory { ValidateEmailUseCase() }
    factory { ValidateFileUploadUseCase() }
    factory { RegisterUseCase(get()) }
    factory { ValidateFormTextUseCase() }
    factory { ValidateMobileNumberUseCase() }
    factory { GetUserIdUseCase(get()) }
    factory { GetIsLoginUseCase(get()) }
    factory { SaveUserIdUseCase(get()) }
    factory { SaveIsLoginUseCase(get()) }
    factory { ValidateLoginUseCase(get(), get(), get()) }
    factory { ClearAllUserInfoUseCase(get()) }
    factory { SaveRoleUseCase(get()) }
    factory { GetUserDataUseCase(get())}
    factory { GetPengajuanByUserIdUseCase(get())}
    factory { LogoutUseCase(get(), get())}
    factory { GetPengajuanDetailUseCase(get())}
    factory { DownloadFileUseCase(get())}
    factory { ValidatePengajuanUseCase()}
    factory { UpdatePengajuanUseCase( get())}
    factory { GetJenisPeriziananUseCase( get())}
    factory { ValidateSubmitFilePengajuan( get())}
    factory { ValidateSubmitPengajuan()}
    factory { GetPersyaratanByJenisIdUseCase(get())}
    factory { SubmitPengajuanUseCase(get())}
    factory { ValidatePengajuanDate()}
    factory { DestroyPengajuanUseCase(get())}
    factory { UserProfilePengajuanUseCase(get())}
    factory { UpdateUserProfileUseCase(get())}
    factory { ValidateUpdateUserProfileUseCase(get(), get(), get(), get(), get())}
    factory { UpdateUserPhotoUseCase(get()) }
    factory { GetRoleUseCase(get()) }

}