package com.rewardculture.ost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The local economy for testing purpose. Can use mock for this as well.
 */
public class LocalEconomy extends TokenEconomy {

    @Override
    public String getCompanyUuid() {
        return "test-company-uuid";
    }

    @Override
    public String createUser(String username)  {
        return "{\"success\": true}";
    }

    @Override
    public String executeTransaction(String fromUser, String toUser, TransactionType transactionKind)
            throws JSONException {
        return "{\"success\": true}";
    }
}
