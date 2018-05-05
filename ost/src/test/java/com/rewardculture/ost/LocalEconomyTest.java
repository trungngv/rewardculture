package com.rewardculture.ost;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class LocalEconomyTest {

    TokenEconomy economy;

    @Before
    public void setUp() {
        economy = new LocalEconomy();
    }

    @Test
    public void createUser() throws IOException {
        JSONObject response = new JSONObject(economy.createUser("newuser"));
        assertTrue((Boolean) response.get("success"));
    }

    @Test
    public void executeTransaction() throws IOException {
        String response = economy.executeTransaction("fromuser", "touser",
                TokenEconomy.TransactionType.REVIEW);
        JSONObject json = new JSONObject(response);
        assertTrue((Boolean) json.get("success"));
    }
}