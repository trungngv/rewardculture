package com.rewardculture.model;

/**
 * BookSnippet model. id is the reference id which can be used to access the full book data
 * in the database.
 */
public class BookSnippet {

    public BookSnippet() {}

    public String bookId;
    public String title;

    public BookSnippet(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
