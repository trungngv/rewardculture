package com.rewardculture.ost;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Helper class for talking to OSTalpha API.
 */
public class OstEconomy extends TokenEconomy {

    private static final String API_KEY = "123093ca867a7c91586e";
    private static final String API_SECRET = "f4bbd1e0e8bcde04bc9d834d49a53e56b7dc84e007ec4c1bdcdeb79a681b7e6c";
    private static final String COMPANY_UUID = "6f7d0e3f-fa62-4a51-bd14-b125b6d5e261";

    private static final String CONTRACT_ADDRESS = "0xF2dB400Fe3A9410fA09eb76E13CD73E17cA9C9B4";
    // The address that initially hold minted tokens
    private static final String RESERVE_ADDRESS = "0x5ea918feB55A1aaD83632e8f77D22211D1FC0CC4";
    // The address that reserve the tokens to be airdrops (not all tokens are to be airdropped)
    private static final String BUDGET_HOLDER_ADDRESS = "0x38a1f09d30fcA61833c8502D9Be7503e4Df88b18";
    private static final String AIRDROP_CONTRACT_ADDRESS = "0x38A5B95875F912300CEa99Ad72F36F5a377DA15f";
    private static final String OST_E20_CONTRACT_ADDRESS = "0xca954C91BE676cBC4D5Ab5F624b37402E5f0d957";

    private static final String OST_URL = "https://playgroundapi.ost.com";
    private static final String ENDPOINT_CREATEUSER = "/users/create";
    private static final String ENDPOINT_TRANSACTION_EXECUTE = "/transaction-types/execute";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT
            = MediaType.parse("text/plain");


    String buildQuery(String endPoint, String[] params, String[] values) {
        StringBuilder builder = new StringBuilder();
        builder.append(endPoint).append("?");
        for (int i = 0; i < params.length; i++) {
            builder.append(String.format("%s=%s&", params[i], values[i]));
        }
        // remove the last & character
        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }


    JSONObject buildPostData(String[] params, String[] values) {
        JSONObject data = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            data.put(params[i], values[i]);
        }

        return data;
    }

    @Override
    public String getCompanyUuid() {
        return COMPANY_UUID;
    }

    @Override
    public String createUser(String username) throws IOException {
        // TODO this is only a hack due to ost api's restriction on the username
        if (username.length() >= 20) {
            username = username.substring(0, 20);
        }
        String[] params = { "api_key", "name", "request_timestamp" };
        String[] values = {
                API_KEY,
                username.replaceAll("%20", "+").replaceAll(" ", "+"),
                String.valueOf(System.currentTimeMillis() / 1000),
        };
        // /users/create?api_key=&name=&request_timestamp=
        String query = buildQuery(ENDPOINT_CREATEUSER, params, values);
        String signature = Crypto.signToHex(API_SECRET, query);

        JSONObject data = buildPostData(params, values);
        data.put("signature", signature);

        Request request = new Request.Builder()
                .url(OST_URL + ENDPOINT_CREATEUSER)
                .post(RequestBody.create(JSON, data.toString()))
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    @Override
    public String executeTransaction(String fromOstId, String toOstId, TransactionType transactionKind)
            throws IOException {
        String[] params = {
                "api_key", "from_uuid", "request_timestamp", "to_uuid", "transaction_kind"
        };
        String[] values = {
                API_KEY,
                fromOstId,
                String.valueOf(System.currentTimeMillis() / 1000),
                toOstId,
                transactionKind.toString()
        };

        // /transaction-types/execute?api_key=API_KEY&from_uuid=FROM_UUID&request_timestamp=EPOCH_TIME_SEC
        // &to_uuid=TO_UUID&transaction_kind=NAME
        String query = buildQuery(ENDPOINT_TRANSACTION_EXECUTE, params, values);
        String signature = Crypto.signToHex(API_SECRET, query);

        JSONObject data = buildPostData(params, values);
        data.put("signature", signature);

        Request request = new Request.Builder()
                .url(OST_URL + ENDPOINT_TRANSACTION_EXECUTE)
                .post(RequestBody.create(JSON, data.toString()))
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
