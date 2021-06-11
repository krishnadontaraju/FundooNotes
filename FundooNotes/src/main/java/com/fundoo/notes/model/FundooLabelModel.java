package com.fundoo.notes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fundoo.notes.dto.FundooLabelDTO;

import lombok.Data;

@Entity
@Table(name = "fundoo_label")
@Data
public class FundooLabelModel {
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="label_name")
	private String labelName;
	
	@Column(name="user_id")
	private Long userId;

	@Column(name = "registered_date")
	private LocalDateTime registerDate;

	@Column(name = "updated_date")
	private LocalDateTime updateDate;


	@ManyToMany(mappedBy = "labelList")
//	@Column(name = "note_id")
	public List<FundooNotesModel> notesList = new ArrayList<FundooNotesModel>();


	public void changeLabel(FundooLabelDTO fundooLabelDTO) {
		this.labelName = fundooLabelDTO.labelName;
		
	}
	
//	public void labelTheNote(FundooNotesModel fundooNotesModel) {
//		notesList.add(fundooNotesModel);
//	}



}
