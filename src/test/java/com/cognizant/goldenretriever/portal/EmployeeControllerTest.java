package com.cognizant.goldenretriever.portal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @MockBean
    private EmployeeRepository repository;

    @MockBean
    private VisitorPortalRepository visitorPortalRepository;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private EmployeeController employeeController;


    @Test
    public void postCheckinDataReturnsBadgeNumber() {

        Employee employee = new Employee("123456", "1234456789");
        when(employeeService.checkin(employee)).thenReturn("789775");

        final String actual = employeeController.checkin(employee);

        //Assert
        assertThat(actual, is("789775"));
        verify(employeeService).checkin(employee);
    }

    @Test
    public void postCheckinDataMissingEmployeeIdReturnsException() {

        Employee employee = new Employee("", "1234456789");
        when(employeeService.checkin(employee)).thenReturn("789775");

        //Assert
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Phone Number and employee id missing");
        employeeController.checkin(employee);

    }

    @Test
    public void postCheckinDataMissingPhoneNumberIdReturnsException() {
        Employee employee = new Employee("12463", "");
        when(employeeService.checkin(employee)).thenReturn("789775");

        //Assert
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Phone Number and employee id missing");
        employeeController.checkin(employee);
    }

    @Test
    public void postCheckoutDataMissingPhoneNumberIdReturnsException() {
        Employee employee = new Employee("12463", "");
        when(employeeService.checkout(employee)).thenReturn(employee);

        //Assert
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Phone Number and employee id missing");
        employeeController.checkout(employee);
    }

    @Test
    public void postCheckoutDataMissingEmployeeIdReturnsException() {

        Employee employee = new Employee("", "1234456789");
        when(employeeService.checkout(employee)).thenReturn(employee);

        //Assert
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Phone Number and employee id missing");
        employeeController.checkout(employee);

    }

    @Test
    public void postCheckoutDataReturnsWrongEmployeeId() {

        Employee employee = new Employee("123457", "1234456789");
        when(employeeService.checkout(employee)).thenReturn(employee);

        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Wrong Employee Id");
        employeeController.checkout(employee);
    }

    @Test
    public void postCheckoutDataReturnsEmployee() {

        Employee employee = new Employee("123457", "1234456789");
        when(employeeService.checkin(employee)).thenReturn("789776");
        when(employeeService.checkout(employee)).thenReturn(employee);
        Optional<Employee> newEmployee = Optional.of(employee);
        when(repository.findByEmployeeId("123457")).thenReturn(newEmployee);

        employeeController.checkin(employee);
        Employee actualEmployee = employeeController.checkout(employee);

        //Assert
        assertThat(actualEmployee, is(employee));
        verify(employeeService).checkout(employee);
    }

    @Test
    public void getAllVisitorTestReturnsEmpty() {
        when(visitorPortalRepository.findAll()).thenReturn(Collections.emptyList());

        Iterable<VisitorPortal> actualVisitors = employeeController.getAllVisitors();

        //Assert
        assertThat(actualVisitors, is(Collections.EMPTY_LIST));
    }

}
