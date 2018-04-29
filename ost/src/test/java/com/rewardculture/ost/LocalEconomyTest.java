package com.rewardculture.ost;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class LocalEconomyTest {

    TokenEconomy economy;

    @Before
    public void setUp() throws Exception {
        economy = new LocalEconomy();
    }

    @Test
    public void createUser() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String response = economy.createUser("newuser");
        JSONObject json = new JSONObject(response);
        assertTrue((Boolean) json.get("success"));
    }

    @Test
    public void executeTransaction() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String response = economy.executeTransaction("fromuser", "touser",
                TokenEconomy.TransactionType.REVIEW);
        JSONObject json = new JSONObject(response);
        assertTrue((Boolean) json.get("success"));
    }
}