package com.rewardculture.ost;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OstEconomyTest {
    OstEconomy ost;
    String testUuid;
    String[] params = {"id", "name"};
    String[] values = {"1", "trung"};

    @org.junit.Before
    public void setUp() {
        ost = new OstEconomy();
        testUuid = "6dc7e33b-e3db-4398-bd37-72e3de4656be";
        // testUuid = "f4ea13ca-3d15-4397-bd03-c017e74228ef";
    }

    @org.junit.Test
    public void createUser() throws IOException {
        final String username = "1dRKccvvgWN4Nj7zAANACdzxOuS2".substring(0, 20);
        JSONObject response = new JSONObject(ost.createUser(username));
        assertTrue("response: " + response, (Boolean) response.get("success"));
    }

    @Test
    public void executeTransaction() throws IOException {
        String response = ost.executeTransaction(ost.getCompanyUuid(), testUuid,
                TokenEconomy.TransactionType.REVIEW);
        JSONObject json = new JSONObject(response);
        assertTrue("response: " + response, (Boolean) json.get("success"));
    }

    @Test
    public void executeReviewTransaction() throws IOException {
        String response = ost.executeReviewTransaction(testUuid);
        JSONObject json = new JSONObject(response);
        assertTrue("response: " + response, (Boolean) json.get("success"));
    }

    @Test
    public void executeLikeTransaction() throws IOException {
        String response = ost.executeLikeTransaction(testUuid);
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

    @Test
    public void parseUserResponse() {
        String response = "{\n" +
                "   \"data\":{\n" +
                "      \"result_type\":\"economy_users\",\n" +
                "      \"meta\":{\n" +
                "         \"next_page_payload\":{\n" +
                "\n" +
                "         }\n" +
                "      },\n" +
                "      \"economy_users\":[\n" +
                "         {\n" +
                "            \"total_airdropped_tokens\":0,\n" +
                "            \"token_balance\":0,\n" +
                "            \"name\":\"user1110\",\n" +
                "            \"id\":\"1f944179-3428-4e9e-b548-7767368edc5f\",\n" +
                "            \"uuid\":\"1f944179-3428-4e9e-b548-7767368edc5f\"\n" +
                "         }\n" +
                "      ]\n" +
                "   },\n" +
                "   \"success\":true\n" +
                "}";
        JSONObject result = ost.parseUserResponse(response);
        String name = result.getString("name");
        assertEquals("test failed", "user1110", name);
        assertNotNull(result.getString("uuid"));
    }
}