package com.example.e_permoziapp.presentation.manual_book.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.e_permoziapp.R
import com.example.e_permoziapp.domain.Entity.OnboardingItemModel
import com.example.e_permoziapp.domain.usecase.auth.SaveIsFinishOnboardingUseCase

class ManualBookViewmodel(
    private val context: Context
): ViewModel() {
    val dataList = listOf(
        OnboardingItemModel(
            R.drawable.iv_manual_book_1, context.getString(R.string.title_manual_book_1), context.getString(
                R.string.desc_manual_book_1)),
        OnboardingItemModel(
            R.drawable.iv_manual_book_2, context.getString(R.string.title_manual_book_2), context.getString(
                R.string.desc_manual_book_2)),
        OnboardingItemModel(
            R.drawable.iv_manual_book_3, context.getString(R.string.title_manual_book_3), context.getString(
                R.string.desc_manual_book_3)),
        OnboardingItemModel(
            R.drawable.iv_manual_book_4, context.getString(R.string.title_manual_book_4), context.getString(
                R.string.desc_manual_book_4)),
        OnboardingItemModel(
            R.drawable.iv_manual_book_5, context.getString(R.string.title_manual_book_5), context.getString(
                R.string.desc_manual_book_5)),
        OnboardingItemModel(
            R.drawable.iv_manual_book_6, context.getString(R.string.title_manual_book_6), context.getString(
                R.string.desc_manual_book_6)),
        OnboardingItemModel(
            R.drawable.iv_manual_book_7, context.getString(R.string.title_manual_book_7), context.getString(
                R.string.desc_manual_book_7)),
    )

}