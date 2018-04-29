package com.rewardculture.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Class for Review.
 */

public class Review implements Serializable {

    private String id;
    private String text;
    private PostedBySnippet postedBy;
    private Map<String, Boolean> likes;

    public Review() {
    }

    public Review(String text, PostedBySnippet postedBy) {
        this.text = text;
        this.postedBy = postedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPostedBy(PostedBySnippet postedBy) {
        this.postedBy = postedBy;
    }

    public PostedBySnippet getPostedBy() {
        return postedBy;
    }

    public String getPosterName() {
        // Earlier data model does not contain this field so need to do a check here
        // But if re-generating data then this should always be not null
        return postedBy != null ? postedBy.getName() : null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public int getNumberOfLikes() {
        return likes != null ? likes.size() : 0;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return String.format("{text: %s, postedBy: %s, likes: %s}", text, postedBy, likes);
    }

    /**
     * Return true if the user liked the review before.
     *
     * @param userId
     * @return
     */
    public Boolean likedByUser(String userId) {
        return likes != null ? likes.containsKey(userId) : false;
    }
}
