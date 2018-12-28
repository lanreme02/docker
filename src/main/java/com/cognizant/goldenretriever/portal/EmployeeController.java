package com.cognizant.goldenretriever.portal;


import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/")
@Api(value="cvp", description="Cognizant Visitor Portal")
final class EmployeeController{

    @Autowired
    EmployeeService employeeService;


    @ApiOperation(value = "User Check-In", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Checked-In"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    } )
    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@RequestBody Employee employee) throws Exception {
        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            return new ResponseEntity<>("Phone Number and employee id missing", HttpStatus.BAD_REQUEST);
        }

        String badgeId = employeeService.checkin(employee);

        return new ResponseEntity(badgeId,HttpStatus.OK);
    }

    @ApiOperation(value = "User Check-Out", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Checked-Out"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    } )
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Employee employee) throws Exception{

        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            return new ResponseEntity<>("Phone Number and employee id missing", HttpStatus.BAD_REQUEST);
        }

        if(!employeeService.employeeRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()){
            return new ResponseEntity<>("Wrong Employee Id",HttpStatus.BAD_REQUEST);
        }

        employeeService.checkout(employee);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public Iterable<VisitorPortal> getAllVisitors(){
        return employeeService.visitorPortalRepository.findAll();
    }
}