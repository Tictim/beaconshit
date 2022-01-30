package beaconshit

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalTime::class) // stfu
fun main() {
    BeaconColorCombinations.populate()
    val t = measureTime {
        checkEverything()
        //parityCheck()
    }
    println("\nCan you believe that this entire shit took $t?\n")
    Record.printResult()
}

fun checkEverything() {
    for (colorCode in 0..0xFFFFFF) {
        val check = check(colorCode)
        if (check < 0) Record.unsolved(colorCode)
        else Record.solved(colorCode, check)
    }
}

fun parityCheck() {
    for (colorCode in 0..0xFFFFFF) {
        val check = check(colorCode)
        val check2 = check2(colorCode)
        if (check != check2)
            println("Fuck ${toHexCode(colorCode)}: $check and $check2")
    }
}

private fun check(colorCode: Int): Int {
    val color = rgbToColor(colorCode)
    for (glasses in 0..MAX_GLASSES) {
        BeaconColorCombinations.colors(glasses).aroundColor(color, APPROX_SQRT) { c, _ ->
            if (color.isApproximate(c)) return glasses
        }
    }
    return -1
}

private fun check2(colorCode: Int): Int {
    val color = rgbToColor(colorCode)
    for (glasses in 0..MAX_GLASSES) {
        BeaconColorCombinations.colors(glasses).forEachColor { c, _ ->
            if (color.isApproximate(c)) return glasses
        }
    }
    return -1
}