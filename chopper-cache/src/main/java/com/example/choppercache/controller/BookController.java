package com.example.choppercache.controller;

import com.example.choppercache.service.BookService;
import com.ido.op.chopper.CacheExpire;
import com.ido.op.chopper.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ido
 * @date 2021/5/31 14:39
 */
@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService bookService;

    @Cacheable(keyPrefix = "books:")
    @GetMapping("")
    public String getBooks(String id) {
        return bookService.getbook(id);
    }

    @PostMapping
    public void updateBook(String id, String name) {
        bookService.updateBook(id, name);
    }


}
