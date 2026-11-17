package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.ccx.islamic.kids.littledeenapp.databinding.DialogExcellentBannerBinding
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ExcellentBannerDialogFragment : DialogFragment() {

    private var _binding: DialogExcellentBannerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    var onCompletedCallback: (() -> Unit)? = null

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogExcellentBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Drop animation with overshoot bounce
        binding.layoutBannerContainer.apply {
            scaleX = 0.2f
            scaleY = 0.2f
            translationY = -300f
            alpha = 0f

            animate()
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .alpha(1f)
                .setDuration(700)
                .setInterpolator(OvershootInterpolator(2.0f))
                .start()
        }

        // Fire Confetti
        val party = Party(
            speed = 5f,
            maxSpeed = 35f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(Color.RED, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN),
            emitter = Emitter(duration = 600, TimeUnit.MILLISECONDS).max(120),
            position = Position.Relative(0.5, 0.4)
        )
        binding.konfettiView.start(party)

        voiceAssistant.speakSuccess()

        // Auto-dismiss after celebration
        lifecycleScope.launch {
            delay(3000)
            if (isAdded) {
                dismissAllowingStateLoss()
                onCompletedCallback?.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ExcellentBannerDialogFragment"

        fun show(fragmentManager: FragmentManager, onCompleted: (() -> Unit)? = null) {
            val dialog = ExcellentBannerDialogFragment()
            dialog.onCompletedCallback = onCompleted
            dialog.show(fragmentManager, TAG)
        }
    }
}