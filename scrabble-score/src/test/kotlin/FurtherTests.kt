import org.junit.Test
import kotlin.test.assertEquals

class FurtherTests {
    @Test
    fun testLongWithAlteredLetters() {
        assertEquals(114, ScrabbleScore.scoreWord("abcdefghijklmnopqrstuvwxyz", mapOf(0 to 2, 2 to 3, 25 to 3)), "a doubled, c and z tripled")
    }

    @Test
    fun testDouble() {
        assertEquals(20, ScrabbleScore.scoreWord("kotlin", doubleWordScore), "double word score - kotlin - 5+1+1+1+1+1")
    }

    @Test
    fun testTriple() {
        assertEquals(30, ScrabbleScore.scoreWord("kotlin", tripleWordScore))
    }

    @Test
    fun testShortMultiplier() {
        assertEquals(91, ScrabbleScore.scoreWord("abcdefghijklmnopqrstuvwxyz", mapOf(0 to 2, 1 to 2)), "A & B doubled, with a short multiplier")
    }
}