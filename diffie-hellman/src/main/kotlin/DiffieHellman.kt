import java.math.BigInteger
import java.security.SecureRandom

/** Cryptographically secure random number generator */
val secureRand = SecureRandom.getInstanceStrong()

object DiffieHellman {

    /** Generates a secure, random private key
     * The key lies in the range 0..prime (exclusive)
     */
    tailrec fun privateKey(prime: BigInteger): BigInteger {
        val key = BigInteger(prime.bitLength(), secureRand)
        return if (key in BigInteger.ONE..(prime - BigInteger.ONE)) key else privateKey(prime)
    }


    /** Generates a secure public key using the formula:
     * A = g**a mod p
     * where g and p are primes
     * also
     * B = g**b mod p
     * where g and p are different primes
     */
    fun publicKey(primeOne: BigInteger, primeTwo: BigInteger, privateKey: BigInteger): BigInteger {
        return primeTwo.modPow(privateKey, primeOne)
    }

    /** Generates a secret using the formula:
     * s = B**a mod p
     * where
     * s = A**b mod p
     * is also true.
     */
    fun secret(prime: BigInteger, publicKey: BigInteger, privateKey: BigInteger): BigInteger {
        return publicKey.modPow(privateKey, prime)
    }
}