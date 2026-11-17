package com.ccx.islamic.kids.littledeenapp.ui.fragment


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentDuasBinding
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.data.DuaItem
import com.ccx.islamic.kids.littledeenapp.ui.activities.dua.DuaDetailActivity
import com.ccx.islamic.kids.littledeenapp.ui.adapter.DuaAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseFragment
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DuasFragment : BaseFragment<FragmentDuasBinding>(FragmentDuasBinding::inflate) {

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    private lateinit var duaAdapter: DuaAdapter
    private var currentShuffledList: List<DuaItem> = emptyList()

    override fun setupViews() {
        applyThemeStyling()
        loadChildAvatar()
        setupRecyclerView()
        setupSwipeRefresh()
        refreshShuffledDuas()
    }


    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor
        val lightColor = theme.lightColor

        // 1. Tint Swipe-to-refresh spinner
        binding.swipeRefreshDuas.setColorSchemeColors(primaryColor)

        // 2. 🎯 Dynamically generate matching gradient for the Dua Hero Banner
        val lighterShade = ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.35f)
        val dynamicGradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(primaryColor, lighterShade)
        )
        binding.flDuasBannerBg.background = dynamicGradient

        // 3. Tint Hero Card borders and avatar ring
        binding.cardDuasHeroBanner.strokeColor = lightColor
        binding.cardAvatarRing.strokeColor = lightColor
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshDuas.setOnRefreshListener {
            refreshShuffledDuas()
            binding.swipeRefreshDuas.isRefreshing = false
            requireContext().showToast("Duas refreshed! 🌟")
        }
    }

    private fun loadChildAvatar() {
        val customUri = preferenceHelper.customAvatarUri
        val defaultAvatarRes = preferenceHelper.childAvatarRes.let {
            if (it != 0) it else R.drawable.avatar_girl_1
        }

        val imageSource: Any = if (!customUri.isNullOrEmpty()) {
            Uri.parse(customUri)
        } else {
            defaultAvatarRes
        }

        Glide.with(this)
            .load(imageSource)
            .circleCrop()
            .placeholder(R.drawable.avatar_girl_1)
            .into(binding.ivDuasHeroAvatar)
    }

    private fun setupRecyclerView() {
        duaAdapter = DuaAdapter(
            duaList = emptyList(),
            activeTheme = themeManager.getSelectedTheme(), // 👈 Pass active theme
            onPlayAudioClick = { dua ->
                voiceAssistant.speak(dua.audioTtsText)
                requireContext().showToast("Playing: ${dua.title} 🔊")
            },
            onCardClick = { dua ->
                val index = currentShuffledList.indexOfFirst { it.id == dua.id }.coerceAtLeast(0)
                val intent = Intent(requireContext(), DuaDetailActivity::class.java).apply {
                    putExtra(DuaDetailActivity.EXTRA_START_INDEX, index)
                    putParcelableArrayListExtra(
                        DuaDetailActivity.EXTRA_DUA_LIST,
                        ArrayList(currentShuffledList)
                    )
                }
                startActivity(intent)
            }
        )

        binding.rvDuas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = duaAdapter
        }
    }

    private fun refreshShuffledDuas() {
        currentShuffledList = DuaItem.getDefaultDuas().shuffled()
        duaAdapter.submitList(currentShuffledList)
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        // 🎯 Re-apply theme styling and update DuaAdapter's color
        applyThemeStyling()
        loadChildAvatar()
        if (::duaAdapter.isInitialized) {
            duaAdapter.updateTheme(themeManager.getSelectedTheme())
        }
    }

}

