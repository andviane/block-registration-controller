package com.smartvalor.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessEntityRepository extends JpaRepository<BusinessEntity, UUID> {

    BusinessEntity findById(UUID id);
}
