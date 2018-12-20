package com.cognizant.goldenretriever.portal;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        when(mockBadgeService.getBadge()).thenReturn("789775");

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

        Optional<Employee> actualEmployee = repository.findByEmployeeId("123456");

        //Assert the repository
        assertThat(actualEmployee.get(), is(employee));

    }


    //@Test
    public void postCheckinTwiceReturnsValueInRepository() throws Exception {

        Employee employee = new Employee("123458","1234456789");

        String employeeJson = mapper.writeValueAsString(employee);

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Iterable<VisitorPortal> visitors =  employeeService.visitorPortalRepository.findAll();
        long size = visitors.spliterator().getExactSizeIfKnown();


        //Assert the repository
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
    public void postCheckoutDataReturnsValueInRepository() throws Exception {

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

        String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Optional<Employee> actualEmployee= repository.findByEmployeeId("123457");

        //Assert the repository
        assertThat(actualEmployee.get(), is(employee));

    }
}
