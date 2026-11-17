package com.ccx.islamic.kids.littledeenapp.ui.activities.namaz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.data.SalahStep
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySalahDetailBinding
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentSalahPageBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SalahDetailActivity : BaseActivity<ActivitySalahDetailBinding>(
    ActivitySalahDetailBinding::inflate
) {
    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var stepsList: List<SalahStep>

    override fun setupViews() {
        stepsList = SalahStep.getAllSteps()
        val targetStepNum = intent.getIntExtra(EXTRA_STEP_NUM, 1)
        val startIndex = stepsList.indexOfFirst { it.stepNumber == targetStepNum }.coerceAtLeast(0)

        applyTheme()
        setupViewPager(startIndex)
        setupClickListeners()
    }

    private fun applyTheme() {
        binding.tvHeaderTitle.setTextColor(themeManager.getSelectedTheme().primaryColor)
    }

    private fun setupViewPager(startIndex: Int) {
        binding.viewPagerSalah.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = stepsList.size
            override fun createFragment(position: Int): Fragment {
                return SalahPageFragment.newInstance(stepsList[position])
            }
        }
        binding.viewPagerSalah.setCurrentItem(startIndex, false)

        binding.viewPagerSalah.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavButtons(position)
            }
        })
    }

    private fun updateNavButtons(position: Int) {
        binding.btnPrevStep.isEnabled = position > 0
        binding.btnNextStep.isEnabled = position < stepsList.size - 1

        val theme = themeManager.getSelectedTheme()
        if (position == stepsList.size - 1) {
            binding.btnNextStep.text = "Finish 🏆"
            binding.btnNextStep.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#00C853"))
        } else {
            binding.btnNextStep.text = "Next Step →"
            binding.btnNextStep.backgroundTintList = android.content.res.ColorStateList.valueOf(theme.primaryColor)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        binding.btnPrevStep.setOnSafeClickListener {
            val current = binding.viewPagerSalah.currentItem
            if (current > 0) binding.viewPagerSalah.currentItem = current - 1
        }

        binding.btnNextStep.setOnSafeClickListener {
            val current = binding.viewPagerSalah.currentItem
            if (current < stepsList.size - 1) {
                binding.viewPagerSalah.currentItem = current + 1
            } else {
                finish() // Exit on finish
            }
        }
    }

    companion object {
        const val EXTRA_STEP_NUM = "extra_step_num"
    }
}

// ==========================================
// SALAH PAGE FRAGMENT
// ==========================================
@AndroidEntryPoint
class SalahPageFragment : Fragment() {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var _binding: FragmentSalahPageBinding? = null
    private val binding get() = _binding!!

    private var stepData: SalahStep? = null
    private var narrationJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepData = arguments?.getParcelable(ARG_STEP)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalahPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val step = stepData ?: return

        binding.tvStepTitle.text = step.title
        binding.tvStepDescription.text = step.description
        binding.tvArabicRecitation.text = step.arabicRecitation
        binding.tvTransliteration.text = step.transliteration

        // Apply theme to button and card borders
        val theme = themeManager.getSelectedTheme()
        binding.btnListenAudio.backgroundTintList = android.content.res.ColorStateList.valueOf(theme.primaryColor)
        binding.cardIllustration.strokeColor = theme.primaryColor

        // We use a placeholder image for now, later you can add imageRes to SalahStep model!
        Glide.with(this).load(R.drawable.avatar_boy_1).centerCrop().into(binding.ivPostureIllustration)

        // Gentle breathing animation on image
        val breathe = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_breathe_zoom)
        binding.ivPostureIllustration.startAnimation(breathe)

        // Auto-play sequence on page entry
        playStepNarration(step)

        // Manual play button speaks just the Arabic part slowly
        binding.btnListenAudio.setOnSafeClickListener {
            narrationJob?.cancel()
            voiceAssistant.stop()
            voiceAssistant.setSpeechRate(0.85f)
            voiceAssistant.speakInLocale(step.arabicRecitation, Locale("ar")) {
                voiceAssistant.setSpeechRate(0.92f)
                _binding?.let { b -> YoYo.with(Techniques.Pulse).duration(500).playOn(b.tvArabicRecitation) }
            }
        }
    }

    private fun playStepNarration(step: SalahStep) {
        narrationJob?.cancel()
        voiceAssistant.stop()

        narrationJob = viewLifecycleOwner.lifecycleScope.launch {
            // 1. Describe the posture in English
            voiceAssistant.speak(step.audioTtsText) {
                // 2. Exact moment it finishes, read Arabic text
                voiceAssistant.speakInLocale(step.arabicRecitation, Locale("ar")) {
                    // 3. Exact moment Arabic finishes, read translation
                    voiceAssistant.speak(step.translation)
                }
            }
        }
    }

    override fun onPause() {
        narrationJob?.cancel()
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        narrationJob?.cancel()
        voiceAssistant.stop()
        binding.ivPostureIllustration.clearAnimation()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_STEP = "arg_step"
        fun newInstance(step: SalahStep) = SalahPageFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_STEP, step) }
        }
    }
}

