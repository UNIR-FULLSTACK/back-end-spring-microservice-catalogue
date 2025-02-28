package com.unir.catalogue.service;

import com.unir.catalogue.controller.model.BookDto;
import com.unir.catalogue.controller.model.CreateBookRequest;
import com.unir.catalogue.data.model.Book;


import java.time.LocalDate;
import java.util.List;

public interface BooksService {
	
	List<Book> getBooks(String titulo, String autor, String isbn, String categoria, Long valoracion, LocalDate fechapublica, Boolean visible);
	
	Book getBook(String bookId);
	
	Boolean removeBook(String bookId);
	
	Book createBook(CreateBookRequest request);

	Book updateBook(String bookId, String updateRequest);

	Book updateBook(String bookId, BookDto updateRequest);

}
