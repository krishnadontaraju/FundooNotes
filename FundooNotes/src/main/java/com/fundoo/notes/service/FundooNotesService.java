package com.fundoo.notes.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundoo.notes.dto.FundooNoteDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.model.FundooNoteCollaborator;
import com.fundoo.notes.model.FundooNotesModel;
import com.fundoo.notes.repository.FundooCollabRepository;
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
	
	@Autowired
	private FundooCollabRepository fundooCollabRepository;
	
	
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
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		if (isNotePresent.isPresent()) {
			isNotePresent.get().changeNote(fundooNoteDTO);
			
			fundooNotesRespository.save(isNotePresent.get());
			
			ResponseDTO changeNoteResponse = new ResponseDTO("Successfully changed the note " , isNotePresent.get());
			
			return changeNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO moveNoteToTrash(long noteId) throws FundooNotesException {
		log.info("Requested to trash the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		if (isNotePresent.isPresent()) {
			
			isNotePresent.get().setTrashed(true);
			
			fundooNotesRespository.save(isNotePresent.get());

			ResponseDTO trashNoteResponse = new ResponseDTO("Successfully trashed the note " , isNotePresent.get().getDescription());
			
			return trashNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO archiveNote(long noteId) throws FundooNotesException {
		log.info("Requested to archive the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		if (isNotePresent.isPresent()) {
			
			isNotePresent.get().setArchived(true);
			
			fundooNotesRespository.save(isNotePresent.get());
			
			ResponseDTO qrchiveNoteResponse = new ResponseDTO("Successfully archived the note " , probableNote.get().getDescription());
			
			return qrchiveNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO pinNote(long noteId) throws FundooNotesException {
		log.info("Requested to pin the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		if (isNotePresent.isPresent()) {
			
			isNotePresent.get().setPinned(true);
			
			fundooNotesRespository.save(isNotePresent.get());
			
			ResponseDTO pinNoteResponse = new ResponseDTO("Successfully pinned the note " , isNotePresent.get().getDescription());
			
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

	@Override
	public ResponseDTO addCollaborator(String emailId , long noteId) throws FundooNotesException {
		
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		if (isNotePresent.isPresent()) {

			FundooNoteCollaborator collaborator = new FundooNoteCollaborator();
			
			collaborator.setEmailAddress(emailId);
			
			fundooCollabRepository.save(collaborator);
			
			Optional<FundooNoteCollaborator> isCollaboratorPresent = fundooCollabRepository.findByEmailAddress(emailId);
			
			isCollaboratorPresent.get().addCollaboartorToTheNote(isNotePresent.get());
			
			fundooCollabRepository.save(isCollaboratorPresent.get());
			
			isNotePresent.get().getCollaborators().add(isCollaboratorPresent.get());
			
			fundooNotesRespository.save(isNotePresent.get());
			
			ResponseDTO addCollaboratorResponse = new ResponseDTO("added Collab ","done");
			
			return addCollaboratorResponse;
		}else {
			throw new FundooNotesException(601 , "Note not found");
		}
		
		
		}

	@Override
	public ResponseDTO removeCollaborator(String emailId , long noteId) throws FundooNotesException {
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		Optional<FundooNoteCollaborator> isCollaboratorPresent = fundooCollabRepository.findByEmailAddress(emailId);
		
		if (isNotePresent.isPresent()) {
			if (isCollaboratorPresent.isEmpty()) {

				isCollaboratorPresent.get().getNotes().remove(isNotePresent.get());
				
				fundooCollabRepository.save(isCollaboratorPresent.get());
				
				isNotePresent.get().getCollaborators().remove(isCollaboratorPresent.get());
				
				fundooNotesRespository.save(isNotePresent.get());
				
				ResponseDTO removeCollaboratorResponse = new ResponseDTO("removed Collab ","done");
				
				return removeCollaboratorResponse;
			}else {
				throw new FundooNotesException(601 , "Collaborator already exists");
			}
		}else {
			throw new FundooNotesException(601 , "Note not found");
		}
		
	}

	@Override
	public ResponseDTO viewAllCollaborators(long noteId) {
		
		List<Object> allCollaboraatorsList = fundooNotesRespository.findAllCollaboratorsById(noteId);
		
		allCollaboraatorsList.forEach(v -> mapper.map(v , FundooNoteCollaborator.class));
	
		ResponseDTO viewAllCollaboratorResponse = new ResponseDTO("Succesful" , allCollaboraatorsList);
		
		return viewAllCollaboratorResponse;
		
	}

	
}
