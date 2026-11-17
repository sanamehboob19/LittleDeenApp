package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.ItemAvatarChoiceBinding
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.visible

class AvatarAdapter(
    private val avatarList: List<Int>,
    private var selectedPosition: Int = 0,
    private val onAvatarSelected: (Int) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>()
{

    inner class AvatarViewHolder(val binding: ItemAvatarChoiceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarChoiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarRes = avatarList[position]
        val isSelected = position == selectedPosition
        val context = holder.itemView.context

        with(holder.binding) {
            ivAvatarImg.setImageResource(avatarRes)

            if (isSelected) {
                cardAvatar.strokeColor = ContextCompat.getColor(context, R.color.color_primary)
                cardAvatar.strokeWidth = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp)
                ivSelectedCheck.visible()
            } else {
                cardAvatar.strokeColor = ContextCompat.getColor(context, R.color.color_border)
                cardAvatar.strokeWidth = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
                ivSelectedCheck.gone()
            }

            root.setOnClickListener {
                if (selectedPosition != holder.adapterPosition) {
                    val prevPos = selectedPosition
                    selectedPosition = holder.adapterPosition
                    notifyItemChanged(prevPos)
                    notifyItemChanged(selectedPosition)
                    onAvatarSelected(avatarRes)
                }
            }
        }
    }

    fun getSelectedAvatar(): Int = avatarList[selectedPosition]

    override fun getItemCount(): Int = avatarList.size
}