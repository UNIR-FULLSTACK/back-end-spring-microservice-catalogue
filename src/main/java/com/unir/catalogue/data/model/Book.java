package com.unir.catalogue.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unir.catalogue.controller.model.BookDto;
import com.unir.catalogue.data.utils.Consts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = Consts.TITULO, unique = true)
	private String titulo;
	
	@Column(name = Consts.AUTOR)
	private String autor;
	
	@Column(name = Consts.ISBN)
	private String isbn;

	@Column(name = Consts.CATEGORIA)
	private String categoria;

	@Column(name = Consts.VALORACION)
	private Long valoracion;

	@Column(name = Consts.FECHAPUBLICA)
	private LocalDate fechapublica;

	@Column(name = Consts.VISIBLE)
	private Boolean visible;

	public void update(BookDto bookDto) {
		this.titulo = bookDto.getTitulo();
		this.autor = bookDto.getAutor();
		this.isbn = bookDto.getIsbn();
		this.categoria = bookDto.getCategoria();
		this.valoracion = bookDto.getValoracion();
		this.fechapublica = bookDto.getFechapublica();
		this.visible = bookDto.getVisible();
	}

}
