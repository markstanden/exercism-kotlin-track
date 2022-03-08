class BankAccount {
    private var isOpen = true
    var balance: Long = 0
        get() {
            check(isOpen)
            return field
        }


    @Synchronized
    fun adjustBalance(amount: Long) {
        check(isOpen)
        balance += amount
    }

    @Synchronized
    fun close() {
        isOpen = false
    }
}