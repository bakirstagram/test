package com.onevizion.repository;

import java.util.List;

public interface CRUDRepository<T> {

    List<T> findAll();

    T save(T t);

    List<T> findAllBooksGroupByAuthor();
    List<T> findAllByCountAndSort(Character c);

}
