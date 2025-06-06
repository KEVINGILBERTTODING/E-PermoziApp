package com.example.e_permoziapp.presentation.common.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_permoziapp.databinding.LogoutBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogOutBottomSheet(
    private val logOut: () -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: LogoutBottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LogoutBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCollectEventState()
    }

    private fun onCollectEventState() {
        binding.btnLogout.setOnClickListener {
            logOut()
            dismiss()
        }
    }
}