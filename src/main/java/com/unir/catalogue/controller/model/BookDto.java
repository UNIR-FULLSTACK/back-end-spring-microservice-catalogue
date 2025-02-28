package com.unir.catalogue.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
	
	private String titulo;
	private String autor;
	private String isbn;
	private String categoria;
	private Long valoracion;
	private LocalDate fechapublica;
	private Boolean visible;

}
