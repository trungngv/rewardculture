package com.rewardculture.model;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

    static final String TRANSACTION_ID = "id";
    static final String FROM_UUID = "from_user_id";
    static final String TO_UUID = "to_user_id";
    static final String ACTION_ID = "action_id";

    String transactionId;
    String fromUuid;
    String toUuid;
    String actionId;
    long transactionTime;

    public Transaction() {}

    public static Transaction fromJsonObject(JsonObject obj) throws JSONException {
        Transaction t = new Transaction();
        if (obj.has(TRANSACTION_ID)) {
            t.setTransactionId(obj.get(TRANSACTION_ID).getAsString());
        }
        if (obj.has(FROM_UUID)) {
            t.setFromUuid(obj.get(FROM_UUID).getAsString());
        }
        if (obj.has(TO_UUID)) {
            t.setToUuid(obj.get(TO_UUID).getAsString());
        }
        if (obj.has(ACTION_ID)) {
            t.setActionId(obj.get(ACTION_ID).getAsString());
        }

        return t;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String toUuid) {
        this.toUuid = toUuid;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(long transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return String.format("transaction{transactionId: %s, fromUuid: %s, toUuid: %s, actionId: %s, transactionTime: %d}",
                transactionId, fromUuid, toUuid, actionId, transactionTime);
    }
}
