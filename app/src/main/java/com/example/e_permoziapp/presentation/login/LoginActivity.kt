package com.example.e_permoziapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.databinding.ActivityLoginBinding
import com.example.e_permoziapp.presentation.common.UiState
import com.example.e_permoziapp.presentation.home.HomeActivity
import com.example.e_permoziapp.presentation.register.RegisterActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val viewmodel : LoginViewmodel by viewModel()
    private lateinit var binding : ActivityLoginBinding
    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onCollectEventState()
        onCollectUiState()
    }

    private fun onCollectUiState() {
       lifecycleScope.launch {
           viewmodel.errorMessageState.collect {
               if (it.isNotEmpty()) Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT)
                   .show()
           }
       }
        lifecycleScope.launch {
            viewmodel.loginState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        launchActivity<HomeActivity>()
                        finish()
                    }
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnLogin.visibility = View.GONE
                    }
                    else  -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.visibility = View.VISIBLE
                    }
                }

            }
        }

    }

    private fun onCollectEventState() {
       binding.btnLogin.setOnClickListener {
           val email = binding.etEmail.editText?.text.toString()
           val password = binding.etPassword.editText?.text.toString()
           viewmodel.validateInput(email, password)
       }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}