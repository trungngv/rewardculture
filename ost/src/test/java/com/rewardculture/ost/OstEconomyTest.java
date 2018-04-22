package com.rewardculture.ost;

import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class OstEconomyTest {
    OstEconomy ost;

    @org.junit.Before
    public void setUp() throws Exception {
        ost = new OstEconomy();
    }

    @org.junit.Test
    public void createUser() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String response = ost.createUser("newuser");
        JSONObject json = new JSONObject(response);
        assertTrue((Boolean) json.get("success"));
    }

    @org.junit.Test
    public void executeTransaction() {
    }
}