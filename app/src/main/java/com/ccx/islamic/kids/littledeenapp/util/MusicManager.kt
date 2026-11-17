package com.ccx.islamic.kids.littledeenapp.util


import android.content.Context
import android.media.MediaPlayer
import com.ccx.islamic.kids.littledeenapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class MusicManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferenceHelper: PreferenceHelper
) {

    private var mediaPlayer: MediaPlayer? = null
    private var isAppInForeground = false

    /**
     * Starts or resumes background music.
     * Respects the switch setting in PreferenceHelper.
     */
    fun startMusic() {
        if (!preferenceHelper.isBgMusicEnabled) return

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.kids_music)?.apply {
                    isLooping = true
                    // 50% default volume scaled by appVolume preference
                    applyCurrentVolume()
                }
            }

            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Pauses background music (when app is minimized or user toggles OFF)
     */
    fun pauseMusic() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Toggles music on/off from the settings switch
     */
    fun setMusicEnabled(enabled: Boolean) {
        preferenceHelper.isBgMusicEnabled = enabled
        if (enabled) {
            if (isAppInForeground) {
                startMusic()
            }
        } else {
            pauseMusic()
        }
    }

    /**
     * Sets volume (progress 0..100) scaled to a max of 50% so it doesn't overpower TTS
     */
    /**
     * Scales the user's slider (0..100) to a maximum of 20% (0.20f).
     * Even if the phone's physical volume is at 100%, the music will
     * never play louder than 20%, keeping it soft and gentle for kids.
     */
    fun updateVolume(progress: Int) {
        val maxCap = 0.20f // Strict 20% maximum ceiling
        val scaledVolume = (progress / 100f) * maxCap

        try {
            mediaPlayer?.setVolume(scaledVolume, scaledVolume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyCurrentVolume() {
        updateVolume(preferenceHelper.appVolume)
    }

    // Lifecycle triggers called by ProcessLifecycleOwner in Application class
    fun onAppForegrounded() {
        isAppInForeground = true
        if (preferenceHelper.isBgMusicEnabled) {
            startMusic()
        }
    }

    fun onAppBackgrounded() {
        isAppInForeground = false
        pauseMusic()
    }

    fun release() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer = null
    }
}