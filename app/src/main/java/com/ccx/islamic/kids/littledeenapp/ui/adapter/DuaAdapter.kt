package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.ItemDuaCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.data.DuaItem
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor


class DuaAdapter(
    private var duaList: List<DuaItem> = emptyList(),
    private var activeTheme: AppThemeColor, // 👈 1. Added activeTheme
    private val onPlayAudioClick: (DuaItem) -> Unit,
    private val onCardClick: (DuaItem) -> Unit
) : RecyclerView.Adapter<DuaAdapter.DuaViewHolder>() {

    inner class DuaViewHolder(val binding: ItemDuaCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuaViewHolder {
        val binding = ItemDuaCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DuaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DuaViewHolder, position: Int) {
        val dua = duaList[position]

        with(holder.binding) {
            tvDuaTitle.text = dua.title
            tvDuaSubtitle.text = dua.subtitle
            ivDuaIllustration.setImageResource(dua.imageRes)

            // 🎯 2. Tint Play Button background with the selected theme color!
            cardPlayDua.setCardBackgroundColor(activeTheme.primaryColor)

            cardPlayDua.setOnSafeClickListener {
                onPlayAudioClick(dua)
            }

            cardDuaRoot.setOnSafeClickListener {
                onCardClick(dua)
            }
        }
    }

    // 🎯 Allows updating theme if child changes color in settings
    fun updateTheme(newTheme: AppThemeColor) {
        this.activeTheme = newTheme
        notifyDataSetChanged()
    }

    fun submitList(list: List<DuaItem>) {
        this.duaList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = duaList.size
}