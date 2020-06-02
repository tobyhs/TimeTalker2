package io.github.tobyhs.timetalker

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.time.LocalTime

class AmericanTimeSpellerTest {
    private val timeSpeller: AmericanTimeSpeller = AmericanTimeSpeller()

    @Test
    fun `spell with AM hours`() {
        mapOf(
            0 to "twelve AM",
            1 to "one AM",
            2 to "two AM",
            3 to "three AM",
            4 to "four AM",
            5 to "five AM",
            6 to "six AM",
            7 to "seven AM",
            8 to "eight AM",
            9 to "nine AM",
            10 to "ten AM",
            11 to "eleven AM"
        ).forEach { (hour, expected) ->
            assertThat(timeSpeller.spell(LocalTime.of(hour, 0)), equalTo(expected))
        }
    }

    @Test
    fun `spell with PM hours`() {
        mapOf(
            12 to "twelve PM",
            13 to "one PM",
            14 to "two PM",
            15 to "three PM",
            16 to "four PM",
            17 to "five PM",
            18 to "six PM",
            19 to "seven PM",
            20 to "eight PM",
            21 to "nine PM",
            22 to "ten PM",
            23 to "eleven PM"
        ).forEach { (hour, expected) ->
            assertThat(timeSpeller.spell(LocalTime.of(hour, 0)), equalTo(expected))
        }
    }

    @Test
    fun `spell with minutes less than 20`() {
        mapOf(
            LocalTime.of(0, 1) to "twelve O one AM",
            LocalTime.of(1, 2) to "one O two AM",
            LocalTime.of(2, 3) to "two O three AM",
            LocalTime.of(3, 4) to "three O four AM",
            LocalTime.of(4, 5) to "four O five AM",
            LocalTime.of(5, 6) to "five O six AM",
            LocalTime.of(6, 7) to "six O seven AM",
            LocalTime.of(7, 8) to "seven O eight AM",
            LocalTime.of(8, 9) to "eight O nine AM",
            LocalTime.of(9, 10) to "nine ten AM",
            LocalTime.of(10, 11) to "ten eleven AM",
            LocalTime.of(11, 12) to "eleven twelve AM",
            LocalTime.of(12, 13) to "twelve thirteen PM",
            LocalTime.of(13, 14) to "one fourteen PM",
            LocalTime.of(14, 15) to "two fifteen PM",
            LocalTime.of(15, 16) to "three sixteen PM",
            LocalTime.of(16, 17) to "four seventeen PM",
            LocalTime.of(17, 18) to "five eighteen PM",
            LocalTime.of(18, 19) to "six nineteen PM"
        ).forEach { (time, expected) ->
            assertThat(timeSpeller.spell(time), equalTo(expected))
        }
    }

    @Test
    fun `spell with minutes greater than or equal to 20`() {
        mapOf(
            LocalTime.of(19, 20) to "seven twenty PM",
            LocalTime.of(20, 21) to "eight twenty one PM",
            LocalTime.of(21, 30) to "nine thirty PM",
            LocalTime.of(22, 32) to "ten thirty two PM",
            LocalTime.of(23, 40) to "eleven forty PM",
            LocalTime.of(0, 43) to "twelve forty three AM",
            LocalTime.of(1, 50) to "one fifty AM",
            LocalTime.of(2, 54) to "two fifty four AM"
        ).forEach { (time, expected) ->
            assertThat(timeSpeller.spell(time), equalTo(expected))
        }
    }
}
