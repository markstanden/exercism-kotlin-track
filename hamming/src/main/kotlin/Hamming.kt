object Hamming {

    private val mismatchedLength = IllegalArgumentException("left and right strands must be of equal length")

    fun compute(leftStrand: String, rightStrand: String): Int {
        if (leftStrand.length != rightStrand.length) throw mismatchedLength

        return leftStrand.zip(rightStrand).count { it.first != it.second }
    }
}