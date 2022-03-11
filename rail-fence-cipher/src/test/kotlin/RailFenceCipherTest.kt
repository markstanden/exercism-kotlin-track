import org.junit.Test
import kotlin.test.assertEquals

class RailFenceCipherTest {

    @Test
    fun encodeWithTwoRails() {
        val railFenceCipher = RailFenceCipher(2)
        assertEquals("XXXXXXXXXOOOOOOOOO", railFenceCipher.getEncryptedData("XOXOXOXOXOXOXOXOXO"))
    }

    @Test
    fun encodeWithThreeRails() {
        val railFenceCipher = RailFenceCipher(3)
        assertEquals("WECRLTEERDSOEEFEAOCAIVDEN", railFenceCipher.getEncryptedData("WEAREDISCOVEREDFLEEATONCE"))
    }

    @Test
    fun encodeWithEndingInTheMiddle() {
        val railFenceCipher = RailFenceCipher(4)
        assertEquals("ESXIEECSR", railFenceCipher.getEncryptedData("EXERCISES"),
                     "Top Row: ES\n2nd Row: XIE\n3rd Row: ECS\nBottom Rail: R\n")
    }

    @Test
    fun decodeWithThreeRails() {
        val railFenceCipher = RailFenceCipher(3)
        assertEquals("THEDEVILISINTHEDETAILS", railFenceCipher.getDecryptedData("TEITELHDVLSNHDTISEIIEA"))
    }

    @Test
    fun decodeWithThreeNumRails() {
        val railFenceCipher = RailFenceCipher(3)
        assertEquals("123456789", railFenceCipher.getDecryptedData("159246837"))
    }

    @Test
    fun encodeWithThreeNumRails() {
        val railFenceCipher = RailFenceCipher(3)
        assertEquals("159246837", railFenceCipher.getEncryptedData("123456789"),
                     "Top Rail: 159 Middle Rail: 2468  Bottom Rail: 37")
    }

    @Test
    fun decodeWithFiveRails() {
        val railFenceCipher = RailFenceCipher(5)
        assertEquals("EXERCISMISAWESOME", railFenceCipher.getDecryptedData("EIEXMSMESAORIWSCE"));
    }

    @Test
    fun decodeWithSixRails() {
        val railFenceCipher = RailFenceCipher(6)
        assertEquals("112358132134558914423337761098715972584418167651094617711286",
                     railFenceCipher.getDecryptedData("133714114238148966225439541018335470986172518171757571896261"))
    }
}