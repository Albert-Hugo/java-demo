package com.example.choppercache.service;

import com.example.choppercache.domain.Book;
import com.ido.op.chopper.CacheExpire;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ido
 * @date 2021/5/31 14:52
 */
@Service
public class BookService {

    static Map<String, String> books = new HashMap<>();

    static {
        books.put("1", "1");
        books.put("2", "2");
        books.put("3", "3");
        books.put("4", "4");
        books.put("5", "5");
    }

    public String getbook(String id) {
        return books.get(id);
    }


    @CacheExpire(keyPattern = ".*#{item}.*", elExpression = "id")
    public Book updateBook(String id, String name) {
        books.put(id, name);
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
