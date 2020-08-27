package com.dell.poc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dell.poc.controller.EmployeeController;

@SpringBootTest
class CrudOperationsApplicationTests {

	@Autowired
	private EmployeeController controller;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

}
