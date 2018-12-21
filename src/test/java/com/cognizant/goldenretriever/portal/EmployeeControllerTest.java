package com.cognizant.goldenretriever.portal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.loader.plan.build.spi.ExpandingCollectionQuerySpace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    EmployeeRepository repository;

    @Autowired
    EmployeeService employeeService;

    @Before
    public void beforeEach(){
        repository.deleteAll();
    }

    @After
    public void afterEach(){
        repository.deleteAll();
    }

    @Test
    public void postToCheckinReturnsFailWhenCredentialNotEntered() throws Exception {


        String actual = mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\" : \"\", \"phoneNumber\" : \"\" }"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        //Assert
        assertThat(actual, is("Phone Number and employee id missing"));
    }


    @Test
    public void postCheckinDataReturnsValueInRepository() throws Exception {


        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("123456","1234456789");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Employee actualEmployee = repository.findByEmployeeId("123456").get();
        String badgeId = employeeService.visitorPortalRepository.findByEmployeeId(actualEmployee.getId()).get().getBadgeId();

        //Assert the employeeRepository
        assertThat(content, is(badgeId));

    }


    @Test
    public void postCheckinTwiceReturnsValueInRepository() throws Exception {

        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123458")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("123458","1234456789");

        String employeeJson = mapper.writeValueAsString(employee);

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        when(mockBadgeService.getBadgeWithEmpId("123458")).thenReturn("789776");

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Iterable<VisitorPortal> visitors =  employeeService.visitorPortalRepository.findAll();
        long size = visitors.spliterator().getExactSizeIfKnown();


        //Assert the employeeRepository
        assertThat(size, is(2L));
    }


    @Test
    public void postToCheckoutReturnsFailWhenCredentialNotEntered() throws Exception {


        String actual = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\" : \"\", \"phoneNumber\" : \"\" }"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        //Assert
        assertThat(actual, is("Phone Number and employee id missing"));
    }


    @Test
    public void CheckoutDataReturnsValueInRepository() throws Exception {


        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123457")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("123457","1234456789");

        String employeeJson = mapper.writeValueAsString(employee);

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        employeeService.checkout(employee);

        when(mockBadgeService.returnBadge("789775")).thenReturn("");

        String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Optional<Employee> actualEmployee= repository.findByEmployeeId("123457");

        //Assert the employeeRepository
        assertThat(actualEmployee.get(), is(employee));

    }

    @Test
    public void CheckoutReturnsInvalidEmployeeId() throws Exception {


        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123457")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("123457","1234456789");

        String employeeJson = mapper.writeValueAsString(employee);

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        employeeService.checkout(employee);

        when(mockBadgeService.returnBadge("789775")).thenReturn("");


         employee = new Employee("123456","1234456789");
         employeeJson = mapper.writeValueAsString(employee);

         String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();


        //Assert the employeeRepository
        assertThat(content, is("Wrong Employee Id"));

    }

   @Test
    public void returnEmptyListOfVisitorsInPortal() throws Exception {

        //Exercise
        // Exercise
        final String actual = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert
        assertThat(actual, is("[]"));

    }

    @Test
    public void CheckinReturnMissingFieldWhenGivenEmptyPhoneNumber() throws Exception {
        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("123456","");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert the employeeRepository
        assertThat(content, is("Phone Number and employee id missing"));

    }

    @Test
    public void CheckinReturnMissingFieldWhenGivenEmployeeId() throws Exception {
        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("","1345734");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert the employeeRepository
        assertThat(content, is("Phone Number and employee id missing"));

    }

    @Test
    public void CheckinReturnMissingFieldWhenGivenEmployeeIdAndPhoneNumberAreMissing() throws Exception {
        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("","");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert the employeeRepository
        assertThat(content, is("Phone Number and employee id missing"));

    }


    @Test
    public void CheckoutReturnMissingFieldWhenGivenEmptyPhoneNumber() throws Exception {
        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("123456","");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert the employeeRepository
        assertThat(content, is("Phone Number and employee id missing"));

    }

    @Test
    public void CheckoutReturnMissingFieldWhenGivenEmployeeId() throws Exception {
        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("","1345734");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert the employeeRepository
        assertThat(content, is("Phone Number and employee id missing"));

    }

    @Test
    public void CheckoutReturnMissingFieldWhenGivenEmployeeIdAndPhoneNumberAreMissing() throws Exception {
        BadgeService mockBadgeService = mock(BadgeService.class);

        when(mockBadgeService.getBadgeWithEmpId("123456")).thenReturn("789775");

        employeeService.setBadgeService(mockBadgeService);

        Employee employee = new Employee("","");

        String employeeJson = mapper.writeValueAsString(employee);

        String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert the employeeRepository
        assertThat(content, is("Phone Number and employee id missing"));

    }






}
