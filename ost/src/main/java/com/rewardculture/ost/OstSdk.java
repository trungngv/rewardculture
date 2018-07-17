package com.rewardculture.ost;

import com.google.gson.JsonObject;
import com.ost.OSTSDK;
import com.ost.services.v1_1.Manifest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * For interfacing with the OST SDK.
 */
public class OstSdk {

    private static final String API_KEY = "123093ca867a7c91586e";
    private static final String API_SECRET = "f4bbd1e0e8bcde04bc9d834d49a53e56b7dc84e007ec4c1bdcdeb79a681b7e6c";
    private static final String API_ENDPOINT = "https://sandboxapi.ost.com/v1.1";

    private com.ost.services.v1_1.Manifest services;
    private static OstSdk instance;

    private OstSdk() {
        HashMap<String, Object> sdkConfig = new HashMap();
        sdkConfig.put("apiEndpoint", API_ENDPOINT);
        sdkConfig.put("apiKey", API_KEY);
        sdkConfig.put("apiSecret", API_SECRET);

        OSTSDK ostObj = new OSTSDK(sdkConfig);
        services = (Manifest) ostObj.services;
    }

    public static OstSdk getInstance() {
        if (instance == null) {
            instance = new OstSdk();
        }

        return instance;
    }

    /**
     * Creates user on OST blockchain.
     *
     * @param username
     * @return
     * @throws IOException
     */
    public JsonObject createUser(String username) throws IOException {
        com.ost.services.v1.Users userService = services.users;
        HashMap <String,Object> params = new HashMap();
        params.put("name", username);
        JsonObject response = userService.create(params);

        return response;
    }

    public JsonObject listUsers() throws IOException {
        com.ost.services.v1.Users userService = services.users;
        HashMap <String,Object> params = new HashMap();
        JsonObject response = userService.list(params);

        return response;
    }

    public JsonObject listActions() throws IOException {
        com.ost.services.v1.Actions actions = services.actions;
        HashMap <String,Object> params = new HashMap();
        JsonObject response = actions.list(params);

        return response;
    }

    public JsonObject airdrop(String userId, float amount) throws IOException {
        com.ost.services.v1.AirDrops airdropService = services.airdrops;
        HashMap <String,Object> params = new HashMap();
        params.put("amount", String.valueOf(amount));
        params.put("user_ids", userId);
        JsonObject response = airdropService.execute(params);

        return response;
    }

    /**
     * Creates action with fixed transaction value amount and commission.
     *
     * @param name
     * @param kind
     * @return
     * @throws IOException
     */
    public JsonObject createActionFixedAmount(String name, String kind, float transactionValue,
                                              float commissionPercent) throws IOException {
        com.ost.services.v1.Actions actionService = services.actions;

        HashMap <String,Object> params = new HashMap();
        params.put("name", name);
        params.put("kind", kind);
        params.put("currency", "BT");
        params.put("arbitrary_amount", false);
        params.put("amount", transactionValue);
        params.put("arbitrary_commission", false);
        params.put("commission_percent", commissionPercent);
        JsonObject response = actionService.create(params);

        return response;
    }

    public JsonObject executeTransaction(String fromOstId, String toOstId, String actionId)
            throws IOException {
        com.ost.services.v1.Transactions transactions = services.transactions;

        Map<String, Object> params = new HashMap<>();
        params.put("from_user_id", fromOstId);
        params.put("to_user_id", toOstId);
        params.put("action_id", actionId);
        JsonObject response = transactions.execute(params);

        return response;
    }
}
