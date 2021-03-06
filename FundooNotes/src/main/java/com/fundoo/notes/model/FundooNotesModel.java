package com.fundoo.notes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundoo.notes.dto.FundooNoteDTO;

import lombok.Data;

@Entity
@Table(name = "fundoo_notes")
public @Data class FundooNotesModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "note_description")
	private String description;
	@Column(name = "user_id")
	private long userNumber;
	@Column(name = "added_date")
	private LocalDateTime addedDate;
	@Column(name = "updated_date")
	private LocalDateTime changedDate;
	@Column(name = "is_trashed")
	private boolean isTrashed;
	@Column(name = "is_archived")
	private boolean isArchived;
	@Column(name = "is_pinned")
	private boolean isPinned;
	@Column(name = "label_id")
	private Long labelNumber;
	@Column(name = "color")
	private String color;
	@Column(name = "reminder")
	private LocalDateTime reminderTime;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "notes_labels",
				joinColumns = @JoinColumn(name = "note_id"),
				inverseJoinColumns = @JoinColumn(name = "label_id"))
	private List<FundooLabelModel> labelList = new ArrayList<FundooLabelModel>();
	
	public void changeNote(FundooNoteDTO fundooNoteDTO) {

		this.title = fundooNoteDTO.title;
		this.description =fundooNoteDTO.description;
		this.labelNumber = fundooNoteDTO.labelNumber;
		this.color = fundooNoteDTO.color;
		
		this.changedDate = LocalDateTime.now();
		
	}
	
	@ManyToMany(mappedBy = "notes")
	private List<FundooNoteCollaborator> collaborators = new ArrayList<FundooNoteCollaborator>();
	
	public FundooNotesModel(long id) {
		this.id = id;
	}
	
	public FundooNotesModel() {}



}
