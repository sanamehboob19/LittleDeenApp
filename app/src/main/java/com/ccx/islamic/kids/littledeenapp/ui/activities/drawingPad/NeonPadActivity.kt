package com.ccx.islamic.kids.littledeenapp.ui.activities.drawingPad


import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityNeonPadBinding
import com.ccx.islamic.kids.littledeenapp.databinding.ItemColorSwatchBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.customViews.DrawingView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NeonPadActivity : BaseActivity<ActivityNeonPadBinding>(ActivityNeonPadBinding::inflate) {

    private var activeNeonColor: Int = Color.parseColor("#00F0FF")

    companion object {
        private var cachedNeonBitmap: android.graphics.Bitmap? = null
    }

    // 🌟 20 High-Intensity Electrifying Neon Colors
    private val neonColors = listOf(
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
        setupCanvas()
        setupTopBar()
        setupTools()
        setupStrokeSlider()
        setupNeonColors()
    }

    private fun setupCanvas() {
        // Pitch-black canvas for intense neon contrast
        binding.drawingView.setCanvasBackgroundColor(Color.BLACK)
        // Enables dual-paint white laser core + glowing bloom
        binding.drawingView.isNeonCoreEnabled = true
        binding.drawingView.currentColor = activeNeonColor

        // Restore drawing if rotating between portrait & landscape
        cachedNeonBitmap?.let { saved ->
            binding.drawingView.restoreBitmap(saved)
            cachedNeonBitmap = null
        }
    }

    private fun setupTopBar() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // 🔄 Switch between Landscape and Portrait
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
        selectTool(DrawingView.ToolMode.GLOW)

        binding.toolGlowPen.setOnSafeClickListener { selectTool(DrawingView.ToolMode.GLOW) }
        binding.toolBrush.setOnSafeClickListener { selectTool(DrawingView.ToolMode.BRUSH) }
        binding.toolLine.setOnSafeClickListener { selectTool(DrawingView.ToolMode.LINE) }
        binding.toolRectangle.setOnSafeClickListener { selectTool(DrawingView.ToolMode.RECTANGLE) }
        binding.toolCircle.setOnSafeClickListener { selectTool(DrawingView.ToolMode.CIRCLE) }
        binding.toolEraser.setOnSafeClickListener { selectTool(DrawingView.ToolMode.ERASER) }

        // Step-by-step Undo & Redo
        binding.btnUndo.setOnSafeClickListener { binding.drawingView.undo() }
        binding.btnRedo.setOnSafeClickListener { binding.drawingView.redo() }
        binding.btnTrash.setOnSafeClickListener {
            binding.drawingView.resetCanvas()
            Toast.makeText(this, "Neon canvas cleared!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectTool(mode: DrawingView.ToolMode) {
        binding.drawingView.currentTool = mode

        val tools = listOf(
            Triple(binding.toolGlowPen, binding.tvToolGlowPen, mode == DrawingView.ToolMode.GLOW),
            Triple(binding.toolBrush, binding.tvToolBrush, mode == DrawingView.ToolMode.BRUSH),
            Triple(binding.toolLine, binding.tvToolLine, mode == DrawingView.ToolMode.LINE),
            Triple(binding.toolRectangle, binding.tvToolRectangle, mode == DrawingView.ToolMode.RECTANGLE),
            Triple(binding.toolCircle, binding.tvToolCircle, mode == DrawingView.ToolMode.CIRCLE),
            Triple(binding.toolEraser, binding.tvToolEraser, mode == DrawingView.ToolMode.ERASER)
        )

        tools.forEach { (card, text, isSelected) ->
            if (isSelected) {
                card.setCardBackgroundColor(activeNeonColor)
                card.strokeWidth = 0
                text.setTextColor(Color.BLACK)
            } else {
                card.setCardBackgroundColor(Color.parseColor("#212638"))
                card.strokeColor = Color.parseColor("#3D4566")
                card.strokeWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
                text.setTextColor(Color.WHITE)
            }
        }
    }

    private fun setupStrokeSlider() {
        binding.sliderStrokeSize.addOnChangeListener { _, value, _ ->
            binding.drawingView.currentStrokeWidth = value
        }
        binding.drawingView.currentStrokeWidth = binding.sliderStrokeSize.value
    }

    private fun setupNeonColors() {
        val adapter = NeonColorAdapter(neonColors, activeNeonColor) { chosenColor ->
            activeNeonColor = chosenColor
            binding.drawingView.currentColor = chosenColor

            // Update slider glow color
//            binding.sliderStrokeSize.thumbColor = ColorStateList.valueOf(chosenColor)
            binding.sliderStrokeSize.trackActiveTintList = ColorStateList.valueOf(chosenColor)

            // Auto-switch to Glow Pen if using Eraser
            if (binding.drawingView.currentTool == DrawingView.ToolMode.ERASER) {
                selectTool(DrawingView.ToolMode.GLOW)
            } else {
                selectTool(binding.drawingView.currentTool)
            }
        }
        binding.rvColors.adapter = adapter

        val initialIndex = neonColors.indexOf(activeNeonColor)
        if (initialIndex != -1) binding.rvColors.scrollToPosition(initialIndex)
    }

    private fun saveImageToGallery() {
        val bitmap = binding.drawingView.getCanvasBitmap() ?: return
        val title = "LittleDeen_Neon_${System.currentTimeMillis()}"
        val uriStr = MediaStore.Images.Media.insertImage(contentResolver, bitmap, title, "Neon Art in Little Deen")
        if (uriStr != null) {
            Toast.makeText(this, "Neon art saved! 🌟", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareImage() {
        val bitmap = binding.drawingView.getCanvasBitmap() ?: return
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Shared Neon Drawing", null)
        val uri = Uri.parse(path)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(intent, "Share Neon Art"))
    }

    override fun onDestroy() {
        if (isChangingConfigurations) {
            cachedNeonBitmap = binding.drawingView.getCanvasBitmap()
        }
        super.onDestroy()
    }

    inner class NeonColorAdapter(
        private val colors: List<Int>,
        private var selectedColor: Int,
        private val onColorSelected: (Int) -> Unit
    ) : RecyclerView.Adapter<NeonColorAdapter.ColorViewHolder>() {

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
                cardColorSwatch.strokeColor = Color.BLACK
                cardColorSwatch.strokeWidth = 2

                if (isSelected) {
                    cardSelectionRing.visibility = View.VISIBLE
                    cardSelectionRing.strokeColor = color
                    ivCheckmark.visibility = View.VISIBLE
                    ivCheckmark.imageTintList = ColorStateList.valueOf(Color.BLACK)
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