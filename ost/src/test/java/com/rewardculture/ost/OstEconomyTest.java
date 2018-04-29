package com.rewardculture.ost;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class OstEconomyTest {
    OstEconomy ost;
    String testUuid;
    String[] params = {"id", "name"};
    String[] values = {"1", "trung"};

    @org.junit.Before
    public void setUp() throws Exception {
        ost = new OstEconomy();
        testUuid = "6dc7e33b-e3db-4398-bd37-72e3de4656be";
        testUuid = "f4ea13ca-3d15-4397-bd03-c017e74228ef";
    }

    @org.junit.Test
    public void createUser() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String response = ost.createUser("newuser");
        JSONObject json = new JSONObject(response);
        assertTrue("response: " + response, (Boolean) json.get("success"));
    }

    @org.junit.Test
    public void executeTransaction() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String response = ost.executeTransaction(ost.getCompanyUuid(), testUuid,
                TokenEconomy.TransactionType.REVIEW);
        JSONObject json = new JSONObject(response);
        assertTrue("response: " + response, (Boolean) json.get("success"));
    }

    @Test
    public void buildQuery() {
        String query = ost.buildQuery("endpoint", params, values);
        assertEquals("endpoint?id=1&name=trung", query);
    }

    @Test
    public void buildPostData() {
        JSONObject data = ost.buildPostData(params, values);
        assertEquals(values[0], data.get(params[0]));
        assertEquals(values[1], data.get(params[1]));
    }

}