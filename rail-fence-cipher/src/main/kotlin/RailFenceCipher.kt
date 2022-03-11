import kotlin.math.ceil

class RailFenceCipher(private val rails: Int) {
    init {
        require(rails >= 2)
    }

    fun getEncryptedData(input: String, totalRails: Int = rails): String {
        val numberMiddleRails = totalRails - 2
        val itemsPerCycle = 2 + (numberMiddleRails * 2)

        val inputAsCycles = input.chunked(itemsPerCycle)
        println(inputAsCycles.joinToString("\n") { cycle -> cycle.middleRailsOnly(numberMiddleRails) })
        return inputAsCycles.joinToString("") { cycle -> cycle.getOrEmpty(0) }
            .plus("")
            .plus(IntRange(0, numberMiddleRails - 1).joinToString("") { index ->
                inputAsCycles.joinToString("") { cycle ->
                    cycle.middleRailsOnly(numberMiddleRails).eitherEnd(index, numberMiddleRails)
                }
            })
            .plus("")
            .plus(inputAsCycles.joinToString("") { cycle -> cycle.getOrEmpty(totalRails - 1) })
    }


    fun getDecryptedData(input: String, totalRails: Int = rails): String {
        val numberMiddleRails = totalRails - 2
        val topLength = numberOfLettersTopRow(input.length, numberMiddleRails)
        val bottomLength = numberOfLettersBottomRow(input.length, numberMiddleRails)
        val bottomRowStartIndex = input.length - bottomLength
        val middleRowTotalLength = input.length - topLength - bottomLength

        // Separate the string into its individual rows.
        val topRow = input.substring(0, topLength)
        val bottomRow = input.substring(bottomRowStartIndex)
        val middleRows: List<String> =
            input.substring(topLength, bottomRowStartIndex)
                .chunked(size = lengthOfEachMiddleRow(middleRowTotalLength, numberMiddleRails))

        return IntRange(0, topRow.length).joinToString("") { index ->
            topRow.getOrEmpty(index)
                .plus(middleRows.extractColumn(index * 2))
                .plus(bottomRow.getOrEmpty(index))
                .plus(middleRows.extractColumn(index * 2 + 1, reversed = true))
        }
    }

    /** Attempts to obtain the char at the given index and return it as a String
     * returns an empty string if the index is invalid.
     */
    private fun String.getOrEmpty(index: Int) =
        (this.getOrNull(index) ?: "").toString()

    /** Takes a value from a list of strings all at the same index.  Returns a concatenated string of the result.
     * Can be reversed, invalid indices are ignored.
     */
    private fun List<String>.extractColumn(index: Int, reversed: Boolean = false): String {
        val result = this.joinToString("") { it.getOrEmpty(index) }
        return if (reversed) result.reversed() else result
    }

    /**
     * Removes the top and bottom rail characters from the string if they are present.
     */
    private fun String.middleRailsOnly(numberMiddleRails: Int): String {
        return when {
            this.length == 1 -> ""
            this.length < (2 + numberMiddleRails) -> this.substring(1)
            else -> this.indices.filterNot { it == 0 }
                .filterNot { it == (1 + numberMiddleRails) }
                .joinToString("") { this[it].toString() }
        }
    }

    /** Returns the character at the provided index,
     * and, if the character has a 'mirror' the 'mirrored' character
     * at the same index from the end of the string combined. */
    private fun String.eitherEnd(indexToTakeFromEitherEnd: Int, numberMiddleRails: Int): String {
        return this.getOrEmpty(indexToTakeFromEitherEnd)
            .plus(if (this.length > numberMiddleRails) this.getOrEmpty(this.length - indexToTakeFromEitherEnd - 1)
                  else "")
    }

    /** Calculates the number of letters on the top row of the 'fence' */
    private fun numberOfLettersTopRow(length: Int, numberMiddleRails: Int) =
        ceil(length.toDouble() / (2 + 2 * numberMiddleRails)).toInt()

    /** Calculates the number of letters on the bottom row of the 'fence' */
    private fun numberOfLettersBottomRow(length: Int, numberMiddleRails: Int) =
        numberOfLettersTopRow(length - 2, numberMiddleRails)

    /** Calculates the number of letters in each row of the middle rows of the 'fence'
     * these are handled slightly differently as they provide two characters each cycle */
    private fun lengthOfEachMiddleRow(middleRowTotalLength: Int, numberMiddleRails: Int) =
        middleRowTotalLength / numberMiddleRails
}