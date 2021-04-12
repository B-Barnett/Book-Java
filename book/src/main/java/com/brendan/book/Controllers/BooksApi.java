package com.brendan.book.Controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.brendan.book.Models.Book;
import com.brendan.book.Services.BookService;

@Controller
public class BooksApi {
	private final BookService bookService;
    public BooksApi(BookService bookService){
        this.bookService = bookService;
    }
    @RequestMapping("/")
    public String index(Model model) {
        List<Book> allBooks = bookService.allBooks();
        model.addAttribute("books", allBooks);
        return "index.jsp";
    }
    
    @RequestMapping("/books/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
    	Book thisBook = bookService.findBook(id);
    	model.addAttribute("book", thisBook);
    	return "show.jsp";
    }
    
    @RequestMapping("/books/new")
    public String newBook(@ModelAttribute("book") Book book) {
    	return "new.jsp";
    }
    
    @RequestMapping("/books/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBook(id);
        model.addAttribute("book", book);
        return "edit.jsp";
    }
    
    @RequestMapping(value="/books/{id}", method=RequestMethod.PUT)
    public String update(@Valid @ModelAttribute("book") Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "edit.jsp";
        } else {
            bookService.updateBook(book);
            return "redirect:/books";
        }
    }
    
    @RequestMapping(value="/books/{id}", method=RequestMethod.DELETE)
    public String destroy(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
    
    @RequestMapping(value="/books", method=RequestMethod.POST)
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult result) {
    	if (result.hasErrors()) {
    		return "new.jsp";
    	} else {
    		bookService.createBook(book);
    		return "redirect:/books";
    	}
    }
    
    @RequestMapping(value="/api/books", method=RequestMethod.POST)
    public Book create(
    	@RequestParam(value="title") String title, 
    	@RequestParam(value="description") String desc, 
    	@RequestParam(value="language") String lang, 
    	@RequestParam(value="pages") Integer numOfPages) {
        Book book = new Book(title, desc, lang, numOfPages);
        return bookService.createBook(book);
    }
    
    @RequestMapping("/api/books/{id}")
    public Book show(@PathVariable("id") Long id) {
        Book book = bookService.findBook(id);
        return book;
    }
    
}
