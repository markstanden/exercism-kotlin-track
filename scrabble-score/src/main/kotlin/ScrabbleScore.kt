/**
 * Extension method for List to convert a list of entries to a Map with a shared value.
 */
fun <K, V> List<K>.toMap(hasValue: V): Map<K, V> {
    return this.associateBy({ it }, { hasValue })
}

fun scrabbleTileValues(): Map<Char, Int> {
    return HashMap<Char, Int>().plus(listOf('A', 'E', 'I', 'O', 'U', 'L', 'N', 'R', 'S', 'T').toMap(1))
        .plus(listOf('D', 'G').toMap(2))
        .plus(listOf('B', 'C', 'M', 'P').toMap(3))
        .plus(listOf('F', 'H', 'V', 'W', 'Y').toMap(4))
        .plus(listOf('K').toMap(5))
        .plus(listOf('J', 'X').toMap(8))
        .plus(listOf('Q', 'Z').toMap(10))
}

object ScrabbleScore {
    private val letterValues = scrabbleTileValues()

    private fun scoreLetter(c: Char): Int {
        return letterValues[c] ?: 0;
    }

    fun scoreWord(word: String, multiplier: Array<Int> = Array(word.length) { 1 }): Int {
        return IntRange(0, word.length - 1).sumOf { scoreLetter(word.uppercase()[it]) * (multiplier[it]) }
    }
}