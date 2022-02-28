object Raindrops {

    fun convert(n: Int): String {
        return String().pling(n).plang(n).plong(n).ifEmpty { n.toString() }
    }

    private fun String.pling(n: Int) =
            this + if (n % 3 == 0) "Pling" else ""

    private fun String.plang(n: Int) =
            this + if (n % 5 == 0) "Plang" else ""

    private fun String.plong(n: Int) =
            this + if (n % 7 == 0) "Plong" else ""
}