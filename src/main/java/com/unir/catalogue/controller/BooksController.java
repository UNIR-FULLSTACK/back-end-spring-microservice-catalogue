package com.unir.catalogue.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.unir.catalogue.controller.model.CreateBookRequest;
import com.unir.catalogue.controller.model.BookDto;
import com.unir.catalogue.data.model.Book;
import com.unir.catalogue.service.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre libros alojados en una base de datos en memoria.")
public class BooksController {

    private final BooksService service;

    @GetMapping("/books")
    @Operation(
            operationId = "Obtener libros",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los libros almacenados en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    public ResponseEntity<List<Book>> getBooks(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "titulo", description = "Título del libro. No tiene por que ser exacto", example = "La Odisea", required = false)
            @RequestParam(required = false) String titulo,
            @Parameter(name = "autor", description = "Autor del libro. Debe ser exacto", example = "HO", required = false)
            @RequestParam(required = false) String autor,
            @Parameter(name = "isbn", description = "ISBN del libro. No tiene por que ser exacta", example = "123456789", required = false)
            @RequestParam(required = false) String isbn,
            @Parameter(name = "categoria", description = "Categoria del libro. No tiene por que ser exacta", example = "Sus", required = false)
            @RequestParam(required = false) String categoria,
            @Parameter(name = "valoracion", description = "Valoración del libro. No tiene por que ser exacta", example = "5", required = false)
            @RequestParam(required = false) Long valoracion,
            @Parameter(name = "fechapublica", description = "Fecha de publicación del libro. No tiene por que ser exacta", example = "2025-02-07", required = false)
            @RequestParam(required = false) LocalDate fechapublica,
            @Parameter(name = "visible", description = "Estado del libro. true o false", example = "true", required = false)
            @RequestParam(required = false) Boolean visible) {

        log.info("headers: {}", headers);
        List<Book> books = service.getBooks(titulo, autor, isbn, categoria, valoracion, fechapublica, visible);

        if (books != null) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/books/{bookId}")
    @Operation(
            operationId = "Obtener un libro",
            description = "Operacion de lectura",
            summary = "Se devuelve un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> getBook(@PathVariable String bookId) {

        log.info("Request received for book {}", bookId);
        Book book = service.getBook(bookId);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/books/{bookId}")
    @Operation(
            operationId = "Eliminar un libro",
            description = "Operacion de escritura",
            summary = "Se elimina un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {

        Boolean removed = service.removeBook(bookId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/books")
    @Operation(
            operationId = "Insertar un libro",
            description = "Operacion de escritura",
            summary = "Se crea un libro a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {

        Book createdBook = service.createBook(request);

        if (createdBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/books/{bookId}")
    @Operation(
            operationId = "Modificar parcialmente un libro",
            description = "RFC 7386. Operacion de escritura",
            summary = "RFC 7386. Se modifica parcialmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro inválido o datos incorrectos introducidos.")
    public ResponseEntity<Book> patchBook(@PathVariable String bookId, @RequestBody String patchBody) {

        Book patched = service.updateBook(bookId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/books/{bookId}")
    @Operation(
            operationId = "Modificar totalmente un libro",
            description = "Operacion de escritura",
            summary = "Se modifica totalmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = BookDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro no encontrado.")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody BookDto body) {

        Book updated = service.updateBook(bookId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
