package com.ccx.islamic.kids.littledeenapp.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UrduAlphabetModel(
    val letter: String,        // "ا", "ب", "پ"
    val name: String,          // "Alif", "Bay", "Pay"
    val word: String,          // "انار", "بلی", "پتنگ"
    val wordEnglish: String,   // "Anaar", "Billi", "Patang"
    val emoji: String,         // "🍎", "🐱", "🪁"
    val glitterColorHex: String
) : Parcelable