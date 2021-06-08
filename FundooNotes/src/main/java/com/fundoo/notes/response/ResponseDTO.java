package com.fundoo.notes.response;

import lombok.Data;

@Data
public class ResponseDTO {

	private String message;
	private Object response;

	public ResponseDTO(String message, Object response) {
		this.message = message;
		this.response = response;
	}

}
