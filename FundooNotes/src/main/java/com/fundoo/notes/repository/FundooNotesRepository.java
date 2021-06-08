package com.fundoo.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fundoo.notes.model.FundooNotesModel;

public interface FundooNotesRepository extends JpaRepository<FundooNotesModel , Long>{
	
	@Query(value = "SELECT * FROM fundoo_notes WHERE is_pinned = 1" , nativeQuery = true)
	List<FundooNotesModel> findByIsPinned();
	
	@Query(value = "SELECT * FROM fundoo_notes WHERE is_archived = 1" , nativeQuery = true)
	List<FundooNotesModel> findByIsArchived();
	
	@Query(value = "SELECT * FROM fundoo_notes WHERE is_trashed = 1" , nativeQuery = true)
	List<FundooNotesModel> findByIsTrashed();

}
