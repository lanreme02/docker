package com.cognizant.goldenretriever.portal;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VisitorPortalRepository extends CrudRepository<VisitorPortal,Long> {
    Optional<VisitorPortal> findByEmployeeId(long employeeId);
}
