package com.ccx.islamic.kids.littledeenapp.ui.activities.pixel_art


import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.PixelArtModel
import com.ccx.islamic.kids.littledeenapp.data.PixelArtProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityPixelSelectionBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemPixelCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PixelSelectionActivity : BaseActivity<ActivityPixelSelectionBinding>(
    ActivityPixelSelectionBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.btnBack.setOnSafeClickListener { finish() }

        val isLand = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val span = if (isLand) 3 else 2

        binding.rvPixelModels.apply {
            layoutManager = GridLayoutManager(this@PixelSelectionActivity, span)
            adapter = PixelAdapter(PixelArtProvider.models) { selected ->
                val intent = Intent(this@PixelSelectionActivity, PixelArtActivity::class.java).apply {
                    putExtra(PixelArtActivity.EXTRA_SELECTED_MODEL, selected)
                }
                startActivity(intent)
            }
        }
    }

    inner class PixelAdapter(
        private val list: List<PixelArtModel>,
        private val onClick: (PixelArtModel) -> Unit
    ) : RecyclerView.Adapter<PixelAdapter.ViewHolder>() {

        inner class ViewHolder(val b: ItemPixelCardBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemPixelCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            with(holder.b) {
                tvTitle.text = item.title
                previewGridView.loadModel(item, isTargetPreview = true)
                cardPixelRoot.setOnSafeClickListener { onClick(item) }
            }
        }

        override fun getItemCount(): Int = list.size
    }
}