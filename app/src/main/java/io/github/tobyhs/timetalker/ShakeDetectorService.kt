package io.github.tobyhs.timetalker

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.Handler
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.widget.Toast

import com.squareup.seismic.ShakeDetector

import java.time.Clock
import java.time.LocalTime

/**
 * A service that speaks the time if shaking is detected
 */
class ShakeDetectorService : Service(), ShakeDetector.Listener {
    internal var clock: Clock = Clock.systemDefaultZone()
    private val timeSpeller: TimeSpeller = AmericanTimeSpeller()
    internal lateinit var textToSpeech: TextToSpeech
    private lateinit var shakeDetector: ShakeDetector

    companion object {
        const val NOTIFICATION_ID = 2
        const val LIFETIME_MILLIS = 10000L
    }

    override fun onCreate() {
        super.onCreate()
        foregroundSelf()

        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(this, R.string.tts_init_failure, Toast.LENGTH_LONG).show()
            }
        }

        shakeDetector = ShakeDetector(this)
        shakeDetector.start(getSystemService(SensorManager::class.java))

        Handler().postDelayed({ stopSelf() }, LIFETIME_MILLIS)
    }

    override fun onDestroy() {
        shakeDetector.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun hearShake() {
        val audioManager = getSystemService(AudioManager::class.java)!!
        if (audioManager.mode == AudioManager.MODE_NORMAL) {
            val time = timeSpeller.spell(LocalTime.now(clock))
            textToSpeech.speak(time, TextToSpeech.QUEUE_FLUSH, null, time)
        }
    }

    /**
     * Makes this service run in the foreground with a notification
     */
    private fun foregroundSelf() {
        val notification = Notification.Builder(this, App.SERVICE_NOTIFICATION_CHANNEL)
            .setContentTitle(getString(R.string.shake_detector_notification_title))
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }
}
