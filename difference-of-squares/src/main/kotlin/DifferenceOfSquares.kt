/**
 * Returns the sum to zero of the provided value,
 * excluding the passed value
 * e.g.
 * beforeToZero(5) returns 4 + 3 + 2 + 1 == 10
 */
fun beforeToZero(exclusive: Int): Int {
    return (1 until exclusive).sum()
}

/**
 * extension method that squares the provided Integer
 */
fun Int.squared(): Int {
    return this * this;
}

class Squares(private val number: Int = 0) {

    /**
     * Return the sum of the squares of the natural numbers
     * from 1 up to and including the provided number.
     * i.e.
     * (a * a) + (b * b) + (c * c)
     */
    fun sumOfSquares(): Int {
        return number.downTo(1)
            .sumOf { it.squared() }
    }

    /**
     * Return the square of the sum of the natural numbers
     * from 1 up to and including the provided number.
     * i.e.
     * (a + b + c) * (a + b + c)
     *
     * @return the square of the sum.
     */
    fun squareOfSum(): Int {
        return difference() + sumOfSquares()
    }

    /**
     * Return the difference between the square of the sum
     * and the sum of the squares of the natural numbers
     * from 1 up to and including the provided number
     *
     * @return the difference of the squares
     */
    fun difference(): Int {
        return number.downTo(1)
            .sumOf { 2 * (it * beforeToZero(it)) }
    }
}