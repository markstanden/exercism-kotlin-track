const val SPACE = " "
const val HYPHEN = "-"
const val EMPTY_STRING = ""

val thousandExponentials = mapOf(1 to "thousand", 2 to "million", 3 to "billion")
val subTwenty =
    mapOf(1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six", 7 to "seven", 8 to "eight",
          9 to "nine", 10 to "ten", 11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen", 15 to "fifteen",
          16 to "sixteen", 17 to "seventeen", 18 to "eighteen", 19 to "nineteen")
val tenMultiples =
    mapOf(2 to "twenty", 3 to "thirty", 4 to "forty", 5 to "fifty", 6 to "sixty", 7 to "seventy", 8 to "eighty",
          9 to "ninety")

/**
 * Recursively converts a number into a reversed list of digits
 * */
private tailrec fun Long.asList(initialList: List<Int> = emptyList()): List<Int> {
    val remainingDigits = this / 10L
    val leastSigDigit = this % 10
    val updatedList = initialList + listOf(leastSigDigit.toInt())

    return if (remainingDigits == 0L) updatedList else remainingDigits.asList(updatedList)
}

/** Safely looks up a number in the list,
 * if the index is out of bounds returns 0 */
private fun List<Int>.getOrZero(index: Int) =
    this.getOrNull(index) ?: 0

/** Tries to look up the provided translation in the provided map
 * if not present returns an empty string */
private fun Map<Int, String>.getOrEmpty(value: Int) =
    this.getOrElse(value) { EMPTY_STRING }

/** safely attempts to look up a value in the provided map, when the value to be looked up may not exist*/
private fun safeConvert(map: Map<Int, String>, list: List<Int>, index: Int) =
    map.getOrEmpty(list.getOrZero(index))

/** Adds the provided text only if the preceding string is not empty */
private fun String.addIfNotEmpty(string: String) =
    if (this.isEmpty()) this else this.plus(string)


class NumberSpeller {

    fun say(input: Long): String {
        require(input >= 0)
        require(input < 1E12)

        // convert the list into a reversed list
        // chunked into groups of thousands of thousands
        val numberInThousands = input.asList().chunked(3)

        return processThousandGroups(numberInThousands).ifEmpty { "zero" }
    }

    /** Processes each thousand group, adding the correct multiplier name */
    private fun processThousandGroups(inputAsList: List<List<Int>>): String {
        return inputAsList.mapIndexed { thousandGroups, value ->
            processHundredGroups(value).addIfNotEmpty(SPACE.plus(thousandExponentials.getOrEmpty(thousandGroups)))
        }
            .reversed()
            .joinToString(SPACE)
            .trim()
    }

    /** Process the multiple groups of one hundred, repeated in each thousand group */
    private fun processHundredGroups(values: List<Int>): String {
        return safeConvert(subTwenty, values, 2)
            .addIfNotEmpty(" hundred ")
            .plus(handleSubHundred(values))
    }


    /** Handles the sub one hundred numbers */
    private fun handleSubHundred(values: List<Int>): String {
        val number = (10 * values.getOrZero(1)) + values.getOrZero(0)

        return if (number >= 20) {
            safeConvert(tenMultiples, values, 1).plus(if (values.getOrZero(0) != 0) HYPHEN else EMPTY_STRING)
                .plus(safeConvert(subTwenty, values, 0))
        }
        else {
            subTwenty.getOrEmpty(number)
        }
    }
}