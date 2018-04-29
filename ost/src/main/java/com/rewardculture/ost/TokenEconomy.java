package com.rewardculture.ost;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public abstract String createUser(String username) throws IOException, NoSuchAlgorithmException,
            JSONException, InvalidKeyException;

    public abstract String executeTransaction(String fromUser, String toUser,
                                              TransactionType transactionKind)
            throws IOException, NoSuchAlgorithmException, JSONException, InvalidKeyException;

    /**
     * Executes a review transaction (i.e. which invokes a reward from company to the poster).
     *
     * @param posterUuid uuid of the poster
     * @return
     */
    public String executeReviewTransaction(String posterUuid) throws IOException,
            NoSuchAlgorithmException, JSONException, InvalidKeyException {
        return executeTransaction(getCompanyUuid(), posterUuid, TransactionType.REVIEW);
    }

}
