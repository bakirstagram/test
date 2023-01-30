package com.onevizion.controller;

import com.onevizion.exception.ValidationException;
import com.onevizion.model.Book;
import com.onevizion.service.BookService;
import com.onevizion.service.impl.BookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/onevizion/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookServiceImpl bookServiceImpl) {
        this.bookService = bookServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    /**
     * Endpoint добавления новой книги в таблицу book
     *
     * @param book fields must contain a maximum of 149 characters
     */
    @PostMapping
    public ResponseEntity<Book> save(@RequestBody @Valid Book book, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors());
        }

        return ResponseEntity.ok(bookService.save(book));
    }

    /**
     * Endpoint принимающий в качестве параметра символ и возвращающий список из 10 авторов,
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
    public ResponseEntity<Object> findAllByCountAndSort(
            @RequestParam("letter") @Valid @NotNull(message = "Character must be only one") @NotBlank(message = "Character must be empty") Character c,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors());
        }

        Map<String, Integer> map = bookService.findAllByCountAndSort(c)
                .stream()
                .collect(Collectors
                        .toMap(Book::getAuthor, book -> StringUtils.countOccurrencesOf(book.getTitle().toLowerCase(), c.toString().toLowerCase())));

        return ResponseEntity.ok(
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
                        toMap(
                            Book::getAuthor,
                            book -> Arrays.asList(book.getTitle().split(", "))
                        )
                ));
    }

}
