package com.onevizion.service.impl;

import com.onevizion.model.Book;
import com.onevizion.repository.CRUDRepository;
import com.onevizion.repository.impl.BookRepository;
import com.onevizion.service.CRUDService;
import com.onevizion.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements CRUDService<Book> {

    private final CRUDRepository<Book> repository;

    @Autowired
    public BookService(BookRepository repository) {

        this.repository = repository;
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Book save(Book book) {
        if (book != null)
            if (book.getTitle() != null && book.getDescription() != null && book.getAuthor() != null)
                if (!(book.getTitle().length() > Constants.STRING_FIELDS_LENGTH ||
                        book.getAuthor().length() > Constants.STRING_FIELDS_LENGTH ||
                        book.getDescription().length() > Constants.STRING_FIELDS_LENGTH))
                    return repository.save(book);
        throw new RuntimeException("Book is not valid! ");
    }

    @Override
    public List<Book> findAllGroupByAuthor() {
        return repository.findAllBooksGroupByAuthor();
    }

    @Override
    public List<Book> findAllByCountAndSort(Character c) {
        if (!c.equals(' ') || !c.toString().isEmpty())
            return repository.findAllByCountAndSort(c);
        throw new RuntimeException("Character must be only one and shouldn't contain any spaces! ");
    }


}
