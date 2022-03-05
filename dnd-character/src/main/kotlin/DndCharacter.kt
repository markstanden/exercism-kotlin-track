import kotlin.random.Random

class DndCharacter {

    val strength: Int = ability()
    val dexterity: Int = ability()
    val constitution: Int = ability()
    val intelligence: Int = ability()
    val wisdom: Int = ability()
    val charisma: Int = ability()
    val hitpoints: Int = 10 + modifier(constitution)

    companion object {
        private val rando = Random
        private val die = { rando.nextInt(6) }

        /**
         * Rolls the provided number of dice using the provided dice roll lambda
         * and discards the lowest roll.
         */
        private fun rollManyAndDiscardLowest(numberOfDice: Int, singleDiceRoll: () -> Int): Int {
            require(numberOfDice >= 2)

            return List(numberOfDice) { singleDiceRoll() }.sortedDescending().take(numberOfDice - 1).sum()
        }

        fun ability() =
            rollManyAndDiscardLowest(4, die)

        fun modifier(score: Int) =
            Math.floorDiv(score - 10, 2)

    }
}