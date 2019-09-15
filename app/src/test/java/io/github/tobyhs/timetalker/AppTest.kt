package io.github.tobyhs.timetalker

import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTest {
    @Test
    fun createNotificationChannels() {
        val app = ApplicationProvider.getApplicationContext<App>()
        val notificationManager = app.getSystemService(NotificationManager::class.java)
        val channel = notificationManager.getNotificationChannel("default")
        assertThat(channel.name.toString(), equalTo("Default"))
        assertThat(channel.importance, equalTo(NotificationManager.IMPORTANCE_LOW))
    }
}
