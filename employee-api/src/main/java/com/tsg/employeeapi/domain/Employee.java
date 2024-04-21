package com.tsg.employeeapi.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("employee")
public class Employee {

    private String id;
    private String name;
    private String email;
    private String title;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;
}
