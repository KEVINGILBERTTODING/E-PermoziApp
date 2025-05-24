package com.example.e_permoziapp.presentation.user.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.e_permoziapp.databinding.ActivityHomeBinding
import com.example.e_permoziapp.presentation.main.ui.BaseActivity

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


    private fun initUi() {
       binding.bottomBar.background = null
        fragmentTransaction(HomeFragment())
    }


    private fun onCollectUiState() {
//        TODO("Not yet implemented")
    }

    private fun onCollectEventState() {
//        TODO("Not yet implemented")
    }
    private fun fragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
        .replace(binding.homeContainer.id, fragment)
            .commit()
    }
}