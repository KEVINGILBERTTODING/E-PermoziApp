package com.example.e_permoziapp.presentation.admin_employee.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.databinding.ActivityAeProfileBinding
import com.example.e_permoziapp.presentation.admin_employee.auth.ui.LoginActivity
import com.example.e_permoziapp.presentation.admin_employee.profile.viewmodel.AeProfileViewmodel
import com.example.e_permoziapp.presentation.common.component.LogOutBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.home.ui.HomeActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AeProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityAeProfileBinding
    private val viewmodel: AeProfileViewmodel by viewModel()
    private lateinit var logOutBottomSheet: LogOutBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectUiState()
        onCollectEventState()
    }

    private fun initUi() {
        resetViewState()
    }

    private fun init() {
        logOutBottomSheet = LogOutBottomSheet {
            logOut()
        }
    }

    private fun onCollectEventState() {
        binding.lrBack.setOnClickListener { finish() }
        binding.btnLogOut.setOnClickListener {
            logOutBottomSheet.show(supportFragmentManager, "")
        }
        binding.btnEdit.setOnClickListener {
            viewmodel.aeModel?.let {
                val data = Gson().toJson(it).toString()
                launchActivity<AeEditProfileActivity>(
                    "data" to data
                )
            }
        }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.profileState.collect {
                resetViewState()
                when(val state = it) {
                    is UiState.Success -> {
                        setSuccessView(state.data)
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@AeProfileActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {
                        setLoadView()
                    }
                    else  -> {}
                }
            }
        }
    }

    private fun resetViewState() {
        binding.skeletonUsername.visibility = View.GONE
        binding.skeletonEmail.visibility = View.GONE
        binding.tvEmail.visibility = View.GONE
        binding.tvUsername.visibility = View.GONE
    }

    private fun setLoadView() {
        binding.skeletonUsername.visibility = View.VISIBLE
        binding.skeletonEmail.visibility = View.VISIBLE
    }
    private fun setSuccessView(param: AEModel) {
        binding.tvUsername.text = param.name
        binding.tvEmail.text = param.email
        Glide.with(this)
            .load("${ServerInfo.IMAGE_PATH}${param.profilePhoto}")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivProfile)
        binding.tvEmail.visibility = View.VISIBLE
        binding.tvUsername.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getProfile()
    }
}