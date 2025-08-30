package io.github.tobyhs.timetalker

import android.content.ComponentName
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
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
            val switch = activity.findViewById<SwitchCompat>(R.id.enabled_switch)
            assertThat(switch.isChecked, equalTo(false))
        }
    }

    @Test
    fun `enabled switch is checked when ScreenService is running`() {
        runService<ScreenService> {
            launch(MainActivity::class.java).onActivity { activity ->
                val switch = activity.findViewById<SwitchCompat>(R.id.enabled_switch)
                assertThat(switch.isChecked, equalTo(true))
            }
        }
    }

    @Test
    fun `starts ScreenService when enabled switch is checked`() {
        launch(MainActivity::class.java).onActivity { activity ->
            val switch = activity.findViewById<SwitchCompat>(R.id.enabled_switch)
            switch.toggle()
            val actualComponent = shadowOf(activity).nextStartedService.component
            val expectedComponent = ComponentName(activity, ScreenService::class.java)
            assertThat(actualComponent, equalTo(expectedComponent))
        }
    }

    @Test
    fun `stops ScreenService when enabled switch is unchecked`() {
        launch(MainActivity::class.java).onActivity { activity ->
            val switch = activity.findViewById<SwitchCompat>(R.id.enabled_switch)
            switch.isChecked = true
            switch.toggle()
            val actualComponent = shadowOf(activity).nextStoppedService.component
            val expectedComponent = ComponentName(activity, ScreenService::class.java)
            assertThat(actualComponent, equalTo(expectedComponent))
        }
    }

    @Test
    fun `starts ShakeDetectorService when button is tapped`() {
        launch(MainActivity::class.java).onActivity { activity ->
            val button = activity.findViewById<AppCompatButton>(R.id.start_shake_detector_service_button)
            button.performClick()
            val actualComponent = shadowOf(activity).nextStartedService.component
            val expectedComponent = ComponentName(activity, ShakeDetectorService::class.java)
            assertThat(actualComponent, equalTo(expectedComponent))
        }
    }
}
