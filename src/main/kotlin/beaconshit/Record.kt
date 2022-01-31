package beaconshit

object Record {
    private val solvedMap = mutableMapOf<Int, ColorDisplay>()
    private var unsolved = ColorDisplay(100)

    fun solved(colorCode: Int, glassesRequired: Int) {
        if (DEBUG) println("${toHexCode(colorCode)} solved with $glassesRequired")
        val list = solvedMap.computeIfAbsent(glassesRequired) { ColorDisplay(20) }
        list.add(colorCode)
    }

    fun unsolved(colorCode: Int) {
        if (DEBUG) println("${toHexCode(colorCode)} unsolved")
        unsolved.add(colorCode)
    }

    fun printResult() {
        println("Result for approximate of $APPROX, boundary set to $BOUNDARY")
        println("${unsolved.count} colors unsolved\n    ${unsolved.finish().joinToString { it.toString() }}\n")
        val sum = solvedMap.values.sumOf { it.count }
        println("$sum colors solved: \n    ${
            solvedMap.entries
                .sortedBy { it.key }
                .joinToString(separator = "\n    ") { (glasses, e) ->
                    "${
                        (e.count.toDouble() / sum) * 100.0
                    }% (${e.count} entries) possible with $glasses stained glasses\n        ${
                        e.finish().joinToString { it.toString() }
                    }"
                }
        }")
    }
}
