package io.github.tobyhs.timetalker

import android.content.ComponentName
import android.content.Intent
import android.media.AudioManager

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
class ScreenOnReceiverTest {
    private val application = ApplicationProvider.getApplicationContext<App>()
    private val audioManager = application.getSystemService(AudioManager::class.java)!!
    private val receiver = ScreenOnReceiver()

    @Test
    fun `onReceive with a positive ring stream volume starts ShakeDetectorService`() {
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 5, 0)
        receiver.onReceive(application, Intent(Intent.ACTION_SCREEN_ON))

        val serviceIntent = shadowOf(application).nextStartedService
        val componentName = ComponentName(application, ShakeDetectorService::class.java)
        assertThat(serviceIntent.component, equalTo(componentName))
    }

    @Test
    fun `onReceive with a zero ring stream volume does not start ShakeDetectorService`() {
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0)
        receiver.onReceive(application, Intent(Intent.ACTION_SCREEN_ON))

        val serviceIntent = shadowOf(application).nextStartedService
        assertThat(serviceIntent, nullValue())
    }
}
