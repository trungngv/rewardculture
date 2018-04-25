package com.rewardculture.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rewardculture.model.User;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Use this class to talk to the kick ass Firebase real time database.
 *
 */

public class FirebaseDatabaseImpl {
    private static FirebaseDatabaseImpl instance = new FirebaseDatabaseImpl();
    private static final String FB_REF_CATEGORY = "categories";

    DatabaseReference database;

    private FirebaseDatabaseImpl() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseDatabaseImpl getInstance() {
        return instance;
    }

    public Query getBookCategories() {
        return database.child(FB_REF_CATEGORY);
    }

    public Query getBooks(String category) {
        Query query = database.child(FB_REF_CATEGORY).child(category);
        return query;
    }

    public User getUser(String userId) {
        return null;
    }

    public List<User> getUsers() {
        return null;
    }

    public static void main(String args[]) throws FileNotFoundException {
        FirebaseDatabaseImpl impl = FirebaseDatabaseImpl.getInstance();
        System.out.println(impl.getBookCategories());
    }
}
