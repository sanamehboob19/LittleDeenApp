package com.ccx.islamic.kids.littledeenapp.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PixelArtModel(
    val id: Int,
    val title: String,
    val gridSize: Int = 10,
    val paletteColorsHex: List<String>,
    val matrix: List<Int>
) : Parcelable