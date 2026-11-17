package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing


import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.NumberModel
import com.ccx.islamic.kids.littledeenapp.data.NumberProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityNumberSelectionBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemNumberCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NumberSelectionActivity : BaseActivity<ActivityNumberSelectionBinding>(
    ActivityNumberSelectionBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)

        binding.btnBack.setOnSafeClickListener { finish() }

        val isLand = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val span = if (isLand) 6 else 4

        binding.rvNumbers.apply {
            layoutManager = GridLayoutManager(this@NumberSelectionActivity, span)
            adapter = NumberAdapter(NumberProvider.numbers) { item ->
                val intent = Intent(this@NumberSelectionActivity, NumberTracingActivity::class.java).apply {
                    putExtra(NumberTracingActivity.EXTRA_SELECTED_NUMBER, item)
                }
                startActivity(intent)
            }
        }
    }

    inner class NumberAdapter(
        private val list: List<NumberModel>,
        private val onClick: (NumberModel) -> Unit
    ) : RecyclerView.Adapter<NumberAdapter.ViewHolder>() {

        inner class ViewHolder(val b: ItemNumberCardBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemNumberCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            with(holder.b) {
                tvNumber.text = item.text
                tvNumberWord.text = item.word
                cardNumberTile.setCardBackgroundColor(Color.parseColor(item.glitterColorHex))
                cardNumberTile.setOnSafeClickListener { onClick(item) }
            }
        }

        override fun getItemCount(): Int = list.size
    }
}