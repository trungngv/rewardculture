package com.rewardculture.controller;

import com.rewardculture.model.Book;
import com.rewardculture.model.User;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LocalDatabaseTest {
    LocalDatabase database;

    @Before
    public void setUp() throws Exception {
        database = LocalDatabase.getInstance();
    }

    @Test
    public void getBooks() {
        List<Book> books = database.getBooks();
        System.out.println(books);
        assertEquals(10, books.size());
    }

    @Test
    public void getUsers() {
        List<User> users = database.getUsers();
        System.out.println(users);
        assertEquals(10, users.size());
    }
}