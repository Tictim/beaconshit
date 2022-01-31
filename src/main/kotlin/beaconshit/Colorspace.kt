package beaconshit

class Colorspace {
    private val colors = Subdivision(
        0f, 1f, 0f, 1f, 0f, 1f,
        threshold = 500,
        thresholdGrowthRate = 2.0,
        maxDepth = Int.MAX_VALUE
    )

    fun anyMatch(predicate: (Color) -> Boolean): Boolean =
        colors.anyMatch(predicate)

    fun anyMatchInBoundary(color: Color, boundary: Float, predicate: (Color) -> Boolean): Boolean {
        val rm = color.red - boundary
        val rx = color.red + boundary
        val gm = color.green - boundary
        val gx = color.green + boundary
        val bm = color.blue - boundary
        val bx = color.blue + boundary

        return colors.anyMatchInBoundary(rm, rx, gm, gx, bm, bx, predicate)
    }

    fun add(color: Color): Boolean = colors.add(color)

    fun forEachColor(consumer: (Color) -> Unit) =
        colors.forEach(consumer)

    val depth
        get() = colors.depth

    class Subdivision(
        val rMin: Float,
        val rMax: Float,
        val gMin: Float,
        val gMax: Float,
        val bMin: Float,
        val bMax: Float,
        val threshold: Int,
        val thresholdGrowthRate: Double,
        val maxDepth: Int
    ) {
        private var map: MutableSet<Color>? = null
        private var subdivisions: Octree? = null

        val depth: Int
            get() {
                var depth = 1
                subdivisions?.let { octree -> depth += octree.arr.maxOf { it.depth } }
                return depth
            }

        init {
            if (threshold <= 0) error("threshold <= 0")
        }

        fun add(color: Color): Boolean {
            if (!isInBoundary(color)) return false
            subdivisions?.let { return it.add(color) }

            val m = map ?: mutableSetOf<Color>().also { map = it }
            val result = m.add(color)
            if (maxDepth > 0 && m.size >= threshold) {
                val octree = Octree(this)
                subdivisions = octree
                octree.addAll(m)
                map = null
            }
            return result
        }

        fun isInBoundary(color: Color): Boolean {
            return color.red in rMin..rMax &&
                    color.green in gMin..gMax &&
                    color.blue in bMin..bMax
        }

        fun isInBoundary(
            rMin: Float,
            rMax: Float,
            gMin: Float,
            gMax: Float,
            bMin: Float,
            bMax: Float
        ): Boolean {
            return rMax >= this.rMin && rMin <= this.rMax &&
                    gMax >= this.gMin && gMin <= this.gMax &&
                    bMax >= this.bMin && bMin <= this.bMax
        }

        fun forEach(f: (Color) -> Unit) {
            subdivisions?.let {
                for (s in it.arr) s.forEach(f)
                return
            }
            map?.forEach(f)
        }

        fun forEachInBoundary(
            rMin: Float,
            rMax: Float,
            gMin: Float,
            gMax: Float,
            bMin: Float,
            bMax: Float,
            f: (Color) -> Unit
        ) {
            map?.let {
                if (!isInBoundary(rMin, rMax, gMin, gMax, bMin, bMax)) return
                it.forEach(f)
                return
            }
            subdivisions?.arr?.forEach {
                it.forEachInBoundary(rMin, rMax, gMin, gMax, bMin, bMax, f)
            }
        }

        fun anyMatch(predicate: (Color) -> Boolean): Boolean {
            map?.let {
                return it.any(predicate)
            }
            subdivisions?.let {
                return it.arr.any { s ->
                    s.anyMatch(predicate)
                }
            }
            return false
        }

        fun anyMatchInBoundary(
            rMin: Float,
            rMax: Float,
            gMin: Float,
            gMax: Float,
            bMin: Float,
            bMax: Float,
            predicate: (Color) -> Boolean
        ): Boolean {
            map?.let {
                if (!isInBoundary(rMin, rMax, gMin, gMax, bMin, bMax)) return false
                return it.any(predicate)
            }
            subdivisions?.let {
                return it.arr.any { s ->
                    s.anyMatchInBoundary(rMin, rMax, gMin, gMax, bMin, bMax, predicate)
                }
            }
            return false
        }
    }

    @JvmInline
    value class Octree private constructor(
        val arr: Array<Subdivision>
    ) {
        constructor(subdivision: Subdivision) : this(subdivision.run {
            Array(8) { i ->
                val r = i and 1 == 0
                val g = i and 2 == 0
                val b = i and 4 == 0

                val rm = if (r) rMin else (rMin + rMax) / 2
                val rx = if (r) (rMin + rMax) / 2 else rMax
                val gm = if (g) gMin else (gMin + gMax) / 2
                val gx = if (g) (gMin + gMax) / 2 else gMax
                val bm = if (b) bMin else (bMin + bMax) / 2
                val bx = if (b) (bMin + bMax) / 2 else bMax

                Subdivision(
                    rm,
                    rx,
                    gm,
                    gx,
                    bm,
                    bx,
                    (threshold * thresholdGrowthRate).toInt(),
                    thresholdGrowthRate,
                    subdivision.maxDepth - 1
                )
            }
        })

        fun add(color: Color): Boolean = arr.any { it.add(color) }

        fun addAll(colors: Iterable<Color>) {
            for (c in colors) run{
                for (subdivision in arr) {
                    if (subdivision.add(c)) return@run
                }
                println("WARNING: $c got ditched for whatever the fucking reason is, go figure that")
            }
        }
    }
}