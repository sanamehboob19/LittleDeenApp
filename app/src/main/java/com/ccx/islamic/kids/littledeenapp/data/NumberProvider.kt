package com.ccx.islamic.kids.littledeenapp.data


import com.ccx.islamic.kids.littledeenapp.model.NumberModel

object NumberProvider {
    val numbers: List<NumberModel> by lazy {
        listOf(
            NumberModel(1, "1", "One", "#E91E63"),
            NumberModel(2, "2", "Two", "#9C27B0"),
            NumberModel(3, "3", "Three", "#00BCD4"),
            NumberModel(4, "4", "Four", "#4CAF50"),
            NumberModel(5, "5", "Five", "#FF9800"),
            NumberModel(6, "6", "Six", "#E91E63"),
            NumberModel(7, "7", "Seven", "#3F51B5"),
            NumberModel(8, "8", "Eight", "#FF4081"),
            NumberModel(9, "9", "Nine", "#7C4DFF"),
            NumberModel(10, "10", "Ten", "#00B0FF"),
            NumberModel(11, "11", "Eleven", "#00E676"),
            NumberModel(12, "12", "Twelve", "#FFD600"),
            NumberModel(13, "13", "Thirteen", "#FF6D00"),
            NumberModel(14, "14", "Fourteen", "#651FFF"),
            NumberModel(15, "15", "Fifteen", "#00E5FF"),
            NumberModel(16, "16", "Sixteen", "#FF1744"),
            NumberModel(17, "17", "Seventeen", "#FFAB00"),
            NumberModel(18, "18", "Eighteen", "#F50057"),
            NumberModel(19, "19", "Nineteen", "#2979FF"),
            NumberModel(20, "20", "Twenty", "#00C853")
        )
    }
}