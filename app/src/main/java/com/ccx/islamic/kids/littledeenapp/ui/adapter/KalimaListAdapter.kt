package com.ccx.islamic.kids.littledeenapp.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.KalimaItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ItemKalimaRowBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener

class KalimaListAdapter(
    private val kalimas: List<KalimaItem>,
    private val themeManager: ThemeManager,
    private val onKalimaClick: (KalimaItem) -> Unit
) : RecyclerView.Adapter<KalimaListAdapter.KalimaViewHolder>() {

    class KalimaViewHolder(val binding: ItemKalimaRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KalimaViewHolder {
        val binding = ItemKalimaRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return KalimaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KalimaViewHolder, position: Int) {
        val kalima = kalimas[position]
        val theme = themeManager.getSelectedTheme()

        with(holder.binding) {
            tvKalimaNum.text = "${kalima.id}"
            tvKalimaName.text = kalima.name
            tvKalimaSubtitle.text = kalima.subtitle

            // 1. Badge Background & Stroke
            // Assuming your badge card root is inside the binding
            // Note: If the badge card doesn't have an ID in your XML,
            // add android:id="@+id/cardBadge" to it in item_kalima_row.xml
            cardBadge.setCardBackgroundColor(theme.lightColor)
//            cardBadge.strokeColor = ColorStateList.valueOf(theme.primaryColor)
            tvKalimaNum.setTextColor(theme.primaryColor)

            // 2. Play Arrow / Container Tints
            // Assuming your arrow container has id="@+id/cardPlayArrow"
            cardPlayArrow.setCardBackgroundColor(theme.primaryColor)

            cardKalimaRow.setOnSafeClickListener {
                onKalimaClick(kalima)
            }
        }
    }

    override fun getItemCount(): Int = kalimas.size
}