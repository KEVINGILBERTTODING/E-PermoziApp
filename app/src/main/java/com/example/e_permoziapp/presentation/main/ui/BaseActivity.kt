package com.example.e_permoziapp.presentation.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.presentation.main.viewmodel.BaseViewmodel
import com.example.e_permoziapp.presentation.user.login.ui.LoginActivity
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
            baseViewmodel.logoutTrigger.collect {
                logOut()
            }
        }

    }

    private fun logOut() {
        baseViewmodel.logOut()
        launchActivity<LoginActivity>(
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        finish()
    }


}