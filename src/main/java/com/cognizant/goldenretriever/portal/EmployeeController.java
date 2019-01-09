package com.cognizant.goldenretriever.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/")
final class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private VisitorPortalRepository visitorPortalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @PostMapping("/checkin")
    public String checkin(@RequestBody Employee employee) {

        if (employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number and employee id missing");
        }
        //employeeService.checkin(employee);



        return employeeService.checkin(employee);
        //return "result";
    }

    @PostMapping("/checkout")
    public Employee checkout(@RequestBody Employee employee) {

        if (employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number and employee id missing");
        }

        if (!employeeRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Employee Id");
        }

        return employeeService.checkout(employee);
    }

    @GetMapping
    public Iterable<VisitorPortal> getAllVisitors() {
        return visitorPortalRepository.findAll();
    }
}