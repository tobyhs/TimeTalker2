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
        // I'm using getStreamVolume instead of ringerMode because ringerMode doesn't return
        // RINGER_MODE_NORMAL when Do Not Disturb is enabled, but I want the actual ringer
        // mode/volume to take precedence over Do Not Disturb
        if (audioManager.getStreamVolume(AudioManager.STREAM_RING) > 0) {
            context.startForegroundService(Intent(context, ShakeDetectorService::class.java))
        }
    }
}
