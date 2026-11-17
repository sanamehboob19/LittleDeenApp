package com.ccx.islamic.kids.littledeenapp.ui.activities.dua

import android.os.Build
import android.os.Bundle
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
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityDuaDetailBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.data.DuaItem
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.fragment.DuaPageFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject



@AndroidEntryPoint
class DuaDetailActivity : BaseActivity<ActivityDuaDetailBinding>(
    ActivityDuaDetailBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    private var currentNarrationJob: Job? = null
    private lateinit var duaList: List<DuaItem>

    override fun setupViews() {
        // 🎯 Read the passed shuffled list (or fallback to default if not provided)
        val passedList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(EXTRA_DUA_LIST, DuaItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(EXTRA_DUA_LIST)
        }

        duaList = passedList ?: DuaItem.getDefaultDuas()
        val startIndex = intent.getIntExtra(EXTRA_START_INDEX, 0)

        binding.viewPagerDuas.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = duaList.size
            override fun createFragment(position: Int): Fragment {
                return DuaPageFragment.newInstance(duaList[position])
            }
        }

        binding.viewPagerDuas.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                playPageNarrationInstantly(duaList[position])
            }
        })

        // 🎯 Start ViewPager at the exact clicked index!
        binding.viewPagerDuas.setCurrentItem(startIndex, false)

        binding.btnBack.setOnSafeClickListener { finish() }
    }


    /**
     * Plays bilingual narration immediately on swipe:
     * English subtitle prompt ➔ Arabic Dua in authentic Arabic voice ➔ English translation.
     */
    private fun playPageNarrationInstantly(dua: DuaItem) {
        // Natural event-driven chain: English ➔ Arabic ➔ English
        voiceAssistant.speakDuaSequence(
            introEnglish = dua.subtitle,
            arabicDua = dua.arabicText,
            translationEnglish = dua.translation
        )
    }

    override fun onPause() {
        currentNarrationJob?.cancel()
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroy() {
        currentNarrationJob?.cancel()
        voiceAssistant.stop()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_START_INDEX = "extra_start_index"
        const val EXTRA_DUA_LIST = "extra_dua_list" // 👈 Added constant
    }
}
