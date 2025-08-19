package com.example.e_permoziapp.presentation.admin_employee.pengajuan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.databinding.ItemPersyaratanAeBinding
import com.example.e_permoziapp.databinding.ItemPersyaratanBinding
import com.example.e_permoziapp.domain.Entity.FilePersyaratanModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import timber.log.Timber
import java.io.File

class PersyaratanPerizinanAeAdapter(
    private var persyaratanList: MutableList<PersyaratanPerizinanModel>,
    private val onClick: (PersyaratanPerizinanModel) -> Unit
): RecyclerView.Adapter<PersyaratanPerizinanAeAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemPersyaratanAeBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersyaratanPerizinanAeAdapter.ViewHolder {
        val binding = ItemPersyaratanAeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    };

    override fun onBindViewHolder(holder: PersyaratanPerizinanAeAdapter.ViewHolder, position: Int) {
        val dataPersyaratan = persyaratanList[position]
        holder.binding.tvTitle.text = dataPersyaratan.name + if (dataPersyaratan.isRequired == 1) "*" else ""
        holder.binding.btnAction.setOnClickListener {
            onClick(dataPersyaratan)
        }
        holder.binding.tvFileNotFound.visibility = if (!dataPersyaratan.content.isNullOrEmpty()) View.GONE
        else View.VISIBLE
        holder.binding.btnAction.visibility = if (!dataPersyaratan.content.isNullOrEmpty()) View.VISIBLE
        else View.GONE
    }

    override fun getItemCount(): Int = persyaratanList.size


    fun updateData(dataList: List<PersyaratanPerizinanModel>) {
        persyaratanList.clear()
        persyaratanList.addAll(dataList)
        notifyDataSetChanged()
    }
}