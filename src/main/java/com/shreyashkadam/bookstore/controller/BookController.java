package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ===================== USER =====================

    // View all books (User)
    @GetMapping("/books")
    public String viewBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    // Search books (User)
    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String keyword, Model model) {
        model.addAttribute("books", bookService.searchBooks(keyword));
        model.addAttribute("keyword", keyword);
        return "books";
    }

    // ===================== ADMIN =====================

    // Admin: View all books
    @GetMapping("/admin/books")
    public String viewAdminBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/admin_books"; // ✅ FIXED PATH
    }

    // Admin: Show add book form
    @GetMapping("/admin/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/add_book"; // ✅ FIXED PATH
    }

    // Admin: Save new book
    @PostMapping("/admin/books/save")
    public String saveBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    // Admin: Show edit book form
    @GetMapping("/admin/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "admin/edit_book"; // ✅ FIXED PATH
    }

    // Admin: Update book
    @PostMapping("/admin/books/update")
    public String updateBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    // Admin: Delete book
    @GetMapping("/admin/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }
}
