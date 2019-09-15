package io.github.tobyhs.timetalker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

/**
 * Application subclass
 */
class App : Application() {
    companion object {
        const val DEFAULT_NOTIFICATION_CHANNEL = "default"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    /**
     * Creates notification channel(s)
     */
    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            DEFAULT_NOTIFICATION_CHANNEL,
            getString(R.string.default_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }
}
