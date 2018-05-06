package com.rewardculture.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

    static final String TRANSACTION_UUID = "transaction_uuid";
    static final String FROM_UUID = "from_uuid";
    static final String TO_UUID = "to_uuid";
    static final String TRANSACTION_KIND = "transaction_kind";

    String transactionUuid;
    String fromUuid;
    String toUuid;
    String transactionKind;
    long transactionTime;

    public Transaction() {}

    public static Transaction fromJsonObject(JSONObject obj) throws JSONException {
        Transaction t = new Transaction();
        if (obj.has(TRANSACTION_UUID)) {
            t.setTransactionUuid(obj.getString(TRANSACTION_UUID));
        }
        if (obj.has(FROM_UUID)) {
            t.setFromUuid(obj.getString(FROM_UUID));
        }
        if (obj.has(TO_UUID)) {
            t.setToUuid(obj.getString(TO_UUID));
        }
        if (obj.has(TRANSACTION_KIND)) {
            t.setTransactionKind(obj.getString(TRANSACTION_KIND));
        }

        return t;
    }

    public String getTransactionUuid() {
        return transactionUuid;
    }

    public void setTransactionUuid(String transactionUuid) {
        this.transactionUuid = transactionUuid;
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

    public String getTransactionKind() {
        return transactionKind;
    }

    public void setTransactionKind(String transactionKind) {
        this.transactionKind = transactionKind;
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(long transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return String.format("transaction{transactionUuid: %s, fromUuid: %s, toUuid: %s, transactionKind: %s, transactionTime: %d}");
    }
}
