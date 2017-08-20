package com.smartvalor.db;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

	// Find active (not deleted, also can be used to list deleted only)
	List<Person> findByDeleted(Boolean deleted);
	
	// Find new reviewed or not reviewed.
	List<Person> findByReviewed(Boolean reviewed);

	
}
