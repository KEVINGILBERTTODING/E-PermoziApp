package com.example.e_permoziapp.presentation.user.Pengajuan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.databinding.ItemPersyaratanBinding
import com.example.e_permoziapp.domain.Entity.FileSelectModel

class PersyaratanPerizinanAdapter(
    private var persyaratanList: MutableList<PersyaratanPerizinanModel>,
    private val isEdit: Boolean,
    private val fileSelectedList: MutableList<FileSelectModel>,
    private val onClick: (PersyaratanPerizinanModel) -> Unit,
    private val chooseFileClick: (Pair<Int, Int>) -> Unit
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
        val fileSelectModel = fileSelectedList.getOrNull(position)
        holder.binding.tvTitle.text = dataPersyaratan.name
        if (isEdit) {
            holder.binding.rlUploadFile.visibility = View.VISIBLE
            holder.binding.btnDownload.visibility = View.GONE
            holder.binding.tvName.text = fileSelectModel?.filename

        }else {
            holder.binding.btnDownload.visibility = if (!dataPersyaratan.content.isNullOrEmpty()) View.VISIBLE else View.GONE
            holder.binding.rlUploadFile.visibility = View.GONE
        }

        holder.binding.tvRequired.visibility = if (dataPersyaratan.isRequired == 1) View.VISIBLE else View.GONE

        holder.binding.btnDownload.setOnClickListener {
            onClick(dataPersyaratan)
        }
        holder.binding.btnUpload.setOnClickListener {
            chooseFileClick(Pair<Int, Int>(position, dataPersyaratan.id))
        }
    }

    override fun getItemCount(): Int = persyaratanList.size

    fun updateFilePersyaratan(fileSelectModel: FileSelectModel, position: Int) {
        fileSelectedList[position] = fileSelectModel
        notifyItemChanged(position)
    }

    fun updateData(dataList: List<PersyaratanPerizinanModel>) {
        persyaratanList.clear()
        fileSelectedList.clear()
        persyaratanList.addAll(dataList)
        fileSelectedList.addAll(List(dataList.size) { FileSelectModel(null, "", "", null) })
        notifyDataSetChanged()
    }
}