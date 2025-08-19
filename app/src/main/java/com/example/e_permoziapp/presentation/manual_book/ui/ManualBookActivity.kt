package com.example.e_permoziapp.presentation.manual_book.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_permoziapp.databinding.ActivityManualBookBinding
import com.example.e_permoziapp.presentation.manual_book.adapter.ManualBookAdapter
import com.example.e_permoziapp.presentation.manual_book.viewmodel.ManualBookViewmodel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManualBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManualBookBinding
    private val viewmodel: ManualBookViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        onCollectEventState()
    }

    private fun onCollectEventState() {
        binding.btnBack.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        val adapter = ManualBookAdapter(viewmodel.dataList)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }
}