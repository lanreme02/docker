package com.cognizant.goldenretriever.portal;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/")
@Api(value="CVP", description="Cognizant Visitor Portal")
final class EmployeeController{

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private VisitorPortalRepository visitorPortalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @ApiOperation(value = "User Check-In", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Checked-In"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    } )
    @PostMapping("/checkin")
    public String checkin(@RequestBody Employee employee){
        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number and employee id missing");
        }

        return employeeService.checkin(employee);
    }

    @ApiOperation(value = "User Check-Out", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Checked-Out"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    } )
    @PostMapping("/checkout")
    public Employee checkout(@RequestBody Employee employee) {

        if(employee.getPhoneNumber().isEmpty() || employee.getEmployeeId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number and employee id missing");
        }

        if(!employeeRepository.findByEmployeeId(employee.getEmployeeId()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Employee Id");
        }

        return employeeService.checkout(employee);
    }

    @GetMapping
    public Iterable<VisitorPortal> getAllVisitors(){
        return visitorPortalRepository.findAll();
    }
}