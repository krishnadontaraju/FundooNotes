package com.fundoo.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundoo.notes.dto.FundooLabelDTO;
import com.fundoo.notes.dto.FundooLabelNoteDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.model.FundooLabelModel;
import com.fundoo.notes.model.FundooNotesModel;
import com.fundoo.notes.response.ResponseDTO;
import com.fundoo.notes.service.IFundooLabelService;



@RestController
@RequestMapping("/fundoo/label")
public class FundooLabelController {

	@Autowired
	private IFundooLabelService fundooLabelService;
	
	
	@GetMapping(value = {"" , "/" , "/users"})
	public ResponseEntity<ResponseDTO> getAllUsers(){
		
		ResponseDTO responseDTO = fundooLabelService.viewAllLabels();
		return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ResponseDTO> registerUsers(@RequestBody FundooLabelDTO userDTO ){

		ResponseDTO responseDTO = fundooLabelService.createLabel(userDTO);
		return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{userID}")
	public ResponseEntity<ResponseDTO> deleteUserData(@PathVariable("userID") int userID) throws FundooNotesException{
		
    	ResponseDTO responseDTO = fundooLabelService.deleteLabel(userID);
        return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.OK);
    }
	
	@PutMapping("/verify/{token}")
	public ResponseEntity<ResponseDTO> verifyUser(@PathVariable long token , @RequestBody FundooLabelDTO fundooLabelDTO ) throws FundooNotesException{
		
		ResponseDTO verifyUserResponse = fundooLabelService.updateLabel(token , fundooLabelDTO);
		
		return new ResponseEntity<ResponseDTO>(verifyUserResponse , HttpStatus.OK);
		
	}
	
	@PutMapping("/labelNotes/{labelId}/note/{noteId}")
	public ResponseEntity<ResponseDTO> labelTheNote (@PathVariable long labelId , @PathVariable long noteId ){
		
		ResponseDTO labelTheNoteResponse = fundooLabelService.labelTheNote(labelId , noteId);
		
		return new ResponseEntity<ResponseDTO>(labelTheNoteResponse , HttpStatus.OK);
		
	}
	
}
