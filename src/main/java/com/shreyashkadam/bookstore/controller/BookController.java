package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    // ADMIN: View all books
    @GetMapping("/admin/books")
    public String viewAdminBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/admin_books";  // template: templates/admin/admin_books.html
    }

    // ADMIN: Add book form
    @GetMapping("/admin/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/add_book"; // template: templates/admin/add_book.html
    }

    // ADMIN: Save new book
    @PostMapping("/admin/books/save")
    public String saveBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/admin_books";
    }

    // ADMIN: Edit book form
    @GetMapping("/admin/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "admin/edit_book"; // template: templates/admin/edit_book.html
    }

    // ADMIN: Update book
    @PostMapping("/admin/books/update")
    public String updateBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/admin_books";
    }

    // ADMIN: Delete book
    @GetMapping("/admin/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/admin_books";
    }

    // USER: View books
    @GetMapping("/books")
    public String viewBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books"; // template: templates/admin_books.html
    }

    // USER: Search books
    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String keyword, Model model) {
        model.addAttribute("books", bookService.searchBooks(keyword));
        model.addAttribute("keyword", keyword);
        return "books";  // reload same template
    }
}
