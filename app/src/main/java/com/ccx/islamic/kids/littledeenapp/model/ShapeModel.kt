package com.ccx.islamic.kids.littledeenapp.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShapeModel(
    val id: String,
    val name: String,          // e.g. "Circle", "Star"
    val emoji: String,         // e.g. "⭕", "⭐"
    val glitterColorHex: String
) : Parcelable