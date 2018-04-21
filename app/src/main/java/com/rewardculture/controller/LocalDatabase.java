package com.rewardculture.controller;

import com.rewardculture.model.Book;
import com.rewardculture.model.Review;
import com.rewardculture.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Minimal local database for testing purpose.
 */
public class LocalDatabase {

    private List<Book> books;
    private List<User> users;
    private List<Review> reviews;

    private List<Book> createBooks(int numBooks) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < numBooks; i++) {
            books.add(new Book(String.format("Title %d", i)));
        }

        return books;
    }

    private List<Review> createReviews(int numReviews) {
        Random rand = new Random();
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < numReviews; i++) {
            Review review = new Review(String.format("Review %d", i),
                    rand.nextInt(10), rand.nextInt(5));
            reviews.add(review);
        }

        return reviews;
    }

    private List<User> createusers(int numUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {
            users.add(new User(String.format("User %d", i)));
        }

        return users;
    }

    private LocalDatabase() {
        Random rand = new Random();

        // create 10 books
        int numBooks = 10;
        books = createBooks(numBooks);

        // create 100 reviews
        reviews = createReviews(100);
        // assign 100 reviews randomly to the book
        for (Review review : reviews) {
            books.get(rand.nextInt(numBooks)).addReview(review);
        }

        // create 10 test users
        users = createusers(10);
    }

    private static final LocalDatabase instance = new LocalDatabase();

    public static LocalDatabase getInstance() {
        return instance;
    }

    /**
     * Return books.
     *
     * @return
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Return users.
     *
     * @return
     */
    public List<User> getUsers() {
        return users;
    }

}
