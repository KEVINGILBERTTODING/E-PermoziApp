package com.example.e_permoziapp.presentation.common.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_permoziapp.databinding.DropDownBottomSheetLayoutBinding
import com.example.e_permoziapp.presentation.common.adapter.DropDownAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DropDownBottomSheet(
    private val title: String,
    private val dataList: List<String>,
    private val callback: (String) -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: DropDownBottomSheetLayoutBinding
    private lateinit var adapter: DropDownAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DropDownBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initAdapter()
    }

    private fun initUi() {
        binding.tvTitle.text = title
    }

    private fun initAdapter() {
        adapter = DropDownAdapter(dataList) { item ->
            callback(item)
            dismiss()
        }
        binding.rvJenisPerizinan.adapter = adapter
        binding.rvJenisPerizinan.layoutManager = LinearLayoutManager(requireContext())
    }
}