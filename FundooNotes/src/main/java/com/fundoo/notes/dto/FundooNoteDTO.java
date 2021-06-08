package com.fundoo.notes.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FundooNoteDTO {

	@NotNull(message = "Title must be present for identification")
	public String title;
	@NotNull(message = "")
	public String description;
	public Long labelNumber;
	public String color;
	public LocalDateTime reminderTime;

}
