package com.rewardculture.ost;

import com.google.gson.JsonObject;
import com.ost.OSTSDK;
import com.ost.services.OSTAPIService;
import com.ost.services.v1_1.Manifest;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;

/**
 * For interfacing with the OST SDK.
 */
public class OstSdk {
    private static final String API_KEY = "123093ca867a7c91586e";
    private static final String API_SECRET = "f4bbd1e0e8bcde04bc9d834d49a53e56b7dc84e007ec4c1bdcdeb79a681b7e6c";
    private static final String API_ENDPOINT = "https://sandboxapi.ost.com/v1.1";

    private static final String ENDPOINT_LISTUSERS = "/users/";
    private static final String ENDPOINT_TRANSACTION_EXECUTE = "/transaction-types/execute";
    private static final String COMPANY_UUID = "6f7d0e3f-fa62-4a51-bd14-b125b6d5e261";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

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

    // @Override
    public String listUsers() throws IOException {
        return null;
    }

    // @Override
    public String getCompanyUuid() {
        return null;
    }

    // @Override
//    public String executeTransaction(String fromUser, String toUser, TransactionType transactionKind) throws IOException {
//        return null;
//    }

    /**
     * Creates user on OST blockchain.
     *
     * @param username
     * @return
     * @throws IOException
     */
    public JsonObject createOstUser(String username) throws IOException {
        com.ost.services.v1.Users userService = services.users;
        HashMap <String,Object> params = new HashMap();
        params.put("name", "Alice");
        JsonObject response = userService.create(params);

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

    public static void main(String[] args) throws IOException, OSTAPIService.MissingParameter {
        OstSdk ost = OstSdk.getInstance();
        JsonObject result = ost.createActionFixedAmount("randomActOfKindness", "user_to_user", 100, 10);
        System.out.println(result);
    }
}
