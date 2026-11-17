package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.SalahStep
import com.ccx.islamic.kids.littledeenapp.databinding.ItemSalahStepRowBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener

class SalahStepAdapter(
    private val steps: List<SalahStep>,
    private val onStepClick: (SalahStep) -> Unit
) : RecyclerView.Adapter<SalahStepAdapter.StepViewHolder>() {

    inner class StepViewHolder(val binding: ItemSalahStepRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding = ItemSalahStepRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]

        with(holder.binding) {
            tvStepNum.text = "${step.stepNumber}"
            tvStepTitle.text = step.title
            tvStepSubtitle.text = step.subtitle

            cardSalahRow.setOnSafeClickListener {
                onStepClick(step)
            }
        }
    }

    override fun getItemCount(): Int = steps.size
}