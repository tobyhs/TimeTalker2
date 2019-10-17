package io.github.tobyhs.timetalker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * This starts {@link ScreenService} after the phone boots
 */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.action)) {
            val serviceIntent = Intent(context, ScreenService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}
