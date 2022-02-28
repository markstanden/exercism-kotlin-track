import java.util.stream.Stream

object Flattener {
    fun flattenIt(source: Collection<Any?>): List<Any> {
        return source.filterNotNull()
            .map { if (it is Collection<Any?>) flatten(it.flatMap { x->x }) else it }
    }

    fun flatten(source: Collection<Any?>): List<Any> {
        val seq = flattenIt(source)
        return seq.toList().
    }
}