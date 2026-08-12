package br.dev.saed.volei.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

@Suppress("DEPRECATION")
fun Context.vibrator(durationMillis: Long = 50) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // For Android 12 (S) and above
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrationEffect = VibrationEffect.createOneShot(durationMillis, VibrationEffect.EFFECT_TICK)
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(vibrationEffect)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            // For Android 8.0 (Oreo) to Android 11 (R)
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val vibrationEffect = VibrationEffect.createOneShot(durationMillis, VibrationEffect.EFFECT_TICK)
            vibrator.vibrate(vibrationEffect)
        }

        else -> {
            // For Android versions below Oreo (API level 26)
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(durationMillis)
        }
    }
}