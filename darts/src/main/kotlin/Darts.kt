import kotlin.math.hypot

const val INNER_RADIUS = 1.0
const val MIDDLE_RADIUS = 5.0
const val OUTER_RADIUS = 10.0
const val INNER_POINTS = 10
const val MIDDLE_POINTS = 5
const val OUTER_POINTS = 1

object Darts {

    fun score(x: Number, y: Number) =
        when (hypot(x.toDouble(), y.toDouble())) {
            in 0.0..INNER_RADIUS -> INNER_POINTS
            in INNER_RADIUS..MIDDLE_RADIUS -> MIDDLE_POINTS
            in MIDDLE_RADIUS..OUTER_RADIUS -> OUTER_POINTS
            else -> 0
        }
}