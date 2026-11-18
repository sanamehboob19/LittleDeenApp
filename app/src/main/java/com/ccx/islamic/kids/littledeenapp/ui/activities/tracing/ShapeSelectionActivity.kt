package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing


import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.ShapeModel
import com.ccx.islamic.kids.littledeenapp.data.ShapeProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityShapeSelectionBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemShapeCardBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShapeSelectionActivity : BaseActivity<ActivityShapeSelectionBinding>(
    ActivityShapeSelectionBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.btnBack.setOnSafeClickListener { finish() }

        val isLand = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val span = if (isLand) 4 else 2

        binding.rvShapes.apply {
            layoutManager = GridLayoutManager(this@ShapeSelectionActivity, span)
            adapter = ShapeAdapter(ShapeProvider.shapes) { item ->
                val intent = Intent(this@ShapeSelectionActivity, ShapeTracingActivity::class.java).apply {
                    putExtra(ShapeTracingActivity.EXTRA_SELECTED_SHAPE, item)
                }
                startActivity(intent)
            }
        }
    }

    inner class ShapeAdapter(
        private val list: List<ShapeModel>,
        private val onClick: (ShapeModel) -> Unit
    ) : RecyclerView.Adapter<ShapeAdapter.ViewHolder>() {

        inner class ViewHolder(val b: ItemShapeCardBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemShapeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            with(holder.b) {
                tvShapeEmoji.text = item.emoji
                tvShapeName.text = item.name
                cardShapeTile.setCardBackgroundColor(Color.parseColor(item.glitterColorHex))
                cardShapeTile.setOnSafeClickListener { onClick(item) }
            }
        }

        override fun getItemCount(): Int = list.size
    }
}