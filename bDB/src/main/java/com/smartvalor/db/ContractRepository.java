package com.smartvalor.db;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html
@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {

	// Find active (not deleted)
	List<Contract> findByDeleted(Boolean deleted);
	
	// Find new reviewed
	List<Contract> findByReviewed(Boolean reviewed);	

}
