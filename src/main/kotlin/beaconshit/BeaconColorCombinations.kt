package beaconshit

object BeaconColorCombinations {
    private val colors = mutableMapOf<Int, Colorspace>()

    fun colors(glasses: Int): Colorspace = colors[glasses]!!

    fun populate() {
        colors[0] = Colorspace().also { it.add(Color.WHITE) }
        colors[1] = Colorspace().also {
            for (c in Color.VANILLA_COLORS)
                it.add(c)
        }
        for (glasses in 2..MAX_GLASSES) {
            val newColors = Colorspace()
            var count = 0
            colors[glasses-1]!!.forEachColor { c ->
                for (c2 in Color.VANILLA_COLORS)
                    if (c !== c2 && newColors.add(c + c2))
                        count++
            }
            println("Beacon with $glasses stained glasses has $count possible unique colors.")
            colors[glasses] = newColors
        }

        println()
        for (glasses in 0..MAX_GLASSES)
            println("Max depth for $glasses glasses: ${colors[glasses]!!.depth}")
    }
}
