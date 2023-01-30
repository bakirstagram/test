package com.onevizion.repository.impl;

import com.onevizion.model.Book;
import com.onevizion.mapper.BookMapper;
import com.onevizion.repository.BookRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private static final BookMapper BOOK_MAPPER = new BookMapper();

    private static final String FIND_ALL_QUERY = "SELECT * FROM book b ORDER BY b.title DESC";

    private final JdbcTemplate jdbcTemplate;

    public BookRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, BOOK_MAPPER);
    }

    /**
     * @param book book mustn't be null
     * @return 1 if successfully added
     * @apiNote book fields shouldn't contain null values
     */
    @Override
    @Transactional
    public Book save(Book book) {
        Book lastBook = jdbcTemplate.queryForObject("SELECT * FROM book order by id desc limit 1", BOOK_MAPPER);

        if (lastBook == null) {
            lastBook = new Book();
            lastBook.setId(1L);
        } else {
            lastBook.setId(lastBook.getId() + 1);
        }

        return jdbcTemplate.queryForObject(
                "INSERT INTO book (id, author, title, description) VALUES(?, ?, ?, ?) RETURNING id, author, title, description ",
                BOOK_MAPPER,
                lastBook.getId(),
                book.getAuthor(), book.getTitle(), book.getDescription());
    }

    /**
     * @return List of objects
     * @apiNote sum(id) and STRING_AGG (description, ', ') used to map using BookMapper,
     * else if we should write different mappers to each request
     */
    @Override
    public List<Book> findAllBooksGroupByAuthor() {
        return jdbcTemplate.query("select sum(id) as id, STRING_AGG (description, ', ') as description, author, STRING_AGG (title, ', ') as title  from book group by author",
                BOOK_MAPPER);
    }

    /**
     * @return List of objects
     * @apiNote sum(id) and STRING_AGG (description, ', ') used to map using BookMapper,
     * else if we should write different mappers to each request
     */
    @Override
    public List<Book> findAllByCountAndSort(Character c) {
        return jdbcTemplate.query("select sum(id) as id, STRING_AGG (description, ', ') as description, author, STRING_AGG(title, ', ') as title" +
                        " from book " +
                        " where (LOWER(COALESCE(title, '')) LIKE LOWER('%' || TRIM(CAST(? as text)) || '%'))" +
                        " group by author ", new Object[]{c.toString()},
                BOOK_MAPPER);
    }

}
