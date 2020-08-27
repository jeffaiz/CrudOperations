package com.dell.poc.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dell.poc.model.Employee;
import com.dell.poc.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import ch.qos.logback.core.net.ObjectWriter;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void should_GetAllEmployees_When_ValidRequest() throws Exception {
		String response = "[\r\n" + "    {\r\n" + "        \"id\": 10,\r\n" + "        \"firstName\": \"Faizal\",\r\n"
				+ "        \"lastName\": \"Khan Jaffry\",\r\n" + "        \"emailId\": \"jeffiaz72@gmail.com\"\r\n"
				+ "    },\r\n" + "    {\r\n" + "        \"id\": 11,\r\n" + "        \"firstName\": \"Alice\",\r\n"
				+ "        \"lastName\": \"Duke\",\r\n" + "        \"emailId\": \"xyz@gmail.com\"\r\n" + "    }\r\n"
				+ "]";
		Employee emp1 = new Employee();
		emp1.setId(10);
		emp1.setFirstName("Faizal");
		emp1.setLastName("Khan Jaffry");
		emp1.setEmailId("jeffiaz72@gmail.com");

		Employee emp2 = new Employee();
		emp2.setId(11);
		emp2.setFirstName("Alice");
		emp2.setLastName("Duke");
		emp2.setEmailId("xyz@gmail.com");

		List<Employee> empList = new ArrayList<>();
		empList.add(emp1);
		empList.add(emp2);

		when(employeeRepository.findAll()).thenReturn(empList);
		this.mockMvc.perform(get("/api/v1/employees")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(response));
	}

	@Test
	public void should_GetNoEmployee_When_NoEmployeePresent() throws Exception {

		List<Employee> empList = new ArrayList<>();
		String response = "[]";
		when(employeeRepository.findAll()).thenReturn(empList);
		this.mockMvc.perform(get("/api/v1/employees")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(response));
	}

	@Test
	public void should_GetEmployee_When_ValidRequest() throws Exception {
		Optional<Employee> emp1 = Optional.ofNullable(new Employee());
		emp1.ifPresent(emp -> {
			emp.setId(12);

			emp.setFirstName("Alice");
			emp.setLastName("Duke");
			emp.setEmailId("xyz@gmail.com");
		});

		when(employeeRepository.findById(12L)).thenReturn(emp1);
		mockMvc.perform(get("/api/v1/employees/12").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value(12))
				.andExpect(jsonPath("$.firstName").value("Alice")).andExpect(jsonPath("$.lastName").value("Duke"))
				.andExpect(jsonPath("$.emailId").value("xyz@gmail.com"));

	}

	@Test
	public void should_return404_When_EmployeeNotFound() throws Exception {
		Employee emp1 = new Employee();
		emp1.setId(12);
		emp1.setFirstName("Alice");
		emp1.setLastName("Duke");
		emp1.setEmailId("xyz@gmail.com");
		when(employeeRepository.findById(12L)).thenReturn(null);

		mockMvc.perform(get("/api/v1/employees/110").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}
}
