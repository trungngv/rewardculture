package com.rewardculture.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Book model.
 * <p>
 * Created by trung on 1/12/2016.
 */


public class Book implements Serializable {

    private static final String TITLE = "title";
    private static final String REVIEWS = "reviews";

    private String id;
    private String title;
    private String category;
    private List<Review> reviews;
    private String firebaseRefKey;

    public Book() {
    }

    public Book(String title, String category) {
        this.title = title;
        this.category = category;
        reviews = new ArrayList<>();
    }

    public Book(String title, String category, List<Review> reviews) {
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title=" + title + "\n" +
                "category='" + category + "\n" +
                ",reviews=" + reviews +
                '}';
    }

//    public void setFirebaseRefKey(String key) {
//        firebaseRefKey = key;
//    }
//
//    public String getFirebaseRefKey() {
//        return firebaseRefKey;
//    }
//
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
