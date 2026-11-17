package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.ItemHistoryCompactBinding
import com.ccx.islamic.kids.littledeenapp.db.SavedVideoEntity
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener

class HistoryAdapter(
    private var historyList: List<SavedVideoEntity> = emptyList(),
    private val onItemClick: (SavedVideoEntity) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemHistoryCompactBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryCompactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        val context = holder.itemView.context

        with(holder.binding) {
            tvHistoryTitle.text = item.title
            tvHistoryChannel.text = item.channelTitle

            val timeAgo = DateUtils.getRelativeTimeSpanString(
                item.lastWatchedAt,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
            tvHistoryTime.text = timeAgo

            Glide.with(context)
                .load(item.thumbnailUrl)
                .placeholder(R.drawable.bg_input_field)
                .into(ivHistoryThumb)

            cardHistoryRoot.setOnSafeClickListener {
                onItemClick(item)
            }
        }
    }

    fun submitList(newList: List<SavedVideoEntity>) {
        this.historyList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = historyList.size
}