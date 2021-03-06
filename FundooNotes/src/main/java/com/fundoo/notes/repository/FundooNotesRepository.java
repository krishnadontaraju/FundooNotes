package com.fundoo.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fundoo.notes.model.FundooNoteCollaborator;
import com.fundoo.notes.model.FundooNotesModel;

public interface FundooNotesRepository extends JpaRepository<FundooNotesModel , Long>{
	
	@Query(value = "SELECT * FROM fundoo_notes WHERE is_pinned = 1" , nativeQuery = true)
	List<FundooNotesModel> findByIsPinned();
	
	@Query(value = "SELECT * FROM fundoo_notes WHERE is_archived = 1" , nativeQuery = true)
	List<FundooNotesModel> findByIsArchived();
	
	@Query(value = "SELECT * FROM fundoo_notes WHERE is_trashed = 1" , nativeQuery = true)
	List<FundooNotesModel> findByIsTrashed();

	@Query(value = "SELECT co.id , co.email_address , co.user_id FROM collaborators as co JOIN collaborators_notes as cn ON co.id = cn.collaborators_id WHERE cn.notes_id = :noteId" ,nativeQuery = true)
	List<Object> findAllCollaboratorsById(long noteId);

}
