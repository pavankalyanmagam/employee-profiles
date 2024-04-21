package com.tsg.employeeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsg.employeeapi.domain.Employee;
import com.tsg.employeeapi.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static com.tsg.employeeapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee("1", "John Doe", "john.doe@example.com", "Developer", "123-456-7890", "123 Street", "Active", "photo.jpg");
        when(employeeService.createOrUpdateEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(employee.getName()));
    }

    @Test
    public void testGetEmployees() throws Exception {
        List<Employee> list = Arrays.asList(new Employee("1", "John Doe", "john.doe@example.com", "Developer", "123-456-7890", "123 Street", "Active", null));
        Page<Employee> page = new PageImpl<>(list);
        when(employeeService.getAllEmployees(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/employees?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee("1", "John Doe", "john.doe@example.com", "Developer", "123-456-7890", "123 Street", "Active", null);
        when(employeeService.getEmployee("1")).thenReturn(employee);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testUploadPhoto() throws Exception {
        String id = "17";
        String photoUrl = "http://localhost/employees/image/test.jpg";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        when(employeeService.uploadPhoto(any(String.class), any(MultipartFile.class))).thenReturn(photoUrl);

        mockMvc.perform(multipart("/employees/photo")
                        .file(file)
                        .param("id", id)
                        .contentType(MediaType.MULTIPART_FORM_DATA)) // This might not be needed as MockMvc configures it
                .andExpect(status().isOk())
                .andExpect(content().string(photoUrl));
    }

    @Test
    public void getPhoto_ShouldReturnImageFile() throws Exception {

        String filename = "test.png";
        byte[] expectedContent = "image content".getBytes(StandardCharsets.UTF_8);
        Path path = Paths.get(PHOTO_DIRECTORY + filename);

        try (var mockedStaticFiles = mockStatic(Files.class)) {
            mockedStaticFiles.when(() -> Files.readAllBytes(path))
                    .thenReturn(expectedContent);

            mockMvc.perform(get("/employees/image/{filename}", filename))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG))
                    .andExpect(content().bytes(expectedContent));
        }
    }




}
