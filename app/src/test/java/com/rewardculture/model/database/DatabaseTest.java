package com.rewardculture.model.database;

import com.rewardculture.model.Book;
import com.rewardculture.model.User;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database local;
    private Database firebase;

    @Before
    public void setUp() {
        local = LocalDatabase.getInstance();
    }

    @Test
    public void getBookCategories_local() {
        List<String> categories = local.getBookCategories();
        assertEquals(LocalDatabase.N_BOOK_CATEGORIES, categories.size());
    }

    @Test
    public void getBooks_local() {
        List<String> categories = local.getBookCategories();
        List<Book> books = local.getBooks(categories.get(0));
        System.out.println(books);
        assertEquals(LocalDatabase.N_BOOKS, books.size());
    }

    @Test
    public void getUsers_local() {
        List<User> users = local.getUsers();
        System.out.println(users);
        assertEquals(LocalDatabase.N_USERS, users.size());
    }

    @Test
    public void getUser_local() {
    }

}