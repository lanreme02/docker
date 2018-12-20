package com.cognizant.goldenretriever.portal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface EmployeeRepository extends CrudRepository<Employee,Long> {

    Optional<Employee> findByEmployeeId(String employeeId);
}
