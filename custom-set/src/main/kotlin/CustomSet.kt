class CustomSet(vararg items: List<Int>) {

    constructor(vararg items: Int) : this(items.asList())

    constructor() : this(emptyList())

    private val internalStore = items.toList().flatten().toMutableList()

    private fun asList(): List<Int> =
            internalStore.toList()

    fun isEmpty(): Boolean =
            internalStore.isEmpty()

    fun isSubset(other: CustomSet): Boolean =
            internalStore.all { other.contains(it) }

    fun isDisjoint(other: CustomSet): Boolean =
            other.asList().none { internalStore.contains(it) }

    fun contains(other: Int): Boolean =
            internalStore.contains(other)

    fun intersection(other: CustomSet): CustomSet {
        val sublist = other.asList().filter { internalStore.contains(it) }
        return CustomSet(sublist)
    }


    fun add(other: Int) {
        if (!internalStore.contains(other)) {
            internalStore.add(other)
        }
    }

    override fun equals(other: Any?): Boolean =
            if (other is CustomSet) {
                other.isSubset(this) && this.isSubset(other)
            } else false


    operator fun plus(other: CustomSet) =
            CustomSet(internalStore, other.asList().filter { !this.contains(it) })


    operator fun minus(other: CustomSet) =
            CustomSet(internalStore.filter { !other.contains(it) })
}