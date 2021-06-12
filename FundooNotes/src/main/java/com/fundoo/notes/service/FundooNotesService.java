package com.fundoo.notes.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	//To Create or Add note 
	@Override
	public ResponseDTO addNote(FundooNoteDTO fundooNoteDTO ,String token) throws FundooNotesException {
		
		long userId = tokenManager.decodeToken(token);
		
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("Requested Note Addition");
			//Mapping the parameter to the Model
			FundooNotesModel note = mapper.map(fundooNoteDTO , FundooNotesModel.class);
			note.setUserNumber(userId);
			fundooNotesRespository.save(note);
			log.info("note suucessfully added with title = "+fundooNoteDTO.title +" , and description = "+fundooNoteDTO.description);
			ResponseDTO addNoterespone = new ResponseDTO("Successfully added note with title", tokenManager.createToken(note.getId()));
			
			return addNoterespone;
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(400 , "Access Denied , User not Present");
		}
		
		
	}

	@Override
	public ResponseDTO viewAllNotes(String token) throws FundooNotesException {
		log.info("Requested to view all notes");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found retrieving all notes"+fundooNotesRespository.findAll());
			return new ResponseDTO("Get all notes Call Successful" , fundooNotesRespository.findAll());
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");
		}
	}

	@Override
	public ResponseDTO changeNote(long noteId, FundooNoteDTO fundooNoteDTO ,String token) throws FundooNotesException {
		log.info("Requested to change the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found ,Checking for Note's presence");
			if (isNotePresent.isPresent()) {
				log.info("Note found initiating Note Change");
				isNotePresent.get().changeNote(fundooNoteDTO);			
				fundooNotesRespository.save(isNotePresent.get());			
				ResponseDTO changeNoteResponse = new ResponseDTO("Successfully changed the note " , isNotePresent.get());
				log.info("Note has been Succesfully changed");
				return changeNoteResponse;
				
			}else {
				log.error("Note was not found with ID "+noteId);
				throw new FundooNotesException(501 , "Note not found");
			}
			
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
	}

	@Override
	public ResponseDTO moveNoteToTrash(long noteId ,String token) throws FundooNotesException {
		log.info("Requested to trash the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found ,Checking for Note's presence");
			if (isNotePresent.isPresent()) {
				log.info("Note found initiating Note trashing");
				isNotePresent.get().setTrashed(true);			
				fundooNotesRespository.save(isNotePresent.get());
				ResponseDTO trashNoteResponse = new ResponseDTO("Successfully trashed the note " , isNotePresent.get().getDescription());
				log.info("Successfully trashed the note");
				return trashNoteResponse;
				
			}else {
				log.error("Note was not found with ID "+noteId);
				throw new FundooNotesException(501 , "Note not found");
			}
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
	}

	@Override
	public ResponseDTO archiveNote(long noteId ,String token) throws FundooNotesException {
		log.info("Requested to archive the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found ,Checking for Note's presence");
			
			if (isNotePresent.isPresent()) {
				
				log.info("Note found initiating Note archiving");
				isNotePresent.get().setArchived(true);
				fundooNotesRespository.save(isNotePresent.get());
				ResponseDTO qrchiveNoteResponse = new ResponseDTO("Successfully archived the note " , isNotePresent.get().getDescription());
				log.info("Successfully archived the note with Id"+noteId);
				
				return qrchiveNoteResponse;
			}else {
				log.error("Note was not found with ID "+noteId);
				throw new FundooNotesException(501 , "Note not found");
			}
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
		
	}

	@Override
	public ResponseDTO pinNote(long noteId ,String token) throws FundooNotesException {
		log.info("Requested to pin the note ");
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found ,Checking for Note's presence");
			if (isNotePresent.isPresent()) {
				log.info("Note found initiating Note pinning");
				isNotePresent.get().setPinned(true);			
				fundooNotesRespository.save(isNotePresent.get());
				ResponseDTO pinNoteResponse = new ResponseDTO("Successfully pinned the note " , isNotePresent.get().getDescription());
				log.info("Successfully pinned the note");
				return pinNoteResponse;
			}else {
				log.error("Note was not found with ID "+noteId);
				throw new FundooNotesException(501 , "Note not found");
			}
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
	}

	@Override
	public ResponseDTO viewAllpinnedNotes(String token) throws FundooNotesException {
		log.info("Requested to view all pinned notes");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found , Retrieving all Pinned Notes");
			List<FundooNotesModel> allPinnedNotes = fundooNotesRespository.findByIsPinned();		
			ResponseDTO allPinnedNotesResponse = new ResponseDTO("Succesfully found all Pinned Notes" , allPinnedNotes);	
			log.info("Retrievd all pinned notes"+allPinnedNotes);
			return allPinnedNotesResponse;
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
		
		
	}

	@Override
	public ResponseDTO viewAllArchivedNotes(String token) throws FundooNotesException {
		log.info("Requested to view all pinned notes");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			List<FundooNotesModel> allArchivedNotes = fundooNotesRespository.findByIsArchived();		
			ResponseDTO allArchivedNotesResponse = new ResponseDTO("Succesfully found all Pinned Notes" , allArchivedNotes);		
			log.info("Retrievd all pinned notes"+allArchivedNotes);
			return allArchivedNotesResponse;
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
		
	}

	@Override
	public ResponseDTO viewAllTrashedNotes(String token) throws FundooNotesException {
		log.info("Requested to view all pinned notes");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			List<FundooNotesModel> allTrashedNotes = fundooNotesRespository.findByIsTrashed();		
			ResponseDTO allTrashedNotesResponse = new ResponseDTO("Succesfully found all Pinned Notes" , allTrashedNotes);		
			log.info("Retrievd all pinned notes"+allTrashedNotes);
			return allTrashedNotesResponse;	
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");		
			}
		
	}

	@Override
	public ResponseDTO addCollaborator(String emailId , long noteId ,String token) throws FundooNotesException {
		
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		boolean isCollaboratorEmailPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkEmailId/"+emailId , boolean.class);
		if (isUserPresent == true) {
			if (isCollaboratorEmailPresent == true) {
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
			else {
				log.error("Collaborator not found with email "+emailId);
				throw new FundooNotesException(601 , "Note not found");
				}
		}
		else {
			log.error("User No found with token "+token);
			throw new FundooNotesException(601 , "Access Denied ,token not correct");
		}
	}
		
	


	@Override
	public ResponseDTO removeCollaborator(String emailId , long noteId ,String token) throws FundooNotesException {
		log.info("Requested for Collaborator removal");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRespository.findById(noteId);
		Optional<FundooNoteCollaborator> isCollaboratorPresent = fundooCollabRepository.findByEmailAddress(emailId);
		
		if (isUserPresent == true) {
			log.info("User found , now looking for Note's Presence");
			if (isNotePresent.isPresent()) {
				log.info("Note found now looking Collaborator's presence");
				if (isCollaboratorPresent.isPresent()) {

					log.info("Collaborator found now initiating removal process");
					isCollaboratorPresent.get().getNotes().remove(isNotePresent.get());				
					fundooCollabRepository.save(isCollaboratorPresent.get());				
					isNotePresent.get().getCollaborators().remove(isCollaboratorPresent.get());				
					fundooNotesRespository.save(isNotePresent.get());				
					ResponseDTO removeCollaboratorResponse = new ResponseDTO("removed Collab ","done");	
					
					log.info("removed Collaborator from");
					return removeCollaboratorResponse;
				
				}else {
					log.error("Collaborator is not found with email "+emailId);
					throw new FundooNotesException(601 , "Collaborator is not found");
				}
			}else {
				log.error("Note was not found with ID "+noteId);
				throw new FundooNotesException(601 , "Note not found");
			}
		}else {
			log.error("User No found with token "+token);
			throw new FundooNotesException(601 , "Access Denied ,token not correct");
		}
		
		
		
	}

	@Override
	public ResponseDTO viewAllCollaborators(long noteId ,String token) throws FundooNotesException {
		
		log.info("Requested to view All Collaborators");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			
			log.info("User found and started retireval of the Collaborators");
			
			List<Object> allCollaboraatorsList = fundooNotesRespository.findAllCollaboratorsById(noteId);		
			allCollaboraatorsList.forEach(v -> mapper.map(v , FundooNoteCollaborator.class));	
			ResponseDTO viewAllCollaboratorResponse = new ResponseDTO("Succesful" , allCollaboraatorsList);
			
			log.info("All Collaborators retrieval Succesful");
			return viewAllCollaboratorResponse;
		}else {
			log.error("User No found with token "+token);
			throw new FundooNotesException(601 , "Access Denied ,token not correct");
		}
	}

	
}
