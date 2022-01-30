package beaconshit

import kotlin.math.max
import kotlin.math.min

fun sq(v: Float) = v * v

fun toHexCode(colorCode: Int) = "%06X".format(colorCode)

inline fun <T> List<T>.forEachElement(min: Int, max: Int, work: (T) -> Unit) {
    for (i in max(0, min)..min(size - 1, max))
        work(this[i])
}

fun intPow(base: Int, ex: Int): Int {
    if (ex < 0) error("ex < 0")
    if (ex == 0) return 1
    if (ex == 1) return base
    var r = base
    repeat(ex - 1) { r *= base }
    return r
}