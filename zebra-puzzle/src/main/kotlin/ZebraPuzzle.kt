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

typealias Clue<T, R> = List<T>.(List<R>) -> Boolean
typealias Clues<T, R> = (List<T>, List<R>) -> Boolean

class ZebraPuzzle() {

    /*
    * Stores a sequence of all possible combinations of the enums
    * */
    val colourCombinations = HouseColours.values().toList().createAllCombinations().asSequence()
    val nationalityCombinations = Nationality.values().toList().createAllCombinations().asSequence()
    val petCombinations = Pet.values().toList().createAllCombinations().asSequence()
    val smokeCombinations = Smokes.values().toList().createAllCombinations().asSequence()
    val beverageCombinations = Beverage.values().toList().createAllCombinations().asSequence()

    val validStreet: Sequence<Street> = createAllPossibleStreets()

    fun createAllPossibleStreets(): Sequence<Street> {
        return colourCombinations
            .filter { it.greenToRightOfIvory() }
            .map { houseColour ->
                nationalityCombinations
                    .filter { englishmanHasRedHouse(it, houseColour) }
                    .filter { it.norwegianHouse1() }
                    .filter { it.norwegianNextToBlue(houseColour) }
                    .map { nationality ->
                        petCombinations
                            .filter { spaniardHasPetDog(nationality, it) }
                            .map { pet ->
                                smokeCombinations
                                    .filter { oldGoldSmokerKeepsSnails(it, pet) }
                                    .filter { koolSmokerLivesInYellowHouse(it, houseColour) }
                                    .filter { it.chesterfieldsNextToFox(pet) }
                                    .filter { it.koolsNextToHorse(pet) }
                                    .filter { nationality.japaneseSmokesParliaments(it) }
                                    .map { smokes ->
                                        beverageCombinations
                                            .filter { coffeeIsDrunkInGreenHouse(it, houseColour) }
                                            .filter { ukrainianDrinksTea(nationality, it) }
                                            .filter { it.milkInMiddle() }
                                            .filter { it.luckyStrikesDrinksOJ(smokes) }
                                            .map { beverage ->
                                                createStreet(houseColour, nationality, pet, smokes, beverage)
                                            }
                                    }.flatten()
                            }.flatten()
                    }.flatten()
            }.flatten()
    }

    fun createStreet(
            colours: List<HouseColours>, nationalities: List<Nationality>, pets: List<Pet>,
            smokes: List<Smokes>, beverages: List<Beverage>,
    ): Street {
        require(colours.size == nationalities.size and pets.size and smokes.size and beverages.size)

        return Street(colours.indices.map {
            House(it.plus(1), colours[it], nationalities[it], pets[it], smokes[it], beverages[it])
        })
    }

    fun <T> List<T>.createAllCombinations(): List<List<T>> {

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
    fun <T : Enum<T>, R : Enum<R>> sameHouse(
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

    /** Clue 7 */
    val oldGoldSmokerKeepsSnails: Clues<Smokes, Pet> = { smokes, pets ->
        sameHouse(smokes, Smokes.OLD_GOLD, pets, Pet.SNAILS)
    }

    val koolSmokerLivesInYellowHouse: Clues<Smokes, HouseColours> = { smokes, colours ->
        sameHouse(smokes, Smokes.KOOLS, colours, HouseColours.YELLOW)
    }


    fun List<Beverage>.milkInMiddle(): Boolean =
        this[2] == Beverage.MILK

    fun List<Nationality>.norwegianHouse1(): Boolean =
        this[0] == Nationality.NORWEGIAN

    val luckyStrikesDrinksOJ: Clue<Beverage, Smokes> = { that ->
        this.indices.any { (this[it] == Beverage.ORANGE_JUICE) and (that[it] == Smokes.LUCKY_STRIKES) }
    }

    val japaneseSmokesParliaments: Clue<Nationality, Smokes> = { that ->
        this.indices.any { (this[it] == Nationality.JAPANESE) and (that[it] == Smokes.PARLIAMENTS) }
    }

    fun List<HouseColours>.greenToRightOfIvory(): Boolean =
        this.indices.any {
            (this[it] == HouseColours.GREEN) and (this.getOrNull(it + 1) == HouseColours.IVORY)
        }

    val chesterfieldsNextToFox: Clue<Smokes, Pet> = { that ->
        this.indices.any {
            (this[it] == Smokes.CHESTERFIELDS) and ((that.getOrNull(it + 1) == Pet.FOX) or (that.getOrNull(
                    it - 1) == Pet.FOX))
        }
    }
    val koolsNextToHorse: Clue<Smokes, Pet> = { that ->
        this.indices.any {
            (this[it] == Smokes.KOOLS) and ((that.getOrNull(it + 1) == Pet.HORSE) or (that.getOrNull(
                    it - 1) == Pet.HORSE))
        }
    }

    val norwegianNextToBlue: Clue<Nationality, HouseColours> = { that ->
        this.indices.any {
            (this[it] == Nationality.NORWEGIAN) and ((that.getOrNull(it + 1) == HouseColours.BLUE) or (that.getOrNull(
                    it - 1) == HouseColours.BLUE))
        }
    }

    fun drinksWater(): String {
        return validStreet.map { it.houses.filter { house -> house.beverage == Beverage.WATER } }
            .flatten()
            .joinToString { it.nationality.toString }
    }

    fun ownsZebra(): String {
        return validStreet.map { it.houses.filter { house -> house.pet == Pet.ZEBRA } }
            .flatten()
            .joinToString { it.nationality.toString }
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