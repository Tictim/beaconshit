package beaconshit

object BeaconColorCombinations {
    private val colors = mutableMapOf<Int, Colorspace<Void?>>()

    fun colors(glasses: Int): Colorspace<Void?> = colors[glasses]!!

    fun populate() {
        colors[0] = Colorspace<Void?>(1).also { it.add(Color.WHITE, null) }
        colors[1] = Colorspace<Void?>(2).also {
            for (c in Color.VANILLA_COLORS)
                it.add(c, null)
        }
        for (glasses in 2..MAX_GLASSES) {
            val newColors = Colorspace<Void?>(intPow(2, glasses + 1))
            var count = 0
            colors[glasses-1]!!.forEachColor { c, _ ->
                for (c2 in Color.VANILLA_COLORS)
                    if (c !== c2 && newColors.add(c + c2, null))
                        count++
            }
            println("Beacon with $glasses stained glasses has $count possible unique colors.")
            colors[glasses] = newColors
        }
    }
}
