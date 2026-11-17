package com.ccx.islamic.kids.littledeenapp.model


import androidx.annotation.ColorInt

data class AppThemeColor(
    val id: String,
    val name: String,
    @ColorInt val primaryColor: Int,
    @ColorInt val lightColor: Int
)