package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing


import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.AlphabetModel
import com.ccx.islamic.kids.littledeenapp.data.AlphabetProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityAlphabetSelectionBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemAlphabetCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlphabetSelectionActivity : BaseActivity<ActivityAlphabetSelectionBinding>(
    ActivityAlphabetSelectionBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)

        binding.btnBack.setOnSafeClickListener { finish() }

        val isLand = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val span = if (isLand) 6 else 4

        binding.rvAlphabets.apply {
            layoutManager = GridLayoutManager(this@AlphabetSelectionActivity, span)
            adapter = AlphabetAdapter(AlphabetProvider.letters) { item ->
                val intent = Intent(this@AlphabetSelectionActivity, AlphabetTracingActivity::class.java).apply {
                    putExtra(AlphabetTracingActivity.EXTRA_SELECTED_LETTER, item)
                }
                startActivity(intent)
            }
        }
    }

    inner class AlphabetAdapter(
        private val list: List<AlphabetModel>,
        private val onClick: (AlphabetModel) -> Unit
    ) : RecyclerView.Adapter<AlphabetAdapter.ViewHolder>() {

        inner class ViewHolder(val b: ItemAlphabetCardBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemAlphabetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            with(holder.b) {
                tvLetter.text = item.letter
                tvWord.text = "${item.word} ${item.emoji}"
                cardLetterTile.setCardBackgroundColor(Color.parseColor(item.glitterColorHex))
                cardLetterTile.setOnSafeClickListener { onClick(item) }
            }
        }

        override fun getItemCount(): Int = list.size
    }
}