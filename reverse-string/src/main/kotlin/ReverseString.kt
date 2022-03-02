/**
 * Reverses a string
 */
fun reverse(input: String) =
        input.indices.joinToString("") { input[input.lastIndex - it].toString() }

/**
 * Obviously this passes too,
 * but is a relatively pointless exercise...
 */
fun reversed(input: String) = input.reversed()