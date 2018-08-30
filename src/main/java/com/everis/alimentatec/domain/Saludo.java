package com.everis.alimentatec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Saludo {
	
	private String frase;
	private Persona persona;
}
