package com.fundoo.notes.exception;

import lombok.Data;

@SuppressWarnings("serial")
public class FundooNotesException extends Exception {

	private int code;
	public FundooNotesException(int code, String message) {
	
		super(message);
		this.code = code;
		
	}

}
