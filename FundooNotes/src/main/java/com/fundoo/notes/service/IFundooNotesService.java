package com.fundoo.notes.service;

import com.fundoo.notes.dto.FundooNoteDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.response.ResponseDTO;

public interface IFundooNotesService {

	ResponseDTO addNote(FundooNoteDTO fundooNoteDTO, String token) throws FundooNotesException;

	ResponseDTO viewAllNotes(String token) throws FundooNotesException;

	ResponseDTO changeNote(long noteId, FundooNoteDTO fundooNoteDTO, String token) throws FundooNotesException;

	ResponseDTO moveNoteToTrash(long noteId, String token) throws FundooNotesException;

	ResponseDTO archiveNote(long noteId, String token) throws FundooNotesException;

	ResponseDTO pinNote(long noteId, String token) throws FundooNotesException;

	ResponseDTO viewAllpinnedNotes(String token) throws FundooNotesException;

	ResponseDTO viewAllArchivedNotes(String token) throws FundooNotesException;

	ResponseDTO viewAllTrashedNotes(String token) throws FundooNotesException;

	ResponseDTO addCollaborator(String emailId, long noteId, String token) throws FundooNotesException;

	ResponseDTO removeCollaborator(String emailId, long noteId, String token) throws FundooNotesException;

	ResponseDTO viewAllCollaborators(long noteId, String token) throws FundooNotesException;

}
