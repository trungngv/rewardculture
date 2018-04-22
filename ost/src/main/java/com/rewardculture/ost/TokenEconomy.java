package com.rewardculture.ost;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Interface for the token economy.
 */
public interface TokenEconomy {

    String createUser(String username) throws IOException, NoSuchAlgorithmException,
            JSONException, InvalidKeyException;

    String executeTransaction(String fromUser, String toUser) throws IOException,
            NoSuchAlgorithmException, JSONException, InvalidKeyException;
}
