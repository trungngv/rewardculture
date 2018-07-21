package com.rewardculture.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.Book;
import com.rewardculture.model.Review;
import com.rewardculture.model.Transaction;
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
    private static final String REF_REVIEWS = "reviews";
    private static final String REF_USERS = "users";
    private static final String REF_TRANSACTION = "transactions";

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
     * Returns the user with given userId (this is the Firebase UUID).
     *
     * @param userId
     * @return
     */
    public DatabaseReference getUser(String userId) {
        return ref.child(REF_USERS).child(userId);
    }

    public void updateUser(User user) {
        ref.child(REF_USERS).child(user.getUserId()).setValue(user);
    }

    public DatabaseReference getBook(String bookId) {
        return ref.child("books").child(bookId);
    }

    /**
     * Perform database transaction for when user {@userId} likes a review.
     *
     * @param userId
     * @param reviewRef
     */
    public void likeReview(String userId, DatabaseReference reviewRef) {
        reviewRef.child("likes").child(userId).setValue(true);
    }

    /**
     * Perform database transaction for when user {@userId} unlikes a review.
     *
     * @param userId
     * @param reviewRef
     */
    public void unlikeReview(String userId, DatabaseReference reviewRef) {
        reviewRef.child("likes").child(userId).setValue(null);
    }

    /**
     * Logs an economy transaction that was executed
     * @param t
     */
    public void logTransaction(Transaction t) {
        ref.child(REF_TRANSACTION).push().setValue(t);
    }
}
