package com.example.e_permoziapp.presentation.user.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.databinding.ItemPengajuanBinding

class PengajuanAdapter(
    private var listPengajuan: MutableList<PengajuanModel>,
    private val onClick: (PengajuanModel, isEdit: Boolean) -> Unit,
): RecyclerView.Adapter<PengajuanAdapter.PengajuanViewHolder>() {

    inner class PengajuanViewHolder(
        val binding: ItemPengajuanBinding
    ) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PengajuanAdapter.PengajuanViewHolder {
        val binding = ItemPengajuanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PengajuanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PengajuanAdapter.PengajuanViewHolder, position: Int) {
        val dataPengajuan = listPengajuan[position]
        holder.binding.tvStatus.text = dataPengajuan.status
        holder.binding.tvJenisPengajuan.text = dataPengajuan.jenisPerizinan?.namaPerizinan

        holder.binding.btnView.setOnClickListener {
            onClick(dataPengajuan, false)
        }
        holder.binding.btnEdit.setOnClickListener {
            onClick(dataPengajuan, true)
        }
    }

    override fun getItemCount(): Int = listPengajuan.size

    fun updateData(params: List<PengajuanModel>) {
        listPengajuan.clear()
        listPengajuan.addAll(params)
    }
}