package com.example.e_permoziapp.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.databinding.ActivitySplashBinding
import com.example.e_permoziapp.presentation.home.HomeActivity
import com.example.e_permoziapp.presentation.login.LoginActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewmodel: SplashViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        onCollectUiState()
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.isLoginValidate.collect  {
                if (it) {
                    launchActivity<HomeActivity>()
                }else {
                    launchActivity<LoginActivity>()
                }
                finish()
            }
        }
    }

    private fun init() {

    }

}