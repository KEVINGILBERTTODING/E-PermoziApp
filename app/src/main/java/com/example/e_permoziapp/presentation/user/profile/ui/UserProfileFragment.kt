package com.example.e_permoziapp.presentation.user.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.databinding.FragmentUserProfileBinding
import com.example.e_permoziapp.domain.usecase.profile.UpdateUserPhotoUseCase
import com.example.e_permoziapp.presentation.common.component.LogOutBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.user.home.ui.HomeActivity
import com.example.e_permoziapp.presentation.user.profile.adapter.PengajuanFragmentAdapter
import com.example.e_permoziapp.presentation.user.profile.viewmodel.UserProfileViewmodel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val viewmodel: UserProfileViewmodel by activityViewModel()
    private lateinit var logOutBottomSheet: LogOutBottomSheet
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initUi()
        initViewPager()
        onCollectEventState()
        onCollectUiState()
        getUserProfile()
        getPengajuan()
    }

    private fun init() {
        logOutBottomSheet = LogOutBottomSheet() {
            Timber.w("onclick logout")
            (activity as? HomeActivity)?.logOut()
        }
    }

    private fun initUi() {
        binding.progressBarPhoto.visibility = View.GONE
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.userProfileState.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                    is UiState.Success -> {
                        setSuccessView(state.data)
                    }
                    is UiState.Error -> {
                        setErrorView(state.message)
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.pengajuanState.collect {
                when(val state = it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> setSuccessPengajuanView(state.data)
                    is UiState.Error -> setErrorPengajuanView(state.message)
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.updatePhotoState.collect {
                when(val state = it) {
                    is UiState.Loading -> {
                        binding.progressBarPhoto.visibility = View.VISIBLE
                        binding.ivProfile.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBarPhoto.visibility = View.GONE
                        binding.ivProfile.setImageURI(state.data)
                        binding.ivProfile.visibility = View.VISIBLE
                    }
                    is UiState.Error -> {
                        binding.progressBarPhoto.visibility = View.GONE
                        binding.ivProfile.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.validateSelectedFile(uri)
            }
        }
    }

    private fun initViewPager() {
        val adapter = PengajuanFragmentAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position)  {
                0 -> "Proses"
                1 -> "Disetujui"
                2 -> "Ditolak"
                else -> ""
            }
        }.attach()
    }

    private fun onCollectEventState() {
        binding.btnLogOut.setOnClickListener {
            logOutBottomSheet.show(requireActivity().supportFragmentManager, "")
        }
        binding.btnEditProfile.setOnClickListener {
            viewmodel.userModel?.let {
                requireContext().launchActivity<EditProfileActivity>(
                    "data" to Gson().toJson(viewmodel.userModel)
                )
            }
        }
        binding.ivProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun getUserProfile() {
        viewmodel.getUserProfile()
    }

    private fun setSuccessPengajuanView(params: UserProfilePengajuanModel) {
        binding.tvTotalSuccess.text = params.dataSuccess.size.toString()
        binding.tvTotalProcess.text = params.dataProccess.size.toString()
        binding.tvTotalFailed.text = params.dataFailed.size.toString()
    }

    private fun setErrorPengajuanView(params: String) {
        Toast.makeText(requireContext(), params, Toast.LENGTH_SHORT).show()
    }

    private fun setLoadingView() {
        binding.progressBarPhoto.visibility = View.VISIBLE
    }

    private fun getPengajuan() {
        viewmodel.getPengajuan()
    }

    private fun setSuccessView(dataUser: UserModel) {
        binding.progressBarPhoto.visibility = View.GONE
        binding.tvFullname.text = dataUser.name
        binding.tvEmail.text = dataUser.email
        Glide.with(requireContext()).load(ServerInfo.IMAGE_PATH + dataUser.profilePhoto)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivProfile)
    }

    private fun setErrorView(params: String) {
        binding.progressBarPhoto.visibility = View.GONE
        Toast.makeText(requireContext(), params, Toast.LENGTH_SHORT).show()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()){
            Timber.w("show fragment")
            getUserProfile()
        }
    }

    override fun onResume() {
        super.onResume()
        getUserProfile()
    }
}