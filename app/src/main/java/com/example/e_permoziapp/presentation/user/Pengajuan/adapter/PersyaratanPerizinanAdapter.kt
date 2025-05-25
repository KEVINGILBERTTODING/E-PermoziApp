package com.example.e_permoziapp.presentation.user.Pengajuan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.databinding.ItemPersyaratanBinding

class PersyaratanPerizinanAdapter(
    private var persyaratanList: MutableList<PersyaratanPerizinanModel>,
    private val isEdit: Boolean,
    private val onClick: (PersyaratanPerizinanModel) -> Unit
): RecyclerView.Adapter<PersyaratanPerizinanAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemPersyaratanBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersyaratanPerizinanAdapter.ViewHolder {
        val binding = ItemPersyaratanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    };

    override fun onBindViewHolder(holder: PersyaratanPerizinanAdapter.ViewHolder, position: Int) {
        val dataPersyaratan = persyaratanList[position]
        holder.binding.tvTitle.text = dataPersyaratan.name
        if (isEdit) {
            holder.binding.rlUploadFile.visibility = View.VISIBLE
            holder.binding.btnDownload.visibility = View.GONE

        }else {
            holder.binding.btnDownload.visibility = if (!dataPersyaratan.content.isNullOrEmpty()) View.VISIBLE else View.GONE
            holder.binding.rlUploadFile.visibility = View.GONE
        }

        holder.binding.btnDownload.setOnClickListener {
            onClick(dataPersyaratan)
        }
    }

    override fun getItemCount(): Int = persyaratanList.size

    fun updateData(dataList: List<PersyaratanPerizinanModel>) {
        persyaratanList.clear()
        persyaratanList.addAll(dataList)
        notifyDataSetChanged()
    }
}