package com.fundoo.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundoo.notes.model.FundooLabelModel;

public interface FundooLabelRepository extends JpaRepository<FundooLabelModel , Long>{
	
	

}
