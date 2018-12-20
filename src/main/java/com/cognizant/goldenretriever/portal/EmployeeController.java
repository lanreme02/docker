package com.cognizant.goldenretriever.portal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
final class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@RequestBody Employee employee) {
        Exception e = new Exception("Phone Number and employee id missing");

        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        employeeService.checkin(employee);

        return new ResponseEntity(employee,HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Employee employee) {
        Exception e = new Exception("Phone Number and employee id missing");

        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        employeeService.checkout(employee);
        return new ResponseEntity(HttpStatus.OK);
    }
}