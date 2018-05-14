package com.rewardculture.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Book model.
 */


public class Book implements Serializable {

    public String id;
    public String title;
    public String author;
    public String category;
    public int year;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title=" + title + "\n" +
                "category='" + category + "\n" +
                ",reviews=" + reviews +
                '}';
    }
}
