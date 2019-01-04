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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private VisitorPortalRepository visitorPortalRepository;

    @Before
    public void beforeEach() {
        repository.deleteAll();
    }

    @After
    public void afterEach() {
        repository.deleteAll();
    }


    @Test
    public void postCheckinTwiceReturnsValueInRepository() throws Exception {

        Employee employee = new Employee("123458", "1234456789");

        String employeeJson = MAPPER.writeValueAsString(employee);

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

        Iterable<VisitorPortal> visitors = visitorPortalRepository.findAll();
        long size = visitors.spliterator().getExactSizeIfKnown();

        //Assert the employeeRepository
        assertThat(size, is(2L));
    }

    @Test
    public void CheckoutDataReturnsValueInRepository() throws Exception {

        Employee employee = new Employee("123457", "1234456789");

        String employeeJson = MAPPER.writeValueAsString(employee);

        mvc.perform(post("/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        String content = mvc.perform(post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Optional<Employee> actualEmployee = repository.findByEmployeeId("123457");

        //Assert the employeeRepository
        assertThat(actualEmployee.get(), is(employee));

    }



}
