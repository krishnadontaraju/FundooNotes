package com.fundoo.notes.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundoo.notes.dto.FundooNoteDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.model.FundooNotesModel;
import com.fundoo.notes.repository.FundooNotesRepository;
import com.fundoo.notes.response.ResponseDTO;
import com.fundoo.notes.util.TokenUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FundooNotesService implements IFundooNotesService{
	
	@Autowired
	private FundooNotesRepository fundooNotesRespository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TokenUtility tokenManager;
	
	
	//To Create or Add note 
	@Override
	public ResponseDTO addNote(FundooNoteDTO fundooNoteDTO) {
		log.info("Requested Note Addition");
		//Mapping the parameter to the Model
		FundooNotesModel note = mapper.map(fundooNoteDTO , FundooNotesModel.class);
		fundooNotesRespository.save(note);
		log.info("note suucessfully added with title "+fundooNoteDTO.title +"description "+fundooNoteDTO.description);
		ResponseDTO addNoterespone = new ResponseDTO("Successfully added note with title", tokenManager.createToken(note.getId()));
		
		return addNoterespone;
	}

	@Override
	public ResponseDTO viewAllNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDTO changeNote(long noteId, FundooNoteDTO fundooNoteDTO) throws FundooNotesException {
		log.info("Requested to change the note ");
		
		Optional<FundooNotesModel> probableNote = fundooNotesRespository.findById(noteId);
		
		if (probableNote.isPresent()) {
			probableNote.get().changeNote(fundooNoteDTO);
			
			fundooNotesRespository.save(probableNote.get());
			
			ResponseDTO changeNoteResponse = new ResponseDTO("Successfully changed the note " , probableNote.get());
			
			return changeNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO moveNoteToTrash(long noteId) throws FundooNotesException {
		log.info("Requested to trash the note ");
		
		Optional<FundooNotesModel> probableNote = fundooNotesRespository.findById(noteId);
		
		if (probableNote.isPresent()) {
			
			probableNote.get().setTrashed(true);
			
			fundooNotesRespository.save(probableNote.get());

			ResponseDTO trashNoteResponse = new ResponseDTO("Successfully trashed the note " , probableNote.get().getDescription());
			
			return trashNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO archiveNote(long noteId) throws FundooNotesException {
		log.info("Requested to archive the note ");
		
		Optional<FundooNotesModel> probableNote = fundooNotesRespository.findById(noteId);
		
		if (probableNote.isPresent()) {
			
			probableNote.get().setArchived(true);
			
			fundooNotesRespository.save(probableNote.get());
			
			ResponseDTO qrchiveNoteResponse = new ResponseDTO("Successfully archived the note " , probableNote.get().getDescription());
			
			return qrchiveNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO pinNote(long noteId) throws FundooNotesException {
		log.info("Requested to pin the note ");
		
		Optional<FundooNotesModel> probableNote = fundooNotesRespository.findById(noteId);
		
		if (probableNote.isPresent()) {
			
			probableNote.get().setPinned(true);
			
			fundooNotesRespository.save(probableNote.get());
			
			ResponseDTO pinNoteResponse = new ResponseDTO("Successfully pinned the note " , probableNote.get().getDescription());
			
			return pinNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO viewAllpinnedNotes() {
		log.info("Requested to view all pinned notes");
		
		List<FundooNotesModel> allPinnedNotes = fundooNotesRespository.findByIsPinned();
		
		ResponseDTO allPinnedNotesResponse = new ResponseDTO("Succesfully found all Pinned Notes" , allPinnedNotes);
		
		return allPinnedNotesResponse;
	}

	@Override
	public ResponseDTO viewAllArchivedNotes() {
		log.info("Requested to view all pinned notes");
		
		List<FundooNotesModel> allArchivedNotes = fundooNotesRespository.findByIsArchived();
		
		ResponseDTO allArchivedNotesResponse = new ResponseDTO("Succesfully found all Pinned Notes" , allArchivedNotes);
		
		return allArchivedNotesResponse;
	}

	@Override
	public ResponseDTO viewAllTrashedNotes() {
		log.info("Requested to view all pinned notes");
		
		List<FundooNotesModel> allTrashedNotes = fundooNotesRespository.findByIsTrashed();
		
		ResponseDTO allTrashedNotesResponse = new ResponseDTO("Succesfully found all Pinned Notes" , allTrashedNotes);
		
		return allTrashedNotesResponse;
	}

	
}
