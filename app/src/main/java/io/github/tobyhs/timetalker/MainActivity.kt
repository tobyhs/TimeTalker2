package io.github.tobyhs.timetalker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.enabled_switch

/**
 * Activity that allows the user to start or stop {@code ScreenService}
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enabled_switch.isChecked = ScreenService.isRunning
        enabled_switch.setOnCheckedChangeListener { _, isChecked ->
            val serviceIntent = Intent(this, ScreenService::class.java)
            if (isChecked) {
                startForegroundService(serviceIntent)
            } else {
                stopService(serviceIntent)
            }
        }
    }
}
