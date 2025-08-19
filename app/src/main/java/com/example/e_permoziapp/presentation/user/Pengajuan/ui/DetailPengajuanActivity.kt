package com.example.e_permoziapp.presentation.user.Pengajuan.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.formatedDateToIndonesia
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.balasan.model.BalasanModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.databinding.ActivityDetailPengajuanBinding
import com.example.e_permoziapp.presentation.common.component.InfoBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.common.ui.PhotoViewActivity
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.adapter.PersyaratanPerizinanAdapter
import com.example.e_permoziapp.presentation.user.Pengajuan.component.ReplyBottomSheet
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import com.example.e_permoziapp.presentation.user.home.adapter.PengajuanAdapter.PengajuanViewHolder
import com.example.e_permoziapp.presentation.user.home.ui.HomeActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailPengajuanActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailPengajuanBinding
    private lateinit var adapter: PersyaratanPerizinanAdapter
    private lateinit var replyBottomSheet: ReplyBottomSheet
    private val viewmodel: DetailPengajuanViewmodel by viewModel()
    private var isEdit = false
    private var isFromProfile = false
    private lateinit var infoBottomSheet: InfoBottomSheet
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
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
        binding.btnSubmit.setOnClickListener {
            viewmodel.validateUpdateData()
        }
        binding.btnDelete.setOnClickListener {
            viewmodel.destroyPengajuan()
        }
        binding.btnBack.btnBack.setOnClickListener { finish() }
        binding.fabMessage.setOnClickListener {
            if (::replyBottomSheet.isInitialized) replyBottomSheet.show(supportFragmentManager, "")
        }
        binding.cvInfo.setOnClickListener { infoBottomSheet.show(supportFragmentManager, "") }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.detailPengajuanState.collect {
                resetAllViewState()
                viewmodel.fileSelectedModlList.clear()
                when (val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }

                    is UiState.Success -> {
                        val dataPengajuan = state.data.dataPengajuan
                        val dataPersyaratan = state.data.dataPersyaratan
                        val dataJenisPerizinan = state.data.dataJenisPersyaratan
                        val dataBalasan = state.data.dataBalasan
                        if (!dataPersyaratan.isNullOrEmpty()) {
                            val dataFiltered = dataPersyaratan.filter { data ->
                                !data.name.lowercase()
                                    .contains("ktp") && !data.name.lowercase().contains("npwp")
                            }
                            adapter.updateData(
                                if (isEdit) {
                                    dataFiltered
                                } else {
                                    dataPersyaratan
                                }
                            )
                            setSuccessView(dataPengajuan, dataJenisPerizinan, dataBalasan)
                        } else {
                            setSuccessView(dataPengajuan, dataJenisPerizinan, dataBalasan)
                        }
                    }

                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.downloadState.collect {
                when (val state = it) {
                    is UiState.Loading -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            "Mengunduh file...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Success -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            "Download berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        FileHelper.openFile(this@DetailPengajuanActivity, state.data)
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.fileSelectedState.collect {
                when (val state = it) {
                    is UiState.Success -> {
                        adapter.updateFilePersyaratan(state.data, viewmodel.currentPosId.first)
                        binding.rlButtonSubmit.visibility = View.VISIBLE
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }

        filePickerLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                viewmodel.validateFileSelected(uri, this@DetailPengajuanActivity)
            }
        }

        lifecycleScope.launch {
            viewmodel.updateDataState.collect {
                when (val state = it) {
                    is UiState.Loading -> {
                        binding.progressBarSubmit.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBarSubmit.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            "Update berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    is UiState.Error -> {
                        binding.progressBarSubmit.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewmodel.destroyState.collect {
                when (val state = it) {
                    is UiState.Loading -> {
                        binding.progressBarDelete.visibility = View.VISIBLE
                        binding.btnDelete.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            "Data berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                        launchActivity<HomeActivity>(
                            "is_from_profile" to  isFromProfile,
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
                        )
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBarDelete.visibility = View.GONE
                        binding.btnDelete.visibility = View.VISIBLE
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getDetailPengajuan() {
        viewmodel.getDetailPengajuan(viewmodel.pengajuanId)
    }

    private fun initAdapter() {
        adapter = PersyaratanPerizinanAdapter(mutableListOf(), isEdit, viewmodel.fileSelectedModlList,
            {
            val url = "${ServerInfo.FILE_PATH_PERSYARATAN}${it.content}"
            Timber.w(url)
            it.content?.let {content ->
                viewmodel.download(url, content, false)
            }
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
        }, {
                if (it.format.isNotEmpty()) {
                    if (it.format == "application/pdf" || it.format == "pdf") {
                        if (it.isUri) {
                            FileHelper.openPdfFromLocalUri(it.uri!!, this@DetailPengajuanActivity, it.fileName ?: "")
                        }else {
                            Timber.w("is not uri")
                            viewmodel.download(it.url, it.fileName, false)
                        }
                    }else {
                        launchActivity<PhotoViewActivity>(
                            "url" to it.url
                        )
                    }
                }
            }
        )
        binding.rvPersyaratan.adapter = adapter
        binding.rvPersyaratan.layoutManager = LinearLayoutManager(this)

        infoBottomSheet = InfoBottomSheet("Informasi", getString(R.string.pengajuan_dapat_diubah), false, {})
    }

    private fun init() {
        viewmodel.pengajuanId = getIntentExtraOrDefault("id", 0)
        isEdit = getIntentExtraOrDefault("is_edit", false)
        isFromProfile = getIntentExtraOrDefault("is_from_profile", false)
    }

    private fun initUi(){
        if (viewmodel.pengajuanId < 1) finish()
        binding.tvHeader.text =
            if (isEdit) getString(R.string.edit_pengajuan)
        else getString(R.string.detail_pengajuan)
    }

    private fun resetAllViewState() {
        binding.cvDelete.visibility = View.GONE
        binding.cvInfo.visibility = View.GONE
        binding.rlButtonSubmit.visibility = View.GONE
        binding.progressBarDelete.visibility = View.GONE
        binding.rvPersyaratan.visibility = View.GONE
        binding.frameSkeleton.visibility = View.GONE
        binding.lrEmptyView.lrEmpty.visibility = View.GONE
        binding.lrErrorView.lrError.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
        binding.skeletonHeader.visibility = View.GONE
        binding.rlStatusTimeStamp.visibility = View.GONE
        binding.fabMessage.visibility = View.GONE
    }

    private fun setLoadingView() {
        binding.frameSkeleton.visibility = View.VISIBLE
        binding.skeletonHeader.visibility = View.VISIBLE
    }

    private fun setSuccessView(dataPengajuan: PengajuanModel?, dataJenisPerizinan: JenisPerizinanModel?,
                               dataBalasanModel: BalasanModel?) {
        if (dataPengajuan != null && dataJenisPerizinan != null) {
            binding.rvPersyaratan.visibility = View.VISIBLE
            setStatusColor(dataPengajuan.status)
            binding.tvTitle.text = dataJenisPerizinan.namaPerizinan
            binding.tvTimeStamp.text = dataPengajuan.createdAt.formatedDateToIndonesia()
            binding.rlButtonSubmit.visibility = if (isEdit) View.VISIBLE else View.GONE
            binding.cvInfo.visibility = View.VISIBLE
            binding.cvDelete.visibility = if (isEdit) View.VISIBLE else View.GONE
            binding.tvTitle.visibility = View.VISIBLE
            binding.rlStatusTimeStamp.visibility = View.VISIBLE
            if (dataBalasanModel != null) {
                binding.fabMessage.visibility = View.VISIBLE
                replyBottomSheet = ReplyBottomSheet(dataBalasanModel, dataPengajuan.status)
            }
        }else {
            binding.lrEmptyView.lrEmpty.visibility = View.VISIBLE
        }

    }

    private fun setStatusColor(status: String) {
        if (status == "proccess") {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.yellow_0e))
            binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.yellow_c7))
            binding.tvStatus.text = "Proses"
        }else if (status == "success") {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.green_34))
            binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_e7))
            binding.tvStatus.text = "Disetujui"
        }else {
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.red_1b))
            binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red_e2))
            binding.tvStatus.text = "Ditolak"
        }
    }

    private fun setErrorView() {
        binding.lrErrorView.lrError.visibility = View.VISIBLE
    }
}