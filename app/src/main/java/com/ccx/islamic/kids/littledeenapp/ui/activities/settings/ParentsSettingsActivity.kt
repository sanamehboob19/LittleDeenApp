package com.ccx.islamic.kids.littledeenapp.ui.activities.settings



import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import com.bumptech.glide.Glide
import com.google.android.material.materialswitch.MaterialSwitch
import com.ccx.islamic.kids.littledeenapp.BuildConfig
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityParentsSettingsBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding.ChildNameActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding.OnboardingActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding.ThemeSelectionActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.AboutLittleDeenDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ParentsSettingsActivity : BaseActivity<ActivityParentsSettingsBinding>(
    ActivityParentsSettingsBinding::inflate
) {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        applyActiveTheme()
        loadChildProfile()
        loadReminderSettings()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Refresh profile in case user changed their name, avatar, or theme
        loadChildProfile()
        applyActiveTheme()
    }

    private fun applyActiveTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.cardProfileAvatar.strokeColor = theme.primaryColor
        binding.tvReminderTime.setTextColor(theme.primaryColor)

        // Tint Reminder Switch
        styleIosSwitch(binding.switchReminder, theme.primaryColor)
    }

    private fun loadChildProfile() {
        val childName = preferenceHelper.childName
        val ageGroup = preferenceHelper.childAgeGroup
        binding.tvProfileName.text = "$childName ($ageGroup)"

        val customUri = preferenceHelper.customAvatarUri
        val defaultAvatarRes = preferenceHelper.childAvatarRes.let {
            if (it != 0) it else R.drawable.avatar_boy_1
        }

        val imageSource: Any = if (!customUri.isNullOrEmpty()) {
            Uri.parse(customUri)
        } else {
            defaultAvatarRes
        }

        Glide.with(this)
            .load(imageSource)
            .circleCrop()
            .placeholder(R.drawable.avatar_boy_1)
            .into(binding.ivChildAvatar)

        binding.tvAppVersion.text = "v${BuildConfig.VERSION_NAME}"
    }

    private fun loadReminderSettings() {
        val isEnabled = preferenceHelper.getBoolean("key_daily_reminder_enabled", true)
        val savedTime = preferenceHelper.getString("key_daily_reminder_time", "05:00 PM")

        binding.switchReminder.isChecked = isEnabled
        binding.tvReminderTime.text = "Daily at $savedTime (Tap to change)"
    }

    private fun setupClickListeners() {
        // Back Button
        binding.btnBack.setOnSafeClickListener {
            finish()
        }

        // Daily Reminder Time Picker
        binding.tvReminderTime.setOnSafeClickListener {
            showReminderTimePicker()
        }

        // Daily Reminder Switch
        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            preferenceHelper.putBoolean("key_daily_reminder_enabled", isChecked)
            showToast(if (isChecked) "Daily reminder activated" else "Reminder turned off")
        }

        // Edit Child Profile
        binding.cardChildProfile.setOnSafeClickListener {
            startActivity(Intent(this, ChildNameActivity::class.java))
        }

        // Theme Selection
        binding.itemTheme.setOnSafeClickListener {
            startActivity(Intent(this, ThemeSelectionActivity::class.java))
        }

        // Audio Preferences
        binding.itemAudioPrefs.setOnSafeClickListener {
            startActivity(Intent(this, AudioPreferencesActivity::class.java))
        }

        // App Guide / Walkthrough Tutorial
        binding.itemAppTutorial.setOnSafeClickListener {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }

        // Share App
        binding.itemShare.setOnSafeClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Little Deen - A safe, halal, and beautiful Islamic learning app for kids! Download today: https://play.google.com/store/apps/details?id=$packageName"
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share Little Deen"))
        }

        // Rate App
        binding.itemRate.setOnSafeClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }

        // Privacy Policy (Google Families Compliance)
        binding.itemPrivacy.setOnSafeClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://littledeenapp.com/privacy-policy") // Replace with your policy link
            )
            try {
                startActivity(browserIntent)
            } catch (e: Exception) {
                showToast("Cannot open web browser")
            }
        }

        // About App
        binding.itemAbout.setOnSafeClickListener {
            val dialog = AboutLittleDeenDialogFragment.newInstance()
            dialog.show(supportFragmentManager, AboutLittleDeenDialogFragment.TAG)
        }
    }

    private fun showReminderTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val isPm = selectedHour >= 12
            val formattedHour = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            val formattedTime = String.format(Locale.US, "%02d:%02d %s", formattedHour, selectedMinute, if (isPm) "PM" else "AM")

            preferenceHelper.putString("key_daily_reminder_time", formattedTime)
            binding.tvReminderTime.text = "Daily at $formattedTime (Tap to change)"
            showToast("Reminder set for $formattedTime")
        }, hour, minute, false).show()
    }

    private fun styleIosSwitch(switchView: MaterialSwitch, activeColor: Int) {
        val trackColors = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
            intArrayOf(activeColor, Color.parseColor("#E0E0E0"))
        )
        val thumbColors = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
            intArrayOf(Color.WHITE, Color.WHITE)
        )
        switchView.trackTintList = trackColors
        switchView.thumbTintList = thumbColors
    }
}

