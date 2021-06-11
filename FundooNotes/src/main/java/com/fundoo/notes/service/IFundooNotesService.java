package com.fundoo.notes.service;

import com.fundoo.notes.dto.FundooNoteDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.response.ResponseDTO;

public interface IFundooNotesService {

	ResponseDTO addNote(FundooNoteDTO fundooNoteDTO);

	ResponseDTO viewAllNotes();

	ResponseDTO changeNote(long noteId, FundooNoteDTO fundooNoteDTO) throws FundooNotesException;

	ResponseDTO moveNoteToTrash(long noteId) throws FundooNotesException;

	ResponseDTO archiveNote(long noteId) throws FundooNotesException;

	ResponseDTO pinNote(long noteId) throws FundooNotesException;

	ResponseDTO viewAllpinnedNotes();

	ResponseDTO viewAllArchivedNotes();

	ResponseDTO viewAllTrashedNotes();

	ResponseDTO addCollaborator(String emailId, long noteId) throws FundooNotesException;

	ResponseDTO removeCollaborator(String emailId, long noteId) throws FundooNotesException;

	ResponseDTO viewAllCollaborators(long noteId);

}
