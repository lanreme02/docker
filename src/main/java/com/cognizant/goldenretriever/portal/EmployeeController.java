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
        if(employee == null || employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            return new ResponseEntity<>("Phone Number and employee id missing", HttpStatus.BAD_REQUEST);
        }

        String badgeId = employeeService.checkin(employee);

        return new ResponseEntity(badgeId,HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Employee employee) {

        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty() || employee ==null ) {
            return new ResponseEntity<>("Phone Number and employee id missing", HttpStatus.BAD_REQUEST);
        }

        if(!employeeService.employeeRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()){
            return new ResponseEntity<>("Wrong Employee Id",HttpStatus.BAD_REQUEST);
        }

        employeeService.checkout(employee);
        return new ResponseEntity(HttpStatus.OK);
    }
}