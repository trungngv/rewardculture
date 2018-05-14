package com.rewardculture.database;

import android.net.Uri;

import com.rewardculture.model.Book;
import com.rewardculture.model.PostedBySnippet;
import com.rewardculture.model.Review;
import com.rewardculture.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Minimal local database for testing purpose.
 */
public class LocalDatabase {

    static final int N_BOOK_CATEGORIES = 5;
    static final int N_BOOKS = 10;
    static final int N_USERS = 10;
    static final int N_REVIEWS = 10;

    private List<String> bookCategories;
    private List<Book> books;
    private List<User> users;
    private List<Review> reviews;
    private static final LocalDatabase instance = new LocalDatabase();

    public static LocalDatabase getInstance() {
        return instance;
    }

    /**
     * This local database will have:
     * - 5 book categories
     * - 10 books, all in the first category
     * - 10 test users
     * - 10 reviews, all created by the first user for the first book.
     */
    private LocalDatabase() {
        bookCategories = createBookCategories();
        users = createUsers(N_USERS);
        books = createBooks(N_BOOKS, bookCategories);
        reviews = createReviews(N_REVIEWS);
        // assign all reviews to the first book
        for (Review review : reviews) {
            books.get(0).putReview("ignoreid", review);
        }
    }

    private List<String> createBookCategories() {
        List<String> categories = new ArrayList<>(N_BOOK_CATEGORIES);
        categories.add("Science Fiction");
        categories.add("Thriller");
        categories.add("Romance");
        categories.add("Fantasy");
        categories.add("Self-help");

        return categories;
    }

    private List<Book> createBooks(int numBooks, List<String> bookCategories) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < numBooks; i++) {
            books.add(new Book(String.format("Book %d", i), bookCategories.get(0)));
        }

        return books;
    }

    private List<Review> createReviews(int numReviews) {
        Random rand = new Random();
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < numReviews; i++) {
            Review review = new Review(String.format("Review %d", i),
                    new PostedBySnippet(users.get(0).getUserId(), "Anonymous",
                            Uri.parse("http://www.notavailable.com")));
            reviews.add(review);
        }

        return reviews;
    }

    private List<User> createUsers(int numUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {
            users.add(new User(String.valueOf(i), String.valueOf("ost" + i)));
        }

        return users;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<String> getBookCategories() {
        return bookCategories;
    }

    public List<Book> getBooks(String category) {
        return books;
    }

    public User getUser(String userId) {
        return null;
    }
}
