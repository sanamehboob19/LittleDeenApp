package com.ccx.islamic.kids.littledeenapp.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasbeeh_counts")
data class TasbeehEntity(
    @PrimaryKey val dhikrId: String,
    val arabicText: String,
    val englishText: String,
    val targetCount: Int = 33,
    val currentCount: Int = 0
)