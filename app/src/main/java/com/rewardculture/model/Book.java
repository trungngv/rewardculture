package com.rewardculture.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Book model.
 */


public class Book implements Serializable {

    private static final String TITLE = "title";
    private static final String REVIEWS = "reviews";

    public String id;
    public String title;
    public String author;
    public String category;
    public Map<String, Review> reviews;

    public Book() {
    }

    public Book(String title, String category) {
        this.title = title;
        this.category = category;
        reviews = new HashMap();
    }

    public Book(String title, String category, Map reviews) {
        this.title = title;
        this.category = category;
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Review> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, Review> reviews) {
        this.reviews = reviews;
    }

    public void putReview(String key, Review review) {
        reviews.put(key, review);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title=" + title + "\n" +
                "category='" + category + "\n" +
                ",reviews=" + reviews +
                '}';
    }

//    public JSONObject toJsonObject() throws JSONException {
//        JSONObject object = new JSONObject();
//        object.put(TITLE, title);
//        object.put(REVIEWS, reviews);
//        return object;
//    }
//
//    public static Book fromJsonObject(JSONObject o) throws JSONException {
//        JSONArray reviewsJson = (JSONArray) o.get(REVIEWS);
//        List<Review> reviews = new ArrayList<>();
//        for (Object obj : reviewsJson.toList()) {
//            reviews.add(Review.fromObject(obj));
//        }
//
//        return new Book(o.getString(TITLE),
//                reviews);
//    }
}
