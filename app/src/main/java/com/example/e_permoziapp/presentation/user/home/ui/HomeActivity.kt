package com.example.e_permoziapp.presentation.user.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.databinding.ActivityHomeBinding
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.ui.SubmitPengajuanActivity
import com.example.e_permoziapp.presentation.user.home.component.JenisPerizinanPickerBottomSheet
import timber.log.Timber

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var jenisPerizinanPickerBottomSheet: JenisPerizinanPickerBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectEventState()
        onCollectUiState()
    }

    private fun init() {
        jenisPerizinanPickerBottomSheet = JenisPerizinanPickerBottomSheet {
            Timber.w("id $it")
            launchActivity<SubmitPengajuanActivity>(
                "id" to it
            )
        }
    }

    private fun initUi() {
       binding.bottomBar.background = null
        fragmentTransaction(HomeFragment())
    }

    private fun onCollectUiState() {

    }

    private fun onCollectEventState() {
        binding.fabAdd.setOnClickListener {
            jenisPerizinanPickerBottomSheet.show(supportFragmentManager, jenisPerizinanPickerBottomSheet.tag)
        }
    }
    private fun fragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
        .replace(binding.homeContainer.id, fragment)
            .commit()
    }
}