package com.unir.catalogue.data;

import com.unir.catalogue.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

interface BookJpaRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	List<Book> findByTitulo(String titulo);

	List<Book> findByAutor(String autor);

	List<Book> findByVisible(Boolean visible);

	List<Book> findByTituloAndAutor(String titulo, String autor);

}
