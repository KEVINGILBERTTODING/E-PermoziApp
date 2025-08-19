package com.example.e_permoziapp.presentation.admin_employee.home.ui

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.core.extention.toPx
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.databinding.ActivityHome2Binding
import com.example.e_permoziapp.presentation.admin_employee.home.adapter.PengajuanAEAdapter
import com.example.e_permoziapp.presentation.admin_employee.home.component.FilterBottomSheet
import com.example.e_permoziapp.presentation.admin_employee.home.viewmodel.HomeAEViewmodel
import com.example.e_permoziapp.presentation.admin_employee.pengajuan.ui.DetailPengajuanAeActivity
import com.example.e_permoziapp.presentation.admin_employee.profile.ui.AeProfileActivity
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHome2Binding
    private lateinit var adapter: PengajuanAEAdapter
    private lateinit var filterBottomSheet: FilterBottomSheet
    private val viewmodel: HomeAEViewmodel by viewModel<HomeAEViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        onCollectUiState()
        onCollectEventState()
        getPengajuan()
    }

    private fun init() {
        initAdapter()
        initFilterBottomSheet()
    }

    private fun initFilterBottomSheet() {
        filterBottomSheet = FilterBottomSheet()
    }

    private fun initAdapter() {
        adapter = PengajuanAEAdapter(mutableListOf()) { data ->
             launchActivity<DetailPengajuanAeActivity>(
                 "id" to data.id
             )
        }
        binding.rvPengajuan.adapter = adapter
        binding.rvPengajuan.layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.rvPengajuan.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if (position == state.itemCount - 1) {
                    outRect.bottom = 16.toPx()
                }
            }
        })
    }


    private fun getPengajuan() {
        viewmodel.filterValidation()
    }

    private fun initUi() {
        resetViewState()
        resetProfileState()
    }

    private fun resetProfileState() {
        binding.skeletonUsername.visibility = View.GONE
        binding.tvUsername.visibility = View.GONE
        binding.ivProfile.visibility = View.GONE
        binding.skeletonPhotoProfile.visibility = View.GONE
    }

    private fun onCollectEventState() {
        binding.swipeRefresh.setOnRefreshListener {
            getPengajuan()
        }
        binding.fabFilter.setOnClickListener { filterBottomSheet.show(supportFragmentManager, "") }
        binding.fabDownload.setOnClickListener {
            viewmodel.downloadReport()
        }
        binding.ivProfile.setOnClickListener {
            launchActivity<AeProfileActivity>()
        }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.getPengajuanState.collect {
                binding.swipeRefresh.isRefreshing = false
                resetViewState()
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Success -> {
                       setSuccessView(state.data)
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@HomeActivity, state.message, Toast.LENGTH_SHORT).show()
                        setErrorView()
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.downloadState.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        binding.fabDownload.visibility = View.GONE
                        Toast.makeText(this@HomeActivity, "Mengunduh file...", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        binding.fabDownload.visibility = View.VISIBLE
                        Toast.makeText(this@HomeActivity, "Berhasil mengunduh file", Toast.LENGTH_SHORT).show()
                        FileHelper.openFile(this@HomeActivity, state.data)
                    }
                    is UiState.Error -> {
                        binding.fabDownload.visibility = View.VISIBLE
                        Toast.makeText(this@HomeActivity, state.message, Toast.LENGTH_SHORT).show()
                        setErrorView()
                    }
                    else -> {
                        binding.fabDownload.visibility = View.VISIBLE
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.getUserState.collect {
                resetProfileState()
                when(val state = it) {
                    is UiState.Success -> {
                      setSuccessProfileView(state.data)
                    }
                    is UiState.Error -> {
                        setErrorProfileView(state.message)
                    }
                    is UiState.Loading -> {
                        setLoadingProfileView()
                    }
                    else  -> {}
                }
            }
        }
    }

    private fun setLoadingProfileView() {
        binding.skeletonUsername.visibility = View.VISIBLE
        binding.skeletonPhotoProfile.visibility = View.VISIBLE
    }

    @SuppressLint("CheckResult")
    private fun setSuccessProfileView(data: AEModel) {
        binding.tvUsername.visibility = View.VISIBLE
        binding.tvUsername.text = "${data.name}"
        Glide.with(this)
            .load("${ServerInfo.IMAGE_PATH}${data.profilePhoto}")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivProfile)
        binding.ivProfile.visibility = View.VISIBLE
    }

    private fun setErrorProfileView(errorMessage: String) {
        binding.tvUsername.visibility = View.VISIBLE
        binding.tvUsername.text = "-"
        Toast.makeText(this@HomeActivity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessView(dataList: List<PengajuanModel>?) {
        binding.lrSkeleton.visibility = View.GONE
        binding.fabFilter.visibility = View.VISIBLE
        if (::adapter.isInitialized) {
            adapter.updateData(dataList)
        }
        if (!dataList.isNullOrEmpty()) {
            binding.rvPengajuan.visibility = View.VISIBLE
            binding.fabDownload.visibility = if (viewmodel.startDate.isNotEmpty()) View.VISIBLE else View.GONE
        }else {
            binding.lrEmpty.lrEmpty.visibility = View.VISIBLE
            var emptyWording = ""
            if (viewmodel.startDate.isNotEmpty()) {
                emptyWording = "Belum ada perizinan yang masuk untuk jenis perizinan ini."
            }else {
                emptyWording = "Belum ada perizinan yang masuk."
            }
            binding.lrEmpty.tvDesc.text = emptyWording
        }
    }

    private fun setErrorView() {
        binding.lrError.lrError.visibility = View.VISIBLE
    }

    private fun resetViewState() {
        binding.lrSkeleton.visibility = View.GONE
        binding.rvPengajuan.visibility = View.GONE
        binding.lrEmpty.lrEmpty.visibility = View.GONE
        binding.lrError.lrError.visibility = View.GONE
        binding.fabDownload.visibility = View.GONE
        binding.fabFilter.visibility = View.GONE
    }

    private fun setLoadingView() {
        binding.lrSkeleton.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getProfile()
    }
}