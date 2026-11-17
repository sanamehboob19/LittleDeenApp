package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.ItemVideoCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.model.KidVideo



class VideoAdapter(
    private var videoList: List<KidVideo> = emptyList(),
    private val onVideoClick: (KidVideo) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]

        with(holder.binding) {
            tvVideoTitle.text = video.title
            tvChannelTitle.text = video.channelTitle
            tvBadgeLabel.text = video.badgeLabel

            // Automatically builds YouTube's high-res thumbnail from the videoId
//            val thumbnail = if (video.thumbnailUrl.isNotBlank() && video.thumbnailUrl.startsWith("http")) {
//                video.thumbnailUrl
//            } else {
//                "https://img.youtube.com/vi/${video.videoId}/hqdefault.jpg"
//            }
            val thumbnail = video.resolvedThumbnailUrl

            Glide.with(holder.itemView.context)
                .load(thumbnail)
                .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.bg_input_field)
                .error(R.drawable.bg_input_field)
                .into(ivThumbnail)

//            Glide.with(holder.itemView.context)
//                .load(thumbnail)
//                .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
//                .placeholder(R.drawable.bg_input_field)
//                .error(R.drawable.bg_input_field)
//                .into(ivThumbnail)

            cardVideoRoot.setOnSafeClickListener {
                onVideoClick(video)
            }
        }



    }

    fun submitList(newList: List<KidVideo>) {
        this.videoList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = videoList.size
}