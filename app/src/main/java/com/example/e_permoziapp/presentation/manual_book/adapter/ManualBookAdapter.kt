package com.example.e_permoziapp.presentation.manual_book.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.R
import com.example.e_permoziapp.databinding.ItemManualBookBinding
import com.example.e_permoziapp.databinding.ItemOnboardingBinding
import com.example.e_permoziapp.domain.Entity.OnboardingItemModel

class ManualBookAdapter(
    private val dataList: List<OnboardingItemModel>
): RecyclerView.Adapter<ManualBookAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemManualBookBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManualBookAdapter.ViewHolder {
        val binding = ItemManualBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManualBookAdapter.ViewHolder, position: Int) {
        val dataOnboarding = dataList[position]
        holder.binding.ivIllustrator.setImageResource(dataOnboarding.image)
        holder.binding.tvTitle.text = dataOnboarding.title
        holder.binding.tvDesc.text = dataOnboarding.desc
    }

    override fun getItemCount(): Int = dataList.size
}