package com.ccx.islamic.kids.littledeenapp.util

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceAssistant @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferenceHelper: PreferenceHelper
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var isReady = false
        private set

    // 🎯 Main thread handler for UI-safe callbacks
    private val mainHandler = Handler(Looper.getMainLooper())

    // Callbacks
    private var onSpeechDoneCallback: (() -> Unit)? = null
    private var onEngineReadyCallback: (() -> Unit)? = null

    // Pending speech queue during cold start
    private var pendingSpeechText: String? = null
    private var pendingSpeechOnDone: (() -> Unit)? = null

    private val successPhrases = listOf(
        "Great job!", "Amazing!", "Well done!", "Fantastic!",
        "You did it!", "Awesome!", "Excellent!", "Super job!"
    )

    private val errorPhrases = listOf(
        "Oops! Try again.", "Almost there!", "Give it another go!", "You can do it!"
    )

    /**
     * Pre-warms the engine early in Application.onCreate()
     */
    fun warmUp() {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext, this)
        }
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            isReady = false
            return
        }

        val speechResult = tts?.setLanguage(Locale.US)
        if (speechResult == TextToSpeech.LANG_MISSING_DATA || speechResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.language = Locale.getDefault()
        }

        configureVoice()
        applySavedVoiceSettings()
        setupProgressListener()

        isReady = true
        onEngineReadyCallback?.invoke()
        onEngineReadyCallback = null

        // Speak pending text immediately if requested during warm-up
        pendingSpeechText?.let { text ->
            val callback = pendingSpeechOnDone
            pendingSpeechText = null
            pendingSpeechOnDone = null
            speak(text, onDone = callback)
        }
    }

    private fun configureVoice() {
        val voices = tts?.voices ?: return
        val femaleVoice = voices.firstOrNull { voice ->
            voice.locale.language == Locale.US.language &&
                    voice.name.contains("female", ignoreCase = true)
        }

        if (femaleVoice != null) {
            tts?.voice = femaleVoice
            return
        }

        val usVoice = voices.firstOrNull { it.locale == Locale.US }
        usVoice?.let { tts?.voice = it }
    }

    fun applySavedVoiceSettings() {
        val rateProgress = preferenceHelper.ttsSpeechRate // 0..100
        val pitchProgress = preferenceHelper.ttsPitch // 0..100

        val rate = 0.5f + (rateProgress / 100f) // 0.5f .. 1.5f
        val pitch = 0.7f + (pitchProgress / 100f) // 0.7f .. 1.7f

        tts?.setSpeechRate(rate)
        tts?.setPitch(pitch)
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                mainHandler.post {
                    val callback = onSpeechDoneCallback
                    onSpeechDoneCallback = null
                    callback?.invoke()
                }
            }

            override fun onError(utteranceId: String?) {
                mainHandler.post {
                    val callback = onSpeechDoneCallback
                    onSpeechDoneCallback = null
                    callback?.invoke()
                }
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                mainHandler.post {
                    val callback = onSpeechDoneCallback
                    onSpeechDoneCallback = null
                    callback?.invoke()
                }
            }
        })
    }

    /**
     * Speaks English text immediately and resets to US locale.
     */
    fun speak(text: String, onDone: (() -> Unit)? = null) {
        if (!preferenceHelper.isTtsEnabled || text.isBlank()) {
            onDone?.invoke()
            return
        }

        if (isReady) {
            this.onSpeechDoneCallback = onDone
            val utteranceId = UUID.randomUUID().toString()
            val params = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            }

            // Ensure language is set to English
            tts?.language = Locale.US
            configureVoice()
            applySavedVoiceSettings()

            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } else {
            pendingSpeechText = text
            pendingSpeechOnDone = onDone
            warmUp()
        }
    }

    /**
     * Speaks text in a specific language locale (e.g., Arabic "ar").
     */
    fun speakInLocale(text: String, locale: Locale, onDone: (() -> Unit)? = null) {
        if (!preferenceHelper.isTtsEnabled || text.isBlank()) {
            onDone?.invoke()
            return
        }

        if (isReady) {
            this.onSpeechDoneCallback = onDone
            val utteranceId = UUID.randomUUID().toString()
            val params = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            }

            tts?.language = locale
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } else {
            warmUp()
        }
    }

    /**
     * Speaks Dua seamlessly in sequence without hardcoded timers:
     * English Introduction ➔ (Waits until finished) ➔ Arabic Dua ➔ (Waits until finished) ➔ English Translation.
     */
    fun speakDuaSequence(
        introEnglish: String,
        arabicDua: String,
        translationEnglish: String,
        onArabicStart: (() -> Unit)? = null
    ) {
        stop()
        if (!preferenceHelper.isTtsEnabled) return

        // 1. Speak English introduction first
        speak(introEnglish) {
            // 2. Runs the exact moment English finishes: Speak Arabic Dua
            onArabicStart?.invoke()
            speakInLocale(arabicDua, Locale("ar")) {
                // 3. Runs the exact moment Arabic finishes: Speak English translation
                speak(translationEnglish)
            }
        }
    }

    /**
     * Speaks "Assalamu Alaikum" in authentic Arabic pronunciation,
     * followed by the English welcome message.
     */
    fun speakSalamWelcome(englishSuffix: String = "Welcome to Little Deen", onDone: (() -> Unit)? = null) {
        if (!preferenceHelper.isTtsEnabled) {
            onDone?.invoke()
            return
        }

        if (!isReady) {
            onEngineReadyCallback = {
                speakSalamWelcome(englishSuffix, onDone)
            }
            warmUp()
            return
        }

        val arabicLocale = Locale("ar")
        val isArabicSupported = tts?.isLanguageAvailable(arabicLocale)

        if (isArabicSupported != null && isArabicSupported >= TextToSpeech.LANG_AVAILABLE) {
            tts?.language = arabicLocale
            val salamId = UUID.randomUUID().toString()
            val salamParams = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, salamId)
            }
            tts?.speak("السَّلَامُ عَلَيْكُمْ", TextToSpeech.QUEUE_FLUSH, salamParams, salamId)

            tts?.language = Locale.US
            configureVoice()
            applySavedVoiceSettings()

            this.onSpeechDoneCallback = onDone
            val englishId = UUID.randomUUID().toString()
            val englishParams = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, englishId)
            }
            tts?.speak(englishSuffix, TextToSpeech.QUEUE_ADD, englishParams, englishId)
        } else {
            speak("As-salaamu alaykum! $englishSuffix", onDone)
        }
    }

    fun speakSuccess(onDone: (() -> Unit)? = null) = speak(successPhrases.random(), onDone)

    fun speakError(onDone: (() -> Unit)? = null) = speak(errorPhrases.random(), onDone)

    fun setPitch(pitch: Float) {
        tts?.setPitch(pitch)
    }

    fun setSpeechRate(rate: Float) {
        tts?.setSpeechRate(rate)
    }

    fun stop() {
        onSpeechDoneCallback = null
        pendingSpeechText = null
        pendingSpeechOnDone = null
        try {
            tts?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shutdown() {
        isReady = false
        stop()
        try {
            tts?.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tts = null
    }
}