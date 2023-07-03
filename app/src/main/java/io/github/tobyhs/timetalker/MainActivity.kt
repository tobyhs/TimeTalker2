package io.github.tobyhs.timetalker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

/**
 * Activity that allows the user to start or stop {@code ScreenService}
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
