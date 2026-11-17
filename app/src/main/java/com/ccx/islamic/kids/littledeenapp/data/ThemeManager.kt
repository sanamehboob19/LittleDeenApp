package com.ccx.islamic.kids.littledeenapp.data


import android.content.SharedPreferences
import android.graphics.Color
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val preferences: SharedPreferences
) {

    companion object {
        const val KEY_SELECTED_THEME_ID = "key_selected_theme_id"
        const val DEFAULT_THEME_ID = "theme_lavender"

        // 10 Curated Halal & Kids-Friendly Colors

        val THEME_PALETTES = listOf(

            // Original Themes
            AppThemeColor("theme_lavender", "Royal Lavender", Color.parseColor("#5C4DB1"), Color.parseColor("#EAE7FB")),
            AppThemeColor("theme_teal", "Ocean Teal", Color.parseColor("#00897B"), Color.parseColor("#E0F2F1")),
            AppThemeColor("theme_sky", "Sky Blue", Color.parseColor("#1E88E5"), Color.parseColor("#E3F2FD")),
            AppThemeColor("theme_mint", "Mint Green", Color.parseColor("#43A047"), Color.parseColor("#E8F5E9")),
            AppThemeColor("theme_amber", "Warm Gold", Color.parseColor("#FB8C00"), Color.parseColor("#FFF3E0")),
            AppThemeColor("theme_rose", "Sweet Rose", Color.parseColor("#E91E63"), Color.parseColor("#FCE4EC")),
            AppThemeColor("theme_coral", "Sunset Coral", Color.parseColor("#F4511E"), Color.parseColor("#FBE9E7")),
            AppThemeColor("theme_indigo", "Deep Indigo", Color.parseColor("#3949AB"), Color.parseColor("#E8EAF6")),
            AppThemeColor("theme_emerald", "Emerald Deen", Color.parseColor("#00796B"), Color.parseColor("#E0F2F1")),
            AppThemeColor("theme_purple", "Berry Purple", Color.parseColor("#8E24AA"), Color.parseColor("#F3E5F5")),

            // Premium Nature Themes
            AppThemeColor("theme_forest", "Forest Green", Color.parseColor("#2E7D32"), Color.parseColor("#E8F5E9")),
            AppThemeColor("theme_sage", "Soft Sage", Color.parseColor("#689F80"), Color.parseColor("#E8F3EC")),
            AppThemeColor("theme_olive", "Olive Garden", Color.parseColor("#827717"), Color.parseColor("#F1F8E9")),
            AppThemeColor("theme_jade", "Jade Green", Color.parseColor("#009688"), Color.parseColor("#E0F2F1")),

            // Beautiful Blue Themes
            AppThemeColor("theme_navy", "Midnight Navy", Color.parseColor("#283593"), Color.parseColor("#E8EAF6")),
            AppThemeColor("theme_ocean", "Deep Ocean", Color.parseColor("#0277BD"), Color.parseColor("#E1F5FE")),
            AppThemeColor("theme_aqua", "Fresh Aqua", Color.parseColor("#00ACC1"), Color.parseColor("#E0F7FA")),
            AppThemeColor("theme_cyan", "Crystal Cyan", Color.parseColor("#0097A7"), Color.parseColor("#E0F7FA")),
            AppThemeColor("theme_periwinkle", "Periwinkle", Color.parseColor("#6574CD"), Color.parseColor("#EEF0FF")),

            // Warm & Golden Themes
            AppThemeColor("theme_sunset", "Golden Sunset", Color.parseColor("#EF6C00"), Color.parseColor("#FFF3E0")),
            AppThemeColor("theme_honey", "Honey Gold", Color.parseColor("#C18B00"), Color.parseColor("#FFF8E1")),
            AppThemeColor("theme_mango", "Mango Yellow", Color.parseColor("#F9A825"), Color.parseColor("#FFFDE7")),
            AppThemeColor("theme_copper", "Soft Copper", Color.parseColor("#BF6D45"), Color.parseColor("#FBE9E7")),

            // Pink & Purple Themes
            AppThemeColor("theme_orchid", "Royal Orchid", Color.parseColor("#AB47BC"), Color.parseColor("#F3E5F5")),
            AppThemeColor("theme_plum", "Deep Plum", Color.parseColor("#6A1B9A"), Color.parseColor("#F3E5F5")),
            AppThemeColor("theme_blush", "Blush Pink", Color.parseColor("#D81B60"), Color.parseColor("#FCE4EC")),
            AppThemeColor("theme_peach", "Peach Blossom", Color.parseColor("#F06292"), Color.parseColor("#FCE4EC")),
            AppThemeColor("theme_magenta", "Soft Magenta", Color.parseColor("#C2185B"), Color.parseColor("#F8BBD0")),

            // Modern & Unique Themes
            AppThemeColor("theme_slate", "Modern Slate", Color.parseColor("#546E7A"), Color.parseColor("#ECEFF1")),
            AppThemeColor("theme_charcoal", "Elegant Charcoal", Color.parseColor("#455A64"), Color.parseColor("#ECEFF1")),
            AppThemeColor("theme_bronze", "Royal Bronze", Color.parseColor("#8D6E63"), Color.parseColor("#EFEBE9"))
        )

    }

    fun saveTheme(themeId: String) {
        preferences.edit().putString(KEY_SELECTED_THEME_ID, themeId).apply()
    }

    fun getSelectedTheme(): AppThemeColor {
        val savedId = preferences.getString(KEY_SELECTED_THEME_ID, DEFAULT_THEME_ID)
        return THEME_PALETTES.find { it.id == savedId } ?: THEME_PALETTES[0]
    }
}