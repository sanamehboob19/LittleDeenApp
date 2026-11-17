package com.ccx.islamic.kids.littledeenapp.data


import com.ccx.islamic.kids.littledeenapp.model.AlphabetModel

object AlphabetProvider {
    val letters: List<AlphabetModel> by lazy {
        listOf(
            AlphabetModel("A", "Apple", "🍎", "#FF1744"),
            AlphabetModel("B", "Ball", "⚽", "#2979FF"),
            AlphabetModel("C", "Cat", "🐱", "#00E676"),
            AlphabetModel("D", "Dog", "🐶", "#FF9100"),
            AlphabetModel("E", "Elephant", "🐘", "#AA00FF"),
            AlphabetModel("F", "Fish", "🐟", "#00E5FF"),
            AlphabetModel("G", "Giraffe", "🦒", "#FFD600"),
            AlphabetModel("H", "Horse", "🐴", "#FF4081"),
            AlphabetModel("I", "Ice Cream", "🍦", "#00B0FF"),
            AlphabetModel("J", "Juice", "🧃", "#FF6D00"),
            AlphabetModel("K", "Kite", "🪁", "#7C4DFF"),
            AlphabetModel("L", "Lion", "🦁", "#64DD17"),
            AlphabetModel("M", "Moon", "🌙", "#D50000"),
            AlphabetModel("N", "Nest", "🪺", "#00BFA5"),
            AlphabetModel("O", "Orange", "🍊", "#FF6D00"),
            AlphabetModel("P", "Parrot", "🦜", "#E040FB"),
            AlphabetModel("Q", "Queen", "👑", "#651FFF"),
            AlphabetModel("R", "Rabbit", "🐰", "#FF5252"),
            AlphabetModel("S", "Sun", "☀️", "#FFD600"),
            AlphabetModel("T", "Tree", "🌳", "#FF3D00"),
            AlphabetModel("U", "Umbrella", "☂️", "#304FFE"),
            AlphabetModel("V", "Violin", "🎻", "#7C4DFF"),
            AlphabetModel("W", "Water", "💧", "#00C853"),
            AlphabetModel("X", "Xylophone", "🎼", "#FF4081"),
            AlphabetModel("Y", "Yacht", "⛵", "#FFAB00"),
            AlphabetModel("Z", "Zebra", "🦓", "#212121")
        )
    }
}