fun scrabbleTileValues(): Map<Char, Int> {
    return HashMap<Char, Int>().plus("AEIOULNRST".associateWith { 1 })
        .plus("DG".associateWith { 2 })
        .plus("BCMP".associateWith { 3 })
        .plus("FHVWY".associateWith { 4 })
        .plus("K".associateWith { 5 })
        .plus("JX".associateWith { 8 })
        .plus("QZ".associateWith { 10 })
}

val doubleWordScore = IntRange(0, 14).associateWith { 2 }
val tripleWordScore = IntRange(0, 14).associateWith { 3 }

object ScrabbleScore {
    private val letterValues = scrabbleTileValues()

    private fun scoreLetter(c: Char) =
        letterValues[c] ?: 0

    /**
     * Scores a scrabble word using the provided multiplier if provided.
     */
    fun scoreWord(word: String, multiplier: Map<Int, Int>? = null): Int {
        return word.withIndex().sumOf { (index, char) ->
            scoreLetter(char.uppercaseChar()) * (multiplier?.get(index) ?: 1)
        }
    }
}