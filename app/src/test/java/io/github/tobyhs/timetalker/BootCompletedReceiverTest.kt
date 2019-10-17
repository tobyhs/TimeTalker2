package io.github.tobyhs.timetalker

import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
class BootCompletedReceiverTest {
    private val application = ApplicationProvider.getApplicationContext<App>()
    private val receiver = BootCompletedReceiver()

    @Test
    fun `onReceive starts ScreenService when the intent action is ACTION_BOOT_COMPLETED`() {
        val intent = Intent(Intent.ACTION_BOOT_COMPLETED)
        receiver.onReceive(application, intent)

        val serviceIntent = shadowOf(application).nextStartedService
        val componentName = ComponentName(application, ScreenService::class.java)
        assertThat(serviceIntent.component, equalTo(componentName))
    }

    @Test
    fun `onReceive does not start ScreenService when the intent action is not ACTION_BOOT_COMPLETED`() {
        val intent = Intent("other")
        receiver.onReceive(application, intent)

        val serviceIntent = shadowOf(application).nextStartedService
        assertThat(serviceIntent, nullValue())
    }
}
