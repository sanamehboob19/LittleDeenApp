package com.ccx.islamic.kids.littledeenapp.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.DuaItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentDuaPageBinding
import kotlinx.coroutines.Job
import java.util.Locale


// ==========================================
// SINGLE DUA PAGE FRAGMENT (WITH ZOOM ANIMATION & TTS)
// ==========================================


@AndroidEntryPoint
class DuaPageFragment : Fragment() {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var _binding: FragmentDuaPageBinding? = null
    private val binding get() = _binding

    private var duaItem: DuaItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        duaItem = arguments?.getParcelable(ARG_DUA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDuaPageBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentBinding = binding ?: return

        duaItem?.let { dua ->
            currentBinding.tvPageTitle.text = dua.title
            currentBinding.tvPageSubtitle.text = dua.subtitle
            currentBinding.tvArabicText.text = dua.arabicText
            currentBinding.tvTranslation.text = dua.translation

            // Apply selected theme color to button
            val primaryColor = themeManager.getSelectedTheme().primaryColor
            currentBinding.btnListenAudio.backgroundTintList = ColorStateList.valueOf(primaryColor)

            // Load matching local illustration
            currentBinding.ivDuaPageArt.setImageResource(dua.imageRes)

            // Smooth breathing zoom animation
            val breatheAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_breathe_zoom)
            currentBinding.ivDuaPageArt.startAnimation(breatheAnimation)

            // 🎯 Button Click: Speaks ONLY the Arabic Dua
            currentBinding.btnListenAudio.setOnSafeClickListener {
                voiceAssistant.stop()
                voiceAssistant.speakInLocale(dua.arabicText, Locale("ar"))

                _binding?.let { b ->
                    YoYo.with(Techniques.Pulse)
                        .duration(500)
                        .playOn(b.tvArabicText)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding?.ivDuaPageArt?.clearAnimation()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_DUA = "arg_dua"

        fun newInstance(dua: DuaItem): DuaPageFragment {
            return DuaPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DUA, dua)
                }
            }
        }
    }
}