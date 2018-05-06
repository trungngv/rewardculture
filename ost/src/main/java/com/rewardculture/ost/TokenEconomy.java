package com.rewardculture.ost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Abstract class for the token economy.
 */
public abstract class TokenEconomy {

    public static final String SUCCESS = "success";
    private static final String DATA = "data";

    public enum TransactionType {
        REVIEW("Review"),
        LIKE("Upvote");

        private final String text;

        TransactionType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    ;

    public abstract String getCompanyUuid();

    public abstract String createUser(String username) throws IOException;

    public abstract String executeTransaction(String fromUser, String toUser,
                                              TransactionType transactionKind) throws IOException;

    /**
     * Parses the response from createuser API call.
     *
     * @param response
     * @return JSONObject containing name and uuid.
     */
    public JSONObject parseUserResponse(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        JSONObject meta = obj.getJSONObject(DATA).getJSONArray("economy_users").getJSONObject(0);
        JSONObject result = new JSONObject();
        Boolean success = obj.getBoolean(SUCCESS);
        result.put(SUCCESS, success);
        if (success) {
            result.put("name", meta.getString("name"));
            result.put("uuid", meta.getString("uuid"));
        }

        return result;
    }

    /**
     * Parse transaction response.
     *
     * @param response
     * @return a json object if transaction was successful, otherwise returns null.
     * @throws JSONException
     */
    public JSONObject parseTransactionResponse(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        Boolean success = obj.getBoolean(SUCCESS);
        return success ? obj.getJSONObject(DATA) : null;
    }

    /**
     * Executes a transaction when an user writes a review, he/she gets some reward from
     * the company.
     *
     * @param posterUuid uuid of the poster
     * @return
     */
    public String executeReviewTransaction(String posterUuid) throws IOException {
        return executeTransaction(getCompanyUuid(), posterUuid, TransactionType.REVIEW);
    }

    /**
     * Executes a transaction when a user likes a review, the poster of the review gets some reward.
     *
     * @param posterUuid uuid of the poster
     * @return
     */
    public String executeLikeTransaction(String posterUuid) throws IOException {
        return executeTransaction(getCompanyUuid(), posterUuid, TransactionType.LIKE);
    }

}
