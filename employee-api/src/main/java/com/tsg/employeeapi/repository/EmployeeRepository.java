package com.tsg.employeeapi.repository;

import com.tsg.employeeapi.domain.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

   Optional<Employee> findById(String id);

   Optional<Employee> findByEmail(String email);
}
