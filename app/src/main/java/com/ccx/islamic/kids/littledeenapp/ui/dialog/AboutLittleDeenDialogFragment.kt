package com.ccx.islamic.kids.littledeenapp.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.DialogAboutLittleDeenBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AboutLittleDeenDialogFragment : DialogFragment() {

    private var _binding: DialogAboutLittleDeenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make it full screen
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAboutLittleDeenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyThemeStyling()
        setupClickListeners()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor

        binding.tvHeaderTitle.setTextColor(primaryColor)

        // Dynamic gradient for Hero Banner
        val lighter = ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.25f)
        val grad = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(primaryColor, lighter)
        )
        binding.flHeroAboutBg.background = grad
        binding.cardHeroAbout.strokeColor = theme.lightColor
        binding.cardHeroAbout.strokeWidth = 2
    }

    private fun setupClickListeners() {
        // Close
        binding.btnClose.setOnSafeClickListener {
            dismiss()
        }

        // Email 1: CodeCraftX
        binding.btnEmailCodecraft.setOnSafeClickListener {
            openEmail("codecraftx@gmail.com", "Little Deen App Feedback")
        }

        // Email 2: CircleUp
        binding.btnEmailCircleup.setOnSafeClickListener {
            openEmail("circleup0719@gmail.com", "Little Deen App Feedback")
        }

        // YouTube Channel
        binding.btnLinkYoutube.setOnSafeClickListener {
            openUrl("https://www.youtube.com/@sanamehboob19")
        }

        // Portfolio
        binding.btnLinkPortfolio.setOnSafeClickListener {
            openUrl("https://sanamehboob19.github.io/portfolio/")
        }

        // LinkedIn
        binding.btnLinkLinkedIn.setOnSafeClickListener {
            openUrl("https://www.linkedin.com/in/sanamehboob19")
        }

        // GitHub
        binding.btnLinkGitHub.setOnSafeClickListener {
            openUrl("https://github.com/sanamehboob19")
        }

        // WhatsApp
        binding.btnLinkWhatsApp.setOnSafeClickListener {
            try {
                val url = "https://api.whatsapp.com/send?phone=923008884224"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                // Fallback dialer
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+923008884224")))
            }
        }
    }

    private fun openEmail(email: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        startActivitySafely(intent)
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivitySafely(intent)
    }

    private fun startActivitySafely(intent: Intent) {
        try {
            startActivity(intent)
        } catch (_: Exception) {
            // No handler found
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AboutLittleDeenDialogFragment"
        fun newInstance() = AboutLittleDeenDialogFragment()
    }
}