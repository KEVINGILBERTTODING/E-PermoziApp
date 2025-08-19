package com.example.e_permoziapp.presentation.user.Pengajuan.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.core.extention.getExtraOrDefault
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.databinding.ActivitySubmitPengajuanBinding
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.common.ui.PhotoViewActivity
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.adapter.PersyaratanPerizinanAdapter
import com.example.e_permoziapp.presentation.user.Pengajuan.component.SuccesSubmitBottomSheet
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.SubmitPengajuanViewmodel
import com.example.e_permoziapp.presentation.user.home.ui.HomeActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubmitPengajuanActivity : BaseActivity() {
    private lateinit var binding : ActivitySubmitPengajuanBinding
    private lateinit var adapter: PersyaratanPerizinanAdapter
    private val viewmodel : SubmitPengajuanViewmodel by viewModel()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var successSubmitBottomSheet: SuccesSubmitBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitPengajuanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        initAdapter()
        onCollectEventState()
        onCollectUiState()
        getPersyaratan()
    }

    private fun initAdapter() {
        adapter = PersyaratanPerizinanAdapter(mutableListOf(), true, viewmodel.fileSelectedModlList,
            { _ ->

            }, {
                viewmodel.currentPosId = it
                if (it.first > -1 && it.second > 0) {
                    filePickerLauncher.launch(arrayOf(
                        "application/pdf",
                        "image/png",
                        "image/jpeg"
                    ))
                }else {
                    Toast.makeText(this, "Pilih persyaratan perizinan", Toast.LENGTH_SHORT).show()
                }
            }, {
                if (it.format.isNotEmpty()) {
                    if (it.format == "application/pdf" || it.format == "pdf") {
                        if (it.isUri) {
                            FileHelper.openPdfFromLocalUri(it.uri!!, this@SubmitPengajuanActivity, it.fileName ?: "")
                        }else {
                            Timber.w("is not uri")
                            viewmodel.download(it.url, it.fileName)
                        }
                    }else {
                        launchActivity<PhotoViewActivity>(
                            "url" to it.url
                        )
                    }
                }
            })
        binding.rvPersyaratan.adapter = adapter
        binding.rvPersyaratan.layoutManager = LinearLayoutManager(this)
    }

    private fun getPersyaratan() {
        viewmodel.getPersyaratan()
    }

    private fun onCollectUiState() {

        lifecycleScope.launch {
            viewmodel.fileSelectedState.collect {
                when(val state = it) {
                    is UiState.Success -> {
                        adapter.updateFilePersyaratan(state.data, viewmodel.currentPosId.first)
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@SubmitPengajuanActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        filePickerLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                viewmodel.validateFileSelected(uri, this@SubmitPengajuanActivity)
            }
        }

        lifecycleScope.launch {
            viewmodel.getPersyaratanState.collect{
                resetAllView()
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Error -> {
                        setErrorView()
                    }
                    is UiState.Success -> {
                        adapter.updateData(state.data)
                        setSuccessView()
                    }
                    else -> {}
                }
            }
        }
        
        lifecycleScope.launch { 
            viewmodel.submitState.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        binding.progressBarSubmit.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        binding.progressBarSubmit.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        Toast.makeText(this@SubmitPengajuanActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        binding.progressBarSubmit.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        if (::successSubmitBottomSheet.isInitialized) {
                            successSubmitBottomSheet.show(supportFragmentManager, "")
                        }else {
                            Toast.makeText(this@SubmitPengajuanActivity, "Berhasil submit pengajuan", Toast.LENGTH_SHORT).show()
                            navigateToHome()
                        }

                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.downloadState.collect {
                when (val state = it) {
                    is UiState.Loading -> {
                        Toast.makeText(
                            this@SubmitPengajuanActivity,
                            "Mengunduh file...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Success -> {
                        Toast.makeText(
                            this@SubmitPengajuanActivity,
                            "Download berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        FileHelper.openFile(this@SubmitPengajuanActivity, state.data)
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@SubmitPengajuanActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }


    }

    private fun navigateToHome() {
        launchActivity<HomeActivity>(
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        finish()
    }

    private fun onCollectEventState() {
        binding.btnSubmit.setOnClickListener {
            viewmodel.validateSubmitForm()
        }
        binding.btnBack.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initUi() {
        if (viewmodel.jenisPerizinanId <= 0) {
            setEmptyView()
        }
        binding.tvPengajuanName.text = viewmodel.jenisPengajuanTitle
        resetAllView()
    }

    private fun init() {
        val id = getIntentExtraOrDefault("id", 0)
        viewmodel.jenisPerizinanId = id
        viewmodel.jenisPengajuanTitle = getIntentExtraOrDefault("title", "")
        successSubmitBottomSheet = SuccesSubmitBottomSheet() {
            navigateToHome()
        }
    }
    private fun resetAllView() {
        binding.rvPersyaratan.visibility = View.GONE
        binding.lrSkeleton.lrSkeletonPersyaratan.visibility = View.GONE
        binding.rlButtonSubmit.visibility = View.GONE
        binding.lrEmptyView.lrEmpty.visibility = View.GONE
        binding.lrErrorView.lrError.visibility = View.GONE
    }

    private fun setLoadingView() {
        binding.lrSkeleton.lrSkeletonPersyaratan.visibility = View.VISIBLE
    }

    private fun setSuccessView() {
        binding.rvPersyaratan.visibility = View.VISIBLE
        binding.rlButtonSubmit.visibility = View.VISIBLE
    }

    private fun setEmptyView() {
        binding.lrEmptyView.lrEmpty.visibility = View.VISIBLE
    }

    private fun setErrorView() {
        binding.lrErrorView.lrError.visibility = View.VISIBLE
    }
}