package com.example.e_permoziapp.presentation.user.profile.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.databinding.ActivityEditProfileBinding
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.profile.viewmodel.EditProfileViewmodel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewmodel: EditProfileViewmodel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectUiState()
        onCollectEventState()
    }

    private fun initUi() {
        val data = viewmodel.userModel
        data?.let {
            binding.etEmail.setText(it.email)
            binding.etFullName.setText(it.name)
            binding.etNoHp.setText(it.mobileNumber)
            binding.tvFileName.text = it.ktp
        }
    }

    private fun init() {
        viewmodel.userModel = Gson().fromJson(getIntentExtraOrDefault("data", ""), UserModel::class.java)
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.updateState.collect {
                when(val state = it) {
                    is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is UiState.Success -> finish()
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
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
                        binding.tvFileName.text = ""
                        Toast.makeText(this@EditProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> binding.tvFileName.text = state.data
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

    private fun onCollectEventState() {
        binding.btnSubmit.setOnClickListener{
            viewmodel.validateForm(
                binding.etFullName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etNoHp.text.toString()
            )
        }
        binding.btnChooseImg.setOnClickListener {
            pickMedia.launch(arrayOf(
                "image/png",
            ))
        }
    }

}