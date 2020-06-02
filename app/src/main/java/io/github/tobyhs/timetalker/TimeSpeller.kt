package io.github.tobyhs.timetalker

import java.time.LocalTime

/**
 * Spells times
 *
 * This exists because passing something like "12 34" to android.speech.tts.TextToSpeech#speak
 * results in "one two three four" being spoken on some Android devices instead of "twelve thirty
 * four".
 */
interface TimeSpeller {
    /**
     * Spells the given time
     *
     * @param time the time to spell
     * @return the time spelled out
     */
    fun spell(time: LocalTime): String
}
