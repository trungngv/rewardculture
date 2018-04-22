package com.rewardculture.controller;

import com.rewardculture.model.Book;
import com.rewardculture.model.database.LocalDatabase;
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

}