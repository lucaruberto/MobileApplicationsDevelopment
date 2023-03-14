class BankAccount {
    var closed:Boolean=false;
    var balance:Long = 0
    get() {
        if(closed)
        {
            throw IllegalStateException();
        }
        else
            return field;

    }


    @Synchronized
    fun adjustBalance(amount: Long){
        if(closed)
        {
            throw  IllegalStateException();
        }
        else{
            this.balance+=amount;
        }
    }

    fun close() {
        this.closed=true;

    }
}
