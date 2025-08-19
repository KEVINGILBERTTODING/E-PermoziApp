package com.example.e_permoziapp.presentation.admin_employee.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.extention.formatedDateToIndonesia
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.databinding.ItemPengajuanAeBinding

class PengajuanAEAdapter(
    private val pengajuanList: MutableList<PengajuanModel>,
    private val onClick: (PengajuanModel) -> Unit
): RecyclerView.Adapter<PengajuanAEAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPengajuanAeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PengajuanAEAdapter.ViewHolder {
        val binding = ItemPengajuanAeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PengajuanAEAdapter.ViewHolder, position: Int) {
        val dataPengajuan = pengajuanList[position]
        holder.binding.tvUsername.text = dataPengajuan.user?.name ?: ""
        holder.binding.tvJenisPengajuan.text = dataPengajuan.jenisPerizinan?.namaPerizinan ?: ""
        holder.binding.tvDate.text = dataPengajuan.createdAt.formatedDateToIndonesia()
        setStatusColor(holder, dataPengajuan.status)

        holder.itemView.setOnClickListener { onClick(dataPengajuan) }

    }

    private fun setStatusColor(holder: ViewHolder, status: String) {
        if (status == "proccess") {
            holder.binding.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.yellow_0e))
            holder.binding.cvStatus.setCardBackgroundColor(holder.itemView.context.getColor(R.color.yellow_c7))
            holder.binding.tvStatus.text = "Proses"
        }else if (status == "success") {
            holder.binding.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.green_34))
            holder.binding.cvStatus.setCardBackgroundColor(holder.itemView.context.getColor(R.color.green_e7))
            holder.binding.tvStatus.text = "Disetujui"
        }else {
            holder.binding.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.red_1b))
            holder.binding.cvStatus.setCardBackgroundColor(holder.itemView.context.getColor(R.color.red_e2))
            holder.binding.tvStatus.text = "Ditolak"
        }
    }

    fun updateData(dataList: List<PengajuanModel>?) {
        pengajuanList.clear()
        if (!dataList.isNullOrEmpty()) {
            pengajuanList.addAll(dataList)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = pengajuanList.size
}