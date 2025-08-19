package com.example.e_permoziapp.presentation.admin_employee.pengajuan.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
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
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.databinding.ActivityDetailPengajuanAeBinding
import com.example.e_permoziapp.databinding.ActivityDetailPengajuanBinding
import com.example.e_permoziapp.presentation.admin_employee.pengajuan.viewmodel.DetailPengajuanAeViewmodel
import com.example.e_permoziapp.presentation.common.component.InfoBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.main.ui.BaseActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import androidx.core.view.isVisible
import com.example.e_permoziapp.presentation.admin_employee.home.ui.HomeActivity
import com.example.e_permoziapp.presentation.admin_employee.pengajuan.adapter.PersyaratanPerizinanAeAdapter
import com.example.e_permoziapp.presentation.admin_employee.pengajuan.component.ReplyAeBottomSheet


class DetailPengajuanAeActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailPengajuanAeBinding
    private lateinit var adapter: PersyaratanPerizinanAeAdapter
    private val viewmodel: DetailPengajuanAeViewmodel by viewModel()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPengajuanAeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initUi()
        initAdapter()
        onCollectEventState()
        onCollectUiState()
        getDetailPengajuan()
    }

    private fun onCollectEventState() {
        binding.btnDelete.setOnClickListener {
            viewmodel.destroyPengajuan()
        }
        binding.btnLayoutProfile.setOnClickListener {
            val isVisible = binding.lrContentProfile.isVisible
            binding.icArrowProfileUp.setImageDrawable(ContextCompat.getDrawable(this,
                if (isVisible) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up))
            binding.lrContentProfile.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
        binding.btnLayoutFile.setOnClickListener {
            val isVisible = binding.lrContentFile.isVisible
            binding.icArrowFileUp.setImageDrawable(ContextCompat.getDrawable(this,
                if (isVisible) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up))
            binding.lrContentFile.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
        binding.lrBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnReject.setOnClickListener {
            viewmodel.isApproveSelectedState = false
            showReplyBottomSheet()
        }
        binding.btnApprove.setOnClickListener {
            viewmodel.isApproveSelectedState = true
            showReplyBottomSheet()
        }

        onBackPressedDispatcher.addCallback(this) {
           if (viewmodel.isSubmitReply) {
               launchActivity<HomeActivity>(
                   flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
               )
               finish()
           }else {
               finish()
           }
        }
    }

    private fun showReplyBottomSheet() {
        val prev = supportFragmentManager.findFragmentByTag("reply_bottom_sheet")
        if (prev != null) {
            supportFragmentManager.beginTransaction().remove(prev).commit()
        }
        val bottomSheet = ReplyAeBottomSheet()
        bottomSheet.show(supportFragmentManager, "reply_bottom_sheet")
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.detailPengajuanState.collect {
                resetAllViewState()
                when (val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }

                    is UiState.Success -> {
                        val dataPengajuan = state.data.dataPengajuan
                        val dataPersyaratan = state.data.dataPersyaratan
                        val dataJenisPerizinan = state.data.dataJenisPersyaratan
                        val dataBalasan = state.data.dataBalasan
                        val dataUser = state.data.dataUser
                        if (!dataPersyaratan.isNullOrEmpty()) {
                            adapter.updateData(
                                dataPersyaratan
                            )
                        }
                        setSuccessView(dataPengajuan, dataJenisPerizinan, dataBalasan, dataUser)
                    }

                    is UiState.Error -> {
                        setErrorView()
                        Toast.makeText(
                            this@DetailPengajuanAeActivity,
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
                            this@DetailPengajuanAeActivity,
                            "Mengunduh file...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Success -> {
                        Toast.makeText(
                            this@DetailPengajuanAeActivity,
                            "Download berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        FileHelper.openFile(this@DetailPengajuanAeActivity, state.data)
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@DetailPengajuanAeActivity,
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
                viewmodel.validateFileSelected(uri, this@DetailPengajuanAeActivity)
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
                            this@DetailPengajuanAeActivity,
                            "Data berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                        launchActivity<HomeActivity>(
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
                        )
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@DetailPengajuanAeActivity,
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
        adapter = PersyaratanPerizinanAeAdapter(mutableListOf()) {
            val url = "${ServerInfo.FILE_PATH_PERSYARATAN}${it.content}"
            Timber.w(url)
            it.content?.let {content ->
                viewmodel.download(url, content, false)
            }
        }
        binding.rvPersyaratan.adapter = adapter
        binding.rvPersyaratan.layoutManager = LinearLayoutManager(this)

    }

    private fun init() {
        viewmodel.pengajuanId = getIntentExtraOrDefault("id", 0)
    }

    private fun initUi(){
        if (viewmodel.pengajuanId < 1) finish()
    }

    private fun resetAllViewState() {
        binding.cvDelete.visibility = View.GONE
        binding.progressBarDelete.visibility = View.GONE
        binding.rvPersyaratan.visibility = View.GONE
        binding.frameSkeleton.visibility = View.GONE
        binding.lrEmptyView.lrEmpty.visibility = View.GONE
        binding.lrErrorView.lrError.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
        binding.cvProfile.visibility = View.GONE
        binding.cvFilePengajuan.visibility = View.GONE
        binding.lrContentProfile.visibility = View.VISIBLE
        binding.lrContentFile.visibility = View.VISIBLE
        binding.skeletonHeader.visibility = View.GONE
        binding.rlStatusTimeStamp.visibility = View.GONE
        binding.lrBottom.visibility = View.GONE
    }

    private fun setLoadingView() {
        binding.frameSkeleton.visibility = View.VISIBLE
        binding.skeletonHeader.visibility = View.VISIBLE
    }

    private fun setSuccessView(dataPengajuan: PengajuanModel?, dataJenisPerizinan: JenisPerizinanModel?,
                               dataBalasanModel: BalasanModel?, dataUser: UserModel?) {
        if (dataPengajuan != null && dataJenisPerizinan != null && dataUser != null) {
            binding.rvPersyaratan.visibility = View.VISIBLE
            setStatusColor(dataPengajuan.status)
            binding.tvTitle.text = dataJenisPerizinan.namaPerizinan
            binding.tvTimeStamp.text = dataPengajuan.createdAt.formatedDateToIndonesia()
            binding.cvDelete.visibility = View.VISIBLE
            binding.tvTitle.visibility = View.VISIBLE
            binding.rlStatusTimeStamp.visibility = View.VISIBLE
            binding.etEmail.setText(dataUser.email)
            binding.etFullName.setText(dataUser.name)
            binding.etNoHp.setText(dataUser.mobileNumber)
            binding.etNib.setText(dataUser.nib)
            binding.cvProfile.visibility = View.VISIBLE
            binding.cvFilePengajuan.visibility = View.VISIBLE
            binding.lrBottom.visibility = View.VISIBLE
            binding.etNik.setText(dataUser.nik)
            binding.etTempatLahir.setText(dataUser.tempatLahir)
            binding.etTglLahir.setText(dataUser.tglLahir)
            binding.etJenisKelamin.setText(
                dataUser.jenisKelamin?.let {
                    if (it.equals("L")) {
                        "Laki-laki"
                    }else {
                        "Perempuan"
                    }
                }
            )
            binding.etAgama.setText(dataUser.agama)
            binding.etPekerjaan.setText(dataUser.pekerjaan)
            binding.etKewarganegaraan.setText(dataUser.kewarganegaraan)
            binding.etAlamat.setText(dataUser.address)
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