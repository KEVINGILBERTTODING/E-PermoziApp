package com.example.e_permoziapp.presentation.user.Pengajuan.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.balasan.model.BalasanModel
import com.example.e_permoziapp.databinding.ReplyBottomSheetLayoutBinding
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class ReplyBottomSheet(
    private val dataBalasan: BalasanModel?,
    private val status: String
): BottomSheetDialogFragment() {
    private lateinit var binding: ReplyBottomSheetLayoutBinding
    private val viewmodel: DetailPengajuanViewmodel by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReplyBottomSheetLayoutBinding.inflate(inflater, container, false)
        initUi()
        onCollectUiState()
        onCollectEventState()

        return binding.root
    }

    private fun onCollectEventState() {
        binding.btnDownload.setOnClickListener{
            viewmodel.download("${ServerInfo.FILE_PATH}/balasan/${dataBalasan?.file!!}",
                dataBalasan.file, true)
        }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.downloadStateBalasan.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        binding.progressBarDownload.visibility = View.VISIBLE
                        binding.btnDownload.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        resetDownloadState()
                        Toast.makeText(requireContext(), "Berhasil download", Toast.LENGTH_SHORT).show()
                        FileHelper.openFile(requireContext(), state.data)
                    }
                    is UiState.Error -> {
                        resetDownloadState()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }else -> {}
                }
            }
        }
    }

    private fun resetDownloadState() {
        binding.progressBarDownload.visibility = View.GONE
        binding.btnDownload.visibility = View.VISIBLE
    }

    private fun initUi() {
        Timber.w("status reply bottomsheet $status")
        if (status == "success") {
            binding.lottie.setAnimation(R.raw.success_anim)
            binding.tvTitle.text = requireContext().getString(R.string.pengajuan_disetujui)
            if (dataBalasan?.file != null) binding.rlButtonDownload.visibility = View.VISIBLE
        }else {
            binding.rlButtonDownload.visibility = View.GONE
            binding.lottie.setAnimation(R.raw.error_anim)
            binding.tvTitle.text = requireContext().getString(R.string.pengajuan_ditolak)
        }
        binding.tvDesc.text = dataBalasan?.balasanText ?: "Tidak ada balasan."
    }
}