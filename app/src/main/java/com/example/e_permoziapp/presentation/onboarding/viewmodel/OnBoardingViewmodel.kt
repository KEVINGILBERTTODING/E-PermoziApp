package com.example.e_permoziapp.presentation.onboarding.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.e_permoziapp.R
import com.example.e_permoziapp.domain.Entity.OnboardingItemModel
import com.example.e_permoziapp.domain.usecase.auth.SaveIsFinishOnboardingUseCase

class OnBoardingViewmodel(
    private val saveIsFinishOnboardingUseCase: SaveIsFinishOnboardingUseCase,
    private val context: Context
): ViewModel() {
    val dataList = listOf(
        OnboardingItemModel(
            R.drawable.iv_onboard_1, context.getString(R.string.title_onboard_1), context.getString(
                R.string.desc_onboard_1)),
        OnboardingItemModel(
            R.drawable.iv_onboard_2, context.getString(R.string.title_onboard_2), context.getString(
                R.string.desc_onboard_2)),
        OnboardingItemModel(
            R.drawable.iv_onboard_3, context.getString(R.string.title_onboard_3), context.getString(
                R.string.desc_onboard_3)),
        OnboardingItemModel(
            R.drawable.iv_onboard_4, context.getString(R.string.title_onboard_4), context.getString(
                R.string.desc_onboard_4)),
        OnboardingItemModel(
            R.drawable.iv_onboard_5, context.getString(R.string.title_onboard_5), context.getString(
                R.string.desc_onboard_5)),
    )

    fun saveIsFirstTime() {
        saveIsFinishOnboardingUseCase()
    }
}