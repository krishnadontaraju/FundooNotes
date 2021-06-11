package com.fundoo.notes.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fundoo.notes.dto.FundooLabelDTO;
import com.fundoo.notes.dto.FundooLabelNoteDTO;
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

	@Override
	public ResponseDTO viewAllLabels() {
		ResponseDTO viewAllLabelsResponse = new ResponseDTO("Get Call Successful " , fundooLabelRepository.findAll());
		return viewAllLabelsResponse;
	}

	@Override
	public ResponseDTO createLabel(FundooLabelDTO userDTO) {
		
		
		log.info("Requested Note Addition");
		//Mapping the parameter to the Model
		FundooLabelModel note = mapper.map(userDTO , FundooLabelModel.class);
		fundooLabelRepository.save(note);
//		log.info("note suucessfully added with title "+userDTO.title +"description "+fundooNoteDTO.description);
		ResponseDTO addNoterespone = new ResponseDTO("Successfully added note with title", tokenManager.createToken(note.getId()));
		
		return addNoterespone;
	}

	@Override
	public ResponseDTO deleteLabel(long userID) throws FundooNotesException {
		log.info("Requested to trash the note ");
		
		Optional<FundooLabelModel> probableNote = fundooLabelRepository.findById(userID);
		
		if (probableNote.isPresent()) {
			
			
			fundooLabelRepository.delete(probableNote.get());

			ResponseDTO trashNoteResponse = new ResponseDTO("Successfully trashed the note " , probableNote.get().getLabelName());
			
			return trashNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO updateLabel(long token, FundooLabelDTO fundooLabelDTO) throws FundooNotesException {
		log.info("Requested to change the note ");
		
		Optional<FundooLabelModel> probableNote = fundooLabelRepository.findById(token);
		
		if (probableNote.isPresent()) {
			probableNote.get().changeLabel(fundooLabelDTO);
			
			fundooLabelRepository.save(probableNote.get());
			
			ResponseDTO changeNoteResponse = new ResponseDTO("Successfully changed the note " , probableNote.get());
			
			return changeNoteResponse;
		}else {
			throw new FundooNotesException(501 , "Note not found");
		}
	}

	@Override
	public ResponseDTO labelTheNote(long labelId , long notesId) {

		Optional<FundooLabelModel> probableLabel = fundooLabelRepository.findById(labelId);
		
		Optional<FundooNotesModel> probableNote = fundooNotesRepository.findById(notesId);
		
		probableLabel.get().getNotesList().add(probableNote.get());
		
		fundooLabelRepository.save(probableLabel.get());
		
		probableNote.get().getLabelList().add(probableLabel.get());
		
		fundooNotesRepository.save(probableNote.get());

		
		ResponseDTO labelTheNoteResponse = new ResponseDTO("labelled Successfully" , "done");
		
		return labelTheNoteResponse;
	}

	
	

}
