package com.rewardculture.ost;

/**
 * The local economy for testing purpose. Can use mock for this as well.
 */
public class LocalEconomy implements TokenEconomy {

    @Override
    public String createUser(String username)  {
        return "{\"success\": true}";
    }

    @Override
    public String executeTransaction(String fromUser, String toUser, String transactionKind) {
        return "{\"success\": true}";
    }
}
