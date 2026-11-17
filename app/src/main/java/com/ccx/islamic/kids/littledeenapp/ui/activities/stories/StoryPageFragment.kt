package com.ccx.islamic.kids.littledeenapp.ui.activities.stories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.data.StoryItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentStoryPageBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// ==========================================
// STORY PAGE FRAGMENT
// ==========================================
@AndroidEntryPoint
class StoryPageFragment : Fragment() {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var _binding: FragmentStoryPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val story = arguments?.getParcelable<StoryItem>(ARG_STORY) ?: return

        val theme = themeManager.getSelectedTheme()
        binding.btnReadToMe.setCardBackgroundColor(theme.primaryColor)

        binding.tvStoryTitle.text = story.title
        binding.tvStoryTheme.text = story.theme
        binding.tvStoryContent.text = story.content
        binding.tvStoryMoral.text = story.moralLesson
        binding.tvStoryTeaching.text = story.islamicTeaching

        Glide.with(this)
            .load(story.imageRes)
            .centerCrop()
            .into(binding.ivStoryIllustration)

        binding.btnReadToMe.setOnSafeClickListener {
            voiceAssistant.stop()
            requireContext().showToast("Reading aloud... 🔊")

            // Speak Title, Content, Moral, and Teaching!
            val fullNarration = "${story.title}. ${story.content}. Moral Lesson: ${story.moralLesson}. Islamic Teaching: ${story.islamicTeaching}."
            voiceAssistant.setSpeechRate(0.9f) // Slightly slower for clear storytelling
            voiceAssistant.speak(fullNarration)

            YoYo.with(Techniques.Pulse).duration(600).playOn(binding.btnReadToMe)
        }
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        voiceAssistant.stop()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_STORY = "arg_story"
        fun newInstance(story: StoryItem) = StoryPageFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_STORY, story) }
        }
    }
}