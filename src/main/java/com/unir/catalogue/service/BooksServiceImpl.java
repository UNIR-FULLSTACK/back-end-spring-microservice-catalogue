package com.unir.catalogue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.catalogue.controller.model.BookDto;
import com.unir.catalogue.controller.model.CreateBookRequest;
import com.unir.catalogue.data.BookRepository;
import com.unir.catalogue.data.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class BooksServiceImpl implements BooksService {

	@Autowired
	private BookRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<Book> getBooks(String titulo, String autor, String isbn, String categoria, Long valoracion, LocalDate fechapublica, Boolean visible) {

		if (StringUtils.hasLength(titulo) || StringUtils.hasLength(autor) || StringUtils.hasLength(isbn) || StringUtils.hasLength(categoria) || valoracion != null || fechapublica != null
				|| visible != null) {
			return repository.search(titulo, autor, isbn, categoria, valoracion, fechapublica, visible);
		}

		List<Book> books = repository.getBooks();
		return books.isEmpty() ? null : books;
	}

	@Override
	public Book getBook(String bookId) {
		return repository.getById(Long.valueOf(bookId));
	}

	@Override
	public Boolean removeBook(String bookId) {

		Book book = repository.getById(Long.valueOf(bookId));

		if (book != null) {
			repository.delete(book);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Book createBook(CreateBookRequest request) {

		//Otra opcion: Jakarta Validation: https://www.baeldung.com/java-validation
		if (request != null && StringUtils.hasLength(request.getTitulo().trim())
				&& StringUtils.hasLength(request.getAutor().trim())
				&& StringUtils.hasLength(request.getIsbn().trim())
				&& StringUtils.hasLength(request.getCategoria().trim())
				&& request.getValoracion() != null
				&& request.getFechapublica() != null && request.getVisible() != null) {

			Book book = Book.builder().titulo(request.getTitulo()).autor(request.getAutor())
					.isbn(request.getIsbn()).categoria(request.getCategoria()).valoracion(request.getValoracion()).fechapublica(request.getFechapublica()).visible(request.getVisible()).build();

			return repository.save(book);
		} else {
			return null;
		}
	}

	@Override
	public Book updateBook(String bookId, String request) {

		//PATCH se implementa en este caso mediante Merge Patch: https://datatracker.ietf.org/doc/html/rfc7386
		Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			try {
				JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
				JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
				Book patched = objectMapper.treeToValue(target, Book.class);
				repository.save(patched);
				return patched;
			} catch (JsonProcessingException | JsonPatchException e) {
				log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
			return null;
		}
	}

	@Override
	public Book updateBook(String bookId, BookDto updateRequest) {
		Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			book.update(updateRequest);
			repository.save(book);
			return book;
		} else {
			return null;
		}
	}

}
