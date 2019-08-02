package com.softplan.jpm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softplan.jpm.controller.PersonController;
import com.softplan.jpm.service.PersonService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {PersonController.class, PersonService.class})
@TestPropertySource(locations="classpath:test.properties")
public class PersonTests {

	private MockMvc mockMvc;

	@Autowired
	private PersonController personController;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
	}

	@Test
	public void contextLoads() {
	}

	

	//	@Test
	//	public void testGETIndexGetPerson() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/person/")).andExpect(MockMvcResultMatchers.status().isOk());
	//	}

}
