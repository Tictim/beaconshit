package beaconshit

fun rgbToColor(rgb: Int): Color { // fun with kotlin bit operators
    val r: Int = rgb and 0xFF0000 shr 16
    val g: Int = rgb and 0x00FF00 shr 8
    val b: Int = rgb and 0x0000FF shr 0
    return Color(r / 255f, g / 255f, b / 255f)
}

data class Color(val red: Float, val green: Float, val blue: Float) {
    operator fun plus(c2: Color): Color = Color(
        (red + c2.red) / 2f,
        (green + c2.green) / 2f,
        (blue + c2.blue) / 2f
    )

    fun isApproximate(o: Color): Boolean = (
            sq(red - o.red) +
                    sq(green - o.green) +
                    sq(blue - o.blue))
        .toDouble() < APPROX // wtf bro

    companion object {
        val WHITE = Color(1f, 1f, 1f)

        val VANILLA_COLORS = listOf(
            rgbToColor(0xf9fffe),
            rgbToColor(0xf9801d),
            rgbToColor(0xc74ebd),
            rgbToColor(0x3ab3da),
            rgbToColor(0xfed83d),
            rgbToColor(0x80c71f),
            rgbToColor(0xf38baa),
            rgbToColor(0x474f52),
            rgbToColor(0x9d9d97),
            rgbToColor(0x169c9c),
            rgbToColor(0x8932b8),
            rgbToColor(0x3c44aa),
            rgbToColor(0x835432),
            rgbToColor(0x5e7c16),
            rgbToColor(0xb02e26),
            rgbToColor(0x1d1d21)
        )
    }
}
