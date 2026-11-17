package com.ccx.islamic.kids.littledeenapp.data



data class VideoCategoryChip(
    val id: String,
    val title: String,
    val emoji: String,
    val searchQuery: String,
    var isSelected: Boolean = false
) {
    companion object {

        fun getDefaultCategories(): List<VideoCategoryChip> {
            return listOf(

                VideoCategoryChip(
                    id = "cat_islamic",
                    title = "Islamic Cartoons",
                    emoji = "🕌",
                    searchQuery = "islamic cartoons for kids muslim kids islamic stories",
                    isSelected = true
                ),

                VideoCategoryChip(
                    id = "cat_manners",
                    title = "Achi Baatein",
                    emoji = "🌟",
                    searchQuery = "islamic good manners moral stories for kids sunnah habits kindness honesty"
                ),

                VideoCategoryChip(
                    id = "cat_learning",
                    title = "Learning",
                    emoji = "🔤",
                    searchQuery = "educational learning videos for kids alphabet phonics numbers colors shapes"
                ),

                VideoCategoryChip(
                    id = "cat_quran",
                    title = "Quran",
                    emoji = "📖",
                    searchQuery = "quran for kids short surahs quran learning tajweed islamic lessons"
                ),

                VideoCategoryChip(
                    id = "cat_cartoon",
                    title = "Cartoons",
                    emoji = "🎬",
                    searchQuery = "educational cartoons for kids animated stories fun learning cartoons"
                ),

                VideoCategoryChip(
                    id = "cat_poems",
                    title = "Poems & Nasheeds",
                    emoji = "🎵",
                    searchQuery = "halal kids poems vocal only islamic nasheeds for kids"
                )
            )
        }
    }
}
