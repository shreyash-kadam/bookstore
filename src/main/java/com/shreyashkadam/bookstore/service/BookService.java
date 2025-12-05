package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Add or Update Book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by id
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Delete book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Search books by keyword across title, author, category
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }

        keyword = keyword.trim();

        // Search in multiple fields
        List<Book> byTitle = bookRepository.findByTitleContainingIgnoreCase(keyword);
        List<Book> byAuthor = bookRepository.findByAuthorContainingIgnoreCase(keyword);
        List<Book> byCategory = bookRepository.findByCategoryContainingIgnoreCase(keyword);

        // Combine results
        return List.of(byTitle, byAuthor, byCategory)
                .stream()
                .flatMap(List::stream)
                .distinct()
                .toList();
    }
}
