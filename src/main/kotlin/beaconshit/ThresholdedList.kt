package beaconshit

class ThresholdedList<T>(private val threshold: Int) : Iterable<T> {
    val list: List<T>
        get() = _list
    var size: Int = 0
        private set

    private val _list: MutableList<T> = mutableListOf()

    fun add(t: T) {
        size++
        if (_list.size < threshold) _list.add(t)
    }

    override fun iterator(): Iterator<T> = list.iterator()
}