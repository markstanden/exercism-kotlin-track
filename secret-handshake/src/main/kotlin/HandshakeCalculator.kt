object HandshakeCalculator {
    fun calculateHandshake(number: Int): List<Signal> {
        return emptyList<Signal>().shouldWink(number)
            .shouldDoubleBlink(number)
            .shouldCloseEyes(number)
            .shouldJump(number)
            .shouldReverse(number)
    }

    private fun Int.should(sigBit: Int): Boolean =
            this.shr(sigBit - 1).takeLowestOneBit() == 1

    private fun List<Signal>.shouldWink(number: Int): List<Signal> =
            if (number.should(1)) this + Signal.WINK else this

    private fun List<Signal>.shouldDoubleBlink(number: Int): List<Signal> =
            if (number.should(2)) this + Signal.DOUBLE_BLINK else this

    private fun List<Signal>.shouldCloseEyes(number: Int): List<Signal> =
            if (number.should(3)) this + Signal.CLOSE_YOUR_EYES else this

    private fun List<Signal>.shouldJump(number: Int): List<Signal> =
            if (number.should(4)) this + Signal.JUMP else this

    private fun List<Signal>.shouldReverse(number: Int): List<Signal> =
            if (number.should(5)) this.reversed() else this

}