package com.example.e_permoziapp.presentation.user.home.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.databinding.ItemJenisPerizinanSkeletonBinding
import com.example.e_permoziapp.databinding.JenisPerizinanPickerLayoutBinding
import com.example.e_permoziapp.presentation.common.UiState
import com.example.e_permoziapp.presentation.user.home.adapter.JenisPerizinanAdapter
import com.example.e_permoziapp.presentation.user.home.viewmodel.HomeViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class JenisPerizinanPickerBottomSheet: BottomSheetDialogFragment() {
    private val homeViewmodel: HomeViewmodel by activityViewModel()
    private lateinit var binding: JenisPerizinanPickerLayoutBinding
    private lateinit var adapter: JenisPerizinanAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JenisPerizinanPickerLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCollectUiState()
        onCollectEventState()
        init()
        getJenisPerizinan()
    }

    private fun init() {
        adapter = JenisPerizinanAdapter(mutableListOf()) {
            Timber.w(it.namaPerizinan)
        }
        binding.rvJenisPerizinan.adapter = adapter
        binding.rvJenisPerizinan.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getJenisPerizinan() {
        homeViewmodel.getJenisPerizinan()
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            homeViewmodel.jenisPerizinanUiState.collect {
                when(val state = it) {
                    is UiState.Error -> {
                        Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
                        this@JenisPerizinanPickerBottomSheet.dismiss()
                    }
                    is UiState.Success -> {
                        setSuccessView()
                        adapter.updateData(state.data)
                    }
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onCollectEventState() {

    }

    private fun setLoadingView() {
        binding.skeletonJenisPerizinan.jenisPerizinanSkeleton.visibility = View.VISIBLE
        binding.rvJenisPerizinan.visibility = View.GONE
    }

    private fun setSuccessView() {
        binding.skeletonJenisPerizinan.jenisPerizinanSkeleton.visibility = View.GONE
        binding.rvJenisPerizinan.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}