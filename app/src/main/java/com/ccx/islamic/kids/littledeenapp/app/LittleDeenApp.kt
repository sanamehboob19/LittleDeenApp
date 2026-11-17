package com.ccx.islamic.kids.littledeenapp.app


import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class LittleDeenApp : Application() {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var musicManager: MusicManager

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Pre-warm the TTS engine
        voiceAssistant.warmUp()

        // Seamless App-Wide Background Music Management
        setupProcessLifecycle()
    }

    private fun setupProcessLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                // App entered foreground (any activity opened)
                musicManager.onAppForegrounded()
            }

            override fun onStop(owner: LifecycleOwner) {
                // App entered background (user went home or locked screen)
                musicManager.onAppBackgrounded()
            }
        })
    }

    override fun onTerminate() {
        voiceAssistant.shutdown()
        musicManager.release()
        super.onTerminate()
    }

    companion object {
        lateinit var instance: LittleDeenApp
            private set
    }
}