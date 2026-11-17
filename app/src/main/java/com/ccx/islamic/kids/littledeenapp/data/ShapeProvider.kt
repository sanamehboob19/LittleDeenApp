package com.ccx.islamic.kids.littledeenapp.data


import com.ccx.islamic.kids.littledeenapp.model.ShapeModel

object ShapeProvider {
    val shapes: List<ShapeModel> by lazy {
        listOf(
            ShapeModel("circle", "Circle", "⭕", "#FF1744"),      // 1. Red
            ShapeModel("square", "Square", "⬛", "#2979FF"),      // 2. Blue
            ShapeModel("triangle", "Triangle", "🔺", "#00E676"),  // 3. Green
            ShapeModel("rectangle", "Rectangle", "▭", "#FF9100"), // 4. Orange
            ShapeModel("oval", "Oval", "🥚", "#AA00FF"),          // 5. Purple
            ShapeModel("star", "Star", "⭐", "#FFD600"),          // 6. Yellow Gold
            ShapeModel("heart", "Heart", "❤️", "#FF4081"),        // 7. Pink
            ShapeModel("diamond", "Diamond", "💎", "#00E5FF"),    // 8. Cyan
            ShapeModel("crescent", "Crescent", "🌙", "#FF6D00"),  // 9. Moon Orange
            ShapeModel("hexagon", "Hexagon", "⬡", "#7C4DFF")      // 10. Violet
        )
    }
}