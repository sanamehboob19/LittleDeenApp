package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.VideoCategoryChip
import com.ccx.islamic.kids.littledeenapp.databinding.ItemCategoryChipBinding
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor


class CategoryChipAdapter(
    private val categories: List<VideoCategoryChip>,
    private var activeTheme: AppThemeColor,
    private val onCategorySelected: (VideoCategoryChip) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.ChipViewHolder>() {

    private var selectedIndex = 0

    inner class ChipViewHolder(val binding: ItemCategoryChipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemCategoryChipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChipViewHolder(binding)
    }

    fun updateSelection(newIndex: Int) {
        if (newIndex in 0 until categories.size && selectedIndex != newIndex) {
            val prevIndex = selectedIndex
            selectedIndex = newIndex
            notifyItemChanged(prevIndex)
            notifyItemChanged(selectedIndex)
        }
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val chip = categories[position]
        val isSelected = position == selectedIndex
        val context = holder.itemView.context

        with(holder.binding) {
            tvChipEmoji.text = chip.emoji
            tvChipTitle.text = chip.title

            if (isSelected) {
                cardChip.setCardBackgroundColor(activeTheme.primaryColor)
                cardChip.strokeColor = activeTheme.primaryColor
                tvChipTitle.setTextColor(Color.WHITE)
            } else {
                cardChip.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.color_surface)
                )
                cardChip.strokeColor = ContextCompat.getColor(context, R.color.color_border)
                tvChipTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.color_text_primary)
                )
            }

            root.setOnClickListener {
                if (selectedIndex != holder.adapterPosition) {
                    val prevIndex = selectedIndex
                    selectedIndex = holder.adapterPosition
                    notifyItemChanged(prevIndex)
                    notifyItemChanged(selectedIndex)
                    onCategorySelected(chip)
                }
            }
        }
    }

    fun updateTheme(newTheme: AppThemeColor) {
        this.activeTheme = newTheme
        notifyDataSetChanged()
    }

    fun getSelectedCategory(): VideoCategoryChip = categories[selectedIndex]

    override fun getItemCount(): Int = categories.size
}