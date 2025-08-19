package com.example.e_permoziapp.presentation.user.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.databinding.ActivityHomeBinding
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.ui.SubmitPengajuanActivity
import com.example.e_permoziapp.presentation.user.home.component.JenisPerizinanPickerBottomSheet
import com.example.e_permoziapp.presentation.user.profile.ui.UserProfileFragment
import com.example.e_permoziapp.presentation.user.profile.viewmodel.UserProfileViewmodel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeFragment = HomeFragment()
    private val userProfileFragment = UserProfileFragment()
    private var activeFragment: Fragment = homeFragment
    private val viewmodel: UserProfileViewmodel by viewModel()
    private var isFromProfile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectUiState()
    }

    private fun init() {
        isFromProfile = getIntentExtraOrDefault("is_from_profile", false)
    }

    private fun initUi() {
        supportFragmentManager.beginTransaction()
            .add(binding.homeContainer.id, userProfileFragment, "PROFILE").hide(userProfileFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(binding.homeContainer.id, homeFragment, "HOME").commit()
        activeFragment = homeFragment
        if (isFromProfile) {
            binding.bottomBar.selectedItemId = R.id.menuProfile
            fragmentTransaction(userProfileFragment)
        }else {
            binding.bottomBar.selectedItemId = R.id.menuHome
            fragmentTransaction(homeFragment)
        }
    }

    private fun onCollectUiState() {
        binding.bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    fragmentTransaction(homeFragment)
                true
                }
                R.id.menuProfile -> {
                    fragmentTransaction(userProfileFragment)
                    true
                }
                else -> {false}
            }
        }

    }

    private fun fragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }
}