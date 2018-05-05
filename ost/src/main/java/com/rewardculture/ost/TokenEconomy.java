package com.rewardculture.ost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Abstract class for the token economy.
 */
public abstract class TokenEconomy {

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
        JSONObject meta = obj.getJSONObject("data").getJSONArray("economy_users").getJSONObject(0);
        JSONObject result = new JSONObject();
        Boolean success = obj.getBoolean("success");
        result.put("success", success);
        if (success) {
            result.put("name", meta.getString("name"));
            result.put("uuid", meta.getString("uuid"));
        }

        return result;
    }

    /**
     * Executes a review transaction (i.e. which invokes a reward from company to the poster).
     *
     * @param posterUuid uuid of the poster
     * @return
     */
    public String executeReviewTransaction(String posterUuid) {
        String response = null;
        try {
            response = executeTransaction(getCompanyUuid(), posterUuid, TransactionType.REVIEW);
        } catch (IOException e) {

        } finally {
            return response;
        }
    }

    /**
     * Executes a transaction when a user likes a review, the poster of the review gets some reward.
     *
     * @param posterUuid uuid of the poster
     * @return
     */
    public String executeLikeTransaction(String posterUuid) {
        String response = null;
        try {
            response = executeTransaction(getCompanyUuid(), posterUuid, TransactionType.LIKE);
        } catch (JSONException e) {

        } catch (IOException e) {

        } finally {
            return response;
        }
    }

}
