package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup

import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.databinding.ItemThemeColorBoxBinding
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor

class ThemeColorAdapter(
    private val colorList: List<AppThemeColor>,
    private var selectedIndex: Int = 0,
    private val onColorSelected: (AppThemeColor) -> Unit
) : RecyclerView.Adapter<ThemeColorAdapter.ColorViewHolder>()
{

    inner class ColorViewHolder(val binding: ItemThemeColorBoxBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemThemeColorBoxBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val themeColor = colorList[position]
        val isSelected = position == selectedIndex

        with(holder.binding) {
            // Set card background to the primary theme color
            cardColorBox.setCardBackgroundColor(themeColor.primaryColor)
            tvColorName.text = themeColor.name

            if (isSelected) {
                cardColorBox.strokeColor = Color.WHITE
                cardColorBox.strokeWidth = holder.itemView.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._3sdp)
                ivSelectedTick.visible()
            } else {
                cardColorBox.strokeColor = Color.TRANSPARENT
                cardColorBox.strokeWidth = 0
                ivSelectedTick.gone()
            }

            root.setOnClickListener {
                if (selectedIndex != holder.adapterPosition) {
                    val prevIndex = selectedIndex
                    selectedIndex = holder.adapterPosition
                    notifyItemChanged(prevIndex)
                    notifyItemChanged(selectedIndex)
                    onColorSelected(themeColor)
                }
            }
        }
    }

    fun getSelectedTheme(): AppThemeColor = colorList[selectedIndex]

    override fun getItemCount(): Int = colorList.size
}