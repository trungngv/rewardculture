package com.rewardculture.data;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
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
    JSONObject buildCategoriesObject() {
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
    JSONObject buildBooksObject(List<Book> books) {
        JSONObject json = new JSONObject();
        for (Book book : books) {
            json.put(book.getId(), book);
        }

        return json;
    }

    /**
     * Build the /category object from books
     *
     * /category/cat_id/books/{bookid:{bookId,title,author}}
     *
     * @return
     */
    JSONObject buildCategoryObject(List<Book> books) {
        JSONObject json = new JSONObject();
        String categoryKey;
        for (Book book : books) {
            // put this category in map if it's not there yet
            categoryKey = book.getString("category");
            JSONObject categoryValue;
            if (!json.has(categoryKey)) {
                categoryValue = new JSONObject();
                categoryValue.put("books", new JSONObject());
                json.put(categoryKey, categoryValue);
            } else {
                categoryValue = json.getJSONObject(categoryKey);
            }
            categoryValue.getJSONObject("books").put(book.getId(), book.getSnippet());

        }

        return json;
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

        JSONObject json = new JSONObject();
        json.put("categories", buildCategoriesObject());
        json.put("books", buildBooksObject(books));
        json.put("category", buildCategoryObject(books));

        return json;
    }

    static class Book extends JSONObject {
        Book(String title, String author, String category) {
            put("title", title);
            put("author", author);
            put("category", category);
        }

        String getId() {
            return String.valueOf(hashCode());
        }

        /**
         * A quick snippet of the book which is used to link back to the book.
         * @return
         */
        JSONObject getSnippet() {
            JSONObject json = new JSONObject();
            json.put("bookId", getId());
            json.put("title", getString("title"));
            json.put("author", getString("author"));

            return json;
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
        JSONObject json = db.buildComplteObject();
        PrintWriter writer = new PrintWriter("books.json");
        writer.println(json.toString());
        writer.close();
    }
}
