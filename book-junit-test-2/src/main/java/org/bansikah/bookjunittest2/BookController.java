package org.bansikah.bookjunittest2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @RequestMapping
    public List<Book> getAllBooks(Book book) {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "{bookid}")
    public Book getBookById(@PathVariable(value = "bookid") Long bookId) {

        return bookRepository.findAllById(bookId);
    }

    @PostMapping
    public Book addBook(@RequestBody Book bookRecord) {
        return bookRepository.save(bookRecord);
    }

    @PutMapping
    public Book updateBook(@RequestBody Book bookRecord) throws Exception {
        if(bookRecord == null || bookRecord.getBookId() == null){
            throw new Exception("Invalid book record");
        }
        Optional<Book> OptionalBook = bookRepository.findById(bookRecord.getBookId());
        if(!OptionalBook.isPresent()){
            throw new ChangeSetPersister.NotFoundException();
        }
        Book book = OptionalBook.get();
        book.setBookId(book.getBookId());
        book.setName(book.getName());
        book.setSummary(book.getSummary());
        book.setRating(book.getRating());

        return bookRepository.save(bookRecord);
    }

    // TODO : write delete method using TDD method

}

