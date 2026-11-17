package com.ccx.islamic.kids.littledeenapp.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlphabetModel(
    val letter: String,        // "A", "B", "C"
    val word: String,          // "Apple", "Ball", "Cat"
    val emoji: String,         // "🍎", "⚽", "🐱"
    val glitterColorHex: String
) : Parcelable