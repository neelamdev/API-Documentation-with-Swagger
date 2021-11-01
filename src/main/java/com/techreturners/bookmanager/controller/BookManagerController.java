package com.techreturners.bookmanager.controller;

import com.techreturners.bookmanager.model.*;
import com.techreturners.bookmanager.service.BookManagerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/book")
public class BookManagerController {

    BookManagerService bookManagerService;

    public BookManagerController(BookManagerService bookManagerService) {
        this.bookManagerService = bookManagerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookManagerService.getAllBooks();
        if(books.isEmpty())
             {
                ErrorMessage errorResponse = new ErrorMessage();
                errorResponse.setMessage("Record not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        else
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping({"/{bookId}"})
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        if(Objects.isNull(bookId)) throw new NullPointerException();
        return new ResponseEntity<>(bookManagerService.getBookById(bookId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book newBook = bookManagerService.insertBook(book);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("book", "/api/v1/book/" + newBook.getId().toString());
        return new ResponseEntity<>(newBook, httpHeaders, HttpStatus.CREATED);
    }

    //User Story 4 - Update Book By Id Solution
    @PutMapping({"/{bookId}"})
    public ResponseEntity<Book> updateBookById(@PathVariable("bookId") Long bookId, @RequestBody Book book) {
        if(Objects.isNull(bookId)) throw new NullPointerException();
        bookManagerService.updateBookById(bookId, book);
        return new ResponseEntity<>(bookManagerService.getBookById(bookId), HttpStatus.OK);
    }

    @DeleteMapping({"/{bookId}"})
    public ResponseEntity<List<Book>> deleteBookById(@PathVariable Long bookId ){
        if(Objects.isNull(bookId)) throw new NullPointerException();
        List<Book> books =bookManagerService.deleteBookById(bookId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

}
