package io.github.tobyhs.timetalker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

/**
 * A broadcast receiver that starts {@link ShakeDetectorService}
 */
class ScreenOnReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val audioManager = context.getSystemService(AudioManager::class.java)!!
        if (audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            context.startForegroundService(Intent(context, ShakeDetectorService::class.java))
        }
    }
}
