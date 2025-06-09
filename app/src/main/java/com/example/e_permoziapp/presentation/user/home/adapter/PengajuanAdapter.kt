package com.example.e_permoziapp.presentation.user.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.R
import com.example.e_permoziapp.core.extention.formatedDateToIndonesia
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
        setStatusColor(holder, dataPengajuan.status)
        holder.binding.tvJenisPengajuan.text = dataPengajuan.jenisPerizinan?.namaPerizinan
        holder.binding.tvDate.text = dataPengajuan.createdAt.formatedDateToIndonesia()
        holder.binding.btnEdit.visibility = if (dataPengajuan.isEdit!!) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            onClick(dataPengajuan, false)
        }
        holder.binding.btnEdit.setOnClickListener {
            onClick(dataPengajuan, true)
        }
    }

    private fun setStatusColor(holder: PengajuanViewHolder, status: String) {
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

    override fun getItemCount(): Int = listPengajuan.size

    fun updateData(params: List<PengajuanModel>) {
        listPengajuan.clear()
        listPengajuan.addAll(params)
        notifyDataSetChanged()
    }
}