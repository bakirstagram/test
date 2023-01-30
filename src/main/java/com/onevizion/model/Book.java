package com.onevizion.model;

import com.onevizion.util.Constants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class Book {

    private Long id;

    @NotNull
    @Length(max = Constants.STRING_FIELDS_LENGTH)
    private String title;

    @NotNull
    @Length(max = Constants.STRING_FIELDS_LENGTH)
    private String description;

    @NotNull
    @Length(max = Constants.STRING_FIELDS_LENGTH)
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
