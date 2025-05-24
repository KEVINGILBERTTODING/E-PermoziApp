package com.example.e_permoziapp.presentation.user.home

import android.os.Bundle
import com.example.e_permoziapp.databinding.ActivityHomeBinding
import com.example.e_permoziapp.presentation.main.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        onCollectEventState()
        onCollectUiState()
    }
    init {

    }

    private fun initUi() {
       binding.bottomBar.background = null
    }


    private fun onCollectUiState() {
//        TODO("Not yet implemented")
    }

    private fun onCollectEventState() {
//        TODO("Not yet implemented")
    }
}