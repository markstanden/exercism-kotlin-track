object CryptoSquare {

    fun ciphertext(plaintext: String): String {
        if (plaintext.isEmpty()) return plaintext

        val normalised = plaintext.normalize()
        val cols = normalised.calcColNumber()
        val rows = normalised.length.ceilingDiv(cols)

        val padded = normalised.padEnd(rows * cols, ' ')
        val inRows = padded.chunked(cols)

        return IntRange(0, cols - 1).joinToString(" ") { inRows.getCol(it) }
    }

    private fun List<String>.getCol(index: Int) =
            this.map { row -> row[index] }.joinToString("")

    private fun Int.ceilingDiv(denominator: Int) =
            (this + denominator - 1) / denominator

    private fun String.normalize() =
            this.filter { it.isLetterOrDigit() }.lowercase();

    private fun String.calcColNumber(): Int {
        return this.length.downTo(1).map { rows ->
                this.length.downTo(1) // Columns
                    .filter { it * rows >= this.length }
                    .filter { it >= rows }
                    .filter { it - rows <= 1 }
            }.flatten().minOf { it }
    }
}