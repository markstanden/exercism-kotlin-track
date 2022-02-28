class Triangle<out T : Number>(val a: T, val b: T, val c: T) {
    private val sides = listOf(a, b, c).map { it.toDouble() }
    private val perimeter = sides.sumOf { it }

    init {
        if (sides.any { (perimeter - it) <= it }) throw IllegalArgumentException()
    }

    val isEquilateral: Boolean = sides.all{ it == sides.first() }
    val isIsosceles: Boolean = sides.filter{ it == sides }
    val isScalene: Boolean = TODO("Implement this getter to complete the task")
}