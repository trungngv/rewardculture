package com.rewardculture.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Crawl books from GoodReads.
 */
public class GoodReadsCrawler {
    static final int THIRTY_SECONDS = 30000;

    String url = "https://www.goodreads.com/shelf/show/";

    FirebaseBookDatabase.Book getBook(Element element, String category) {
        return new FirebaseBookDatabase.Book(
                element.select(".bookTitle").first().text(),
                element.select(".authorName").first().text(),
                category);
    }

    /**
     * Crawl the list of books for a given category.
     *
     * @param category
     * @return
     * @throws IOException
     */
    List<FirebaseBookDatabase.Book> getBooks(String category) throws IOException {
        String targetUrl = String.format("%s/%s", url, category);
        Document doc = Jsoup.connect(targetUrl).timeout(THIRTY_SECONDS).userAgent("Mozilla").get();
        Elements list = doc.select(".elementList");

        List<FirebaseBookDatabase.Book> result = new ArrayList<>();
        FirebaseBookDatabase.Book book = null;
        for (Element element : list) {
            try {
                book = getBook(element, category);
                if (book != null) {
                    result.add(book);
                }
            } catch (NullPointerException e) {

            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        GoodReadsCrawler crawler = new GoodReadsCrawler();
        List<FirebaseBookDatabase.Book> books = crawler.getBooks("biography");
        System.out.println(books.toString());
    }
}
