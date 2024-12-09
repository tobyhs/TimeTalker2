package io.github.tobyhs.timetalker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

/**
 * Activity that allows the user to start or stop {@code ScreenService}
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentView = findViewById<View>(R.id.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }

        val enabledSwitch = findViewById<SwitchCompat>(R.id.enabled_switch)
        enabledSwitch.isChecked = ScreenService.isRunning
        enabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            val serviceIntent = Intent(this, ScreenService::class.java)
            if (isChecked) {
                startForegroundService(serviceIntent)
            } else {
                stopService(serviceIntent)
            }
        }
    }
}
