package com.rewardculture.model;

/**
 * Snippet for information about author of a review. This is so that the review reference contains
 * all required info to display the review. It prevents the need to make a call to each user profile
 * and retrieve information which can cause severe latency.
 */
public class PostedBySnippet {

    public PostedBySnippet() {}

    public String id;
    public String name;

    public PostedBySnippet(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
