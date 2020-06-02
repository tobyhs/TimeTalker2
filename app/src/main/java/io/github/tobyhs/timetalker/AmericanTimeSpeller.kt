package io.github.tobyhs.timetalker

import java.time.LocalTime

/**
 * Spells times in the U.S. 12-hour clock style
 */
class AmericanTimeSpeller : TimeSpeller {
    override fun spell(time: LocalTime): String {
        var hour: Int = time.hour
        val period: String = if (hour >= 12) "PM" else "AM"
        if (hour > 12) {
            hour -= 12
        } else if (hour == 0) {
            hour = 12
        }

        var words: String = spellNumber(hour)
        if (time.minute != 0) {
            if (time.minute <= 9) {
                words += " O"
            }
            words += " ${spellNumber(time.minute)}"
        }
        return "$words $period"
    }

    /**
     * Spells the given number (as long as the number is an integer between 0 and 60 exclusive)
     *
     * @param number the number to spell
     * @return the number spelled out
     */
    private fun spellNumber(number: Int): String {
        return if (number <= 20) {
            numberMap.getValue(number)
        } else {
            val onesPlace: Int = number % 10
            val tensPlace: Int = number - onesPlace
            val tensPlaceWord: String = numberMap.getValue(tensPlace)
            if (onesPlace == 0) {
                tensPlaceWord
            } else {
                "$tensPlaceWord ${numberMap.getValue(onesPlace)}"
            }
        }
    }
}

private val numberMap: Map<Int, String> = mapOf(
    1 to "one",
    2 to "two",
    3 to "three",
    4 to "four",
    5 to "five",
    6 to "six",
    7 to "seven",
    8 to "eight",
    9 to "nine",

    10 to "ten",
    11 to "eleven",
    12 to "twelve",
    13 to "thirteen",
    14 to "fourteen",
    15 to "fifteen",
    16 to "sixteen",
    17 to "seventeen",
    18 to "eighteen",
    19 to "nineteen",

    20 to "twenty",
    30 to "thirty",
    40 to "forty",
    50 to "fifty"
)
