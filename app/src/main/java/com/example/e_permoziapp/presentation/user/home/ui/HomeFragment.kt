package com.example.e_permoziapp.presentation.user.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.databinding.FragmentHomeBinding
import com.example.e_permoziapp.presentation.common.component.InfoBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.manual_book.ui.ManualBookActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.ui.DetailPengajuanActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.ui.SubmitPengajuanActivity
import com.example.e_permoziapp.presentation.user.home.adapter.PengajuanAdapter
import com.example.e_permoziapp.presentation.user.home.component.JenisPerizinanPickerBottomSheet
import com.example.e_permoziapp.presentation.user.home.viewmodel.HomeViewmodel
import com.example.e_permoziapp.presentation.user.profile.viewmodel.UserProfileViewmodel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pengajuanAdapter: PengajuanAdapter
    private val viewmodel: HomeViewmodel by viewModel()
    private val userProfileViewmodel : UserProfileViewmodel by activityViewModel()
    private lateinit var jenisPerizinanPickerBottomSheet: JenisPerizinanPickerBottomSheet
    private lateinit var infoBottomSheet: InfoBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        initUi()
        initAdapter()
        onCollectUiState()
        onCollectEventState()
        getAllPengajuan()

        return binding.root
    }

    private fun init() {
        jenisPerizinanPickerBottomSheet = JenisPerizinanPickerBottomSheet {
            if (it.id > 0) {
                requireActivity().launchActivity<SubmitPengajuanActivity>(
                    "id" to it.id,
                    "title" to it.namaPerizinan
                )
            }
        }
        infoBottomSheet = InfoBottomSheet(
            title = "Informasi",
            desc = requireContext().getString(R.string.warning_message_data_not_verified),
            isShowButton = true
        ) {

        }
    }

    private fun onCollectEventState() {
        binding.swipeRefresh.setOnRefreshListener {
            getAllPengajuan()
        }
        binding.fabAdd.setOnClickListener {
            if (viewmodel.isUserDataVerified) {
                jenisPerizinanPickerBottomSheet.show(requireActivity().supportFragmentManager, "")
            }else {
                infoBottomSheet.show(requireActivity().supportFragmentManager, "")
            }
        }
        binding.cvManualBook.setOnClickListener {
            requireActivity().launchActivity<ManualBookActivity>()
        }

    }

    private fun getAllPengajuan() {
        viewmodel.getAllPengajuan()
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.uiState.collect {
                binding.swipeRefresh.isRefreshing = false
                binding.fabAdd.visibility = View.GONE
                when(val state= it) {
                    is UiState.Idle -> {
                    }
                    is UiState.Loading -> {setLoadingView()}
                    is UiState.Success -> {
                        state.data?.let {
                            pengajuanAdapter.updateData(state.data)
                        }
                        setSuccessView(state.data)
                    }
                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        lifecycleScope.launch {
            viewmodel.greetings.collect {
                binding.tvGreeting.text = it
            }
        }
        lifecycleScope.launch {
            userProfileViewmodel.userProfileState.collect {
                when(val state = it) {
                    is UiState.Success -> {
                        val dataUser = state.data
                        viewmodel.isUserDataVerified = dataUser.isVerified ?: false
                    }else -> {
                        viewmodel.isUserDataVerified = false
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        pengajuanAdapter = PengajuanAdapter(
            mutableListOf()
        ){ data, isEdit ->
            requireActivity().launchActivity<DetailPengajuanActivity>(
                "id" to  data.id,
                "is_edit" to isEdit
            )
        }
        binding.rvPengajuan.adapter = pengajuanAdapter
        binding.rvPengajuan.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initUi() {
        binding.lrEmpty.tvDesc.text = "Belum ada pengajuan perizinan dari kamu. Yuk, mulai ajukan sekarang."
        binding.lrEmpty.lrEmpty.visibility = View.GONE
        binding.lrError.lrError.visibility = View.GONE
        binding.lrSkeleton.visibility = View.GONE
    }

    private fun setLoadingView() {
        binding.lrSkeleton.visibility = View.VISIBLE
        binding.rvPengajuan.visibility = View.GONE
        binding.lrEmpty.lrEmpty.visibility = View.GONE
        binding.lrError.lrError.visibility = View.GONE
    }

    private fun setSuccessView(data: List<PengajuanModel>?) {
        binding.lrSkeleton.visibility = View.GONE
        binding.lrError.lrError.visibility = View.GONE
        if (data.isNullOrEmpty()) {
            binding.lrEmpty.lrEmpty.visibility = View.VISIBLE
        }else {
            binding.rvPengajuan.visibility = View.VISIBLE
        }
        binding.fabAdd.visibility = View.VISIBLE
    }

    private fun setErrorView() {
        binding.lrSkeleton.visibility = View.GONE
        binding.rvPengajuan.visibility = View.GONE
        binding.lrEmpty.lrEmpty.visibility = View.GONE
        binding.lrError.lrError.visibility = View.VISIBLE
        binding.fabAdd.visibility = View.VISIBLE
    }

}