package io.github.tobyhs.timetalker

import android.content.ComponentName
import android.widget.Switch
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.Shadows.shadowOf

import io.github.tobyhs.timetalker.test.runService

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun `enabled switch is unchecked when ScreenService is not running`() {
        launch(MainActivity::class.java).onActivity { activity ->
            val switch = activity.findViewById<Switch>(R.id.enabled_switch)
            assertThat(switch.isChecked, equalTo(false))
        }
    }

    @Test
    fun `enabled switch is checked when ScreenService is running`() {
        runService<ScreenService> {
            launch(MainActivity::class.java).onActivity { activity ->
                val switch = activity.findViewById<Switch>(R.id.enabled_switch)
                assertThat(switch.isChecked, equalTo(true))
            }
        }
    }

    @Test
    fun `starts ScreenService when enabled switch is checked`() {
        launch(MainActivity::class.java).onActivity { activity ->
            val switch = activity.findViewById<Switch>(R.id.enabled_switch)
            switch.toggle()
            val actualComponent = shadowOf(activity).nextStartedService.component
            val expectedComponent = ComponentName(activity, ScreenService::class.java)
            assertThat(actualComponent, equalTo(expectedComponent))
        }
    }

    @Test
    fun `stops ScreenService when enabled switch is unchecked`() {
        launch(MainActivity::class.java).onActivity { activity ->
            val switch = activity.findViewById<Switch>(R.id.enabled_switch)
            switch.isChecked = true
            switch.toggle()
            val actualComponent = shadowOf(activity).nextStoppedService.component
            val expectedComponent = ComponentName(activity, ScreenService::class.java)
            assertThat(actualComponent, equalTo(expectedComponent))
        }
    }
}
