class BankAccount   {
    
    private var open: Boolean = true
    var balance:Long = 0
    get() {
        if(this.open)
            return field
        else
            throw IllegalStateException()
    }

    
    @Synchronized
    fun adjustBalance(amount: Long){
        this.balance += amount
    }

    fun close() {
        this.open = false
    }
}
