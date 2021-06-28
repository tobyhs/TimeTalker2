package io.github.tobyhs.timetalker

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.media.AudioManager
import android.os.Looper
import android.speech.tts.TextToSpeech

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.TimeZone
import java.util.concurrent.TimeUnit

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowToast

import io.github.tobyhs.timetalker.test.runService

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ShakeDetectorServiceTest {
    @Test
    fun `onCreate foregrounds the service`() {
        runService<ShakeDetectorService> { service ->
            assertThat(shadowOf(service).isLastForegroundNotificationAttached, equalTo(true))

            val notificationManager = service.getSystemService(NotificationManager::class.java)
            val notifications = notificationManager!!.activeNotifications
            assertThat(notifications.size, equalTo(1))

            val notification = notifications.first()
            val channelId = notification.notification.channelId
            assertThat(channelId, equalTo(App.SERVICE_NOTIFICATION_CHANNEL))
            val text = notification.notification.extras.getString(Notification.EXTRA_TITLE)
            assertThat(text, equalTo("Detecting shaking"))
            assertThat(notification.id, equalTo(ShakeDetectorService.NOTIFICATION_ID))
        }
    }

    @Test
    fun `shows a toast when TextToSpeech initialization fails`() {
        runService<ShakeDetectorService> { service ->
            ShadowToast.reset()
            val listener = shadowOf(service.textToSpeech).onInitListener
            listener.onInit(TextToSpeech.ERROR)
            val lastToastText = ShadowToast.getTextOfLatestToast()
            assertThat(lastToastText, equalTo("Time Talker: Failed to initialize TextToSpeech"))
        }
    }

    @Test
    fun `does not show a toast when TextToSpeech initialization succeeds` () {
        runService<ShakeDetectorService> { service ->
            ShadowToast.reset()
            val listener = shadowOf(service.textToSpeech).onInitListener
            listener.onInit(TextToSpeech.SUCCESS)
            val lastToastText = ShadowToast.getTextOfLatestToast()
            assertThat(lastToastText, nullValue())
        }
    }

    @Test
    fun `stops itself eventually`() {
        val service = Robolectric.setupService(ShakeDetectorService::class.java)
        shadowOf(Looper.getMainLooper()).idleFor(
                ShakeDetectorService.LIFETIME_MILLIS + 1000,
                TimeUnit.MILLISECONDS
        )
        assertThat(shadowOf(service).isStoppedBySelf, equalTo(true))
    }

    @Test
    fun `onDestroy shuts down textToSpeech`() {
        val service = runService<ShakeDetectorService>()
        assertThat(shadowOf(service.textToSpeech).isShutdown, equalTo(true))
    }

    @Test
    fun `onBind returns null`() {
        runService<ShakeDetectorService> { service ->
            assertThat(service.onBind(Intent()), nullValue())
        }
    }

    @Test
    fun `hearShake speaks the time`() {
        runService<ShakeDetectorService> { service ->
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            service.clock = Clock.fixed(Instant.ofEpochSecond(1569182800), ZoneId.systemDefault())
            service.hearShake()
            assertThat(shadowOf(service.textToSpeech).lastSpokenText, equalTo("eight O six PM"))
        }
    }

    @Test
    fun `hearShake does not speak when the audio mode is not normal`() {
        runService<ShakeDetectorService> { service ->
            val audioManager = service.getSystemService(AudioManager::class.java)!!
            audioManager.mode = AudioManager.MODE_IN_CALL
            service.hearShake()
            assertThat(shadowOf(service.textToSpeech).lastSpokenText, nullValue())
        }
    }
}
