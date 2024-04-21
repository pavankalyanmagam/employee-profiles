package com.tsg.employeeapi.controller;


import com.tsg.employeeapi.domain.Employee;
import com.tsg.employeeapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.tsg.employeeapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.created(URI.create("/employees/userID")).body(employeeService.createOrUpdateEmployee(employee));
    }


    @GetMapping
    public ResponseEntity<Page<Employee>> getEmployees(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(employeeService.getAllEmployees(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(employeeService.getEmployee(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(employeeService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE} )
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

}
