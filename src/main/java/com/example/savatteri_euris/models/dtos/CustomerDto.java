package com.example.savatteri_euris.models.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CustomerDto {

	private String name;
	private String familyName;
	private Date birthDate;
	private String codiceFiscale;
	private String email;
	
}
