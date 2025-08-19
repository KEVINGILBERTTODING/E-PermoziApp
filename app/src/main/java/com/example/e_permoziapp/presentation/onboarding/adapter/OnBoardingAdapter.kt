package com.example.e_permoziapp.presentation.onboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.R
import com.example.e_permoziapp.databinding.ItemOnboardingBinding
import com.example.e_permoziapp.domain.Entity.OnboardingItemModel

class OnBoardingAdapter(
    private val dataList: List<OnboardingItemModel>,
    private val onClick: () -> Unit
): RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemOnboardingBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnBoardingAdapter.ViewHolder {
        val binding = ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingAdapter.ViewHolder, position: Int) {
        val dataOnboarding = dataList[position]
        holder.binding.ivIllustrator.setImageResource(dataOnboarding.image)
        holder.binding.tvTitle.text = dataOnboarding.title
        holder.binding.tvDesc.text = dataOnboarding.desc
        if (position == dataList.size - 1) {
            holder.binding.btnGetStarted.setOnClickListener { onClick() }
            holder.binding.btnGetStarted.visibility = View.VISIBLE
        }else {
            holder.binding.btnGetStarted.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int = dataList.size
}