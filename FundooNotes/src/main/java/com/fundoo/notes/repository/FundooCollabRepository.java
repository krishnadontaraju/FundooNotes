package com.fundoo.notes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundoo.notes.model.FundooNoteCollaborator;

public interface FundooCollabRepository extends JpaRepository<FundooNoteCollaborator ,Long>{

	Optional<FundooNoteCollaborator> findByEmailAddress(String emailId);

}
