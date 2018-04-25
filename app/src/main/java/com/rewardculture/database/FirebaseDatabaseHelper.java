package com.rewardculture.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rewardculture.model.Book;
import com.rewardculture.model.User;

import java.util.List;

/**
 * Use this class to talk to the kick ass Firebase real time database.
 *
 */

public class FirebaseDatabaseHelper {
    private static final String REF_CATEGORIES = "categories";
    private static final String REF_CATEGORY = "category";
    private static final String REF_BOOKS = "books";
    private static final String REF_BOOK = "books";

    private final DatabaseReference ref;
    private static FirebaseDatabaseHelper instance = new FirebaseDatabaseHelper();

    private FirebaseDatabaseHelper() {
         ref = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseDatabaseHelper getInstance() {
        return instance;
    }

    public Query getBookCategories() {
        return ref.child(REF_CATEGORIES);
    }

    public Query getBooks(String category) {
        return ref.child(REF_CATEGORY).child(category).child(REF_BOOKS);
    }

    public User getUser(String userId) {
        return null;
    }

    public List<User> getUsers() {
        return null;
    }

    public DatabaseReference getBook(String bookId) {
        return ref.child("books").child(bookId);
    }
}
