package com.ccx.islamic.kids.littledeenapp.data


import android.os.Parcelable
import androidx.annotation.RawRes
import com.ccx.islamic.kids.littledeenapp.R
import kotlinx.parcelize.Parcelize



@Parcelize
data class KalimaItem(
    val id: Int,
    val name: String,
    val subtitle: String,
    val arabicText: String,
    val transliteration: String,
    val englishTranslation: String,
    val urduTranslation: String,
    @RawRes val audioRes: Int // 👈 Instant offline audio resource
) : Parcelable {
    companion object {
        fun getAllKalimas(): List<KalimaItem> {
            return listOf(
                KalimaItem(
                    id = 1,
                    name = "First Kalima: Tayyibah",
                    subtitle = "Word of Purity",
                    arabicText = "لَا إِلَهَ إِلَّا اللَّهُ مُحَمَّدٌ رَسُولُ اللَّهِ",
                    transliteration = "La ilaha illallahu Muhammadur Rasulullah",
                    englishTranslation = "There is none worthy of worship except Allah, Muhammad is the Messenger of Allah.",
                    urduTranslation = "اللہ کے سوا کوئی معبود نہیں، محمد اللہ کے رسول ہیں۔",
                    audioRes = R.raw.kalima_1
                ),
                KalimaItem(
                    id = 2,
                    name = "Second Kalima: Shahadah",
                    subtitle = "Word of Testimony",
                    arabicText = "أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                    transliteration = "Ashhadu an la ilaha illallahu wahdahu la sharika lahu, wa ashhadu anna Muhammadan 'abduhu wa Rasuluhu",
                    englishTranslation = "I bear witness that none has the right to be worshipped except Allah, alone without partner, and I bear witness that Muhammad is His servant and Messenger.",
                    urduTranslation = "میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں، اور میں گواہی دیتا ہوں کہ محمد صلی اللہ علیہ وسلم اس کے بندے اور رسول ہیں۔",
                    audioRes = R.raw.kalima_2
                ),
                KalimaItem(
                    id = 3,
                    name = "Third Kalima: Tamjeed",
                    subtitle = "Word of Majesty",
                    arabicText = "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ، وَلَا إِلَهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
                    transliteration = "Subhanallahi walhamdulillahi, wa la ilaha illallahu, wallahu akbar, wa la hawla wa la quwwata illa billahil aliyyil azeem",
                    englishTranslation = "Glory be to Allah, and all praise is for Allah, and there is none worthy of worship except Allah, and Allah is the greatest. And there is no might nor power except with Allah, the Most High, the Supreme.",
                    urduTranslation = "اللہ پاک ہے اور تمام تعریفیں اللہ ہی کے لیے ہیں، اور اللہ کے سوا کوئی معبود نہیں، اور اللہ سب سے بڑا ہے، اور گناہوں سے بچنے کی طاقت اور نیکی کرنے کی توفیق اللہ ہی کی طرف سے ہے جو بہت بلند اور عظمت والا ہے۔",
                    audioRes = R.raw.kalima_3
                ),
                KalimaItem(
                    id = 4,
                    name = "Fourth Kalima: Tawheed",
                    subtitle = "Word of Oneness",
                    arabicText = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِیکَ لَہُ، لَہُ الْمُلْکُ وَلَہُ الْحَمْدُ، یُحْیِی وَیُمِیْتُ، وَھُوَ حَیٌّ لَّا یَمُوْتُ اَبَدًا اَبَدًا، ذُو الْجَلَالِ وَالْاِکْرَامِ، بِیَدِہِ الْخَیْرُ، وَھُوَ عَلٰی کُلِّ شَیْءٍ قَدِیْرٌ",
                    transliteration = "La ilaha illallahu wahdahu la sharika lahu, lahul mulku wa lahul hamdu, yuhyi wa yumitu, wa huwa hayyun la yamutu abadan abada...",
                    englishTranslation = "There is none worthy of worship except Allah, alone without partner. His is the kingdom and His is the praise...",
                    urduTranslation = "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں، اسی کی بادشاہی ہے اور اسی کے لیے تمام تعریفیں ہیں...",
                    audioRes = R.raw.kalima_4
                ),
                KalimaItem(
                    id = 5,
                    name = "Fifth Kalima: Astaghfar",
                    subtitle = "Word of Seeking Forgiveness",
                    arabicText = "أَسْتَغْفِرُ اللَّهَ رَبِّي مِنْ كُلِّ ذَنْبٍ أَذْنَبْتُهُ عَمْدًا أَوْ خَطَأً، سِرًّا أَوْ عَلَانِيَةً، وَأَتُوبُ إِلَيْهِ مِنَ الذَّنْبِ الَّذِي أَعْلَمُ وَمِنَ الذَّنْبِ الَّذِي لَا أَعْلَمُ...",
                    transliteration = "Astaghfirullaha Rabbi min kulli dhambin adhnabtuhu 'amadan aw khata'an...",
                    englishTranslation = "I seek forgiveness from Allah, my Lord, from every sin I committed knowingly or unknowingly...",
                    urduTranslation = "میں اللہ سے اپنے تمام گناہوں کی بخشش مانگتا ہوں جو میں نے جان بوجھ کر کیے یا بھول کر کیے...",
                    audioRes = R.raw.kalima_5
                ),
                KalimaItem(
                    id = 6,
                    name = "Sixth Kalima: Radd-e-Kufr",
                    subtitle = "Repudiation of Disbelief",
                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ، تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ...",
                    transliteration = "Allahumma inni a'udhu bika min an ushrika bika shay'an wa ana a'lamu bihi...",
                    englishTranslation = "O Allah! I seek refuge in You from knowingly associating anything with You. I seek Your forgiveness for what I do unknowingly...",
                    urduTranslation = "اے اللہ! میں تیری پناہ مانگتا ہوں اس بات سے کہ میں جان بوجھ کر کسی کو تیرا شریک ٹھہراؤں...",
                    audioRes = R.raw.kalima_6
                )
            )
        }
    }
}






//@Parcelize
//data class KalimaItem(
//    val id: Int,
//    val name: String,
//    val subtitle: String,
//    val arabicText: String,
//    val transliteration: String,
//    val englishTranslation: String,
//    val urduTranslation: String
//) : Parcelable {
//    companion object {
//        fun getAllKalimas(): List<KalimaItem> {
//            return listOf(
//                KalimaItem(
//                    id = 1,
//                    name = "First Kalima: Tayyibah",
//                    subtitle = "Word of Purity",
//                    arabicText = "لَا إِلَهَ إِلَّا اللَّهُ مُحَمَّدٌ رَسُولُ اللَّهِ",
//                    transliteration = "La ilaha illallahu Muhammadur Rasulullah",
//                    englishTranslation = "There is none worthy of worship except Allah, Muhammad is the Messenger of Allah.",
//                    urduTranslation = "اللہ کے سوا کوئی معبود نہیں، محمد اللہ کے رسول ہیں۔"
//                ),
//                KalimaItem(
//                    id = 2,
//                    name = "Second Kalima: Shahadah",
//                    subtitle = "Word of Testimony",
//                    arabicText = "أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
//                    transliteration = "Ashhadu an la ilaha illallahu wahdahu la sharika lahu, wa ashhadu anna Muhammadan 'abduhu wa Rasuluhu",
//                    englishTranslation = "I bear witness that none has the right to be worshipped except Allah, alone without partner, and I bear witness that Muhammad is His servant and Messenger.",
//                    urduTranslation = "میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں، اور میں گواہی دیتا ہوں کہ محمد صلی اللہ علیہ وسلم اس کے بندے اور رسول ہیں۔"
//                ),
//                KalimaItem(
//                    id = 3,
//                    name = "Third Kalima: Tamjeed",
//                    subtitle = "Word of Majesty",
//                    arabicText = "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ، وَلَا إِلَهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
//                    transliteration = "Subhanallahi walhamdulillahi, wa la ilaha illallahu, wallahu akbar, wa la hawla wa la quwwata illa billahil aliyyil azeem",
//                    englishTranslation = "Glory be to Allah, and all praise is for Allah, and there is none worthy of worship except Allah, and Allah is the greatest. And there is no might nor power except with Allah, the Most High, the Supreme.",
//                    urduTranslation = "اللہ پاک ہے اور تمام تعریفیں اللہ ہی کے لیے ہیں، اور اللہ کے سوا کوئی معبود نہیں، اور اللہ سب سے بڑا ہے، اور گناہوں سے بچنے کی طاقت اور نیکی کرنے کی توفیق اللہ ہی کی طرف سے ہے جو بہت بلند اور عظمت والا ہے۔"
//                ),
//                KalimaItem(
//                    id = 4,
//                    name = "Fourth Kalima: Tawheed",
//                    subtitle = "Word of Oneness",
//                    arabicText = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، يُحْيِي وَيُمِيتُ، وَهُوَ حَيُّ لَا يَمُوتُ أَبَدًا أَبَدًا، ذُو الْجَلَالِ وَالْإِكْرَامِ، بِيَدِهِ الْخَيْرُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
//                    transliteration = "La ilaha illallahu wahdahu la sharika lahu, lahul mulku wa lahul hamdu, yuhyi wa yumitu, wa huwa hayyun la yamutu abadan abada, dhul jalali wal ikram, biyadihil khair, wa huwa 'ala kulli shay'in qadeer",
//                    englishTranslation = "There is none worthy of worship except Allah, alone without partner. His is the kingdom and His is the praise. He gives life and causes death, and He is Alive and does not die ever, ever. Possessor of Majesty and Honor. In His hand is all goodness, and He has power over all things.",
//                    urduTranslation = "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں، اسی کی بادشاہی ہے اور اسی کے لیے تمام تعریفیں ہیں، وہی زندہ کرتا ہے اور وہی مارتا ہے، اور وہ زندہ ہے جسے کبھی موت نہیں آئے گی، بڑے جلال اور بزرگی والا ہے، اسی کے ہاتھ میں تمام بھلائی ہے اور وہ ہر چیز پر قادر ہے۔"
//                ),
//                KalimaItem(
//                    id = 5,
//                    name = "Fifth Kalima: Radd al-Kufr",
//                    subtitle = "Word of Rejection of Disbelief",
//                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا أَنَا أَعْلَمُ بِهِ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ، تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ وَالْكِذْبِ وَالْغِيبَةِ وَالْبِدْعَةِ وَالنَّمِيمَةِ وَالْفَوَاحِشِ وَالْبُهْتَانِ وَالْمَعَاصِي كُلِّهَا، وَأَسْلَمْتُ وَأَقُولُ لَا إِلَهَ إِلَّا اللَّهُ مُحَمَّدٌ رَسُولُ اللَّهِ",
//                    transliteration = "Allahumma inni a'udhu bika min an ushrika bika shay'an ana a'lamu bihi, wa astaghfiruka lima la a'lamu bihi...",
//                    englishTranslation = "O Allah! I seek protection in You from knowingly associating any partner with You, and I seek Your forgiveness for what I do not know...",
//                    urduTranslation = "اے اللہ! میں تیری پناه مانگتا ہوں اس بات سے کہ میں جانے بوجھ کر تیرا کسی کو شریک بناؤں، اور میں تجھ سے بخشش مانگتا ہوں اس کی جو میرے علم میں نہیں۔"
//                ),
//                KalimaItem(
//                    id = 6,
//                    name = "Sixth Kalima: Radd-e-Kufr",
//                    subtitle = "Repudiation of Disbelief",
//
//                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ، تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ وَالْكِذْبِ وَالْغِيبَةِ وَالْبِدْعَةِ وَالنَّمِيمَةِ وَالْفَوَاحِشِ وَالْبُهْتَانِ وَالْمَعَاصِي كُلِّهَا، وَأَسْلَمْتُ وَأَقُولُ لَا إِلَٰهَ إِلَّا اللَّهُ مُحَمَّدٌ رَسُولُ اللَّهِ",
//
//                    transliteration = "Allahumma inni a'udhu bika min an ushrika bika shay'an wa ana a'lamu bihi, wa astaghfiruka lima la a'lamu bihi, tubtu anhu wa tabarra'tu minal-kufri wash-shirki wal-kidhbi wal-ghibati wal-bid'ati wan-namimati wal-fawahishi wal-buhtani wal-ma'asi kulliha, wa aslamtu wa aqulu la ilaha illallahu Muhammadur Rasulullah.",
//
//                    englishTranslation = "O Allah! I seek refuge in You from knowingly associating anything with You. I seek Your forgiveness for what I do unknowingly. I repent from it and disassociate myself from disbelief, polytheism, lying, backbiting, innovation, tale-bearing, indecency, slander, and all sins. I submit to You and declare that there is no god except Allah, and Muhammad is the Messenger of Allah.",
//
//                    urduTranslation = "اے اللہ! میں تیری پناہ مانگتا ہوں اس بات سے کہ میں جان بوجھ کر کسی کو تیرا شریک ٹھہراؤں، اور میں تجھ سے ان باتوں کی بخشش مانگتا ہوں جنہیں میں نہیں جانتا۔ میں ان سے توبہ کرتا ہوں اور کفر، شرک، جھوٹ، غیبت، بدعت، چغلی، بے حیائی، بہتان اور تمام گناہوں سے بیزاری اختیار کرتا ہوں۔ میں اسلام قبول کرتا ہوں اور اقرار کرتا ہوں کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اللہ کے رسول ہیں۔"
//                )
//
//
//            )
//        }
//    }
//}