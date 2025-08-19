package com.example.e_permoziapp.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.databinding.ItemJenisPerizinanBinding

class DropDownAdapter(
    private val dataList: List<String>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<DropDownAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemJenisPerizinanBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DropDownAdapter.ViewHolder {
        val binding = ItemJenisPerizinanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DropDownAdapter.ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.tvJenisPerizinan.text = item
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = dataList.size
}