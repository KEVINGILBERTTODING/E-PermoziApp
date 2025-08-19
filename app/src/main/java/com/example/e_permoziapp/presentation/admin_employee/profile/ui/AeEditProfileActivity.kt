package com.example.e_permoziapp.presentation.admin_employee.profile.ui

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.databinding.ActivityAeEditProfileBinding
import com.example.e_permoziapp.databinding.ActivityEditProfileBinding
import com.example.e_permoziapp.presentation.admin_employee.profile.viewmodel.AeEditProfileViewmodel
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

class AeEditProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityAeEditProfileBinding
    private val viewmodel: AeEditProfileViewmodel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAeEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectUiState()
        onCollectEventState()
    }

    private fun initUi() {
        val data = viewmodel.aeModel
        data?.let {
            binding.etEmail.setText(it.email)
            binding.etFullName.setText(it.name)
            setImage("${ServerInfo.IMAGE_PATH}${data.profilePhoto}")
        }
    }

    private fun init() {
        viewmodel.aeModel = Gson().fromJson(getIntentExtraOrDefault("data", ""), AEModel::class.java)
    }

    private fun setImage(url: String) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivProfile)
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.updateState.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Success -> {
                        Toast.makeText(this@AeEditProfileActivity, "Berhasil mengubah profil", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(this@AeEditProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.selectedFileState.collect {
                when(val state = it) {
                    is UiState.Error -> {
                        Toast.makeText(this@AeEditProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        viewmodel.fileSelectModel?.let {
                            setImage(it.uri.toString())
                        }
                    }
                    else -> {}
                }
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
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
                binding.etPassword.text.toString()
            )
        }
        binding.ivProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.lrBack.setOnClickListener { finish() }
        binding.cvEdit.setOnClickListener { binding.ivProfile.performClick() }
        binding.btnEdit.setOnClickListener { binding.ivProfile.performClick() }
    }



}