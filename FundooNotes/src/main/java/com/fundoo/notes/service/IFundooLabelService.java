package com.fundoo.notes.service;

import com.fundoo.notes.dto.FundooLabelDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.response.ResponseDTO;

public interface IFundooLabelService {

	ResponseDTO viewAllLabels();

	ResponseDTO createLabel(FundooLabelDTO userDTO);

	ResponseDTO deleteLabel(long userID) throws FundooNotesException;

	ResponseDTO updateLabel(long token, FundooLabelDTO fundooLabelDTO) throws FundooNotesException;

	ResponseDTO labelTheNote(long labelId, long noteId);

}
