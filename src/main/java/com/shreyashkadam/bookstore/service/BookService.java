
package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Add or Update book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Fetch all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Find book by ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Delete book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Search books
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }

        keyword = keyword.trim();

        List<Book> titleMatches = bookRepository.findByTitleContainingIgnoreCase(keyword);
        List<Book> authorMatches = bookRepository.findByAuthorContainingIgnoreCase(keyword);
        List<Book> categoryMatches = bookRepository.findByCategoryContainingIgnoreCase(keyword);

        return List.of(titleMatches, authorMatches, categoryMatches)
                .stream()
                .flatMap(List::stream)
                .distinct()
                .toList();
    }
}
