package com.ccx.islamic.kids.littledeenapp.ui.activities.drawingPad

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityDrawPadBinding
import com.ccx.islamic.kids.littledeenapp.databinding.DialogColorPaletteBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemColorSwatchBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.customViews.DrawingView
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ColorPaletteDialogFragment
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




@AndroidEntryPoint
class DrawPadActivity : BaseActivity<ActivityDrawPadBinding>(ActivityDrawPadBinding::inflate) {

    @Inject
    lateinit var themeManager: ThemeManager

    private var activeColor: Int = Color.parseColor("#E91E63")

    companion object {
        private var cachedDrawingBitmap: Bitmap? = null
    }

    private val thirtyColors = listOf(
        Color.parseColor("#000000"), // 1. Black
        Color.parseColor("#FF4081"), // 2. Hot Pink
        Color.parseColor("#455A64"), // 3. Slate Grey
        Color.parseColor("#FFD700"), // 4. Gold
        Color.parseColor("#795548"), // 5. Brown
        Color.parseColor("#00BCD4"), // 6. Cyan
        Color.parseColor("#8D6E63"), // 7. Light Brown
        Color.parseColor("#7CFC00"), // 8. Lawn Green
        Color.parseColor("#B71C1C"), // 9. Dark Red
        Color.parseColor("#9C27B0"), // 10. Purple
        Color.parseColor("#FF5722"), // 11. Deep Orange
        Color.parseColor("#87CEEB"), // 12. Sky Blue
        Color.parseColor("#FF9800"), // 13. Orange
        Color.parseColor("#A0522D"), // 14. Sienna
        Color.parseColor("#FFA726"), // 15. Warm Orange
        Color.parseColor("#40E0D0"), // 16. Turquoise
        Color.parseColor("#FFB74D"), // 17. Peach
        Color.parseColor("#6A5ACD"), // 18. Slate Blue
        Color.parseColor("#FFEB3B"), // 19. Yellow
        Color.parseColor("#E6E6FA"), // 20. Lavender
        Color.parseColor("#FFF176"), // 21. Soft Yellow
        Color.parseColor("#CDDC39"), // 22. Lime Green
        Color.parseColor("#FF69B4"), // 23. Pink
        Color.parseColor("#8BC34A"), // 24. Light Green
        Color.parseColor("#4CAF50"), // 25. Green
        Color.parseColor("#FF6347"), // 26. Tomato
        Color.parseColor("#2E7D32"), // 27. Forest Green
        Color.parseColor("#009688"), // 28. Teal
        Color.parseColor("#DA70D6"), // 29. Orchid
        Color.parseColor("#00BCD4"), // 30. Cyan
        Color.parseColor("#26C6DA"), // 31. Aqua
        Color.parseColor("#D2691E"), // 32. Chocolate
        Color.parseColor("#03A9F4"), // 33. Light Blue
        Color.parseColor("#F5DEB3"), // 34. Wheat
        Color.parseColor("#2196F3"), // 35. Sky Blue
        Color.parseColor("#1976D2"), // 36. Blue
        Color.parseColor("#FF1493"), // 37. Deep Pink
        Color.parseColor("#0D47A1"), // 38. Midnight Blue
        Color.parseColor("#3F51B5"), // 39. Indigo
        Color.parseColor("#ADFF2F"), // 40. Green Yellow
        Color.parseColor("#673AB7"), // 41. Deep Purple
        Color.parseColor("#9C27B0"), // 42. Purple
        Color.parseColor("#BA68C8"), // 43. Lavender
        Color.parseColor("#E91E63"), // 44. Vibrant Pink
        Color.parseColor("#F06292"), // 45. Sweet Rose
        Color.parseColor("#FFFFFF"), // 46. White

        // 40 New Colors
        Color.parseColor("#C0C0C0"), // 47. Silver
        Color.parseColor("#FF7F50"), // 48. Coral
        Color.parseColor("#191970"), // 49. Navy
        Color.parseColor("#98FB98"), // 50. Pale Green
        Color.parseColor("#800000"), // 51. Maroon
        Color.parseColor("#F4A460"), // 52. Sandy Brown
        Color.parseColor("#7B68EE"), // 53. Medium Slate Blue
        Color.parseColor("#FA8072"), // 54. Salmon
        Color.parseColor("#008080"), // 55. Dark Teal
        Color.parseColor("#FFE4B5"), // 56. Moccasin
        Color.parseColor("#556B2F"), // 57. Olive Green
        Color.parseColor("#BA55D3"), // 58. Medium Orchid
        Color.parseColor("#4682B4"), // 59. Steel Blue
        Color.parseColor("#F0E68C"), // 60. Khaki
        Color.parseColor("#DC143C"), // 61. Crimson
        Color.parseColor("#20B2AA"), // 62. Light Sea Green
        Color.parseColor("#DDA0DD"), // 63. Plum
        Color.parseColor("#BDB76B"), // 64. Dark Khaki
        Color.parseColor("#FF4500"), // 65. Orange Red
        Color.parseColor("#6495ED"), // 66. Cornflower Blue
        Color.parseColor("#B0C4DE"), // 67. Light Steel Blue
        Color.parseColor("#8B4513"), // 68. Saddle Brown
        Color.parseColor("#00FA9A"), // 69. Medium Spring Green
        Color.parseColor("#F08080")  // 70. Light Coral
    )

    override fun setupViews() {
        applyThemeStyling()
        setupCanvas()
        setupTopBar()
        setupTools()
        setupStrokeSlider()
        setupColorsList()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.btnBack.setCardBackgroundColor(theme.primaryColor)
//        binding.sliderStrokeSize.thumbColor = ColorStateList.valueOf(theme.primaryColor)
        binding.sliderStrokeSize.trackActiveTintList = ColorStateList.valueOf(theme.primaryColor)
        selectTool(DrawingView.ToolMode.PENCIL)
    }

    private fun setupCanvas() {
        binding.drawingView.setCanvasBackgroundColor(Color.WHITE)
        binding.drawingView.currentColor = activeColor

        // 🎯 Restore drawing if rotating between portrait & landscape
        cachedDrawingBitmap?.let { saved ->
            binding.drawingView.restoreBitmap(saved)
            cachedDrawingBitmap = null
        }
    }

    private fun setupTopBar() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // 🔄 Smoothly switch between Landscape and Portrait
        binding.btnRotateScreen.setOnSafeClickListener {
            requestedOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }

        binding.btnSave.setOnSafeClickListener { saveImageToGallery() }
        binding.btnShare.setOnSafeClickListener { shareImage() }
    }

    private fun setupTools() {
        binding.toolPencil.setOnSafeClickListener { selectTool(DrawingView.ToolMode.PENCIL) }
        binding.toolBrush.setOnSafeClickListener { selectTool(DrawingView.ToolMode.BRUSH) }
        binding.toolLine.setOnSafeClickListener { selectTool(DrawingView.ToolMode.LINE) }
        binding.toolRectangle.setOnSafeClickListener { selectTool(DrawingView.ToolMode.RECTANGLE) }
        binding.toolCircle.setOnSafeClickListener { selectTool(DrawingView.ToolMode.CIRCLE) }
        binding.toolEraser.setOnSafeClickListener { selectTool(DrawingView.ToolMode.ERASER) }

        binding.btnUndo.setOnSafeClickListener { binding.drawingView.undo() }
        binding.btnRedo.setOnSafeClickListener { binding.drawingView.redo() }
        binding.btnTrash.setOnSafeClickListener {
            binding.drawingView.resetCanvas()
            Toast.makeText(this, "Canvas cleared!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectTool(mode: DrawingView.ToolMode) {
        binding.drawingView.currentTool = mode
        val theme = themeManager.getSelectedTheme()

        val tools = listOf(
            Triple(binding.toolPencil, binding.tvToolPencil, mode == DrawingView.ToolMode.PENCIL),
            Triple(binding.toolBrush, binding.tvToolBrush, mode == DrawingView.ToolMode.BRUSH),
            Triple(binding.toolLine, binding.tvToolLine, mode == DrawingView.ToolMode.LINE),
            Triple(binding.toolRectangle, binding.tvToolRectangle, mode == DrawingView.ToolMode.RECTANGLE),
            Triple(binding.toolCircle, binding.tvToolCircle, mode == DrawingView.ToolMode.CIRCLE),
            Triple(binding.toolEraser, binding.tvToolEraser, mode == DrawingView.ToolMode.ERASER)
        )

        tools.forEach { (card, text, isSelected) ->
            if (isSelected) {
                card.setCardBackgroundColor(theme.primaryColor)
                card.strokeWidth = 0
                text.setTextColor(Color.WHITE)
            } else {
                card.setCardBackgroundColor(Color.WHITE)
                card.strokeColor = Color.parseColor("#EAEAEA")
                card.strokeWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
                text.setTextColor(getColor(R.color.color_text_primary))
            }
        }
    }

    private fun setupStrokeSlider() {
        binding.sliderStrokeSize.addOnChangeListener { _, value, _ ->
            binding.drawingView.currentStrokeWidth = value
        }
        binding.drawingView.currentStrokeWidth = binding.sliderStrokeSize.value
    }

    private fun setupColorsList() {
        val adapter = ColorPaletteInlineAdapter(thirtyColors, activeColor) { chosenColor ->
            activeColor = chosenColor
            binding.drawingView.currentColor = chosenColor

            if (binding.drawingView.currentTool == DrawingView.ToolMode.ERASER) {
                selectTool(DrawingView.ToolMode.PENCIL)
            }
        }
        binding.rvColors.adapter = adapter

        val initialIndex = thirtyColors.indexOf(activeColor)
        if (initialIndex != -1) {
            binding.rvColors.scrollToPosition(initialIndex)
        }
    }

    private fun saveImageToGallery() {
        val bitmap = binding.drawingView.getCanvasBitmap() ?: return
        val title = "LittleDeen_Draw_${System.currentTimeMillis()}"
        val uriStr = MediaStore.Images.Media.insertImage(contentResolver, bitmap, title, "Drawn in Little Deen")
        if (uriStr != null) {
            Toast.makeText(this, "Saved to Gallery! 🎨", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareImage() {
        val bitmap = binding.drawingView.getCanvasBitmap() ?: return
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Shared Drawing", null)
        val uri = Uri.parse(path)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(intent, "Share Drawing"))
    }

    override fun onDestroy() {
        // Cache the drawing so rotating between portrait and landscape never loses progress
        if (isChangingConfigurations) {
            cachedDrawingBitmap = binding.drawingView.getCanvasBitmap()
        }
        super.onDestroy()
    }

    inner class ColorPaletteInlineAdapter(
        private val colors: List<Int>,
        private var selectedColor: Int,
        private val onColorSelected: (Int) -> Unit
    ) : RecyclerView.Adapter<ColorPaletteInlineAdapter.ColorViewHolder>() {

        inner class ColorViewHolder(val binding: ItemColorSwatchBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
            val binding = ItemColorSwatchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ColorViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            val color = colors[position]
            val isSelected = (color == selectedColor)

            with(holder.binding) {
                cardColorSwatch.setCardBackgroundColor(ColorStateList.valueOf(color))

                if (color == Color.WHITE) {
                    cardColorSwatch.strokeColor = Color.parseColor("#BDBDBD")
                    cardColorSwatch.strokeWidth = 2
                } else {
                    cardColorSwatch.strokeColor = Color.WHITE
                    cardColorSwatch.strokeWidth = 2
                }

                if (isSelected) {
                    cardSelectionRing.visibility = View.VISIBLE
                    cardSelectionRing.strokeColor = if (color == Color.WHITE) Color.BLACK else color
                    ivCheckmark.visibility = View.VISIBLE

                    val luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
                    ivCheckmark.imageTintList = ColorStateList.valueOf(
                        if (luminance > 0.65) Color.BLACK else Color.WHITE
                    )
                } else {
                    cardSelectionRing.visibility = View.INVISIBLE
                    ivCheckmark.visibility = View.GONE
                }

                root.setOnClickListener {
                    val prevColor = selectedColor
                    selectedColor = color

                    val prevIndex = colors.indexOf(prevColor)
                    if (prevIndex != -1) notifyItemChanged(prevIndex)
                    notifyItemChanged(holder.bindingAdapterPosition)

                    onColorSelected(color)
                }
            }
        }

        override fun getItemCount(): Int = colors.size
    }
}
