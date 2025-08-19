package com.example.e_permoziapp.presentation.onboarding.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.databinding.ActivityOnboardingBinding
import com.example.e_permoziapp.domain.Entity.OnboardingItemModel
import com.example.e_permoziapp.presentation.onboarding.adapter.OnBoardingAdapter
import com.example.e_permoziapp.presentation.onboarding.viewmodel.OnBoardingViewmodel
import com.example.e_permoziapp.presentation.user.login.ui.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private val viewmodel: OnBoardingViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        onCollectEventState()
    }

    private fun onCollectEventState() {
        binding.tvSkip.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun initAdapter() {
        val adapter = OnBoardingAdapter(viewmodel.dataList) {
            navigateToLogin()
        }
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun navigateToLogin() {
        viewmodel.saveIsFirstTime()
        launchActivity<LoginActivity>(
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        finish()
    }
}