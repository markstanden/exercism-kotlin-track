enum class Relationship {

    EQUAL, SUBLIST, SUPERLIST, UNEQUAL

}

fun <T> List<T>.relationshipTo(that: List<T>): Relationship {
    if (that == this) return Relationship.EQUAL
    if (this.isSublistOf(that)) return Relationship.SUBLIST
    if (that.isSublistOf(this)) return Relationship.SUPERLIST
    return Relationship.UNEQUAL
}

/**
 * Checks whether the current list is
 * contained sequentially within the provided list
 */
fun <T> List<T>.isSublistOf(bigList: List<T>): Boolean {
    return bigList.indices.any { bigIndex ->
        this.indices.all {
            this[it] == bigList.getOrNull(it + bigIndex)
        }
    }
}