package com.ccx.islamic.kids.littledeenapp.extension


import android.os.SystemClock
import android.view.View
import com.ccx.islamic.kids.littledeenapp.util.AppConstants




/**
 * Prevents rapid double clicks and plays a smooth, tactile
 * press & bounce animation whenever the view is tapped.
 */
fun View.setOnSafeClickListener(
    debounceTime: Long = AppConstants.CLICK_DEBOUNCE_MS,
    onSafeClick: (View) -> Unit
) {
    var lastClickTime = 0L
    this.setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime

            // 🎯 Tactile Button Press Animation (Compress ➔ Bounce Back)
            view.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .setDuration(80)
                .withEndAction {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(80)
                        .withEndAction {
                            onSafeClick(view)
                        }
                        .start()
                }
                .start()
        }
    }
}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

