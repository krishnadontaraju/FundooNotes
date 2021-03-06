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

import com.fundoo.notes.dto.FundooNoteDTO;
import com.fundoo.notes.exception.FundooNotesException;
import com.fundoo.notes.response.ResponseDTO;
import com.fundoo.notes.service.IFundooNotesService;

@RestController
@RequestMapping("/fundoo/notes")
public class FundooNotesController {

	@Autowired
	private IFundooNotesService fundooNotesService;
	
	@PostMapping("/addNote")
	public ResponseEntity<ResponseDTO> addNote(@RequestBody FundooNoteDTO fundooNoteDTO , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO addNoteResponse =  fundooNotesService.addNote(fundooNoteDTO ,token);		
		return new ResponseEntity<ResponseDTO>(addNoteResponse , HttpStatus.CREATED);
	}
	
	@PostMapping("/viewNotes")
	public ResponseEntity<ResponseDTO> viewNotes(@RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO viewNoteResponse = fundooNotesService.viewAllNotes(token);		
		return new ResponseEntity<ResponseDTO>(viewNoteResponse , HttpStatus.FOUND);
		
	}
	
	@PutMapping("/changeNote/{noteId}")
	public ResponseEntity<ResponseDTO> changeNote(@PathVariable long noteId  , @RequestBody FundooNoteDTO fundooNoteDTO , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO changeNoteResponse = fundooNotesService.changeNote(noteId , fundooNoteDTO ,token);		
		return new ResponseEntity<ResponseDTO> (changeNoteResponse , HttpStatus.ACCEPTED);
		
	}
	
	@DeleteMapping("/trash/{noteId}")
	public ResponseEntity<ResponseDTO> trashTheNote (@PathVariable long noteId , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO trashNoteResponse = fundooNotesService.moveNoteToTrash(noteId ,token);
		return new ResponseEntity<ResponseDTO>(trashNoteResponse , HttpStatus.MOVED_PERMANENTLY);		
	}
	
	@PutMapping("/archiveNote/{noteId}")
	public ResponseEntity<ResponseDTO> archiveNote(@PathVariable long noteId , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO archiveNoteResponse = fundooNotesService.archiveNote(noteId ,token);		
		return new ResponseEntity<ResponseDTO> (archiveNoteResponse , HttpStatus.OK);
		
	}
	
	@PutMapping("/pinNote/{noteId}")
	public ResponseEntity<ResponseDTO> pinNote(@PathVariable long noteId , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO pinNoteResponse = fundooNotesService.pinNote(noteId ,token);		
		return new ResponseEntity<ResponseDTO> (pinNoteResponse , HttpStatus.OK);
		
	}
	
	@GetMapping("/pinnedNotes")
	public ResponseEntity<ResponseDTO> viewAllPinnedNotes(@RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO pinnedNotesResponse =  fundooNotesService.viewAllpinnedNotes(token);		
		return new ResponseEntity<ResponseDTO> (pinnedNotesResponse , HttpStatus.OK);
		
	}
	
	@GetMapping("/archivedNotes")
	public ResponseEntity<ResponseDTO> viewAllArchivedNotes(@RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO pinnedNotesResponse =  fundooNotesService.viewAllArchivedNotes(token);		
		return new ResponseEntity<ResponseDTO> (pinnedNotesResponse , HttpStatus.OK);
		
	}
	
	@GetMapping("/trashedNotes")
	public ResponseEntity<ResponseDTO> viewAllTrashedNotes(@RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO pinnedNotesResponse =  fundooNotesService.viewAllTrashedNotes(token);		
		return new ResponseEntity<ResponseDTO> (pinnedNotesResponse , HttpStatus.OK);
		
	}
	
	@PutMapping("/addCollaborator/{emailId}/noteId/{noteId}")
	public ResponseEntity<ResponseDTO> addCollaboratorToTheNote(@PathVariable String emailId ,@PathVariable long noteId , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO addCollaboratorResponse = fundooNotesService.addCollaborator(emailId ,noteId ,token);		
		return new ResponseEntity<ResponseDTO> (addCollaboratorResponse ,HttpStatus.ACCEPTED);
		
	}
	
	@PutMapping("/removeCollaborator/{emailId}/noteId/{noteId}")
	public ResponseEntity<ResponseDTO> removeCollaboratorFromTheNote(@PathVariable String emailId , @PathVariable long noteId , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO removeCollaboratorResponse = fundooNotesService.removeCollaborator(emailId , noteId ,token);		
		return new ResponseEntity<ResponseDTO> (removeCollaboratorResponse ,HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/viewCollaborators/{noteId}")
	public ResponseEntity<ResponseDTO> viewAllCollaaaborators(@PathVariable long noteId , @RequestHeader String token) throws FundooNotesException{
		
		ResponseDTO viewAllCollaboratorsResponse = fundooNotesService.viewAllCollaborators(noteId ,token);		
		return new ResponseEntity<ResponseDTO> (viewAllCollaboratorsResponse , HttpStatus.FOUND);
		
	}
	
}
