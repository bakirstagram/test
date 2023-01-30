package com.onevizion.controller;

import com.onevizion.model.Book;
import com.onevizion.service.CRUDService;
import com.onevizion.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/onevizion/books")
public class BookController {

    private final CRUDService<Book> bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    @ResponseBody
    public Object findAll() {
        return bookService.findAll();
    }

    /**
     * endpoint добавления новой книги в таблицу book
     *
     * @param book fields must contain a maximum of 149 characters
     */
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Book book) {
        return ResponseEntity.ok().body(bookService.save(book));
    }

    /**
     * endpoint принимающий в качестве параметра символ и возвращающий список из 10 авторов,
     * в названии книг которых этот символ встречается наибольшее количество раз вместе с
     * количеством вхождений этого символа во все названия книг автора.
     * Регистр символа не имеет значения. Авторы, в названии книг которых символ отсутствует, не
     * должны присутствовать в возвращаемом значении.
     * Пример:
     * Входной параметр: “а”
     * Результат: [{L. Tolstoy, 7},{F. Dostoevsky, 4},{N. Gogol, 1}]
     *
     * @param c search param
     * @return 10 authors in which the character "c" occurs more times
     * @apiNote Character must be only one and couldn't contain any spaces
     * link /onevizion/books/counting-sort HTTP Method: GET
     */
    @GetMapping("/counting-sort")
    public ResponseEntity<Object> findAllByCountAndSort(@RequestParam("letter") Character c) {
        if (c == null)
            return ResponseEntity.badRequest().body("Character must be only one");

        Map<String, Integer> map = bookService.findAllByCountAndSort(c).
                stream().
                collect(Collectors.
                        toMap(Book::getAuthor, book -> StringUtils.countOccurrencesOf(book.getTitle().toLowerCase(), c.toString().toLowerCase())));

        return ResponseEntity.ok().body(
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(10)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));

    }

    /**
     * endpoint возвращающий список всех книг, сгруппированных по автору
     * книги(book.author)
     *
     * @return Book group by author
     * @apiNote link /onevizion/books/group-by-author HTTP Method: GET
     **/
    @GetMapping("/group-by-author")
    public ResponseEntity<Object> findAllGroupByAuthor() {
        return ResponseEntity.ok().body(bookService.
                findAllGroupByAuthor().
                stream().
                collect(Collectors.
                        toMap(Book::getAuthor, book ->
                                new ArrayList<>(Arrays.asList(book.getTitle().split(", ")))
                        )));
    }

}
