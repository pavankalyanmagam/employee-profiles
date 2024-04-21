package com.tsg.employeeapi.service;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.tsg.employeeapi.domain.Employee;
import com.tsg.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository repository;

    @Mock
    SequenceGenerator sequenceGenerator;

    @Mock
    private BiFunction<String, MultipartFile, String> photoFunction;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        // Arrange
        int page = 0;
        int size = 5;
        Page<Employee> expectedPage = mock(Page.class);

        when(repository.findAll(PageRequest.of(page, size, Sort.by("name")))).thenReturn(expectedPage);

        Page<Employee> result = employeeService.getAllEmployees(page, size);

        // Assert
        assertSame(expectedPage, result);
        verify(repository).findAll(PageRequest.of(page, size, Sort.by("name")));
    }
    @Test
    void testGetEmployeeById() {

        String id = "1";

        Employee expectedEmployee = Employee.builder()
                .id("1")
                .name("Employee")
                .title("Developer")
                .address("Dallas,TX")
                .phone("999-888-9999")
                .email("employee@gmail.com")
                .photoUrl("localhost:9090/photo.png")
                .status("EAD")
                .build();
        when(repository.findById("1")).thenReturn(Optional.ofNullable(expectedEmployee));

        Employee result = employeeService.getEmployee(id);

        assertSame(expectedEmployee, result);
    }
    @Test
    void testGetEmployeeNotFound() {

        String id = "1";
        when(repository.findById(id)).thenReturn(Optional.empty());


        Exception exception = assertThrows(RuntimeException.class, () -> employeeService.getEmployee(id));
        assertEquals("Employee not found", exception.getMessage());
    }
    @Test
    void testUpdateExistingEmployee() {

        Employee existingEmployee = Employee.builder()
                .id("1")
                .name("Employee")
                .email("employee@gmail.com")
                .title("Developer")
                .phone("999-888-9999")
                .address("Dallas,TX")
                .status("EAD")
                .photoUrl("localhost:9090/photo.png")
                .build();

        Employee updatedInfo = Employee.builder()
                .email("employee@gmail.com")
                .name("New Name")
                .build();

        when(repository.findByEmail("employee@gmail.com")).thenReturn(Optional.of(existingEmployee));
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.createOrUpdateEmployee(updatedInfo);

        verify(repository).save(any(Employee.class));
        assertEquals("New Name", result.getName());
    }

    @Test
    void testCreateNewEmployee() {

        Employee newEmployee = new Employee();
        newEmployee.setEmail("new@gmail.com");

        when(repository.findByEmail("new@gmail.com")).thenReturn(Optional.empty());
        when(repository.save(any(Employee.class))).thenReturn(newEmployee);
        when(sequenceGenerator.generateNextId()).thenReturn("2");

        Employee result = employeeService.createOrUpdateEmployee(newEmployee);


        verify(repository).save(newEmployee);
        assertEquals("2", newEmployee.getId());
    }


    @Test
    void testUploadPhotoFailure() {
        // Arrange
        String id = "1";
        MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", "image/jpeg", "photo data".getBytes());

        when(repository.findById(id)).thenReturn(Optional.of(new Employee()));
        doThrow(new RuntimeException("Unable to save image")).when(photoFunction).apply(id, file);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> employeeService.uploadPhoto(id, file), "Expected to throw, but did not");
    }

    @Test
    void testUploadPhotoEmployeeNotFound() {
        // Arrange
        String id = "1";
        MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", "image/jpeg", new byte[0]);
        when(repository.findById(id)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> employeeService.uploadPhoto(id, file));
    }


    @Test
    void testFileExtensionWithExtension() {
        // Arrange
        String filename = "example.jpeg";

        // Act
        String extension = employeeService.fileExtension.apply(filename);

        // Assert
        assertEquals(".jpeg", extension);
    }

    @Test
    void testFileExtensionWithoutExtension() {
        // Arrange
        String filename = "example";

        // Act
        String extension = employeeService.fileExtension.apply(filename);

        // Assert
        assertEquals(".png", extension);
    }


    @Test
    void testPhotoFunctionThrowsException() {
        // Arrange
        String id = "123";
        MockMultipartFile file = new MockMultipartFile("file", "testfile.txt", "text/plain", new byte[0]);

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy((Path) any(), any(), any())).thenThrow(new IOException());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> employeeService.photoFunction.apply(id, file), "Unable to save image");
        }
    }


}
