package com.rewardculture.model;

import com.google.gson.JsonObject;

public class Transaction {

    static final String TRANSACTION_ID = "id";
    static final String FROM_UUID = "from_user_id";
    static final String TO_UUID = "to_user_id";
    static final String ACTION_ID = "action_id";
    static final String TIMESTAMP = "timestamp";
    static final String AMOUNT = "amount";

    String transactionId;
    String fromUuid;
    String toUuid;
    String actionId;
    long transactionTime;
    float amount;

    public Transaction() {
    }

    public static Transaction fromJsonObject(JsonObject obj) {
        Transaction t = new Transaction();
        t.setTransactionId(obj.get(TRANSACTION_ID).getAsString());
        t.setFromUuid(obj.get(FROM_UUID).getAsString());
        t.setToUuid(obj.get(TO_UUID).getAsString());
        t.setActionId(obj.get(ACTION_ID).getAsString());
        t.setTransactionTime(obj.get(TIMESTAMP).getAsLong());
        // amount is null when obj is a response from a fixed amount transaction action
        if (!obj.get(AMOUNT).isJsonNull()) {
            t.setAmount(obj.get(AMOUNT).getAsFloat());
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
