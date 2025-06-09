package com.example.e_permoziapp.presentation.user.Pengajuan.component

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.e_permoziapp.databinding.SuccessRegisterBottomSheetLayoutBinding
import com.example.e_permoziapp.databinding.SuccessSubmitBottomSheetLayoutBinding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SuccesSubmitBottomSheet(
    private val onClick: () -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: SuccessSubmitBottomSheetLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener { dialogInterface: DialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from<View>(bottomSheet)
                behavior.isDraggable = false
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SuccessSubmitBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCollectEventState()
    }

    private fun onCollectEventState() {
        binding.btnOke.setOnClickListener {
            onClick()
        }
    }
}