package com.unir.catalogue.data;

import com.unir.catalogue.data.model.Book;
import com.unir.catalogue.data.utils.Consts;
import com.unir.catalogue.data.utils.SearchCriteria;
import com.unir.catalogue.data.utils.SearchOperation;
import com.unir.catalogue.data.utils.SearchStatement;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final BookJpaRepository repository;

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public List<Book> search(String titulo, String autor, String isbn, String categoria, Long valoracion, LocalDate fechapublica, Boolean visible) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(titulo)) {
            spec.add(new SearchStatement(Consts.TITULO, titulo, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(autor)) {
            spec.add(new SearchStatement(Consts.AUTOR, autor, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(isbn)) {
            spec.add(new SearchStatement(Consts.ISBN, isbn, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(categoria)) {
            spec.add(new SearchStatement(Consts.CATEGORIA, categoria, SearchOperation.MATCH));
        }

        if ((valoracion != null)) {
            spec.add(new SearchStatement(Consts.VALORACION, valoracion, SearchOperation.EQUAL));
        }

        if (fechapublica != null) {
            spec.add(new SearchStatement(Consts.FECHAPUBLICA, java.sql.Date.valueOf(fechapublica), SearchOperation.EQUAL));
        }


        if (visible != null) {
            spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }

}
