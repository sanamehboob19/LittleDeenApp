package com.ccx.islamic.kids.littledeenapp.data


object ShapePathsData {
    fun getStrokes(shapeId: String): List<String> {
        return when (shapeId.lowercase()) {
            // 1. Circle
            "circle" -> listOf(
                "M 50 15 C 72 15, 85 28, 85 50 C 85 72, 72 85, 50 85 C 28 85, 15 72, 15 50 C 15 28, 28 15, 50 15 Z"
            )

            // 2. Square
            "square" -> listOf(
                "M 20 20 L 80 20 L 80 80 L 20 80 Z"
            )

            // 3. Triangle
            "triangle" -> listOf(
                "M 50 15 L 85 82 L 15 82 Z"
            )

            // 4. Rectangle
            "rectangle" -> listOf(
                "M 16 26 L 84 26 L 84 74 L 16 74 Z"
            )

            // 5. Oval
            "oval" -> listOf(
                "M 50 16 C 76 16, 76 84, 50 84 C 24 84, 24 16, 50 16 Z"
            )

            // 6. Star (5-Point Star)
            "star" -> listOf(
                "M 50 14 L 61 38 L 88 38 L 66 54 L 75 80 L 50 64 L 25 80 L 34 54 L 12 38 L 39 38 Z"
            )

            // 7. Heart
            "heart" -> listOf(
                "M 50 32 C 50 16, 22 16, 22 42 C 22 62, 50 84, 50 84 C 50 84, 78 62, 78 42 C 78 16, 50 16, 50 32 Z"
            )

            // 8. Diamond
            "diamond" -> listOf(
                "M 50 15 L 85 50 L 50 85 L 15 50 Z"
            )

            // 9. Crescent Moon
            "crescent" -> listOf(
                "M 65 18 C 35 18, 25 45, 45 75 C 65 95, 82 76, 82 76 C 60 80, 42 62, 45 42 C 48 26, 65 18, 65 18 Z"
            )

            // 10. Hexagon (6-sided polygon)
            "hexagon" -> listOf(
                "M 50 15 L 82 32 L 82 68 L 50 85 L 18 68 L 18 32 Z"
            )

            else -> listOf("M 50 15 C 72 15, 85 28, 85 50 C 85 72, 72 85, 50 85 C 28 85, 15 72, 15 50 C 15 28, 28 15, 50 15 Z")
        }
    }
}