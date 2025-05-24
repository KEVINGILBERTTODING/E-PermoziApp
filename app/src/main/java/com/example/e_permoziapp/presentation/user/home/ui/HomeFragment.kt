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
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.databinding.FragmentHomeBinding
import com.example.e_permoziapp.presentation.common.UiState
import com.example.e_permoziapp.presentation.user.home.adapter.PengajuanAdapter
import com.example.e_permoziapp.presentation.user.home.viewmodel.HomeViewmodel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pengajuanAdapter: PengajuanAdapter
    private val viewmodel: HomeViewmodel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initUi()
        initAdapter()
        onCollectUiState()
        getAllPengajuan()

        return binding.root
    }

    private fun getAllPengajuan() {
        viewmodel.getAllPengajuan()
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.uiState.collect {
                when(val state= it) {
                    is UiState.Idle -> {}
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
    }

    private fun initAdapter() {
        pengajuanAdapter = PengajuanAdapter(
            mutableListOf()
        ){
            Toast.makeText(requireActivity(), it.jenisPerizinan.namaPerizinan, Toast.LENGTH_SHORT).show()
        }
        binding.rvPengajuan.adapter = pengajuanAdapter
        binding.rvPengajuan.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initUi() {

    }

    private fun setLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvPengajuan.visibility = View.GONE
    }

    private fun setSuccessView(data: List<PengajuanModel>?) {
        binding.progressBar.visibility = View.GONE
        binding.rvPengajuan.visibility = View.VISIBLE
    }

    private fun setErrorView() {
        binding.progressBar.visibility = View.GONE
        binding.rvPengajuan.visibility = View.GONE
    }


}