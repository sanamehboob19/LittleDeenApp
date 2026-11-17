package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.databinding.ItemColorSwatchBinding

class ColorPaletteAdapter(
    private val colors: List<Int>,
    private var selectedColor: Int,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorPaletteAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(val binding: ItemColorSwatchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorSwatchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        val isSelected = color == selectedColor

        with(holder.binding) {
            // Set circle background color
            cardColorSwatch.setCardBackgroundColor(color)

            // Show selection indicator
            if (isSelected) {
                cardSelectionRing.strokeColor = color
                cardSelectionRing.visibility = View.VISIBLE
                ivCheckmark.visibility = View.VISIBLE

                // Contrast checkmark: if color is light yellow or white, make checkmark dark
                val luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
                ivCheckmark.imageTintList = ColorStateList.valueOf(
                    if (luminance > 0.65) Color.BLACK else Color.WHITE
                )
            } else {
                cardSelectionRing.visibility = View.INVISIBLE
                ivCheckmark.visibility = View.GONE
            }

            root.setOnClickListener {
                val previousColor = selectedColor
                selectedColor = color

                // Rebind only previous and new selected positions
                val prevPos = colors.indexOf(previousColor)
                if (prevPos != -1) notifyItemChanged(prevPos)
                notifyItemChanged(holder.bindingAdapterPosition)

                onColorSelected(color)
            }
        }
    }

    override fun getItemCount(): Int = colors.size
}