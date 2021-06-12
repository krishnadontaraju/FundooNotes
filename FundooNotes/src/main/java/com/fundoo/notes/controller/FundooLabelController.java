package com.fundoo.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundoo.notes.dto.FundooLabelDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.response.ResponseDTO;
import com.fundoo.notes.service.IFundooLabelService;



@RestController
@RequestMapping("/fundoo/label")
public class FundooLabelController {

	@Autowired
	private IFundooLabelService fundooLabelService;
	
	
	@GetMapping(value = {"" , "/" , "/users"})
	public ResponseEntity<ResponseDTO> getAllUsers(@RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO responseDTO = fundooLabelService.viewAllLabels(token);
		return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ResponseDTO> registerUsers(@RequestBody FundooLabelDTO labelDTO ,@RequestHeader String token) throws FundooNotesException{

		ResponseDTO responseDTO = fundooLabelService.createLabel(labelDTO,token);
		return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{labelID}")
	public ResponseEntity<ResponseDTO> deleteUserData(@PathVariable int labelId ,@RequestHeader String token) throws FundooNotesException{
		
    	ResponseDTO responseDTO = fundooLabelService.deleteLabel(labelId ,token);
        return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.OK);
    }
	
	@PutMapping("/verify/{labelId}")
	public ResponseEntity<ResponseDTO> verifyUser(@PathVariable long labelId , @RequestBody FundooLabelDTO fundooLabelDTO ,@RequestHeader String token ) throws FundooNotesException{
		
		ResponseDTO verifyUserResponse = fundooLabelService.updateLabel(labelId , fundooLabelDTO ,token);
		
		return new ResponseEntity<ResponseDTO>(verifyUserResponse , HttpStatus.OK);
		
	}
	
	@PutMapping("/labelNotes/{labelId}/note/{noteId}")
	public ResponseEntity<ResponseDTO> labelTheNote (@PathVariable long labelId , @PathVariable long noteId ,@RequestHeader String token ) throws FundooNotesException{
		
		ResponseDTO labelTheNoteResponse = fundooLabelService.labelTheNote(labelId , noteId ,token);
		
		return new ResponseEntity<ResponseDTO>(labelTheNoteResponse , HttpStatus.OK);
		
	}
	
}
