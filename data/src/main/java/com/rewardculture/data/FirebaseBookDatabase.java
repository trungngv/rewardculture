package com.rewardculture.data;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirebaseBookDatabase {

    static final String[] CATEGORIES = {
            "Art",
            "Biography",
            "Business",
            "Christian",
            "Classics",
            "Comics",
            "Cookbooks",
            "Ebooks",
            "Fantasy",
            "Fiction",
            "History",
            "Horror",
            "Memoir",
            "Music",
            "Mystery",
            "Nonfiction",
            "Poetry",
            "Psychology",
            "Romance",
            "Science",
            "Sports",
            "Thriller",
            "Travel",
    };

    /**
     * The /categories object in firebase database.
     *
     * @return
     */
    JSONObject getCategoriesObject() {
        JSONObject json = new JSONObject();
        for (String category : CATEGORIES) {
            json.put(category.toLowerCase(), new Category(category.toLowerCase(), category));
        }

        return json;
    }

    /**
     * The /books object in firebase database.
     *
     * @return
     */
    JSONObject booksToJsonObject(List<Book> books) throws IOException {
        JSONObject json = new JSONObject();
        for (Book book : books) {
            // use hashcode as the book id
            json.put(String.valueOf(book.hashCode()), book);
        }

        return json;
    }

    /**
     * Build the /category object from books
     *
     * @return
     */
    JSONObject buildCategoryObject() {
        return null;
    }

    JSONObject buildComplteObject() throws IOException {

        GoodReadsCrawler crawler = new GoodReadsCrawler();
        List<Book> books = new ArrayList<>();
        for (String category : CATEGORIES) {
            List<Book> thisCategory = crawler.getBooks(category.toLowerCase());
            System.out.printf("\ncategory: %s has %d books", category, thisCategory.size());
            books.addAll(thisCategory);
        }
        System.out.printf("\nTotal #books: %d", books.size());
        JSONObject bookJson = booksToJsonObject(books);
        System.out.println(bookJson);

        JSONObject json = new JSONObject();
        json.put("books", bookJson);
        json.put("categories", getCategoriesObject());

        // /category/cat_id/books/{randomid:{bookId,title}}
//        JSONObject category = new JSONObject();
//
//        // for each category
//        JSONObject categoryDetail = new JSONObject();
//        // books is a map
//        categoryDetail.put("books", new HashMap<>());
//        category.put("biography", categoryDetail);
//
//        json.put("category", category);

        //System.out.println(json.toString());
        return json;
    }

    static class Book extends JSONObject {
        Book(String title, String author, String category) {
            put("title", title);
            put("author", author);
            put("category", category);
        }
    }

    class Category extends JSONObject {
        Category(String id, String name) {
            put("id", id);
            put("name", name);
        }
    }

    public static void main(String[] args) throws IOException {
        FirebaseBookDatabase db = new FirebaseBookDatabase();
        System.out.println(db.buildComplteObject());
    }
}
