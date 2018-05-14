package com.rewardculture.model;

/**
 * BookSnippet model. id is the reference id which can be used to access the full book data
 * in the database.
 */
public class BookSnippet {

    public BookSnippet() {}

    public String bookId;
    public String title;
    public String author;
    public String coverUrl;

    public BookSnippet(String bookId, String title, String author, String coverUrl) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
