import java.security.SecureRandom
import kotlin.streams.toList


/** Cryptographically secure random number generator */
val secureRand = SecureRandom.getInstanceStrong()


/** Generates a lowercase key of the desired length */
fun generateLCKey(length: Long = 100) =
    secureRand.ints(length, 'a'.code, 'z'.code + 1)
        .toList()
        .joinToString("") { it.toChar().toString() }


/** Rotates a number the desired amount,
 * between two provided inclusive bounds */
tailrec fun Char.rotate(rotations: Int, lowerBoundInc: Char = 'a', upperBoundInc: Char = 'z'): Char {
    val range = upperBoundInc.code - lowerBoundInc.code + 1
    val result = this.plus(rotations)
    return when {
        result < lowerBoundInc -> result.rotate(rotations = range)
        result > upperBoundInc -> result.rotate(rotations = range * -1)
        else -> result
    }
}


/** Calculates the offset (i.e the amount of rotations)
 * from the provided base character */
fun Char.getOffset(baseChar: Char = 'a') =
    this.code.minus(baseChar.code)


/** Returns the value at the provided index
 * if the string were infinitely repeated */
tailrec fun String.getRepeated(index: Int): Char {
    return this.getOrNull(index) ?: this.getRepeated(index.minus(length))
}


data class Cipher(val key: String = generateLCKey(length = 100)) {
    init {
        require(key.isNotEmpty()) { "Key must not be an empty string" }
        require(key.all { it.isLowerCase() }) { "Supplied key should consist of lowercase letters only." }
    }


    fun encode(s: String) =
        s.mapIndexed() { index, it ->
            it.rotate(key.getRepeated(index).getOffset())
        }.joinToString("")


    fun decode(s: String) =
        s.mapIndexed() { index, it ->
            it.rotate(key.getRepeated(index).getOffset() * -1)
        }.joinToString("")
}