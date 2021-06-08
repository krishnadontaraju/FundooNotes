package com.fundoo.notes.exception;

@SuppressWarnings("serial")
public class FundooNotesException extends Exception {

	private int code;
	public FundooNotesException(int code, String message) {
	
		super(message);
		this.code = code;
		
	}

}
