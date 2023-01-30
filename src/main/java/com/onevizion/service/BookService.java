package com.onevizion.service;

import com.onevizion.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book save(Book t);

    List<Book> findAllGroupByAuthor();

    List<Book> findAllByCountAndSort(Character c);

}
