package com.rewardculture.model.database;

import com.rewardculture.model.Book;
import com.rewardculture.model.User;

import java.util.List;

public interface Database {

    /**
     * Returns the list of all book categories in the database.
     *
     * @return
     */
    List<String> getBookCategories();

    /**
     * Returns the list of all books in the given {category}.
     *
     * @param category
     * @return
     */
    List<Book> getBooks(String category);

    //Book getBook(String id);

    /**
     * Returns user with given {userId}.
     *
     * @param userId
     * @return
     */
    User getUser(String userId);

    List<User> getUsers();
}
