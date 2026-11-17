package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.data.StoryItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager

import com.ccx.islamic.kids.littledeenapp.databinding.ItemStoryRowBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener



class StoryListAdapter(
    private val stories: List<StoryItem>,
    private val themeManager: ThemeManager,
    private val onStoryClick: (StoryItem) -> Unit
) : RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>()
{

    class StoryViewHolder(val binding: ItemStoryRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        val theme = themeManager.getSelectedTheme()

        with(holder.binding) {
            tvStoryTitle.text = story.title
            tvStoryTheme.text = story.theme

            // --- FIX: Use the imageRes from the data class ---
            ivStoryThumb.setImageResource(story.imageRes)

            // OR if you want to use Glide:
            // Glide.with(ivStoryThumb.context).load(story.imageRes).into(ivStoryThumb)

            // Dynamic Theming
            cardThemeBadge.setCardBackgroundColor(theme.lightColor)
            tvStoryTheme.setTextColor(theme.primaryColor)
            cardStoryRow.strokeWidth = 2

            cardStoryRow.setOnSafeClickListener {
                onStoryClick(story)
            }
        }
    }

    override fun getItemCount(): Int = stories.size
}