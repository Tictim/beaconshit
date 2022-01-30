package beaconshit

class Colorspace<T>(subdivision: Int) {
    val colors: List<List<List<MutableMap<Color, T>>>>

    init {
        if (subdivision <= 0) error("Subdivision less than 1")
        val actualSubdivision = if (SUBDIVISION) subdivision else 1

        colors = List(actualSubdivision) { List(actualSubdivision) { List(actualSubdivision) { mutableMapOf() } } }
    }

    inline fun aroundColor(color: Color, boundary: Float, consumer: (Color, T) -> Unit) {
        val rm = minBoundary(color.red - boundary)
        val rx = maxBoundary(color.red + boundary)
        val gm = minBoundary(color.green - boundary)
        val gx = maxBoundary(color.green + boundary)
        val bm = minBoundary(color.blue - boundary)
        val bx = maxBoundary(color.blue + boundary)

        colors.forEachElement(rm, rx) { g ->
            g.forEachElement(gm, gx) { b ->
                b.forEachElement(bm, bx) {
                    for ((c2, t) in it)
                        consumer(c2, t)
                }
            }
        }
    }

    inline fun add(
        color: Color,
        new: T,
        overwrite: (old: T, new: T) -> Boolean = { _, _ -> false }
    ): Boolean {
        val m = getMap(color)
        val old = m[color]
        if (old == null || overwrite(old, new)) {
            m[color] = new
            return true
        }
        return false
    }

    inline fun forEachColor(consumer: (Color, T) -> Unit) {
        for (g in colors)
            for (b in g)
                for (m in b)
                    m.forEach { (k, v) -> consumer(k, v) }
    }

    @PublishedApi
    internal fun getMap(color: Color): MutableMap<Color, T> {
        return colors[i(color.red)][i(color.green)][i(color.blue)]
    }

    private fun i(v: Float) = (v * colors.size)
        .toInt()
        .coerceIn(0, colors.lastIndex)

    fun minBoundary(v: Float) =
        when {
            v <= 0 -> 0
            v >= 1 -> colors.lastIndex
            else -> i(v)
        }

    fun maxBoundary(v: Float) =
        when {
            v <= 0 -> 0
            v >= 1 -> colors.lastIndex
            else -> i(v)
        }
}