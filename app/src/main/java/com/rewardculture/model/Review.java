package com.rewardculture.model;

import java.io.Serializable;

/**
 * Class for Review.
 */

public class Review implements Serializable {

    private String id;
    private String text;
    private String authorId;
    private int upvotes;
    private int downvotes;

    public Review() {
    }

    public Review(String text, String authorId, int upvotes, int downvotes) {
        this.text = text;
        this.authorId = authorId;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    @Override
    public String toString() {
        return String.format("{title: %s, upvotes: %d, downvotes: %d}", text, upvotes, downvotes);
    }
}
