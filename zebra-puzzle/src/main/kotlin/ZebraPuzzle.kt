enum class Nationality(val toString: String) {
    ENGLISHMAN("Englishman"), NORWEGIAN("Norwegian"), JAPANESE("Japanese"), SPANIARD("Spaniard"), UKRAINIAN("Ukrainian")
}

enum class Pet { DOG, FOX, HORSE, SNAILS, ZEBRA }
enum class Smokes { CHESTERFIELDS, KOOLS, LUCKY_STRIKES, OLD_GOLD, PARLIAMENTS }
enum class HouseColours { BLUE, GREEN, IVORY, RED, YELLOW }
enum class Beverage { COFFEE, ORANGE_JUICE, MILK, TEA, WATER }


data class Street(val houses: List<House>)
data class House(
        val houseNumber: Int,
        val colour: HouseColours,
        val nationality: Nationality,
        val pet: Pet,
        val smokes: Smokes,
        val beverage: Beverage,
)

typealias Clues<T, R> = (List<T>, List<R>) -> Boolean

class ZebraPuzzle() {

    /*
    * Stores a sequence of all possible combinations of the enums
    * */
    private val colourCombinations = HouseColours.values().toList().getAllPossibleCombinations().asSequence()
    private val nationalityCombinations = Nationality.values().toList().getAllPossibleCombinations().asSequence()
    private val petCombinations = Pet.values().toList().getAllPossibleCombinations().asSequence()
    private val smokeCombinations = Smokes.values().toList().getAllPossibleCombinations().asSequence()
    private val beverageCombinations = Beverage.values().toList().getAllPossibleCombinations().asSequence()

    /**
     * Stores the result of the filtering by clues
     */
    private val validStreet: Sequence<Street> = findValidStreets()

    /** Identify the nationality of the house owner who drinks water */
    fun drinksWater(): String {
        return validStreet.map { it.houses.filter { house -> house.beverage == Beverage.WATER } }
            .flatten()
            .joinToString { it.nationality.toString }
    }

    /** Identify the nationality of the house owner that keeps a zebra as a pet */
    fun ownsZebra(): String {
        return validStreet.map { it.houses.filter { house -> house.pet == Pet.ZEBRA } }
            .flatten()
            .joinToString { it.nationality.toString }
    }

    /** Combines and filters the combinations to determine only the valid combinations based on the given clues */
    private fun findValidStreets(): Sequence<Street> {
        return colourCombinations.filter { it.greenToRightOfIvory() }.map { houseColour ->
                nationalityCombinations.filter { englishmanHasRedHouse(it, houseColour) }
                    .filter { norwegianLivesInFirstHouse(it) }
                    .filter { norwegianLivesNextToBlueHouse(it, houseColour) }
                    .map { nationality ->
                        petCombinations.filter { spaniardHasPetDog(nationality, it) }.map { pet ->
                                smokeCombinations.filter { oldGoldSmokerKeepsSnails(it, pet) }
                                    .filter { koolSmokerLivesInYellowHouse(it, houseColour) }
                                    .filter { chesterfieldSmokerLivesNextToFox(it, pet) }
                                    .filter { koolsSmokerLivesNextToFox(it, pet) }
                                    .filter { japaneseSmokesParliaments(nationality, it) }
                                    .map { smokes ->
                                        beverageCombinations.filter { coffeeIsDrunkInGreenHouse(it, houseColour) }
                                            .filter { ukrainianDrinksTea(nationality, it) }
                                            .filter { milkDrinkerInMiddleHouse(it) }
                                            .filter { luckyStrikeSmokerDrinksOJ(smokes, it) }
                                            .map { beverage ->
                                                createStreetFromLists(houseColour, nationality, pet, smokes, beverage)
                                            }
                                    }
                                    .flatten()
                            }.flatten()
                    }
                    .flatten()
            }.flatten()
    }

    /**
     * Creates a Street of houses from a combination of lists.
     * */
    private fun createStreetFromLists(
            colours: List<HouseColours>,
            nationalities: List<Nationality>,
            pets: List<Pet>,
            smokes: List<Smokes>,
            beverages: List<Beverage>,
    ): Street {
        require(colours.size == nationalities.size and pets.size and smokes.size and beverages.size)

        return Street(colours.indices.map {
            House(it.plus(1), colours[it], nationalities[it], pets[it], smokes[it], beverages[it])
        })
    }

    /**
     * Recursively generates all combinations of the supplied list
     * */
    private fun <T> List<T>.getAllPossibleCombinations(): List<List<T>> {
        fun go(remaining: List<T>, ordered: List<T>): List<List<T>> {
            if (remaining.isEmpty()) {
                return listOf(ordered)
            }

            return remaining.map { go(remaining.without(it), ordered.plus(it)) }.flatten()
        }

        return go(this, emptyList())
    }

    /**
     * Checks whether the first and second are located at the same index within the list
     * i.e they share a house
     * */
    private fun <T : Enum<T>, R : Enum<R>> sameHouse(
            first: List<Enum<T>>, requiredFirst: T, second: List<Enum<R>>, requiredSecond: R,
    ) =
        first.indices.any { (first[it] == requiredFirst) and (second[it] == requiredSecond) }

    /** Clue 2 */
    val englishmanHasRedHouse: Clues<Nationality, HouseColours> = { nationalities, colours ->
        sameHouse(nationalities, Nationality.ENGLISHMAN, colours, HouseColours.RED)
    }

    /** Clue 3 */
    val spaniardHasPetDog: Clues<Nationality, Pet> = { nationalities, pets ->
        sameHouse(nationalities, Nationality.SPANIARD, pets, Pet.DOG)
    }

    /** Clue 4 */
    val coffeeIsDrunkInGreenHouse: Clues<Beverage, HouseColours> = { drinks, colours ->
        sameHouse(drinks, Beverage.COFFEE, colours, HouseColours.GREEN)
    }

    /** Clue 5 */
    val ukrainianDrinksTea: Clues<Nationality, Beverage> = { nationalities, drinks ->
        sameHouse(nationalities, Nationality.UKRAINIAN, drinks, Beverage.TEA)
    }

    /** Clue 6 */
    fun List<HouseColours>.greenToRightOfIvory(): Boolean =
        this.indices.any {
            (this[it] == HouseColours.GREEN) and (this.getOrNull(it + 1) == HouseColours.IVORY)
        }

    /** Clue 7 */
    val oldGoldSmokerKeepsSnails: Clues<Smokes, Pet> = { smokes, pets ->
        sameHouse(smokes, Smokes.OLD_GOLD, pets, Pet.SNAILS)
    }

    /** Clue 8 */
    val koolSmokerLivesInYellowHouse: Clues<Smokes, HouseColours> = { smokes, colours ->
        sameHouse(smokes, Smokes.KOOLS, colours, HouseColours.YELLOW)
    }

    /** Clue 9 */
    fun milkDrinkerInMiddleHouse(beverage: List<Beverage>): Boolean =
        beverage[2] == Beverage.MILK

    /** Clue 10 */
    fun norwegianLivesInFirstHouse(nationality: List<Nationality>): Boolean =
        nationality[0] == Nationality.NORWEGIAN

    /** Clue 11 */
    val chesterfieldSmokerLivesNextToFox: Clues<Smokes, Pet> = { smokes, pets ->
        smokes.indices.any {
            (smokes[it] == Smokes.CHESTERFIELDS) and ((pets.getOrNull(it + 1) == Pet.FOX) or (pets.getOrNull(
                    it - 1) == Pet.FOX))
        }
    }

    /** Clue 12 */
    val koolsSmokerLivesNextToFox: Clues<Smokes, Pet> = { smokes, pets ->
        smokes.indices.any {
            (smokes[it] == Smokes.KOOLS) and ((pets.getOrNull(it + 1) == Pet.HORSE) or (pets.getOrNull(
                    it - 1) == Pet.HORSE))
        }
    }

    /** Clue 13 */
    val luckyStrikeSmokerDrinksOJ: Clues<Smokes, Beverage> = { smokes, beverage ->
        smokes.indices.any { (smokes[it] == Smokes.LUCKY_STRIKES) and (beverage[it] == Beverage.ORANGE_JUICE) }
    }

    /** Clue 14 */
    val japaneseSmokesParliaments: Clues<Nationality, Smokes> = { nationality, smokes ->
        nationality.indices.any { (nationality[it] == Nationality.JAPANESE) and (smokes[it] == Smokes.PARLIAMENTS) }
    }

    /** Clue 15 */
    val norwegianLivesNextToBlueHouse: Clues<Nationality, HouseColours> = { nationality, houseColours ->
        nationality.indices.any {
            (nationality[it] == Nationality.NORWEGIAN) and ((houseColours.getOrNull(
                    it + 1) == HouseColours.BLUE) or (houseColours.getOrNull(it - 1) == HouseColours.BLUE))
        }
    }

    /**
     * Filters a list and returns a new list without the first element matching the supplied predicate.
     * @param      item      The Item to filter
     * @returns    List with the first structurally equal occurrence removed.
     */
    fun <T> List<T>.without(item: T): List<T> =
        this.withoutMatching { it == item }

    /**
     * Filters a list and returns a new list without the first element matching the supplied predicate.
     * @param      predicate   The predicate to match the first item to exclude
     * @returns    List with only the first matching occurrence removed.
     */
    fun <T> List<T>.withoutMatching(predicate: (T) -> Boolean): List<T> {
        val first = this.firstOrNull() { predicate(it) }
        return this.filter { first !== it }
    }

}