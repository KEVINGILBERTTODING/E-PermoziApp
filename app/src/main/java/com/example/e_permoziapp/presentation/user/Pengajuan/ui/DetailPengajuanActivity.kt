package com.example.e_permoziapp.presentation.user.Pengajuan.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.databinding.ActivityDetailPengajuanBinding
import com.example.e_permoziapp.presentation.common.UiState
import com.example.e_permoziapp.presentation.user.Pengajuan.adapter.PersyaratanPerizinanAdapter
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailPengajuanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPengajuanBinding
    private lateinit var adapter: PersyaratanPerizinanAdapter
    private val viewmodel: DetailPengajuanViewmodel by viewModel()
    private var pengajuanId = 0
    private var isEdit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPengajuanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        initAdapter()
        onCollectEventState()
        onCollectUiState()
        getDetailPengajuan()
    }

    private fun onCollectEventState() {
        binding.swipeRefresh.setOnRefreshListener {
            getDetailPengajuan()
        }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.detailPengajuanState.collect {
                binding.swipeRefresh.isRefreshing = false
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Success -> {
                        val dataPersyaratan = state.data.dataPersyaratan
                        if (!dataPersyaratan.isNullOrEmpty()) {
                            adapter.updateData(dataPersyaratan)
                            setSuccessView()
                        }else {
                            Toast.makeText(this@DetailPengajuanActivity, Constant.somethingWrong, Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }
                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(this@DetailPengajuanActivity, state.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.downloadState.collect {
                when(it) {
                    is UiState.Loading -> {
                        Toast.makeText(this@DetailPengajuanActivity, "Mengunduh file...", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        Toast.makeText(this@DetailPengajuanActivity, "Download berhasil", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@DetailPengajuanActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getDetailPengajuan() {
        viewmodel.getDetailPengajuan(pengajuanId)
    }

    private fun initAdapter() {
        adapter = PersyaratanPerizinanAdapter(mutableListOf(), isEdit) {
            val url = "${Constant.FILE_PATH_PERSYARATAN}${it.content}"
            Timber.w(url)
            it.content?.let {content ->
                viewmodel.download(url, content)
            }
        }
        binding.rvPersyaratan.adapter = adapter
        binding.rvPersyaratan.layoutManager = LinearLayoutManager(this)
    }

    private fun init() {
        pengajuanId = getIntentExtraOrDefault("id", 0)



    }

    private fun initUi(){
        if (pengajuanId < 1) finish()
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