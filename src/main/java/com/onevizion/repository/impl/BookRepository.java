package com.onevizion.repository.impl;

import com.onevizion.model.Book;
import com.onevizion.model.mapper.BookMapper;
import com.onevizion.repository.CRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BookRepository implements CRUDRepository<Book> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book b ORDER BY b.title DESC", new BookMapper());
    }

    /**
     * @param book book mustn't be null
     * @return 1 if successfully added
     * @apiNote book fields shouldn't contain null values
     */
    @Override
    @Transactional
    public Book save(Book book) {
        Book lastBook = jdbcTemplate.queryForObject("SELECT * FROM book order by id desc limit 1",
                BeanPropertyRowMapper.newInstance(Book.class));

        if (lastBook == null) {
            lastBook = new Book();
            lastBook.setId(1L);
        } else
            lastBook.setId(lastBook.getId() + 1);

        return jdbcTemplate.queryForObject(
                "INSERT INTO book (id, author, title, description) VALUES(?, ?, ?, ?) RETURNING id, author, title, description ",
                new BookMapper(),
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
                new BookMapper());
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
                new BookMapper());
    }

}
