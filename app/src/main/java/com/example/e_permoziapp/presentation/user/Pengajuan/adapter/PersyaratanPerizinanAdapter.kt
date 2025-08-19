package com.example.e_permoziapp.presentation.user.Pengajuan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.databinding.ItemPersyaratanBinding
import com.example.e_permoziapp.domain.Entity.FilePersyaratanModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import timber.log.Timber
import java.io.File

class PersyaratanPerizinanAdapter(
    private var persyaratanList: MutableList<PersyaratanPerizinanModel>,
    private val isEdit: Boolean,
    private val fileSelectedList: MutableList<FileSelectModel>,
    private val onClick: (PersyaratanPerizinanModel) -> Unit,
    private val chooseFileClick: (Pair<Int, Int>) -> Unit,
    private val fileClick: (FilePersyaratanModel) -> Unit
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
        holder.binding.tvTitle.text = dataPersyaratan.name + if (dataPersyaratan.isRequired == 1) "*" else ""
        Timber.w("content ${dataPersyaratan.content}")
        if (isEdit) {
            var filePersyaratanModel = FilePersyaratanModel()
            if (fileSelectModel != null && fileSelectModel.filename.isNullOrEmpty().not()) {
                filePersyaratanModel = FilePersyaratanModel(
                    url = fileSelectModel.uri.toString(),
                    uri = fileSelectModel.uri,
                    fileName = fileSelectModel.filename ?: "",
                    format = fileSelectModel.format ?: "",
                    isUri = true
                )
                holder.binding.etFileName.setText(fileSelectModel.filename)
            }else {
                filePersyaratanModel = if (dataPersyaratan.content.isNullOrEmpty().not()) {
                    val url = "${ServerInfo.FILE_PATH_PERSYARATAN}${dataPersyaratan.content}"
                    val format = FileHelper.getFileExtension(dataPersyaratan.content ?: "")
                    FilePersyaratanModel(
                        url = url,
                        fileName = dataPersyaratan.content ?: "",
                        format = format,
                        isUri = false
                    )
                }else FilePersyaratanModel()
                holder.binding.etFileName.setText(dataPersyaratan.content)
            }
            holder.binding.btnAction.text = "Pilih file"
            holder.binding.btnAction.setOnClickListener {
                chooseFileClick(Pair<Int, Int>(position, dataPersyaratan.id))
            }
            holder.binding.btnFileName.setOnClickListener {
                if (filePersyaratanModel.url.isNotEmpty() || filePersyaratanModel.uri != null){
                    fileClick(filePersyaratanModel)
                }
            }
        }else {
            holder.binding.btnAction.text = "Download"
            holder.binding.etFileName.visibility = View.GONE
            if (dataPersyaratan.content.isNullOrEmpty()) {
                holder.binding.tvFileNotFound.visibility = View.VISIBLE
                holder.binding.rlButton.visibility = View.GONE
            }else {
                holder.binding.tvFileNotFound.visibility = View.GONE
                holder.binding.rlButton.visibility = View.VISIBLE
            }
            holder.binding.btnAction.setOnClickListener {
                onClick(dataPersyaratan)
            }
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