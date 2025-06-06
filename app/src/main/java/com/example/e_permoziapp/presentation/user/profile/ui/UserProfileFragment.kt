package com.example.e_permoziapp.presentation.user.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.databinding.FragmentUserProfileBinding
import com.example.e_permoziapp.presentation.common.component.LogOutBottomSheet
import com.example.e_permoziapp.presentation.common.state.UiState
import com.example.e_permoziapp.presentation.user.home.ui.HomeActivity
import com.example.e_permoziapp.presentation.user.profile.adapter.PengajuanFragmentAdapter
import com.example.e_permoziapp.presentation.user.profile.viewmodel.UserProfileViewmodel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val viewmodel: UserProfileViewmodel by activityViewModel()
    private lateinit var logOutBottomSheet: LogOutBottomSheet

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

    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.userProfileState.collect {
                when(val state = it) {
                    is UiState.Loading -> setLoadingView()
                    is UiState.Success -> setSuccessView(state.data)
                    is UiState.Error -> setErrorView(state.message)
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.pengajuanState.collect {
                when(val state = it) {
                    is UiState.Loading -> setLoadingPengajuanView()
                    is UiState.Success -> setSuccessPengajuanView(state.data)
                    is UiState.Error -> setErrorPengajuanView(state.message)
                    else -> {}
                }
            }
        }
    }

    private fun initViewPager() {
        val adapter = PengajuanFragmentAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position)  {
                0 -> "Process"
                1 -> "Success"
                2 -> "Failed"
                else -> ""
            }
        }.attach()
    }

    private fun onCollectEventState() {
        binding.btnLogOut.setOnClickListener {
            logOutBottomSheet.show(requireActivity().supportFragmentManager, "")
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

    private fun setLoadingPengajuanView() {

    }

    private fun setLoadingView() {

    }

    private fun getPengajuan() {
        viewmodel.getPengajuan()
    }

    private fun setSuccessView(dataUser: UserModel) {
        binding.tvFullname.text = dataUser.name
        binding.tvEmail.text = dataUser.email
        Glide.with(requireContext()).load(ServerInfo.IMAGE_PATH + dataUser.profilePhoto).into(binding.ivProfile)
    }

    private fun setErrorView(params: String) {
        Toast.makeText(requireContext(), params, Toast.LENGTH_SHORT).show()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()){
            Timber.w("show fragment")
            getUserProfile()
        }
    }
}