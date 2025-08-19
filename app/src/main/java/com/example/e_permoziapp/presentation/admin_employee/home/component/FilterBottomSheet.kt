package com.example.e_permoziapp.presentation.admin_employee.home.component

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.e_permoziapp.R
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.databinding.FilterBottomsheetLayoutBinding
import com.example.e_permoziapp.presentation.admin_employee.home.viewmodel.HomeAEViewmodel
import com.example.e_permoziapp.presentation.common.state.UiState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FilterBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: FilterBottomsheetLayoutBinding
    private lateinit var adapter: ArrayAdapter<JenisPerizinanModel>
    private val viewmodel: HomeAEViewmodel by activityViewModel<HomeAEViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterBottomsheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initSpinner()
        onCollectEventState()
        onCollectUiState()
    }

    private fun initUi() {
        binding.btnReset.visibility = View.GONE
    }

    private fun onCollectUiState() {
        lifecycleScope.launch {
            viewmodel.jenisPerizinanState.collect {
                adapter.clear()
                it?.let {
                    it.add(0, JenisPerizinanModel(0, "Semua"))
                    adapter.addAll(it) }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initSpinner() {
        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
    }

    private fun onCollectEventState() {
        binding.etStartDate.setOnClickListener {
            showDatePicker(requireContext(), binding.etStartDate)
        }
        binding.etStartDate.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewmodel.startDate = p0.toString()
                binding.etEndDate.setText("")
            }

        })

        binding.etEndDate.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewmodel.endDate = p0.toString()
                binding.btnReset.visibility = if (p0.toString().isNotEmpty()) View.VISIBLE else View.GONE
            }

        })
        binding.etEndDate.setOnClickListener {
            val startDateStr = binding.etStartDate.text.toString()
            if (startDateStr.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih Start Date dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate = formatter.parse(startDateStr)
            val minDate = startDate?.time ?: System.currentTimeMillis()

            showDatePicker(
                context = requireContext(),
                editText = binding.etEndDate,
                minDate = minDate,
                maxDate = System.currentTimeMillis()
            )
        }

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val jenisPerizinan = p0?.getItemAtPosition(p2) as JenisPerizinanModel
                viewmodel.idJenisPerizinan = jenisPerizinan.id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewmodel.idJenisPerizinan =  0
            }

        }
        binding.btnOke.setOnClickListener {
            val startDate = binding.etStartDate.text.toString()
            val endDate = binding.etEndDate.text.toString()
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(requireContext(), "Tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewmodel.filterValidation()
            dismiss()
        }
        binding.btnReset.setOnClickListener {
            viewmodel.idJenisPerizinan = 0
            viewmodel.startDate = ""
            viewmodel.endDate = ""
            binding.etStartDate.setText("")
            binding.etEndDate.setText("")
            binding.spinner.setSelection(0)
            viewmodel.filterValidation()
            dismiss()
        }
    }

    private fun showDatePicker(
        context: Context,
        editText: EditText,
        minDate: Long? = null,
        maxDate: Long = System.currentTimeMillis(),
        onDateSelected: ((String) -> Unit)? = null
    ) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            context,
            R.style.MyDatePickerDialogTheme,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateStr = formatter.format(selectedDate.time)
                editText.setText(dateStr)
                onDateSelected?.invoke(dateStr)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.setOnShowListener {
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(context, R.color.main))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(context, R.color.black_33))
        }

        datePicker.datePicker.maxDate = maxDate
        if (minDate != null) {
            datePicker.datePicker.minDate = minDate
        }

        datePicker.show()
    }
}