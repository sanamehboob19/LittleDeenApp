package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.VideoCategoryChip
import com.ccx.islamic.kids.littledeenapp.databinding.ItemHubCategoryCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener

class HubCategoryAdapter(
    private val categories: List<VideoCategoryChip>,
    private val onCategoryClick: (VideoCategoryChip) -> Unit
) : RecyclerView.Adapter<HubCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemHubCategoryCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemHubCategoryCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        with(holder.binding) {
            tvCatEmoji.text = category.emoji
            tvCatTitle.text = category.title

            cardHubCategory.setOnSafeClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun getItemCount(): Int = categories.size
}