package com.unir.catalogue.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

	private String titulo;
	private String autor;
	private String isbn;
	private String categoria;
	private Long valoracion;
	private LocalDate fechapublica;
	private Boolean visible;
}
