package com.rewardculture.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.Book;
import com.rewardculture.model.Review;
import com.rewardculture.model.User;
import com.rewardculture.view.BookActivity;

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
    private static final String REF_REVIEWS = "reviews";

    // this is user trung1110
    private String testUuid = "6dc7e33b-e3db-4398-bd37-72e3de4656be";

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

    public DatabaseReference getReviews(DatabaseReference bookRef) {
        return bookRef.child(REF_REVIEWS);
    }

    public void addReview(DatabaseReference bookRef, Review review,
                          DatabaseReference.CompletionListener listener) {
        bookRef.child(REF_REVIEWS).push().setValue(review, listener);
    }

    /**
     * Returns the ost id of the current logged in user (for transaction)
     * @return
     */
    public String getTestUuid() {
        return testUuid;
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
