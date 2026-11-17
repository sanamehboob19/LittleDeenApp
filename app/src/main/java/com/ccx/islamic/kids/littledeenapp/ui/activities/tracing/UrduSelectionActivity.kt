package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.UrduAlphabetModel
import com.ccx.islamic.kids.littledeenapp.data.UrduAlphabetProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityUrduSelectionBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemUrduAlphabetCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UrduSelectionActivity : BaseActivity<ActivityUrduSelectionBinding>(
    ActivityUrduSelectionBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)

        binding.btnBack.setOnSafeClickListener { finish() }

        val isLand = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val span = if (isLand) 6 else 4

        binding.rvUrduLetters.apply {
            layoutManager = GridLayoutManager(this@UrduSelectionActivity, span)
            adapter = UrduAdapter(UrduAlphabetProvider.letters) { item ->
                val intent = Intent(this@UrduSelectionActivity, UrduTracingActivity::class.java).apply {
                    putExtra(UrduTracingActivity.EXTRA_SELECTED_URDU_LETTER, item)
                }
                startActivity(intent)
            }
        }
    }

    inner class UrduAdapter(
        private val list: List<UrduAlphabetModel>,
        private val onClick: (UrduAlphabetModel) -> Unit
    ) : RecyclerView.Adapter<UrduAdapter.ViewHolder>() {

        inner class ViewHolder(val b: ItemUrduAlphabetCardBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemUrduAlphabetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            with(holder.b) {
                tvLetter.text = item.letter
                tvWord.text = "${item.word} (${item.wordEnglish}) ${item.emoji}"
                cardLetterTile.setCardBackgroundColor(Color.parseColor(item.glitterColorHex))
                cardLetterTile.setOnSafeClickListener { onClick(item) }
            }
        }

        override fun getItemCount(): Int = list.size
    }
}