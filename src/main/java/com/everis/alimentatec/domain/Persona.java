package com.everis.alimentatec.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Persona {

	private String nombre;
	private String apellidos;
	@JsonIgnore
	private String dni;

}
