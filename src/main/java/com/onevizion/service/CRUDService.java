package com.onevizion.service;

import java.util.List;

public interface CRUDService<T> {

    List<T> findAll();

    T save(T t);

    List<T> findAllGroupByAuthor();

    List<T> findAllByCountAndSort(Character c);

}
