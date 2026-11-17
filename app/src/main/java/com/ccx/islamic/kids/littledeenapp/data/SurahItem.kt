package com.ccx.islamic.kids.littledeenapp.data


import android.os.Parcelable
import kotlinx.parcelize.Parcelize




@Parcelize
data class SurahItem(
    val id: Int,
    val name: String,
    val arabicName: String,
    val meaning: String,
    val totalVerses: Int,
    val arabicText: String,
    val transliteration: String,
    val englishTranslation: String,
    val urduTranslation: String,
    val audioUrl: String // 👈 Real MP3 link
) : Parcelable {
    companion object {
        fun getAllSurahs(): List<SurahItem> {
            return listOf(
                SurahItem(
                    id = 1,
                    name = "Surah Al-Fatiha",
                    arabicName = "سورة الفاتحة",
                    meaning = "The Opening",
                    totalVerses = 7,
                    arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                    transliteration = "Bismillahi-r-rahmani-r-raheem. Alhamdulillah-i rabbi-l-'alameen...",
                    englishTranslation = "In the name of Allah, the Entirely Merciful, the Especially Merciful. [All] praise is [due] to Allah, Lord of the worlds...",
                    urduTranslation = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔ سب تعریفیں اللہ کے لیے ہیں جو تمام جہانوں کا پالنے والا ہے۔",
                    audioUrl = "https://server8.mp3quran.net/afs/001.mp3"
                ),
                SurahItem(
                    id = 108,
                    name = "Surah Al-Kawthar",
                    arabicName = "سورة الكوثر",
                    meaning = "The Abundance",
                    totalVerses = 3,
                    arabicText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ ۝ فَصَلِّ لِرَبِّكَ وَانْحَرْ ۝ إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
                    transliteration = "Inna a'ataynaka-l-kawthar. Fa salli li rabbika wanhar. Inna shani-aka huwal abtar.",
                    englishTranslation = "Indeed, We have granted you, [O Muhammad], Al-Kawthar. So pray to your Lord and sacrifice [to Him alone]. Indeed, your enemy is the one cut off.",
                    urduTranslation = "بے شک ہم نے آپ کو کوثر عطا فرما دی ہے۔ پس آپ اپنے رب کے لیے نماز پڑھیں اور قربانی دیں۔ بے شک آپ کا دشمن ہی بے نام و نشاں رہے گا۔",
                    audioUrl = "https://server8.mp3quran.net/afs/108.mp3"
                ),
                SurahItem(
                    id = 112,
                    name = "Surah Al-Ikhlas",
                    arabicName = "سورة الإخلاص",
                    meaning = "The Sincerity (Oneness of Allah)",
                    totalVerses = 4,
                    arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                    transliteration = "Qul huwallahu ahad. Allahus-samad. Lam yalid wa lam yulad. Wa lam yakun lahu kufuwan ahad.",
                    englishTranslation = "Say, 'He is Allah, [who is] One. Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.'",
                    urduTranslation = "آپ کہہ دیجیے: وہ اللہ ایک ہے۔ اللہ بے نیاز ہے۔ نہ اس نے کسی کو جنا اور نہ وہ جنا گیا، اور کوئی بھی اس کا ہمسر نہیں۔",
                    audioUrl = "https://server8.mp3quran.net/afs/112.mp3"
                ),
                SurahItem(
                    id = 113,
                    name = "Surah Al-Falaq",
                    arabicName = "سورة الفلق",
                    meaning = "The Daybreak (Protection Surah)",
                    totalVerses = 5,
                    arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
                    transliteration = "Qul a'udhu bi rabbi-l-falaq. Min sharri ma khalaq. Wa min sharri ghasiqin idha waqab...",
                    englishTranslation = "Say, 'I seek refuge in the Lord of daybreak from the evil of that which He created...' ",
                    urduTranslation = "آپ کہہ دیجیے: میں صبح کے رب کی پناہ مانگتا ہوں، ہر اس چیز کے شر سے جو اس نے پیدا کی...",
                    audioUrl = "https://server8.mp3quran.net/afs/113.mp3"
                ),
                SurahItem(
                    id = 114,
                    name = "Surah An-Nas",
                    arabicName = "سورة الناس",
                    meaning = "Mankind (Protection Surah)",
                    totalVerses = 6,
                    arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
                    transliteration = "Qul a'udhu bi rabbi-n-nas. Maliki-n-nas. Ilahi-n-nas...",
                    englishTranslation = "Say, 'I seek refuge in the Lord of mankind, The Sovereign of mankind. The God of mankind...'",
                    urduTranslation = "آپ کہہ دیجیے: میں لوگوں کے رب کی پناہ مانگتا ہوں، جو لوگوں کا بادشاہ ہے، لوگوں کا معبود ہے۔",
                    audioUrl = "https://server8.mp3quran.net/afs/114.mp3"
                )
            )
        }
    }
}











//@Parcelize
//data class SurahItem(
//    val id: Int,
//    val name: String,
//    val arabicName: String,
//    val meaning: String,
//    val totalVerses: Int,
//    val arabicText: String,
//    val transliteration: String,
//    val englishTranslation: String,
//    val urduTranslation: String
//) : Parcelable {
//    companion object {
//        fun getAllSurahs(): List<SurahItem> {
//            return listOf(
//                SurahItem(
//                    id = 1,
//                    name = "Surah Al-Fatiha",
//                    arabicName = "سورة الفاتحة",
//                    meaning = "The Opening",
//                    totalVerses = 7,
//                    arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
//                    transliteration = "Bismillahi-r-rahmani-r-raheem. Alhamdulillah-i rabbi-l-'alameen...",
//                    englishTranslation = "In the name of Allah, the Entirely Merciful, the Especially Merciful. [All] praise is [due] to Allah, Lord of the worlds...",
//                    urduTranslation = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔ سب تعریفیں اللہ کے لیے ہیں جو تمام جہانوں کا پالنے والا ہے۔"
//                ),
//                SurahItem(
//                    id = 112,
//                    name = "Surah Al-Ikhlas",
//                    arabicName = "سورة الإخلاص",
//                    meaning = "The Sincerity (Oneness of Allah)",
//                    totalVerses = 4,
//                    arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
//                    transliteration = "Qul huwallahu ahad. Allahus-samad. Lam yalid wa lam yulad. Wa lam yakun lahu kufuwan ahad.",
//                    englishTranslation = "Say, 'He is Allah, [who is] One. Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.'",
//                    urduTranslation = "آپ کہہ دیجیے: وہ اللہ ایک ہے۔ اللہ بے نیاز ہے۔ نہ اس نے کسی کو جنا اور نہ وہ جنا گیا، اور کوئی بھی اس کا ہمسر نہیں۔"
//                ),
//                SurahItem(
//                    id = 113,
//                    name = "Surah Al-Falaq",
//                    arabicName = "سورة الفلق",
//                    meaning = "The Daybreak (Protection Surah)",
//                    totalVerses = 5,
//                    arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
//                    transliteration = "Qul a'udhu bi rabbi-l-falaq. Min sharri ma khalaq. Wa min sharri ghasiqin idha waqab...",
//                    englishTranslation = "Say, 'I seek refuge in the Lord of daybreak from the evil of that which He created...' ",
//                    urduTranslation = "آپ کہہ دیجیے: میں صبح کے رب کی پناہ مانگتا ہوں، ہر اس چیز کے شر سے جو اس نے پیدا کی..."
//                ),
//                SurahItem(
//                    id = 114,
//                    name = "Surah An-Nas",
//                    arabicName = "سورة الناس",
//                    meaning = "Mankind (Protection Surah)",
//                    totalVerses = 6,
//                    arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
//                    transliteration = "Qul a'udhu bi rabbi-n-nas. Maliki-n-nas. Ilahi-n-nas...",
//                    englishTranslation = "Say, 'I seek refuge in the Lord of mankind, The Sovereign of mankind. The God of mankind...'",
//                    urduTranslation = "آپ کہہ دیجیے: میں لوگوں کے رب کی پناہ مانگتا ہوں، جو لوگوں کا بادشاہ ہے، لوگوں کا معبود ہے۔"
//                ),
//                SurahItem(
//                    id = 108,
//                    name = "Surah Al-Kawthar",
//                    arabicName = "سورة الكوثر",
//                    meaning = "The Abundance",
//                    totalVerses = 3,
//                    arabicText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ ۝ فَصَلِّ لِرَبِّكَ وَانْحَرْ ۝ إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
//                    transliteration = "Inna a'ataynaka-l-kawthar. Fa salli li rabbika wanhar. Inna shani-aka huwal abtar.",
//                    englishTranslation = "Indeed, We have granted you, [O Muhammad], Al-Kawthar. So pray to your Lord and sacrifice [to Him alone]. Indeed, your enemy is the one cut off.",
//                    urduTranslation = "بے شک ہم نے آپ کو کوثر عطا فرما دی ہے۔ پس آپ اپنے رب کے لیے نماز پڑھیں اور قربانی دیں۔ بے شک آپ کا دشمن ہی بے نام و نشاں رہے گا۔"
//                )
//            )
//        }
//    }
//}