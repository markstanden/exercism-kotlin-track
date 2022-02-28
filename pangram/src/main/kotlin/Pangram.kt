object Pangram {

    fun isPangram(input: String): Boolean =
            input.uppercase()
                .filter { it.isLetter() }
                .toSet()
                .count() == 26

}