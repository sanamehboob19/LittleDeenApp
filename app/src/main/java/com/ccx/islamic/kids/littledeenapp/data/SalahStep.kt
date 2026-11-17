package com.ccx.islamic.kids.littledeenapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SalahStep(
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val arabicRecitation: String,
    val transliteration: String,
    val translation: String,
    val audioTtsText: String,
    val postureCategory: String = "Prayer"
) : Parcelable {
    companion object {
        fun getAllSteps(): List<SalahStep> {
            return listOf(
                SalahStep(
                    stepNumber = 1,
                    title = "1. Making Wudu (Ablution)",
                    subtitle = "Cleanliness is half of faith",
                    description = "Before starting prayer, we must wash our hands, rinse our mouth and nose, wash our face, arms up to the elbows, wipe our head, and wash our feet.",
                    arabicRecitation = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    transliteration = "Bismillahi-r-rahmani-r-raheem",
                    translation = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                    audioTtsText = "Step 1: Making Wudu. Always begin by washing your hands and cleaning up before standing before Allah.",
                    postureCategory = "Wudu"
                ),
                SalahStep(
                    stepNumber = 2,
                    title = "2. Qiyam (Standing & Niyyah)",
                    subtitle = "Stand straight and make intention",
                    description = "Stand facing the Qiblah (Kaaba). Raise your hands to your ears and say Takbeer to begin your prayer.",
                    arabicRecitation = "اللَّهُ أَكْبَرُ",
                    transliteration = "Allahu Akbar",
                    translation = "Allah is the Greatest.",
                    audioTtsText = "Step 2: Qiyam. Stand straight, look at the place of sujood, raise your hands, and say Allahu Akbar.",
                    postureCategory = "Prayer"
                ),
                SalahStep(
                    stepNumber = 3,
                    title = "3. Reciting Surah Al-Fatiha",
                    subtitle = "Read the opening chapter of the Quran",
                    description = "Place your right hand over your left hand on your chest. Recite Surah Al-Fatiha and a short surah like Al-Ikhlas.",
                    arabicRecitation = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                    transliteration = "Bismillah... Alhamdulillah-i rabbi-l-'alameen",
                    translation = "In the name of Allah... All praise is for Allah, Lord of the worlds.",
                    audioTtsText = "Step 3: Recitation. Recite Surah Al-Fatiha with a peaceful and quiet voice.",
                    postureCategory = "Prayer"
                ),
                SalahStep(
                    stepNumber = 4,
                    title = "4. Ruku (Bowing)",
                    subtitle = "Bow down with respect",
                    description = "Bow down with your back straight, placing your hands on your knees, and praise Allah 3 times.",
                    arabicRecitation = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
                    transliteration = "Subhana Rabbiyal Azeem (3x)",
                    translation = "Glory be to my Lord, the Supreme.",
                    audioTtsText = "Step 4: Ruku. Bow down and say Subhana Rabbiyal Azeem three times.",
                    postureCategory = "Prayer"
                ),
                SalahStep(
                    stepNumber = 5,
                    title = "5. Sujood (Prostration)",
                    subtitle = "The closest position to Allah",
                    description = "Gently kneel down placing your forehead, nose, palms, knees, and toes on the ground. Praise Allah 3 times.",
                    arabicRecitation = "سُبْحَانَ رَبِّيَ الأَعْلَى",
                    transliteration = "Subhana Rabbiyal A'la (3x)",
                    translation = "Glory be to my Lord, the Most High.",
                    audioTtsText = "Step 5: Sujood. Prostrate on the floor and say Subhana Rabbiyal A'la three times.",
                    postureCategory = "Prayer"
                ),
                SalahStep(
                    stepNumber = 6,
                    title = "6. Tashahhud & Salam",
                    subtitle = "Completing your prayer with peace",
                    description = "Sit on your knees, recite Tashahhud, and turn your head to the right then left saying Assalamu Alaikum wa rahmatullah to end your prayer.",
                    arabicRecitation = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                    transliteration = "As-salaamu alaykum wa rahmatullah",
                    translation = "Peace be upon you, and the mercy of Allah.",
                    audioTtsText = "Step 6: Salam. Turn your head to the right and left, saying As-salaamu alaykum wa rahmatullah to finish your prayer.",
                    postureCategory = "Prayer"
                )
            )
        }
    }
}