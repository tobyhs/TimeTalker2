package io.github.tobyhs.timetalker

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Intent

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

import io.github.tobyhs.timetalker.test.runService

@RunWith(RobolectricTestRunner::class)
class ScreenServiceTest {
    @Test
    fun `onCreate sets isRunning to true`() {
        runService<ScreenService> {
            assertThat(ScreenService.isRunning, equalTo(true))
        }
    }

    @Test
    fun `onCreate foregrounds itself`() {
        runService<ScreenService> { service ->
            assertThat(shadowOf(service).isLastForegroundNotificationAttached, equalTo(true))

            val notificationManager = service.getSystemService(NotificationManager::class.java)
            val notifications = notificationManager!!.activeNotifications
            assertThat(notifications.size, equalTo(1))

            val notification = notifications.first()
            val channelId = notification.notification.channelId
            assertThat(channelId, equalTo(App.SERVICE_NOTIFICATION_CHANNEL))
            val text = notification.notification.extras.getString(Notification.EXTRA_TITLE)
            assertThat(text, equalTo("Detecting when screen turns on"))
            assertThat(notification.id, equalTo(ScreenService.NOTIFICATION_ID))
        }
    }

    @Test
    fun `onCreate registers a ScreenOnReceiver`() {
        runService<ScreenService> { service ->
            val receiver = findReceiverForIntentAction(service.application, Intent.ACTION_SCREEN_ON)
            assertThat(receiver is ScreenOnReceiver, equalTo(true))
        }
    }

    @Test
    fun `onDestroy unregisters the ScreenOnReceiver`() {
        val service = runService<ScreenService>()
        val receiver = findReceiverForIntentAction(service.application, Intent.ACTION_SCREEN_ON)
        assertThat(receiver, nullValue())
    }

    @Test
    fun `onDestroy sets isRunning to false`() {
        runService<ScreenService>()
        assertThat(ScreenService.isRunning, equalTo(false))
    }

    @Test
    fun `onBind returns null`() {
        val service = ScreenService()
        assertThat(service.onBind(Intent()), nullValue())
    }

    /**
     * Finds a broadcast receiver that can matches the given intent action
     *
     * This exists because {@code org.robolectric.shadows.ShadowApplication#getReceiversForIntent}
     * is deprecated.
     *
     * @param application the application instance
     * @param action intent action to match against
     * @return a broadcast receiver that handles the given intent action, or null if there isn't a
     *   matching receiver
     */
    private fun findReceiverForIntentAction(application: Application, action: String): BroadcastReceiver? {
        for (wrapper in shadowOf(application).registeredReceivers) {
            if (wrapper.intentFilter.matchAction(action)) {
                return wrapper.broadcastReceiver
            }
        }
        return null
    }
}
