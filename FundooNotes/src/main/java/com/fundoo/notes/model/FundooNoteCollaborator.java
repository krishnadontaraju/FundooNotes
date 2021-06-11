package com.fundoo.notes.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "collaborators")
@Data
public class FundooNoteCollaborator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String emailAddress;
	private long userId;

	@JsonIgnore
	@ManyToMany
	private List<FundooNotesModel> notes = new ArrayList<FundooNotesModel>();
	
	public FundooNoteCollaborator(){}

	public void addCollaboartorToTheNote(FundooNotesModel fundooNotesModel) {
		
		this.notes.add(fundooNotesModel);
		
	}
	
	
}
