package com.onevizion.mapper;

import com.onevizion.model.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String DESCRIPTION = "description";

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong(ID));
        book.setTitle(rs.getString(TITLE));
        book.setAuthor(rs.getString(AUTHOR));
        book.setDescription(rs.getString(DESCRIPTION));

        return book;
    }

}
