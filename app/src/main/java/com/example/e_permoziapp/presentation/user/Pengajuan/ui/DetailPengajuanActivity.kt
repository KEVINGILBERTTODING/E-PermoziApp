package com.example.e_permoziapp.presentation.user.Pengajuan.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.getIntentExtraOrDefault
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.databinding.ActivityDetailPengajuanBinding
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import com.example.e_permoziapp.presentation.user.Pengajuan.adapter.PersyaratanPerizinanAdapter
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import com.example.e_permoziapp.presentation.user.home.ui.HomeActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailPengajuanActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailPengajuanBinding
    private lateinit var adapter: PersyaratanPerizinanAdapter
    private val viewmodel: DetailPengajuanViewmodel by viewModel()
    private var isEdit = false
    private var isFromProfile = false
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
        binding.swipeRefresh.setOnRefreshListener {
            getDetailPengajuan()
        }
        binding.btnSave.setOnClickListener {
            viewmodel.validateUpdateData()
        }
        binding.btnDelete.setOnClickListener {
            viewmodel.destroyPengajuan()
        }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.detailPengajuanState.collect {
                binding.swipeRefresh.isRefreshing = false
                viewmodel.fileSelectedModlList.clear()
                binding.btnSave.visibility = View.GONE
                when (val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }

                    is UiState.Success -> {
                        val dataPersyaratan = state.data.dataPersyaratan
                        if (!dataPersyaratan.isNullOrEmpty()) {
                            val dataFiltered = dataPersyaratan.filter {
                                it.name.lowercase()
                                    .contains("ktp").not()
                            }
                            adapter.updateData(
                                if (isEdit) {
                                    dataFiltered
                                } else {
                                    dataPersyaratan
                                }
                            )
                            setSuccessView()
                        } else {
                            Toast.makeText(
                                this@DetailPengajuanActivity,
                                Constant.somethingWrong,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            finish()
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
                        binding.btnSave.visibility = View.VISIBLE
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
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            "Mengupdate data...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Success -> {
                        Toast.makeText(
                            this@DetailPengajuanActivity,
                            "Update berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
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
            viewmodel.destroyState.collect {
                when (val state = it) {
                    is UiState.Loading -> {
                        Toast.makeText(this@DetailPengajuanActivity, "Loading", Toast.LENGTH_SHORT)
                            .show()
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
                viewmodel.download(url, content)
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
        })
        binding.rvPersyaratan.adapter = adapter
        binding.rvPersyaratan.layoutManager = LinearLayoutManager(this)
    }

    private fun init() {
        viewmodel.pengajuanId = getIntentExtraOrDefault("id", 0)
        isEdit = getIntentExtraOrDefault("is_edit", false)
        isFromProfile = getIntentExtraOrDefault("is_from_profile", false)
    }

    private fun initUi(){
        if (viewmodel.pengajuanId < 1) finish()
        binding.lrBottom.visibility = if (isEdit) View.VISIBLE else View.GONE
        binding.btnSave.visibility = View.GONE
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