package com.sahil.CRUDApplication.controller;

import com.sahil.CRUDApplication.model.Book;
import com.sahil.CRUDApplication.repo.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BooksController {

    @Autowired
    private BooksRepository booksRepository;

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = new ArrayList<>();
            booksRepository.findAll().forEach(books::add);

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookByID(@PathVariable Long id) {
        Optional<Book> bookData = booksRepository.findById(id);

        if (bookData.isPresent()) {
            return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = booksRepository.save(book);

        return new ResponseEntity<>(savedBook, HttpStatus.OK);
    }


    @PutMapping("updateBookById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBook) {
        Optional<Book> oldBook = booksRepository.findById(id);

        if (oldBook.isPresent()) {
            Book updatedBook = oldBook.get();
            updatedBook.setTitle(newBook.getTitle());
            updatedBook.setAuthor(newBook.getAuthor());

            Book bookobj = booksRepository.save(updatedBook);
            return new ResponseEntity<>(bookobj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("deleteBookById/{id}")
    public ResponseEntity<Book> deleteBookById(@PathVariable Long id) {

        Optional<Book> book = booksRepository.findById(id);

        if (book.isPresent()) {
            booksRepository.delete(book.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}


