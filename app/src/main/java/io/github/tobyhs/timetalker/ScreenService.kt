package io.github.tobyhs.timetalker

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

/**
 * A service that registers {@link ScreenOnReceiver}
 */
class ScreenService : Service() {
    private val screenOnReceiver = ScreenOnReceiver()

    companion object {
        internal const val NOTIFICATION_ID = 1
        internal var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        foregroundSelf()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(screenOnReceiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(screenOnReceiver)
        isRunning = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Makes this service run in the foreground with a notification
     */
    private fun foregroundSelf() {
        val notification = Notification.Builder(this, App.SERVICE_NOTIFICATION_CHANNEL)
            .setContentTitle(getString(R.string.screen_service_notification_title))
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }
}
