package com.example.e_permoziapp.presentation.common.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.e_permoziapp.core.extention.getExtraOrDefault
import com.example.e_permoziapp.databinding.ActivityPhotoViewBinding
import com.example.e_permoziapp.presentation.common.viewmodel.PhotoViewmodel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class PhotoViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoViewBinding
    private val viewmodel: PhotoViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        onCollectEventState()
        setDataPhoto()
    }

    private fun setDataPhoto() {
        Glide.with(this)
            .load(viewmodel.url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.photoView)
    }

    private fun onCollectEventState() {
        binding.lrButtonBack.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        val url = intent.getExtraOrDefault("url", "")
        Timber.w("url $url")
        viewmodel.url = url
    }
}