package com.ccx.islamic.kids.littledeenapp.extension



import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(messageRes), duration).show()
}

fun Any.logDebug(message: String, tag: String = this::class.java.simpleName) {
    Log.d(tag, message)
}

fun Any.logError(message: String, throwable: Throwable? = null, tag: String = this::class.java.simpleName) {
    Log.e(tag, message, throwable)
}