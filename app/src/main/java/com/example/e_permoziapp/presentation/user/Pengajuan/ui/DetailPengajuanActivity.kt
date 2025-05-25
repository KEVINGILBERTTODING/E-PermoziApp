package com.example.e_permoziapp.presentation.user.Pengajuan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.databinding.ActivityDetailPengajuanBinding
import com.example.e_permoziapp.presentation.common.UiState
import com.example.e_permoziapp.presentation.user.Pengajuan.adapter.PersyaratanPerizinanAdapter
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import kotlinx.coroutines.flow.collect
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
    }

    private fun getDetailPengajuan() {
        viewmodel.getDetailPengajuan(pengajuanId)
    }

    private fun initAdapter() {
        adapter = PersyaratanPerizinanAdapter(mutableListOf(), isEdit) {
            Timber.w("${it.content}")
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