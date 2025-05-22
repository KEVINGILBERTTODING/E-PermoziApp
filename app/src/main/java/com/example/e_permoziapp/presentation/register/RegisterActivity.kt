package com.example.e_permoziapp.presentation.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.databinding.ActivityRegisterBinding
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val viewmodel: RegisterViewModel by viewModel()
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        onCollectEventState()
        onCollectUiState()
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.imgSelected.collect {
                it.fileName?.let { it1 -> binding.tvFileName.text = it1 }
            }
        }
        lifecycleScope.launch {
            viewmodel.uiState.collect {
                when (it) {
                    is UiState.Success -> {
                        updateUiVisibility(false)
                        finish()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                        updateUiVisibility(false)
                    } 
                    is UiState.Loading -> {
                        updateUiVisibility(true)
                    }
                    else -> {updateUiVisibility(false)}
                }
            }
        }
    }

    private fun initUi() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.getFilename(uri)
            }
        }
    }

    private fun onCollectEventState() {
        binding.btnChooseImg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnRegister.setOnClickListener {
            viewmodel.validateRegiserForm(
                binding.etEmail.text.toString(),
                binding.etFullName.text.toString(),
                binding.etPassword.text.toString(),
                binding.etNoHp.text.toString(),
            )
        }
    }

    private fun updateUiVisibility(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}