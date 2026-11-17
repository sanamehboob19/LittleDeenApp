package com.ccx.islamic.kids.littledeenapp.model



import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingSlide(
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    @StringRes val ttsRes: Int,
    @DrawableRes val imageRes: Int
)