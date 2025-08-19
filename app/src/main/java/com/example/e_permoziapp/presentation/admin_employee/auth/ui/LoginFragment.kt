package com.example.e_permoziapp.presentation.admin_employee.auth.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.core.extention.launchActivity
import com.example.e_permoziapp.databinding.FragmentLoginBinding
import com.example.e_permoziapp.presentation.admin_employee.auth.viewmodel.LoginAEViewmodel
import com.example.e_permoziapp.presentation.admin_employee.home.ui.HomeActivity
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginFragment : Fragment() {
    private var role: String = ""
    private lateinit var binding: FragmentLoginBinding
    private val viewmodel: LoginAEViewmodel by activityViewModel<LoginAEViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            role = it.getString("role", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        onCollectUiState()
        onCollectEventState()
    }

    private fun initUi() {
        binding.tvGreeting.text = "Halo, ${if (role == "admin") "Admin!" else "Pegawai!"}"
        resetViewState()
    }

    private fun onCollectEventState() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()
            viewmodel.formValidation(email, password, role)
        }
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.loginState.collect {
                resetViewState()
                when(val state = it) {
                    is UiState.Idle -> {setIdleView()}
                    is UiState.Success -> {
                        setSuccessView()
                        requireActivity().launchActivity<HomeActivity>(
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                        requireActivity().finish()
                        setSuccessView()
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        setErrorView()
                    }
                    is UiState.Loading -> {
                        setLoadingView()
                    }
                }
            }
        }
    }

    private fun resetViewState() {
        binding.progressBar.visibility = View.GONE
        binding.btnLogin.visibility = View.VISIBLE
    }

    private fun setSuccessView() {
        binding.btnLogin.visibility = View.VISIBLE
    }

    private fun setLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE
    }

    private fun setIdleView() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setErrorView() {
        binding.btnLogin.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(role: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString("role", role)
                }
            }
    }
}