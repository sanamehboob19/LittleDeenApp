package com.ccx.islamic.kids.littledeenapp.ui.activities.pixel_art


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.PixelArtModel
import com.ccx.islamic.kids.littledeenapp.data.PixelArtProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityPixelArtBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ExcellentBannerDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PixelArtActivity : BaseActivity<ActivityPixelArtBinding>(
    ActivityPixelArtBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var currentIndex = 0
    private val modelsList = PixelArtProvider.models
    private val colorSwatches = mutableListOf<CardView>()

    override fun setupViews() {
        extractSelectedModel()
        applyThemeStyling()
        setupTopBar()
        setupNavigation()
        setupCompletionListener()
        loadCurrentModel()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvModelTitle.setTextColor(theme.primaryColor)
        binding.btnNextModel.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)
        binding.cardTargetPreview.strokeColor = theme.primaryColor
    }

    private fun extractSelectedModel() {
        val selected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_SELECTED_MODEL, PixelArtModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_SELECTED_MODEL)
        }

        currentIndex = if (selected != null) {
            modelsList.indexOfFirst { it.id == selected.id }.coerceAtLeast(0)
        } else {
            0
        }
    }

    private fun loadCurrentModel() {
        val model = modelsList[currentIndex]

        binding.tvModelTitle.text = model.title
        binding.targetGridView.loadModel(model, isTargetPreview = true)
        binding.interactiveGridView.loadModel(model, isTargetPreview = false)

        binding.btnPrevModel.isEnabled = currentIndex > 0
        binding.btnPrevModel.alpha = if (currentIndex > 0) 1.0f else 0.4f

        binding.btnNextModel.isEnabled = currentIndex < modelsList.size - 1
        binding.btnNextModel.alpha = if (currentIndex < modelsList.size - 1) 1.0f else 0.4f

        buildColorPalette(model.paletteColorsHex)
    }

    private fun buildColorPalette(paletteHexList: List<String>) {
        binding.layoutColorPalette.removeAllViews()
        colorSwatches.clear()

        val size = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._32sdp)
        val margin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._4sdp)

        paletteHexList.forEachIndexed { index, hex ->
            val color = Color.parseColor(hex)

            val card = CardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(margin, 0, margin, 0)
                }
                radius = size / 2f
                cardElevation = 4f
                setCardBackgroundColor(color)
                isClickable = true
                isFocusable = true

                setOnClickListener { selectPaletteColor(this, color) }
            }

            colorSwatches.add(card)
            binding.layoutColorPalette.addView(card)

            // Auto-select first non-white color
            if (index == 1) selectPaletteColor(card, color)
        }
    }

    private fun selectPaletteColor(selectedCard: CardView, color: Int) {
        binding.interactiveGridView.currentColor = color

        colorSwatches.forEach { card ->
            val isSelected = card == selectedCard
            card.scaleX = if (isSelected) 1.25f else 1.0f
            card.scaleY = if (isSelected) 1.25f else 1.0f
        }
    }

    private fun setupTopBar() {
        binding.btnBack.setOnSafeClickListener { finish() }

        binding.btnHint.alpha = 0.5f
        binding.btnHint.setOnSafeClickListener {
            val isShowing = !binding.interactiveGridView.showHintDots
            binding.interactiveGridView.showHintDots = isShowing
            binding.interactiveGridView.invalidate()
            binding.btnHint.alpha = if (isShowing) 1.0f else 0.5f
        }

        binding.btnClear.setOnSafeClickListener {
            binding.interactiveGridView.resetGrid()
        }
    }

    private fun setupNavigation() {
        binding.btnPrevModel.setOnSafeClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadCurrentModel()
            }
        }

        binding.btnNextModel.setOnSafeClickListener {
            if (currentIndex < modelsList.size - 1) {
                currentIndex++
                loadCurrentModel()
            }
        }
    }

    private fun setupCompletionListener() {
        binding.interactiveGridView.onWrongColor = {
            playErrorShakeAnimation()
        }

        binding.interactiveGridView.onCompleted = {
            voiceAssistant.speakSuccess()
            ExcellentBannerDialogFragment.show(supportFragmentManager) {
                if (currentIndex < modelsList.size - 1) {
                    currentIndex++
                    loadCurrentModel()
                } else {
                    finish()
                }
            }
        }
    }

    private fun playErrorShakeAnimation() {
        voiceAssistant.speakError()

        val card = binding.cardInteractiveGrid
        card.animate()
            .translationXBy(20f)
            .setDuration(50)
            .withEndAction {
                card.animate().translationXBy(-40f).setDuration(80).withEndAction {
                    card.animate().translationXBy(30f).setDuration(70).withEndAction {
                        card.animate().translationX(0f).setDuration(60).start()
                    }.start()
                }.start()
            }.start()
    }

    companion object {
        const val EXTRA_SELECTED_MODEL = "extra_selected_model"
    }
}