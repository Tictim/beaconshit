package beaconshit

object Record {
    private val solvedMap = mutableMapOf<Int, ThresholdedList<Int>>()
    private var unsolved = ThresholdedList<Int>(200)

    fun solved(colorCode: Int, glassesRequired: Int) {
        // println("${toHexCode(colorCode)} solved with $glassesRequired")
        val list = solvedMap.computeIfAbsent(glassesRequired) { ThresholdedList(20) }
        list.add(colorCode)
    }

    fun unsolved(colorCode: Int) {
        // println("${toHexCode(colorCode)} unsolved")
        unsolved.add(colorCode)
    }

    fun printResult() {
        println("Result for approximate of $APPROX, enable subdivision: $SUBDIVISION")
        println("${unsolved.size} colors unsolved\n    ${unsolved.joinToString { toHexCode(it) }}\n")
        val sum = solvedMap.values.sumOf { it.size }
        println("$sum colors solved: \n    ${
            solvedMap.entries
                .sortedBy { it.key }
                .joinToString(separator = "\n    ") { (glasses, e) ->
                    "${
                        (e.size.toDouble() / sum) * 100.0
                    }% (${e.size} entries) possible with $glasses stained glasses\n        ${
                        e.joinToString { toHexCode(it) }
                    }"
                }
        }")
    }
}
