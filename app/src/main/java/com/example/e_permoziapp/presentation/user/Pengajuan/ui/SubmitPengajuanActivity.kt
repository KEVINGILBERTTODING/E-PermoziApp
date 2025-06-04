package com.example.e_permoziapp.presentation.user.Pengajuan.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.getExtraOrDefault
import com.example.e_permoziapp.databinding.ActivitySubmitPengajuanBinding
import com.example.e_permoziapp.presentation.common.UiState
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.adapter.PersyaratanPerizinanAdapter
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.SubmitPengajuanViewmodel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinApplication.Companion.init
import timber.log.Timber

class SubmitPengajuanActivity : BaseActivity() {
    private lateinit var binding : ActivitySubmitPengajuanBinding
    private lateinit var adapter: PersyaratanPerizinanAdapter
    private val viewmodel : SubmitPengajuanViewmodel by viewModel()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
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
            {

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
                        binding.btnSave.visibility = View.VISIBLE
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
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(this@SubmitPengajuanActivity, state.message, Toast.LENGTH_SHORT).show()
                        finish()
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
                        Toast.makeText(this@SubmitPengajuanActivity, "loading", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@SubmitPengajuanActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        Toast.makeText(this@SubmitPengajuanActivity, "Berhasil submit pengajuan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else -> {}
                }
            }
        }


    }

    private fun onCollectEventState() {
        binding.btnSave.setOnClickListener {
            viewmodel.validateSubmitForm()
        }

    }

    private fun initUi() {
        if (viewmodel.jenisPerizinanId <= 0) {
            Toast.makeText(this@SubmitPengajuanActivity, "Jenis perizinan tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnSave.visibility = View.GONE

    }

    private fun init() {
        val id = intent.getExtraOrDefault("id", 0)
        viewmodel.jenisPerizinanId = id
        Timber.w("id submit ${viewmodel.jenisPerizinanId}")
    }

    private fun setLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvPersyaratan.visibility = View.GONE
    }

    private fun setSuccessView() {
        binding.progressBar.visibility = View.GONE
        binding.rvPersyaratan.visibility = View.VISIBLE
    }

    private fun setErrorView() {
        binding.progressBar.visibility = View.GONE
        binding.rvPersyaratan.visibility = View.GONE
    }
}