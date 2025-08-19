package com.example.e_permoziapp.presentation.common.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_permoziapp.databinding.InfoBottomsheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet(
    private val title: String,
    private val desc: String,
    private val isShowButton: Boolean,
    private val onOkeClick: () -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: InfoBottomsheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoBottomsheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCollectEventState()
        initUi()
    }

    private fun initUi() {
        binding.tvTile.text = title
        binding.tvDesc.text = desc
        binding.btnOke.visibility = if (isShowButton) View.VISIBLE else View.GONE
    }

    private fun onCollectEventState() {
        binding.btnOke.setOnClickListener {
            onOkeClick()
            dismiss()
        }
    }
}