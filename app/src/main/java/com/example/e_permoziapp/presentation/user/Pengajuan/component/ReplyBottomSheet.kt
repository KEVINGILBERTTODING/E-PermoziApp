package com.example.e_permoziapp.presentation.user.Pengajuan.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_permoziapp.data.balasan.model.BalasanModel
import com.example.e_permoziapp.databinding.ReplyBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReplyBottomSheet(
    private val dataBalasan: BalasanModel
): BottomSheetDialogFragment() {
    private lateinit var binding: ReplyBottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReplyBottomSheetLayoutBinding.inflate(inflater, container, false)
        binding.tvContent.text = dataBalasan.balasanText ?: "Tidak ada balasan."
        return binding.root
    }
}