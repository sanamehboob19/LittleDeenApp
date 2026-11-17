package com.ccx.islamic.kids.littledeenapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.SurahItem
import com.ccx.islamic.kids.littledeenapp.databinding.ItemSurahRowBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener

class SurahListAdapter(
    private val surahs: List<SurahItem>,
    private val onSurahClick: (SurahItem) -> Unit
) : RecyclerView.Adapter<SurahListAdapter.SurahViewHolder>()
{

    inner class SurahViewHolder(val binding: ItemSurahRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val binding = ItemSurahRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SurahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val surah = surahs[position]

        with(holder.binding) {
            tvSurahNum.text = "${surah.id}"
            tvSurahName.text = surah.name
            tvArabicName.text = surah.arabicName
            tvSurahSubtitle.text = "${surah.meaning} • ${surah.totalVerses} Verses"

            cardSurahRow.setOnSafeClickListener {
                onSurahClick(surah)
            }
        }
    }

    override fun getItemCount(): Int = surahs.size
}