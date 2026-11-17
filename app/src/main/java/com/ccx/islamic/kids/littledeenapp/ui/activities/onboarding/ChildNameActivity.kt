package com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding



import android.Manifest
import android.content.Intent
import com.ccx.islamic.kids.littledeenapp.R
import android.content.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.recyclerview.widget.GridLayoutManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityChildNameBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.adapter.AvatarAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.helper.PermissionHelper
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper



@AndroidEntryPoint
class ChildNameActivity : BaseActivity<ActivityChildNameBinding>(
    ActivityChildNameBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var avatarAdapter: AvatarAdapter
    private var customPhotoUri: Uri? = null

    // Reusable Permission Helper for Camera
    private lateinit var cameraPermissionHelper: PermissionHelper

    // CanHub Cropper Launcher (Camera + Gallery)
    private val cropImageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            if (uriContent != null) {
                customPhotoUri = uriContent
                Glide.with(this)
                    .load(uriContent)
                    .circleCrop()
                    .into(binding.ivHeroAvatar)
            }
        } else if (result.error != null) {
            showToast("Photo selection cancelled")
        }
    }

    override fun setupViews() {
        initPermissionHelper()
        updateAudioToggleIcon()
        setupAvatarList()
        setupClickListeners()

        // 🎯 If editing from settings, update button text to "Save"
        if (!preferenceHelper.isFirstLaunch) {
            binding.btnContinue.text = "Save Changes"
        }
    }

    private fun saveChildDataAndProceed() {
        val enteredName = binding.etChildName.text.toString().trim()
        val finalName = enteredName.ifEmpty {
            getString(R.string.default_child_name)
        }

        preferenceHelper.childName = finalName

        if (customPhotoUri != null) {
            preferenceHelper.customAvatarUri = customPhotoUri.toString()
            preferenceHelper.childAvatarRes = 0
        } else {
            preferenceHelper.customAvatarUri = null
            preferenceHelper.childAvatarRes = avatarAdapter.getSelectedAvatar()
        }

        voiceAssistant.stop()

        // 🎯 Check mode: Onboarding vs Settings Edit
        if (preferenceHelper.isFirstLaunch) {
            navigateToAgeSelection()
        } else {
            showToast("Child profile updated!")
            finish() // 👈 Returns straight back to ParentsSettingsActivity!
        }
    }

    private fun initPermissionHelper() {
        cameraPermissionHelper = PermissionHelper(
            activity = this,
            onAllGranted = {
                launchImageCropper()
            },
            onDenied = {
                showToast("Camera permission is required to take a photo")
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (preferenceHelper.isTtsEnabled) {
            voiceAssistant.speak(getString(R.string.tts_child_name))
        }
    }

    private fun setupAvatarList() {
        val avatars = listOf(
            R.drawable.avatar_boy_1,
            R.drawable.avatar_boy_2,
            R.drawable.avatar_boy_3,
            R.drawable.avatar_boy_4,
            R.drawable.avatar_girl_1,
            R.drawable.avatar_girl_2,
            R.drawable.avatar_girl_3,
            R.drawable.avatar_girl_4
        )

        avatarAdapter = AvatarAdapter(avatars) { selectedDrawableRes ->
            customPhotoUri = null
            binding.ivHeroAvatar.setImageResource(selectedDrawableRes)
        }

        binding.rvAvatars.apply {
            layoutManager = LinearLayoutManager(this@ChildNameActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = avatarAdapter
        }
    }

    private fun setupClickListeners() {
        // Toggle Audio narration
        binding.cardAudioToggle.setOnSafeClickListener {
            preferenceHelper.isTtsEnabled = !preferenceHelper.isTtsEnabled
            updateAudioToggleIcon()

            if (preferenceHelper.isTtsEnabled) {
                voiceAssistant.speak(getString(R.string.tts_child_name))
            } else {
                voiceAssistant.stop()
            }
        }

        // Camera / Gallery Photo Picker
        binding.cardPickCustomPhoto.setOnSafeClickListener {
            cameraPermissionHelper.checkAndRequestPermissions(Manifest.permission.CAMERA)
        }

        // Continue Button
        binding.btnContinue.setOnSafeClickListener {
            saveChildDataAndProceed()
        }
    }

    private fun launchImageCropper() {
        val cropOptions = CropImageOptions().apply {
            imageSourceIncludeGallery = true
            imageSourceIncludeCamera = true
            cropShape = CropImageView.CropShape.OVAL
            fixAspectRatio = true
            aspectRatioX = 1
            aspectRatioY = 1
            guidelines = CropImageView.Guidelines.ON
        }

        cropImageLauncher.launch(CropImageContractOptions(uri = null, cropImageOptions = cropOptions))
    }


    private fun updateAudioToggleIcon() {
        val iconRes = if (preferenceHelper.isTtsEnabled) {
            R.drawable.ic_volume_up
        } else {
            R.drawable.ic_volume_off
        }
        binding.ivAudioToggle.setImageResource(iconRes)
    }

    private fun navigateToAgeSelection() {
        startActivity(Intent(this, AgeSelectionActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroy() {
        voiceAssistant.stop()
        super.onDestroy()
    }
}












