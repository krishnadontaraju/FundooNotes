package com.fundoo.notes.service;

import com.fundoo.notes.dto.FundooLabelDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.response.ResponseDTO;

public interface IFundooLabelService {

	ResponseDTO viewAllLabels(String token) throws FundooNotesException;

	ResponseDTO createLabel(FundooLabelDTO userDTO, String token) throws FundooNotesException;

	ResponseDTO deleteLabel(long userID, String token) throws FundooNotesException;

	ResponseDTO labelTheNote(long labelId, long noteId, String token) throws FundooNotesException;

	ResponseDTO updateLabel(long labelId, FundooLabelDTO fundooLabelDTO ,String token) throws FundooNotesException;

	ResponseDTO removeLabelFromTheNote(long labelId, long noteId, String token) throws FundooNotesException;

}
