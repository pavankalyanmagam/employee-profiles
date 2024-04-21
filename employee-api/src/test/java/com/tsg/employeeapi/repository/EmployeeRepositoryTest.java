package com.tsg.employeeapi.repository;


import com.tsg.employeeapi.domain.Employee;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;



}
