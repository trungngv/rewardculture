package com.rewardculture.controller;

import com.rewardculture.database.LocalDatabase;

import org.junit.Before;

public class LocalDatabaseTest {
    LocalDatabase database;

    @Before
    public void setUp() throws Exception {
        database = LocalDatabase.getInstance();
    }

}