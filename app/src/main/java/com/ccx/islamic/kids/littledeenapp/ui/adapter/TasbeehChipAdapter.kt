package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.databinding.ItemCategoryChipBinding
import com.ccx.islamic.kids.littledeenapp.db.TasbeehEntity
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor

class TasbeehChipAdapter(
    private var list: List<TasbeehEntity> = emptyList(),
    private var activeTheme: AppThemeColor,
    private val onSelected: (TasbeehEntity) -> Unit
) : RecyclerView.Adapter<TasbeehChipAdapter.ChipViewHolder>() {

    private var selectedId: String = ""

    inner class ChipViewHolder(val binding: ItemCategoryChipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemCategoryChipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val item = list[position]
        val isSelected = item.dhikrId == selectedId

        with(holder.binding) {
            tvChipEmoji.text = "📿"
            tvChipTitle.text = item.englishText.substringBefore("(")

            if (isSelected) {
                cardChip.setCardBackgroundColor(activeTheme.primaryColor)
                cardChip.strokeColor = activeTheme.primaryColor
                tvChipTitle.setTextColor(Color.WHITE)
            } else {
                cardChip.setCardBackgroundColor(Color.parseColor("#F5F5F5"))
                cardChip.strokeColor = Color.parseColor("#E0E0E0")
                tvChipTitle.setTextColor(Color.parseColor("#333333"))
            }

            root.setOnClickListener {
                selectedId = item.dhikrId
                notifyDataSetChanged()
                onSelected(item)
            }
        }
    }

    fun setSelected(id: String) {
        selectedId = id
        notifyDataSetChanged()
    }

    fun submitList(newList: List<TasbeehEntity>) {
        this.list = newList
        if (selectedId.isEmpty() && newList.isNotEmpty()) {
            selectedId = newList[0].dhikrId
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}