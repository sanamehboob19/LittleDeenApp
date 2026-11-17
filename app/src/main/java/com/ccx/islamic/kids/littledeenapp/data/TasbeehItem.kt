package com.ccx.islamic.kids.littledeenapp.data



data class TasbeehItem(
    val id: String,
    val arabic: String,
    val english: String,
    val target: Int = 33
) {
    companion object {
        fun getDefaultList(): List<TasbeehItem> {
            return listOf(
                TasbeehItem("subhanallah", "سُبْحَانَ اللَّهِ", "SubhanAllah (Glory be to Allah)", 33),
                TasbeehItem("alhamdulillah", "الْحَمْدُ لِلَّهِ", "Alhamdulillah (Praise be to Allah)", 33),
                TasbeehItem("allahuakbar", "اللَّهُ أَكْبَرُ", "Allahu Akbar (Allah is the Greatest)", 33),
                TasbeehItem("astaghfirullah", "أَسْتَغْفِرُ اللَّهَ", "Astaghfirullah (I seek forgiveness from Allah)", 100),
                TasbeehItem("lailahailla", "لَا إِلَهَ إِلَّا اللَّهُ", "La ilaha illallah (There is no god but Allah)", 100),
                TasbeehItem("yunus_dua", "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ", "Dua of Prophet Yunus (AS)", 33),
                TasbeehItem("salawat", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ", "Darood / Salawat upon Prophet Muhammad (PBUH)", 100)
            )
        }
    }
}