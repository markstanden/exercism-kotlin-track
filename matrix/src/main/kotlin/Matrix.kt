class Matrix(private val matrixAsString: String) {
    private val matrix = matrixFromSpacesAndLineFeed(matrixAsString);

    fun column(colNr: Int): List<Int> = IntRange(0, matrix.size - 1).map { matrix[it][colNr - 1] }

    fun row(rowNr: Int): List<Int> = matrix[rowNr - 1]

    fun matrixFromSpacesAndLineFeed(input: String): List<List<Int>> {
        return input.split("\n")
            .map {
                it.split(" ")
                    .map { str -> str.trim() }
                    .filter { str -> str != "" }
                    .map { str -> str.toInt() }
            }
    }

}