package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing


import android.os.Build
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.ShapeModel
import com.ccx.islamic.kids.littledeenapp.data.ShapeProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityShapeTracingBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ExcellentBannerDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShapeTracingActivity : BaseActivity<ActivityShapeTracingBinding>(
    ActivityShapeTracingBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var currentIndex = 0
    private val shapesList = ShapeProvider.shapes

    override fun setupViews() {
        extractInitialShape()
        applyThemeStyling()
        setupTopBar()
        setupBottomControls()
        setupTracingCallbacks()
        loadCurrentShape()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvShapeTitle.setTextColor(theme.primaryColor)
        binding.btnNextShape.backgroundTintList = android.content.res.ColorStateList.valueOf(theme.primaryColor)
    }

    private fun extractInitialShape() {
        val selected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_SELECTED_SHAPE, ShapeModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_SELECTED_SHAPE)
        }

        currentIndex = if (selected != null) {
            shapesList.indexOfFirst { it.id == selected.id }.coerceAtLeast(0)
        } else {
            0
        }
    }

    private fun loadCurrentShape() {
        val current = shapesList[currentIndex]

        binding.tvShapeTitle.text = "${current.name} ${current.emoji}"
        binding.shapeTracingView.loadShape(current)

        // Pronounce via VoiceAssistant
        voiceAssistant.speak("${current.name}! Can you trace the ${current.name}?")

        binding.btnPrevShape.isEnabled = currentIndex > 0
        binding.btnPrevShape.alpha = if (currentIndex > 0) 1.0f else 0.4f

        binding.btnNextShape.isEnabled = currentIndex < shapesList.size - 1
        binding.btnNextShape.alpha = if (currentIndex < shapesList.size - 1) 1.0f else 0.4f
    }

    private fun setupTopBar() {
        binding.btnBack.setOnSafeClickListener { finish() }
        binding.btnReset.setOnSafeClickListener { loadCurrentShape() }
    }

    private fun setupBottomControls() {
        binding.btnPrevShape.setOnSafeClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadCurrentShape()
            }
        }

        binding.btnNextShape.setOnSafeClickListener {
            if (currentIndex < shapesList.size - 1) {
                currentIndex++
                loadCurrentShape()
            }
        }
    }

    private fun setupTracingCallbacks() {
        binding.shapeTracingView.onCompleted = {
            // Show the celebratory garland banner
            ExcellentBannerDialogFragment.show(supportFragmentManager) {
                if (currentIndex < shapesList.size - 1) {
                    currentIndex++
                    loadCurrentShape()
                } else {
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_SELECTED_SHAPE = "extra_selected_shape"
    }
}