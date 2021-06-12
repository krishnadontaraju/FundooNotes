package com.fundoo.notes.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fundoo.notes.dto.FundooLabelDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.model.FundooLabelModel;
import com.fundoo.notes.model.FundooNotesModel;
import com.fundoo.notes.repository.FundooLabelRepository;
import com.fundoo.notes.repository.FundooNotesRepository;
import com.fundoo.notes.response.ResponseDTO;
import com.fundoo.notes.util.TokenUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FundooLabelService implements IFundooLabelService{
	
	@Autowired
	private FundooLabelRepository fundooLabelRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TokenUtility tokenManager;
	
	@Autowired
	private FundooNotesRepository fundooNotesRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ResponseDTO viewAllLabels(String token) throws FundooNotesException {
		log.info("Requested to view All the Labels");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found , now looking for Note's Presence");
			ResponseDTO viewAllLabelsResponse = new ResponseDTO("Get Call Successful " , fundooLabelRepository.findAll());
			return viewAllLabelsResponse;
			
		}else {
			log.error("User was not found with token");
			throw new FundooNotesException(601 , "Access Denied");
		}
		
	}

	@Override
	public ResponseDTO createLabel(FundooLabelDTO labelDTO , String token) throws FundooNotesException {
		log.info("Requested Label Addition");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			
			log.info("User found , now initiating Label creation");
			//Mapping the parameter to the Model
			FundooLabelModel note = mapper.map(labelDTO , FundooLabelModel.class);
			fundooLabelRepository.save(note);
			log.info("Label suucessfully added with Name "+labelDTO.labelName);
			ResponseDTO addLabelrespone = new ResponseDTO("Successfully added Label ", tokenManager.createToken(note.getId()));
			
			return addLabelrespone;
		}else {
				log.error("User was not found with token "+token);
				throw new FundooNotesException(401 , "Access Denied , User not Present");
		}
		
		
	}

	@Override
	public ResponseDTO deleteLabel(long labelID , String token) throws FundooNotesException {
		
		log.info("Requested to delete the label");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);		
		
		Optional<FundooLabelModel> isLabelPresent = fundooLabelRepository.findById(labelID);
		
		if (isUserPresent == true) {
			log.info("User found , now looking for Note's Presence");
			
			if (isLabelPresent.isPresent()) {
				log.info("Note found now initiating deltion of Label");				
				fundooLabelRepository.delete(isLabelPresent.get());
				ResponseDTO trashNoteResponse = new ResponseDTO("Successfully deleted the label " , isLabelPresent.get().getLabelName());				
				return trashNoteResponse;
				
			}else {
				log.error("Note was not found with Id" +labelID);
				throw new FundooNotesException(501 , "Note not found");
			}
		}else {
			log.error("User was not found with token "+token);
			throw new FundooNotesException(401 , "Access Denied , User not Present");
	}
		
	}


	@Override
	public ResponseDTO updateLabel(long labelId, FundooLabelDTO fundooLabelDTO ,String token) throws FundooNotesException {
		
		log.info("Requested to update the Label ");
		
		Optional<FundooLabelModel> isLabelPresent = fundooLabelRepository.findById(labelId);
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		if (isUserPresent == true) {
			log.info("User found , now looking for Note's Presence");
			
			if (isLabelPresent.isPresent()) {
				
				log.info("Note found now looking Label's presence");
				isLabelPresent.get().changeLabel(fundooLabelDTO);				
				fundooLabelRepository.save(isLabelPresent.get());				
				ResponseDTO changeNoteResponse = new ResponseDTO("Successfully changed the note " , isLabelPresent.get());
				log.info("Successfully Updated the label");
				return changeNoteResponse;
				
			}else {
				log.error("Label was Not found with Id "+labelId);
				throw new FundooNotesException(501 , "Note not found");
			}
		}else {
			log.error("User No found with token "+token);
			throw new FundooNotesException(601 , "Access Denied ,token not correct");
		}
		
	}

	@Override
	public ResponseDTO labelTheNote(long labelId , long notesId ,String token) throws FundooNotesException {
		
		log.info("Requested to Label the Note");

		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		Optional<FundooLabelModel> isLabelPresentt = fundooLabelRepository.findById(labelId);
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRepository.findById(notesId);
		
		if (isUserPresent == true) {
			log.info("User found , now looking for Note's Presence");
			
			if (isNotePresent.isPresent()) {
				log.info("Note found now looking Label's presence");
				
				if (isLabelPresentt.isEmpty()) {
					log.info("Label found, now initiating removal process");
					
					isLabelPresentt.get().getNotesList().add(isNotePresent.get());
					fundooLabelRepository.save(isLabelPresentt.get());					
					isNotePresent.get().getLabelList().add(isLabelPresentt.get());					
					fundooNotesRepository.save(isNotePresent.get());
					
					ResponseDTO labelTheNoteResponse = new ResponseDTO("labelled Successfully" , "done");
					
					return labelTheNoteResponse;
					
				}else {
					log.error("Duplicate Label was found with Id "+labelId);
					throw new FundooNotesException(601 , "Duplicate Label is found");
				}
			}else {
				log.error("Note was not found with ID "+notesId);
				throw new FundooNotesException(601 , "Note not found");
			}
			
		}else {
			log.error("User No found with token "+token);
			throw new FundooNotesException(601 , "Access Denied ,token not correct");
		}
		
	}
	
	

	@Override
	public ResponseDTO removeLabelFromTheNote(long labelId , long noteId ,String token) throws FundooNotesException {
		log.info("Requested for Collaborator removal");
		
		long userId = tokenManager.decodeToken(token);
		boolean isUserPresent = restTemplate.getForObject("http://localhost:8080/fundoo/users/checkUser/"+userId , boolean.class);
		
		Optional<FundooNotesModel> isNotePresent = fundooNotesRepository.findById(noteId);
		Optional<FundooLabelModel> isLabelPresentt = fundooLabelRepository.findById(labelId);
		
		if (isUserPresent == true) {
			log.info("User found , now looking for Note's Presence");
			if (isNotePresent.isPresent()) {
				log.info("Note found now looking Label's presence");
				if (isLabelPresentt.isPresent()) {

					log.info("Label found, now initiating removal process");
					isLabelPresentt.get().getNotesList().remove(isNotePresent.get());			
					fundooLabelRepository.save(isLabelPresentt.get());				
					isNotePresent.get().getLabelList().remove(isLabelPresentt.get());				
					fundooNotesRepository.save(isNotePresent.get());				
					ResponseDTO removeLabelResponse = new ResponseDTO("removed Label ","done");	
					
					log.info("removed Label from");
					return removeLabelResponse;
				
				}else {
					log.error("Label is not found with Id "+labelId);
					throw new FundooNotesException(601 , "Label is not found");
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
	

}
