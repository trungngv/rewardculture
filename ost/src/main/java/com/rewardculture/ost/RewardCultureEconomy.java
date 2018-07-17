package com.rewardculture.ost;

import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * For managing the RewardCulture economy.
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
     * @return
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

    public JsonObject executeReviewTransaction(String posterUuid) throws IOException {
        return ost.executeTransaction(COMPANY_UUID, posterUuid, ActionType.REVIEW.actionId);
    }

    public JsonObject executeLikeTransaction(String posterUuid) throws IOException {
        return ost.executeTransaction(COMPANY_UUID, posterUuid, ActionType.LIKE.actionId);
    }

    /**
     * Parse transaction response.
     *
     * @param response
     * @return a json object if transaction was successful, otherwise returns null.
     */
    public JsonObject parseTransactionResponse(JsonObject response) {
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
        JsonObject response = economy.createUser("bluesky101");
        System.out.println(economy.parseUserResponse(response));
        //JsonObject response = economy.executeReviewTransaction("6a791a28-f156-49dd-a751-263a053fca25");
        //JsonObject transaction = economy.parseTransactionResponse(response);
    }
}
