package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.databinding.ItemOnboardingSlideBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.model.OnboardingSlide



class OnboardingAdapter(
    private val slides: List<OnboardingSlide>
) : RecyclerView.Adapter<OnboardingAdapter.SlideViewHolder>()
{

    inner class SlideViewHolder(val binding: ItemOnboardingSlideBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val binding = ItemOnboardingSlideBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val slide = slides[position]
        with(holder.binding) {
            tvSlideTitle.setText(slide.titleRes)
            tvSlideDesc.setText(slide.descRes)
            ivSlideImage.setImageResource(slide.imageRes)
        }
    }

    override fun getItemCount(): Int = slides.size
}
