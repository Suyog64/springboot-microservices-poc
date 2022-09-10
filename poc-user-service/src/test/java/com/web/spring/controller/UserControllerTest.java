package com.web.spring.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.spring.model.User;
import com.web.spring.service.UserService;
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc  mockMvc;
	@MockBean
	private UserService service;
	@Autowired
	private ObjectMapper objectMapper;
	private User user;
	
	@BeforeEach
	public void setup() {
		user = new User(1, "Suyog", "456", "user");
		
		//UserController uc = webApplicationContext.getBean(UserController.class);
		
		//repo.save(user);
	}
	@AfterEach
	public void destroy() {
		user = null;
	}
//	@Test
//	public void signupTest() throws  Exception {
//		
//		ResultActions response = mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(user)));
//		response.andDo(print()).andExpect(status().isOk())
//		.andReturn();
//	}

}
