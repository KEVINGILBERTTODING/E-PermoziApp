package com.example.e_permoziapp.presentation.admin_employee.auth.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.R
import com.example.e_permoziapp.databinding.ActivityLogin2Binding
import com.example.e_permoziapp.presentation.admin_employee.auth.adapter.LoginAdapter
import com.example.e_permoziapp.presentation.admin_employee.auth.viewmodel.LoginAEViewmodel
import com.example.e_permoziapp.presentation.common.state.UiState
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding
    private lateinit var loginAdapter: LoginAdapter
    private val viewmodel: LoginAEViewmodel by viewModel<LoginAEViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectEventState()
        onCollectUistate()
    }

    private fun onCollectUistate() {
        lifecycleScope.launch {
            viewmodel.loginState.collect {
                when(it) {
                    is UiState.Loading ->  {
                        binding.viewPager.isUserInputEnabled = false
                    }else -> {
                    binding.viewPager.isUserInputEnabled = true
                    }
                }
            }
        }
    }

    private fun init() {
        initViewPager()
    }

    private fun initViewPager() {
        loginAdapter = LoginAdapter(this@LoginActivity)
        binding.viewPager.adapter = loginAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Admin"
                1 -> "Pegawai"
                else -> ""
            }
        }.attach()
    }

    private fun initUi() {
        binding.viewPager.isUserInputEnabled = true
    }

    private fun onCollectEventState() {
        binding.lrBack.btnBack.setOnClickListener { finish() }
    }
}