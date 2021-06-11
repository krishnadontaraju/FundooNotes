package com.fundoo.notes.dto;

import java.util.List;

import com.fundoo.notes.model.FundooLabelModel;
import com.fundoo.notes.model.FundooNotesModel;

import lombok.Data;

@Data
public class FundooLabelNoteDTO {

	public List<FundooLabelModel> labelIds;

	public List<FundooNotesModel> noteIds;

}
