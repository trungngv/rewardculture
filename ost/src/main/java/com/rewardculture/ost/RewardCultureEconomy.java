package com.rewardculture.ost;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ost.services.OSTAPIService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * For managing the RewardCulture economy (currently using OST blockchain services)
 */
public class RewardCultureEconomy {
    private static final String SUCCESS = "success";
    private static final String DATA = "data";
    private static final String USER = "user";
    private static final String TRANSACTION = "transaction";

    private static final String COMPANY_UUID = "6f7d0e3f-fa62-4a51-bd14-b125b6d5e261";

    private static final OstSdk ost = OstSdk.getInstance();

    /**
     * The actions in this economy.
     */
    public enum ActionType {
        REVIEW("29914"),
        LIKE("29913");

        final String actionId;

        ActionType(final String actionId) {
            this.actionId = actionId;
        }

        @Override
        public String toString() {
            return actionId;
        }
    }

    /**
     * Creates a user in the economy.
     *
     * @param username
     * @return a JsonObject containing "available_balance", "airdropped_balance", and "token_balance"
     * @throws IOException
     */
    public JsonObject createUser(String username) throws IOException {
        // this is a hack to use the  user id generated in firebase
        // as username in ost but limit to the first 20 characters due to OST length limit
        if (username.length() >= 20) {
            username = username.substring(0, 20);
        }

        return ost.createUser(username);
    }

    public JsonObject getUserBalances(String userId) throws IOException {
        try {
            JsonObject response = ost.getUserBalance(userId);
            return response.getAsJsonObject("data").getAsJsonObject("balance");
        } catch (OSTAPIService.MissingParameter e) {

        }

        return null;
    }

    public float getAvailableBalance(String userId) throws IOException {
        JsonObject balances = getUserBalances(userId);
        return balances.get("available_balance").getAsFloat();
    }


    /**
     * Returns all transactions involving the user with user id. Transactions are sorted by most
     * frequent first.
     *
     * @param userId
     * @return
     * @throws IOException
     */
    public JsonArray getTransactions(String userId) throws IOException {
        try {
            JsonObject response = ost.getTransactions(userId);
            return response.getAsJsonObject("data").getAsJsonArray("transactions");
        } catch (OSTAPIService.MissingParameter e) {

        }

        return null;
    }

    public JsonObject executeReviewTransaction(String posterUuid) throws IOException {
        JsonObject response = ost.executeTransaction(COMPANY_UUID, posterUuid,
                ActionType.REVIEW.actionId);

        return parseTransactionResponse(response);
    }

    public JsonObject executeLikeTransaction(String posterUuid) throws IOException {
        JsonObject response = ost.executeTransaction(COMPANY_UUID, posterUuid,
                ActionType.LIKE.actionId);

        return parseTransactionResponse(response);
    }

    /**
     * Parse transaction response.
     *
     * @param response
     * @return a json object if transaction was successful, otherwise returns null.
     */
    JsonObject parseTransactionResponse(JsonObject response) {
        Boolean success = response.get(SUCCESS).getAsBoolean();
        return success ? response.getAsJsonObject(DATA).getAsJsonObject(TRANSACTION) : null;
    }

    /**
     * Parses the response from createuser API call.
     *
     * @param response
     * @return JsonObject containing name and uuid.
     */
    public JsonObject parseUserResponse(JsonObject response) {
        JsonObject user = response.getAsJsonObject(DATA).getAsJsonObject(USER);
        Boolean success = response.get(SUCCESS).getAsBoolean();
        JsonObject result = new JsonObject();
        result.addProperty(SUCCESS, success);
        if (success) {
            result.addProperty("name", user.get("name").getAsString());
            result.addProperty("uuid", user.get("id").getAsString());
        }

        return result;
    }

    public static void main(String args[]) throws IOException {
        RewardCultureEconomy economy = new RewardCultureEconomy();
        String aliceId = "6a791a28-f156-49dd-a751-263a053fca25";
        //JsonObject response = economy.createUser("bluesky101");
        //System.out.println(economy.parseUserResponse(response));
        //JsonObject response = economy.executeReviewTransaction(aliceId);
        //System.out.println(response);
        //System.out.println(economy.getUserBalances(aliceId));
        //System.out.println(economy.getTransactions(aliceId));
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy, HH:mm:ss");
        System.out.println(formatter.format(new Date(1532135588734L)));
    }
}
