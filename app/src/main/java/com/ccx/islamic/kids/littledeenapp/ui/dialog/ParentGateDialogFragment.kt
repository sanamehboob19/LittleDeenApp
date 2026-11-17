package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.button.MaterialButton
import com.ccx.islamic.kids.littledeenapp.databinding.DialogParentGateBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class ParentGateDialogFragment : BaseDialogFragment<DialogParentGateBinding>(
    DialogParentGateBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    private var onParentVerified: (() -> Unit)? = null
    private var correctAnswer: Int = 0
    private var enteredInput = StringBuilder()

    override fun onStart() {
        super.onStart()
        // 🎯 Sets exact 95% width on any screen
        dialog?.window?.apply {
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun setupViews() {
        applyThemeStyling()
        generateDifficultChallenge()
        setupNumpadListeners()

        // Friendly parent TTS instruction
        voiceAssistant.speak("Parents only! Please solve the question to continue.")

        binding.ivCloseGate.setOnSafeClickListener {
            dismiss()
        }
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvMathEquation.setTextColor(theme.primaryColor)
        binding.cardInputDisplay.strokeColor = theme.primaryColor
        binding.btnSubmit.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)
    }

    /**
     * Generates a 2-digit addition problem requiring mental carrying
     * (e.g. 48 + 37 = 85), stopping kids below 12 years old.
     */
    /**
     * Generates a 2-digit addition challenge where the sum never exceeds 50.
     */
    private fun generateDifficultChallenge() {
        val num1 = (12..32).random()
        val num2 = (8..(50 - num1)).random()
        correctAnswer = num1 + num2

        binding.tvMathEquation.text = "$num1 + $num2 = ?"
        binding.tvEquationInWords.text = "(${numberToWords(num1)} + ${numberToWords(num2)})"

        enteredInput.clear()
        binding.tvEnteredAnswer.text = ""
    }

    private fun setupNumpadListeners() {
        val digitButtons = listOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9"
        )

        // Digits
        digitButtons.forEach { (button, digit) ->
            button.setOnSafeClickListener {
                if (enteredInput.length < 3) {
                    enteredInput.append(digit)
                    binding.tvEnteredAnswer.text = enteredInput.toString()
                }
            }
        }

        // Backspace
        binding.btnBackspace.setOnSafeClickListener {
            if (enteredInput.isNotEmpty()) {
                enteredInput.deleteCharAt(enteredInput.length - 1)
                binding.tvEnteredAnswer.text = enteredInput.toString()
            }
        }

        // Submit & Verify
        binding.btnSubmit.setOnSafeClickListener {
            verifyAnswer()
        }
    }

    private fun verifyAnswer() {
        val answer = enteredInput.toString().toIntOrNull()

        if (answer != null && answer == correctAnswer) {
            // ✅ SUCCESS
            voiceAssistant.speakSuccess()
            binding.cardInputDisplay.setCardBackgroundColor(Color.parseColor("#E8F5E9"))

            YoYo.with(Techniques.Pulse)
                .duration(300)
                .onEnd {
                    dismiss()
                    onParentVerified?.invoke()
                }
                .playOn(binding.cardInputDisplay)
        } else {
            // ❌ FAILED
            voiceAssistant.speak("Oops! Try again.")

            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(binding.cardChallengeBox)

            // Generate fresh math equation so kids cannot spam
            generateDifficultChallenge()
        }
    }

    /**
     * Converts integer to English words
     */
    private fun numberToWords(num: Int): String {
        val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")
        val units = arrayOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")

        return when {
            num < 20 -> units[num]
            num % 10 == 0 -> tens[num / 10]
            else -> "${tens[num / 10]}-${units[num % 10]}"
        }
    }

    companion object {
        const val TAG = "ParentGateDialogFragment"

        fun newInstance(onVerified: () -> Unit): ParentGateDialogFragment {
            return ParentGateDialogFragment().apply {
                this.onParentVerified = onVerified
            }
        }
    }
}