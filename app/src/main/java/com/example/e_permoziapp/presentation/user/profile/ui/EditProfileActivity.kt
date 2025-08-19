package com.example.e_permoziapp.presentation.user.profile.ui

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.databinding.ActivityEditProfileBinding
import com.example.e_permoziapp.presentation.common.component.DropDownBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.common.ui.PhotoViewActivity
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.profile.viewmodel.EditProfileViewmodel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewmodel: EditProfileViewmodel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<Array<String>>
    private lateinit var dialogGender: DropDownBottomSheet
    private lateinit var dialogReligion: DropDownBottomSheet
    private lateinit var dialogRegion: DropDownBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        initBottomSheet()
        onCollectUiState()
        onCollectEventState()
    }

    private fun initBottomSheet() {
        val dataGenderList = listOf("Laki-laki", "Perempuan")
        dialogGender = DropDownBottomSheet(
            title = getString(R.string.pilih_jenis_kelamin),
            dataList = dataGenderList
        ) {
            binding.etJenisKelamin.setText(it)
        }

        val religionList = listOf(
            "Islam",
            "Kristen",
            "Katolik",
            "Hindu",
            "Buddha",
            "Konghucu",
            "Lainnya"
        )
        dialogReligion = DropDownBottomSheet(
            title = getString(R.string.pilih_agama),
            dataList = religionList
        ) {
            binding.etAgama.setText(it)
        }
        val regionList = listOf(
            "WNI",
            "WNA"
        )
        dialogRegion = DropDownBottomSheet(
            title = getString(R.string.pilih_kewarganegaraan),
            dataList = regionList
        ) {
            binding.etKewarganegaraan.setText(it)
        }
    }

    private fun initUi() {
        val data = viewmodel.userModel
        data?.let {
            binding.etEmail.setText(it.email)
            binding.etFullName.setText(it.name)
            binding.etNoHp.setText(it.mobileNumber)
            binding.etFileName.setText(it.ktp)
            binding.etNib.setText(it.nib)
            binding.etFileNameNpwp.setText(it.npwp)
            binding.etNik.setText(data.nik)
            binding.etTempatLahir.setText(data.tempatLahir)
            binding.etTglLahir.setText(data.tglLahir)
            binding.etJenisKelamin.setText(
                data.jenisKelamin?.let {
                    if (it.equals("L")) {
                        "Laki-laki"
                    }else {
                        "Perempuan"
                    }
                }
            )
            binding.etAgama.setText(data.agama)
            binding.etPekerjaan.setText(data.pekerjaan)
            binding.etKewarganegaraan.setText(data.kewarganegaraan)
            binding.etAlamat.setText(data.address)
        }
    }

    private fun init() {
        viewmodel.userModel = Gson().fromJson(getIntentExtraOrDefault("data", ""), UserModel::class.java)
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.updateState.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Success -> {
                        Toast.makeText(this@EditProfileActivity, "Berhasil mengubah profil", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(this@EditProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.selectedFileState.collect {
                when(val state = it) {
                    is UiState.Error -> {
                        binding.etFileName.setText("")
                        Toast.makeText(this@EditProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> binding.etFileName.setText(state.data)
                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewmodel.selectedFileNpwpState.collect {
                when(val state = it) {
                    is UiState.Error -> {
                        binding.etFileNameNpwp.setText("")
                        Toast.makeText(this@EditProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> binding.etFileNameNpwp.setText(state.data)
                    else -> {}
                }
            }
        }


        pickMedia = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                viewmodel.validateSelectedFile(uri)
            }
        }

    }

    private fun setErrorView() {
        binding.progressBar.visibility = View.GONE
        binding.btnSubmit.visibility = View.VISIBLE
    }

    private fun setLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.GONE
    }

    private fun onCollectEventState() {
        binding.btnSubmit.setOnClickListener{
            viewmodel.validateForm(
                binding.etFullName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etNoHp.text.toString(),
                binding.etNib.text.toString(),
                binding.etNik.text.toString(),
                binding.etTempatLahir.text.toString(),
                binding.etTglLahir.text.toString(),
                binding.etJenisKelamin.text.toString(),
                binding.etAgama.text.toString(),
                binding.etPekerjaan.text.toString(),
                binding.etKewarganegaraan.text.toString(),
                binding.etAlamat.text.toString()
            )
        }
        binding.btnChooseImg.setOnClickListener {
            viewmodel.typeFileSelectedState = "ktp"
            pickMedia.launch(arrayOf(
                "image/png",
                "image/jpeg"
            ))
        }
        binding.btnChooseNpwp.setOnClickListener {
            viewmodel.typeFileSelectedState = "npwp"
            pickMedia.launch(arrayOf(
                "image/png",
                "image/jpeg"
            ))
        }
        binding.lrBack.btnBack.setOnClickListener { finish() }
        binding.btnFileName.setOnClickListener {
            val filename = binding.etFileName.text
            if (filename.isNullOrEmpty().not()) {
                if (viewmodel.fileSelectModel != null) {
                    navigateToPhotoView(viewmodel.fileSelectModel?.uri.toString())
                }else {
                    val ktpFile = "${ServerInfo.FILE_PATH_PERSYARATAN}${viewmodel.userModel?.ktp ?: ""}"
                    navigateToPhotoView(ktpFile)
                }
            }
        }
        binding.btnFileNameNpwp.setOnClickListener {
            val filename = binding.etFileNameNpwp.text
            if (filename.isNullOrEmpty().not()) {
                if (viewmodel.fileSelectNpwpModel != null) {
                    navigateToPhotoView(viewmodel.fileSelectNpwpModel?.uri.toString())
                }else {
                    val ktpFile = "${ServerInfo.FILE_PATH_PERSYARATAN}${viewmodel.userModel?.npwp ?: ""}"
                    navigateToPhotoView(ktpFile)
                }
            }
        }

        binding.etTglLahir.setOnClickListener {
            showDatePicker()
        }

        binding.etJenisKelamin.setOnClickListener {
            dialogGender.show(supportFragmentManager, "")
        }

        binding.etAgama.setOnClickListener {
            dialogReligion.show(supportFragmentManager, "")
        }

        binding.etKewarganegaraan.setOnClickListener {
            dialogRegion.show(supportFragmentManager, "")
        }
    }

    private fun navigateToPhotoView(url: String) {
        launchActivity<PhotoViewActivity>(
            "url" to url
        )
    }

    private fun showDatePicker(
    ) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            R.style.MyDatePickerDialogTheme,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateStr = formatter.format(selectedDate.time)
                binding.etTglLahir.setText(dateStr)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.setOnShowListener {
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this, R.color.main))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(this, R.color.black_33))
        }

        datePicker.show()
    }


}