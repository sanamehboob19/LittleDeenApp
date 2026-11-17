package com.ccx.islamic.kids.littledeenapp.helper


import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionHelper(
    private val activity: AppCompatActivity,
    private val onAllGranted: () -> Unit,
    private val onDenied: (List<String>) -> Unit = {}
) {

    private val launcher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val deniedList = results.filter { !it.value }.map { it.key }
            if (deniedList.isEmpty()) {
                onAllGranted()
            } else {
                onDenied(deniedList)
            }
        }

    fun checkAndRequestPermissions(vararg permissions: String) {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            onAllGranted()
        } else {
            launcher.launch(notGranted.toTypedArray())
        }
    }

    companion object {
        fun hasPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}