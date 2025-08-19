package com.example.e_permoziapp.presentation.admin_employee.pengajuan.component

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.balasan.model.BalasanModel
import com.example.e_permoziapp.databinding.ReplyAeBottomSheetLayoutBinding
import com.example.e_permoziapp.databinding.ReplyBottomSheetLayoutBinding
import com.example.e_permoziapp.presentation.admin_employee.pengajuan.viewmodel.DetailPengajuanAeViewmodel
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class ReplyAeBottomSheet(
): BottomSheetDialogFragment() {
    private lateinit var binding: ReplyAeBottomSheetLayoutBinding
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private val viewmodel: DetailPengajuanAeViewmodel by activityViewModel()
    private var dataBalasan: BalasanModel? = null
    private var status: String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initUi()
        onCollectUiState()
        onCollectEventState()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                true
            } else {
                false
            }
        }


        dialog.setOnShowListener { dialogInterface: DialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from<View>(bottomSheet)
                behavior.isDraggable = false
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReplyAeBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() {
        viewmodel.fileSelectedModel = null
        viewmodel.setIdleFileSelected()
        viewmodel.setUpdateStateIdle()
        viewmodel.setIdleDownloadState()
        dataBalasan = viewmodel.userPengajuanDetailModel?.dataBalasan
        status = viewmodel.userPengajuanDetailModel?.dataPengajuan?.status
    }

    private fun onCollectEventState() {
        binding.btnFileName.setOnClickListener{
            if (status == "success") {
                val fileSelected = viewmodel.fileSelectedModel
                if (fileSelected != null) {
                    FileHelper.openPdfFromLocalUri(fileSelected.uri!!, requireContext(), fileSelected.filename ?: "")
                }else {
                    dataBalasan?.let {
                        if (!it.file.isNullOrEmpty()) {
                            viewmodel.download("${ServerInfo.FILE_PATH}/balasan/${it.file}",
                                it.file, true)
                        }
                    }
                }
            }

        }

        binding.btnFilePicker.setOnClickListener {
            filePickerLauncher.launch(arrayOf(
                "application/pdf",
            ))
        }
        binding.btnSubmit.setOnClickListener {
            viewmodel.validateUpdateData(binding.etBalasan.text.toString())
        }
        binding.frameClose.btnClose.setOnClickListener { dismiss() }
    }


    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.downloadStateBalasan.collect {
                resetDownloadState()
                when(val state = it) {
                    is UiState.Loading -> {
                        Toast.makeText(requireContext(), "Mengunduh file...", Toast.LENGTH_SHORT).show()
                        setLoadingDownloadState()
                    }
                    is UiState.Success -> {
                        setIdleDownloadState()
                        Toast.makeText(requireContext(), "Berhasil download", Toast.LENGTH_SHORT).show()
                        FileHelper.openFile(requireContext(), state.data)
                    }
                    is UiState.Error -> {
                        setIdleDownloadState()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }else -> {
                        setIdleDownloadState()
                    }
                }
            }
        }

        filePickerLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                viewmodel.validateFileSelected(uri, requireContext())
            }
        }

        lifecycleScope.launch {
            viewmodel.fileSelectedState.collect {
                viewmodel.fileSelectedModel = null
                when(val state = it) {
                    is UiState.Success -> {
                        viewmodel.fileSelectedModel = state.data
                        binding.etFileName.setText(state.data.filename)
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewmodel.updateDataState.collect {
                resetSubmitState()
                when(val state = it) {
                    is UiState.Success -> {
                        dismiss()
                        viewmodel.setUpdateStateIdle()
                        binding.btnSubmit.visibility = View.VISIBLE
                        viewmodel.getDetailPengajuan(viewmodel.pengajuanId)
                        binding.frameClose.btnClose.visibility = View.VISIBLE
                    }
                    is UiState.Error -> {
                        binding.btnSubmit.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading ->  {
                        binding.frameClose.btnClose.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }else -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.VISIBLE
                        binding.frameClose.btnClose.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun resetSubmitState() {
        binding.progressBar.visibility = View.GONE
        binding.btnSubmit.visibility = View.GONE
    }

    private fun setLoadingDownloadState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun resetDownloadState() {
        binding.progressBarDownload.visibility = View.GONE
        binding.tilFilename.visibility = View.GONE
        binding.btnFileName.visibility = View.GONE
    }

    private fun setIdleDownloadState() {
        binding.btnFileName.visibility = View.VISIBLE
        binding.progressBarDownload.visibility = View.GONE
        binding.tilFilename.visibility = View.VISIBLE
    }

    private fun setStatusState(isSuccess: Boolean) {
        binding.tvTitle.text = requireContext().getString(if (isSuccess) R.string.setuju_pengajuan_perizinan else R.string.tolak_pengajuan_perizinan)
        binding.rlChooseFile.visibility = if (isSuccess) View.VISIBLE else View.GONE
        binding.tilReply.visibility = if (isSuccess) View.GONE else View.VISIBLE
    }

    private fun initUi() {
        setIdleDownloadState()
        setStatusState(viewmodel.isApproveSelectedState)
        dataBalasan?.let {
            if (status == "success") {
                binding.etFileName.setText(it.file ?: "")
            }else {
                binding.etBalasan.setText(it.balasanText ?: "")
            }
        }
    }


}