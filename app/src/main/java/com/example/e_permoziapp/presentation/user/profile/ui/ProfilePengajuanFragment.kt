package com.example.e_permoziapp.presentation.user.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.databinding.FragmentProfilePengajuanBinding
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.user.Pengajuan.ui.DetailPengajuanActivity
import com.example.e_permoziapp.presentation.user.home.adapter.PengajuanAdapter
import com.example.e_permoziapp.presentation.user.profile.viewmodel.UserProfileViewmodel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfilePengajuanFragment : Fragment() {
    private lateinit var binding: FragmentProfilePengajuanBinding
    private lateinit var status: String
    private lateinit var adapter: PengajuanAdapter
    private val viewmodel: UserProfileViewmodel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        status = arguments?.getString("status") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePengajuanBinding.inflate(inflater, container, false)
        init()
        initUi()
        getDataPengajuan()
        onCollectUiState()
        onCollectEventState()
        return binding.root
    }

    private fun onCollectEventState() {
        binding.swipeRefresh.setOnRefreshListener {
            getDataPengajuan()
        }
    }

    private fun initUi() {
        resetAllStateView()
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.pengajuanState.collect {
                binding.swipeRefresh.isRefreshing = false
                when(val state = it) {
                    is UiState.Loading -> setLoadingView()
                    is UiState.Success -> (setSuccessView(state.data))
                    is UiState.Error -> (setErrorView(state.message))
                    else -> {}
                }
            }
        }
    }

    private fun init() {
        adapter = PengajuanAdapter(mutableListOf()) { data, isEdit->
            requireActivity().launchActivity<DetailPengajuanActivity>(
                "id" to  data.id,
                "is_edit" to isEdit,
                "is_from_profile" to true
            )
        }
        binding.rvPengajuan.adapter = adapter
        binding.rvPengajuan.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setLoadingView() {
        resetAllStateView()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setErrorView(params: String) {
        resetAllStateView()
        binding.lrError.visibility = View.VISIBLE
        binding.errorStateLayout.tvDesc.text = params
    }

    private fun setSuccessView(params: UserProfilePengajuanModel) {
        resetAllStateView()
        val dataPengajuanList = if (status == "process") {
            params.dataProccess
        }else if (status == "success") {
            params.dataSuccess
        }else {
            params.dataFailed
        }
        if (dataPengajuanList.isEmpty()) {
            binding.lrEmpty.visibility = View.VISIBLE
            binding.emptyStateLayout.tvDesc.text = "Tidak ada pengajuan ditemukan."
        }else {
            adapter.updateData(dataPengajuanList)
            binding.rvPengajuan.visibility = View.VISIBLE
        }
    }

    private fun getDataPengajuan() {
        viewmodel.getPengajuan()
    }

    private fun resetAllStateView() {
        binding.progressBar.visibility = View.GONE
        binding.rvPengajuan.visibility = View.GONE
        binding.lrEmpty.visibility = View.GONE
        binding.lrError.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(status: String) =
            ProfilePengajuanFragment().apply {
                arguments = Bundle().apply {
                    putString("status", status)
                }
            }
    }
}