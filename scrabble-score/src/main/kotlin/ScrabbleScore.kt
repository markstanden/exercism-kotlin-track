/**
 * Extension method for List to convert a list of entries to a Map with a shared value.
 */
fun String.toMap(hasValue: Int): Map<Char, Int> {
    return this.associateBy({ it }, { hasValue })
}

fun scrabbleTileValues(): Map<Char, Int> {
    return HashMap<Char, Int>().plus("AEIOULNRST".toMap(1))
        .plus("DG".toMap(2))
        .plus("BCMP".toMap(3))
        .plus("FHVWY".toMap(4))
        .plus("K".toMap(5))
        .plus("JX".toMap(8))
        .plus("QZ".toMap(10))
}

object ScrabbleScore {
    private val letterValues = scrabbleTileValues()

    private fun scoreLetter(c: Char): Int {
        return letterValues[c] ?: 0;
    }

    fun scoreWord(word: String, multiplier: Array<Int> = Array(word.length) { 1 }): Int {
        return IntRange(0, word.length - 1).sumOf { scoreLetter(word.uppercase()[it]) * (multiplier.getOrNull(it) ?: 1) }
    }
}