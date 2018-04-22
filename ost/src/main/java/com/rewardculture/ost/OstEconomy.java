package com.rewardculture.ost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Helper class for talking to OSTalpha API.
 */
public class OstEconomy implements TokenEconomy {

    private static final String API_KEY = "123093ca867a7c91586e";
    private static final String API_SECRET = "f4bbd1e0e8bcde04bc9d834d49a53e56b7dc84e007ec4c1bdcdeb79a681b7e6c";

    public static final String CONTRACT_ADDRESS = "0xF2dB400Fe3A9410fA09eb76E13CD73E17cA9C9B4";
    // The address that initially hold minted tokens
    public static final String RESERVE_ADDRESS = "0x5ea918feB55A1aaD83632e8f77D22211D1FC0CC4";
    // The address that reserve the tokens to be airdrops (not all tokens are to be airdropped)
    public static final String BUDGET_HOLDER_ADDRESS = "0x38a1f09d30fcA61833c8502D9Be7503e4Df88b18";
    public static final String AIRDROP_CONTRACT_ADDRESS = "0x38A5B95875F912300CEa99Ad72F36F5a377DA15f";
    public static final String OST_E20_CONTRACT_ADDRESS = "0xca954C91BE676cBC4D5Ab5F624b37402E5f0d957";
    public static final String COMPANY_UUID = "6f7d0e3f-fa62-4a51-bd14-b125b6d5e261";

    public static final String OST_URL = "https://playgroundapi.ost.com";
    public static final String ENDPOINT_CREATEUSER = "/users/create";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT
            = MediaType.parse("text/plain");


    public String createUser(String username) throws IOException, NoSuchAlgorithmException,
            JSONException, InvalidKeyException {
        String uname = username.replaceAll("%20", "+");
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // users/create?api_key=&name=&request_timestamp=
        String query = String.format("%s?api_key=%s&name=%s&request_timestamp=%s",
                ENDPOINT_CREATEUSER, API_KEY, uname, timestamp);
        String signature = Crypto.signToHex(API_SECRET, query);

        JSONObject data = new JSONObject();
        data.put("api_key", API_KEY);
        data.put("name", uname);
        data.put("request_timestamp", timestamp);
        data.put("signature", signature);

        Request request = new Request.Builder()
                .url(OST_URL + ENDPOINT_CREATEUSER)
                .post(RequestBody.create(JSON, data.toString()))
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String executeTransaction(String fromUser, String toUser) {
        return "transaction executed";
    }

    public static void main(String args[]) throws JSONException, NoSuchAlgorithmException,
            IOException, InvalidKeyException {
        OstEconomy helper = new OstEconomy();
        System.out.println(helper.createUser("trung"));
    }
}
