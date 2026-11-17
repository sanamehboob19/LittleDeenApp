package com.ccx.islamic.kids.littledeenapp.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.ccx.islamic.kids.littledeenapp.R
import kotlinx.parcelize.Parcelize



@Parcelize
data class DuaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val arabicText: String,
    val translation: String,
    val audioTtsText: String,
    val category: String = "All",
    @DrawableRes val imageRes: Int = R.drawable.avatar_girl_1 // 👈 Local Drawable Resource ID
) : Parcelable {
    companion object {
        fun getDefaultDuas(): List<DuaItem> {
            return listOf(
                DuaItem(
                    id = "dua_sneeze",
                    title = "When You Sneeze",
                    subtitle = "Say Alhamdulillah when you sneeze!",
                    arabicText = "الْحَمْدُ لِلَّهِ",
                    translation = "All praise is for Allah (Alhamdulillah).",
                    audioTtsText = "When you sneeze, say Alhamdulillah! All praise is for Allah.",
                    category = "Daily Habits",
                    imageRes = R.drawable.ic_sneeze
                ),
                DuaItem(
                    id = "dua_eating",
                    title = "Before Eating",
                    subtitle = "Say Bismillah before eating your food",
                    arabicText = "بِسْمِ اللَّهِ",
                    translation = "In the name of Allah (Bismillah).",
                    audioTtsText = "Before eating or drinking, say Bismillah! In the name of Allah.",
                    category = "Daily Habits",
                    imageRes = R.drawable.ic_bismillah
                ),
                DuaItem(
                    id = "dua_sleeping",
                    title = "Before Sleeping",
                    subtitle = "Sleep peacefully under Allah's protection",
                    arabicText = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                    translation = "In Your name, O Allah, I die and I live.",
                    audioTtsText = "Before going to bed at night, say: Bismikallahumma amootu wa ahya. In Your name, O Allah, I live and I die.",
                    category = "Evening",
                    imageRes = R.drawable.dua_sleeping
                ),
                DuaItem(
                    id = "dua_gratitude",
                    title = "Showing Gratitude",
                    subtitle = "Say Jazakallah khair and smile to parents!",
                    arabicText = "جَزَاكَ اللَّهُ خَيْرًا",
                    translation = "May Allah reward you with goodness (Jazakallah khair).",
                    audioTtsText = "When someone helps you or gives you a gift, say Thank you and Jazakallah khair, and smile happily to your parents!",
                    category = "Good Manners",
                    imageRes = R.drawable.dua_gratitude
                ),
                DuaItem(
                    id = "dua_parents",
                    title = "Dua for Parents",
                    subtitle = "Ask Allah to have mercy on mom and dad",
                    arabicText = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                    translation = "My Lord, have mercy upon them as they brought me up [when I was] small.",
                    audioTtsText = "Rabbir-hamhuma kama rabbayani sagheera. My Lord, have mercy upon my parents as they raised me when I was small.",
                    category = "Family",
                    imageRes = R.drawable.dua_parents
                ),
                DuaItem(
                    id = "dua_home",
                    title = "Entering Home",
                    subtitle = "Greet your family with Salam",
                    arabicText = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى اللَّهِ رَبِّنَا تَوَكَّلْنَا",
                    translation = "In the name of Allah we enter, and in the name of Allah we leave, and upon our Lord we rely.",
                    audioTtsText = "Bismillahi walajna wa bismillahi kharajna, wa 'ala rabbina tawakkalna. When entering your home, greet your family with Assalamu Alaikum!",
                    category = "Daily Habits",
                    imageRes = R.drawable.dua_home
                ),
                DuaItem(
                    id = "dua_knowledge",
                    title = "Dua for Knowledge",
                    subtitle = "Ask Allah to increase your knowledge",
                    arabicText = "رَّبِّ زِدْنِي عِلْمًا",
                    translation = "My Lord, increase me in knowledge.",
                    audioTtsText = "Rabbi zidni ilma. My Lord, increase me in knowledge and learning.",
                    category = "Learning",
                    imageRes = R.drawable.dua_knowledge
                ),
                // ==========================================
// 1. TOILET / WASHROOM
// ==========================================
                DuaItem(
                    id = "dua_toilet_enter",
                    title = "Entering the Washroom",
                    subtitle = "Step in with your left foot and seek protection",
                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ",
                    translation = "O Allah, I seek refuge in You from evil and evil spirits.",
                    audioTtsText = "Before entering the washroom, say: Allahumma inni a'udhu bika minal khubuthi wal khaba'ith.",
                    category = "Toilet",
                    imageRes = R.drawable.dua_toilet_enter
                ),
                DuaItem(
                    id = "dua_toilet_leave",
                    title = "Leaving the Washroom",
                    subtitle = "Step out with your right foot and say Ghufranak",
                    arabicText = "غُفْرَانَكَ",
                    translation = "I ask You for forgiveness (Ghufranak).",
                    audioTtsText = "When stepping out of the washroom, say: Ghufranak! I ask Allah for forgiveness.",
                    category = "Toilet",
                    imageRes = R.drawable.dua_toilet_leave
                ),

// ==========================================
// 2. MORNING
// ==========================================
                DuaItem(
                    id = "dua_morning_wake",
                    title = "Waking Up",
                    subtitle = "Thank Allah for giving us life today",
                    arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَمَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                    translation = "All praise is for Allah who gave us life after causing us to die.",
                    audioTtsText = "When you open your eyes in the morning, say: Alhamdulillahil ladhi ahyana ba'da ma amatana wa ilayhin nushoor.",
                    category = "Morning",
                    imageRes = R.drawable.dua_morning_wake
                ),
                DuaItem(
                    id = "dua_morning_praise",
                    title = "Morning Praise",
                    subtitle = "Start your day remembering Allah's kingdom",
                    arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ",
                    translation = "We have entered the morning and the kingdom belongs to Allah.",
                    audioTtsText = "Asbahna wa asbahal mulku lillah, walhamdulillah. Morning has come, and all kingdom belongs to Allah.",
                    category = "Morning",
                    imageRes = R.drawable.dua_morning_praise
                ),

// ==========================================
// 3. NIGHT & SLEEPING
// ==========================================
                DuaItem(
                    id = "dua_night_sleep",
                    title = "Before Sleeping",
                    subtitle = "Sleep peacefully under Allah's care",
                    arabicText = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                    translation = "In Your name, O Allah, I die and I live.",
                    audioTtsText = "Before you sleep at night, say: Bismikallahumma amootu wa ahya. In Your name, O Allah, I live and die.",
                    category = "Night",
                    imageRes = R.drawable.dua_night_sleep
                ),
                DuaItem(
                    id = "dua_night_peace",
                    title = "Peaceful Sleep",
                    subtitle = "Dua for sweet dreams and protection",
                    arabicText = "الْحَمْدُ لِلَّهِ الَّذِي كَفَانَا وَآوَانَا",
                    translation = "Praise be to Allah who has provided for us and given us shelter.",
                    audioTtsText = "Alhamdulillahil ladhi kafana wa awana. Praise be to Allah who takes care of us and gives us a cozy home.",
                    category = "Night",
                    imageRes = R.drawable.dua_night_peace
                ),

// ==========================================
// 4. PRAYER & WUDU
// ==========================================
                DuaItem(
                    id = "dua_before_wudu",
                    title = "Before Wudu",
                    subtitle = "Begin making Wudu with Bismillah",
                    arabicText = "بِسْمِ اللَّهِ",
                    translation = "In the name of Allah.",
                    audioTtsText = "Before making wudu with fresh clean water, say: Bismillah! In the name of Allah.",
                    category = "Prayer",
                    imageRes = R.drawable.dua_before_wudu
                ),
                DuaItem(
                    id = "dua_after_wudu",
                    title = "After Wudu",
                    subtitle = "Ask Allah to make you clean and pure",
                    arabicText = "اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
                    translation = "O Allah, make me of those who repent and make me of those who purify themselves.",
                    audioTtsText = "Allahummaj-'alni minat tawwabeen, waj-'alni minal mutatahhireen. O Allah, make me pure and clean.",
                    category = "Prayer",
                    imageRes = R.drawable.dua_after_wudu
                ),

// ==========================================
// 5. PARENTS
// ==========================================
                DuaItem(
                    id = "dua_parents_mercy",
                    title = "Mercy for Parents",
                    subtitle = "Ask Allah to shower kindness on mom and dad",
                    arabicText = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                    translation = "My Lord, have mercy upon them as they brought me up when I was small.",
                    audioTtsText = "Rabbir-hamhuma kama rabbayani sagheera. My Lord, bless my parents with mercy and love.",
                    category = "Parents",
                    imageRes = R.drawable.dua_parents_mercy
                ),
                DuaItem(
                    id = "dua_parents_forgive",
                    title = "Forgiveness for Family",
                    subtitle = "Pray for your parents and all believers",
                    arabicText = "رَبَّنَا اغْفِرْ لِي وَلِوَالِدَيَّ وَلِلْمُؤْمِنِينَ",
                    translation = "Our Lord, forgive me and my parents and the believers.",
                    audioTtsText = "Rabbana-ghfir li wa li-walidayya wa lil-mu'mineen. Our Lord, forgive me, my parents, and all believers.",
                    category = "Parents",
                    imageRes = R.drawable.dua_parents_forgive
                ),

// ==========================================
// 6. HOME
// ==========================================
                DuaItem(
                    id = "dua_leave_home",
                    title = "Leaving Home",
                    subtitle = "Place your trust in Allah as you step outside",
                    arabicText = "بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
                    translation = "In the name of Allah, I place my trust in Allah. There is no power except with Allah.",
                    audioTtsText = "Bismillahi tawakkaltu 'alallah, wa la hawla wa la quwwata illa billah.",
                    category = "Home",
                    imageRes = R.drawable.dua_leave_home
                ),
                DuaItem(
                    id = "dua_enter_home",
                    title = "Entering Home",
                    subtitle = "Say Bismillah and greet with Salam",
                    arabicText = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا",
                    translation = "In the name of Allah we enter, and in the name of Allah we leave.",
                    audioTtsText = "Bismillahi walajna wa bismillahi kharajna. Greet your family warmly with As-salaamu alaykum!",
                    category = "Home",
                    imageRes = R.drawable.dua_enter_home
                ),

// ==========================================
// 7. TRAVEL
// ==========================================
                DuaItem(
                    id = "dua_board_car",
                    title = "Riding in a Vehicle",
                    subtitle = "Say this when boarding a car, bus, or plane",
                    arabicText = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ",
                    translation = "Glory to Him who has subjected this to us, and we could never have done it ourselves.",
                    audioTtsText = "Subhanal ladhi sakh-khara lana hadha wa ma kunna lahu muqrineen.",
                    category = "Travel",
                    imageRes = R.drawable.dua_board_car
                ),
                DuaItem(
                    id = "dua_travel_safety",
                    title = "Safe Journey",
                    subtitle = "Ask Allah for a safe and easy trip",
                    arabicText = "اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى",
                    translation = "O Allah, we ask You in this journey of ours for goodness and piety.",
                    audioTtsText = "Allahumma inna nas'aluka fi safarina hadhal birra wat taqwa. O Allah, keep us safe on our trip.",
                    category = "Travel",
                    imageRes = R.drawable.dua_travel_safety
                ),

// ==========================================
// 8. FASTING (RAMADAN)
// ==========================================
                DuaItem(
                    id = "dua_fast_start",
                    title = "Intent for Fasting",
                    subtitle = "Set your intention at Suhoor time",
                    arabicText = "وَبِصَوْمِ غَدٍ نَوَيْتُ مِنْ شَهْرِ رَمَضَانَ",
                    translation = "I intend to keep the fast tomorrow for the month of Ramadan.",
                    audioTtsText = "Wa bi-sawmi ghadin nawaytu min shahri Ramadan. I intend to fast today for Allah.",
                    category = "Fasting",
                    imageRes = R.drawable.dua_fast_start
                ),
                DuaItem(
                    id = "dua_fast_iftar",
                    title = "Breaking the Fast (Iftar)",
                    subtitle = "Say this when breaking your fast with dates or water",
                    arabicText = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ، وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
                    translation = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
                    audioTtsText = "Dhahabaz zama'u wabtallatil 'urooqu wa thabatal ajru in sha Allah.",
                    category = "Fasting",
                    imageRes = R.drawable.dua_fast_iftar
                ),

// ==========================================
// 9. EATING
// ==========================================
                DuaItem(
                    id = "dua_eat_before",
                    title = "Before Eating",
                    subtitle = "Say Bismillah before starting your meal",
                    arabicText = "بِسْمِ اللَّهِ",
                    translation = "In the name of Allah.",
                    audioTtsText = "Bismillah! In the name of Allah, always eat with your right hand.",
                    category = "Eating",
                    imageRes = R.drawable.dua_eat_before
                ),
                DuaItem(
                    id = "dua_eat_forgot",
                    title = "If You Forgot to Say Bismillah",
                    subtitle = "Say this if you forget before starting your meal",
                    arabicText = "بِسْمِ اللَّهِ أَوَّلَهُ وَآخِرَهُ",
                    translation = "In the name of Allah, in the beginning and at the end.",
                    audioTtsText = "Bismillahi awwalahu wa akhirahu. In the name of Allah at the beginning and the end.",
                    category = "Eating",
                    imageRes = R.drawable.dua_eat_forgot
                ),

// ==========================================
// 10. GREETING & MANNERS
// ==========================================
                DuaItem(
                    id = "dua_greet_salam",
                    title = "Greeting with Salam",
                    subtitle = "Spread peace whenever you meet someone",
                    arabicText = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ",
                    translation = "Peace be upon you, and the mercy of Allah and His blessings.",
                    audioTtsText = "As-salaamu alaykum wa rahmatullahi wa barakatuh. Spread smiles and greeting of peace!",
                    category = "Manners",
                    imageRes = R.drawable.dua_greet_salam
                ),
                DuaItem(
                    id = "dua_gift_thanks",
                    title = "Receiving a Gift or Favor",
                    subtitle = "Say Jazakallah khair when someone is kind to you",
                    arabicText = "جَزَاكَ اللَّهُ خَيْرًا",
                    translation = "May Allah reward you with goodness.",
                    audioTtsText = "When someone helps you or gives you a gift, say Jazakallah khair! May Allah reward you.",
                    category = "Manners",
                    imageRes = R.drawable.dua_gift_thanks
                ),

// ==========================================
// 11. PROTECTION
// ==========================================
                DuaItem(
                    id = "dua_protect_harm",
                    title = "Protection from Harm",
                    subtitle = "Allah protects you from all danger",
                    arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلَا فِي السَّمَاءِ",
                    translation = "In the name of Allah, with whose name nothing can cause harm in the earth or in the heavens.",
                    audioTtsText = "Bismillahil ladhi la yadurru ma'asmihi shay'un fil ardi wa la fis sama'i, wa huwas samee'ul 'aleem.",
                    category = "Protection",
                    imageRes = R.drawable.dua_protect_harm
                ),
                DuaItem(
                    id = "dua_seek_refuge",
                    title = "Seeking Refuge",
                    subtitle = "Seek Allah's help when feeling scared or upset",
                    arabicText = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ",
                    translation = "I seek refuge in Allah from the accursed Shaytan.",
                    audioTtsText = "A'udhu billahi minash shaytanir rajeem. I seek protection in Allah from all bad thoughts.",
                    category = "Protection",
                    imageRes = R.drawable.dua_seek_refuge
                ),

// ==========================================
// 12. EVENING
// ==========================================
                DuaItem(
                    id = "dua_evening_peace",
                    title = "Evening Peace",
                    subtitle = "Dua as the sun sets and night begins",
                    arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ",
                    translation = "We have entered the evening, and the kingdom belongs to Allah.",
                    audioTtsText = "Amsayna wa amsal mulku lillah. All praise is due to Allah as night begins.",
                    category = "Evening",
                    imageRes = R.drawable.dua_evening_peace
                ),
                DuaItem(
                    id = "dua_evening_praise",
                    title = "Evening Gratitude",
                    subtitle = "Thank Allah for a safe and happy day",
                    arabicText = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ",
                    translation = "O Allah, by You we enter the evening and by You we enter the morning.",
                    audioTtsText = "Allahumma bika amsayna wa bika asbahna wa bika nahya wa bika namoot.",
                    category = "Evening",
                    imageRes = R.drawable.dua_evening_praise
                ),

// ==========================================
// 13. DAILY HABITS
// ==========================================
                DuaItem(
                    id = "dua_sneeze_reply",
                    title = "Replying to a Sneeze",
                    subtitle = "Say Yarhamukallah when a friend says Alhamdulillah",
                    arabicText = "يَرْحَمُكَ اللَّهُ",
                    translation = "May Allah have mercy on you.",
                    audioTtsText = "When your friend sneezes and says Alhamdulillah, reply: Yarhamukallah! May Allah have mercy on you.",
                    category = "Daily Habits",
                    imageRes = R.drawable.ic_sneeze
                ),
                DuaItem(
                    id = "dua_wearing_clothes",
                    title = "Wearing New Clothes",
                    subtitle = "Praise Allah when putting on your nice clothes",
                    arabicText = "الْحَمْدُ لِلَّهِ الَّذِي كَسَانِي هَذَا",
                    translation = "All praise is for Allah who clothed me with this.",
                    audioTtsText = "Alhamdulillahil ladhi kasani hadha. Praise be to Allah who blessed me with clean, cozy clothes.",
                    category = "Daily Habits",
                    imageRes = R.drawable.dua_wearing_clothes
                )


            )
        }
    }
}