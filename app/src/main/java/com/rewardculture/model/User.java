package com.rewardculture.model;

public class User {

    private String username;
    private String userId;
    private String ostId;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String userId, String ostId) {
        this.userId = userId;
        this.username = username;
        this.ostId = ostId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOstId() {
        return ostId;
    }

    public void setOstId(String ostId) {
        this.ostId = ostId;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", username, userId, ostId);
    }
}
