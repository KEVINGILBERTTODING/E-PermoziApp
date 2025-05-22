package com.example.e_permoziapp.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.presentation.login.LoginActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseActivity : AppCompatActivity() {
    private val baseViewmodel: BaseViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            baseViewmodel.isLogout.collect {
                if (it){
                    logOut()
                }
            }
        }
    }

    fun logOut() {
        baseViewmodel.logOut()
        launchActivity<LoginActivity>()
        finish()
    }


}