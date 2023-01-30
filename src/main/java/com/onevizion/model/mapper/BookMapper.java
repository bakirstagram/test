package com.onevizion.model.mapper;

import com.onevizion.model.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setAuthor(rs.getString("author"));
        book.setId(rs.getLong("id"));
        book.setDescription(rs.getString("description"));
        book.setTitle(rs.getString("title"));
        return book;
    }
}
