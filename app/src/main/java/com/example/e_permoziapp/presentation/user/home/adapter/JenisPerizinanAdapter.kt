package com.example.e_permoziapp.presentation.user.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.databinding.ItemJenisPerizinanBinding

class JenisPerizinanAdapter(
    private val jenisPerizinanList: MutableList<JenisPerizinanModel>,
    private val onClick: (JenisPerizinanModel) -> Unit
): RecyclerView.Adapter<JenisPerizinanAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemJenisPerizinanBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JenisPerizinanAdapter.ViewHolder {
        val binding = ItemJenisPerizinanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JenisPerizinanAdapter.ViewHolder, position: Int) {
        val data = jenisPerizinanList[position]
        holder.binding.tvJenisPerizinan.text = data.namaPerizinan
        holder.itemView.setOnClickListener {
            onClick(data)
        }
    }

    fun updateData(data: List<JenisPerizinanModel>) {
        jenisPerizinanList.clear()
        jenisPerizinanList.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = jenisPerizinanList.size
}