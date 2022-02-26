import java.time.LocalDate
import java.time.LocalDateTime

class Gigasecond(moment: LocalDateTime) {
    val date: LocalDateTime

    init {
        date = moment.plusSeconds(1e9.toLong())
    }

    constructor(moment: LocalDate) : this(moment.atStartOfDay())


}