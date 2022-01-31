package beaconshit

class ColorDisplay(private val listElementThreshold: Int = 200) {
    var count: Int = 0
        private set

    private val list = mutableListOf<Element>()
    private var prev: Int? = null
    private var prevStart: Int? = null

    private var full: Boolean = false

    fun add(color: Int) {
        count++
        if (full) return
        if (color - 1 == prev) prev = color
        else {
            idontwanttothinkaboutanothermethodname()
            prev = color
            prevStart = color
        }
    }

    fun idontwanttothinkaboutanothermethodname() {
        prev?.let { prev ->
            prevStart?.let { prevStart ->
                list.add(Element(prevStart, prev))
                this.prev = null
                this.prevStart = null
                if (list.size >= listElementThreshold) full = true
            }
        }
    }

    fun finish(): List<Element> {
        idontwanttothinkaboutanothermethodname()
        return list.toList()
    }

    data class Element(val min: Int, val max: Int) {
        override fun toString(): String =
            if (min == max) toHexCode(min)
            else "${toHexCode(min)}~${toHexCode(max)}"
    }
}