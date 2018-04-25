package com.rewardculture.model;

/**
 * CategorySnippet model. id is the reference id which can be used to access the full category
 * in the database.
 */
public class CategorySnippet {

    public CategorySnippet() {}

    public String id;
    public String name;

    public CategorySnippet(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
