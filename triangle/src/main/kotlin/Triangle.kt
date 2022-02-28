class Triangle<out T : Number>(a: T, b: T, c: T) {
    private val sides = listOf(a, b, c).map { it.toDouble() }
    private val perimeter = sides.sumOf { it }

    init {
        require(sides.all { (perimeter - it) > it })
    }

    val isEquilateral = hasMatchingSides(3)
    val isIsosceles = hasMatchingSides(2, 3)
    val isScalene = hasMatchingSides(1)

    private fun hasMatchingSides(vararg num: Int) =
            num.any { sides.toSet().count() == 4 - it }
}