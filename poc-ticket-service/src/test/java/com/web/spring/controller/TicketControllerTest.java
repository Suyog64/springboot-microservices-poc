package com.web.spring.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.spring.model.Flight;
import com.web.spring.model.Ticket;
import com.web.spring.model.TicketPojo;
import com.web.spring.model.User;
import com.web.spring.repository.FlightRepo;
import com.web.spring.repository.UserAuth;
import com.web.spring.service.TicketService;
@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MockMvc  mockMvc;
	@Autowired
    private ObjectMapper objectMapper;
	@MockBean
	private TicketService service;
	@Mock
	private UserAuth ua;
	@Mock
	private FlightRepo fs;
	
	private Ticket ticket;
	private TicketPojo ticketPojo;
	
	@BeforeEach
	public void mockFeignClient() throws Exception {
		TicketController ts = webApplicationContext.getBean(TicketController.class);
		Field fieldUserApiClient = TicketController.class.getDeclaredField("us");
		fieldUserApiClient.setAccessible(true);
		fieldUserApiClient.set(ts, this.ua);
		
		TicketController ts1 = webApplicationContext.getBean(TicketController.class);
		Field fielFlightApiClient = TicketController.class.getDeclaredField("fs");
		fielFlightApiClient.setAccessible(true);
		fielFlightApiClient.set(ts1, this.fs);
		
		ticketPojo = new TicketPojo("22APR", 100, 2, 400.0, "Confirmed");
		ticket = new Ticket(ticketPojo.getDOJ(), ticketPojo.getFlightId(), ticketPojo.getNumOfPas(), ticketPojo.getTotalFare(), ticketPojo.getStatus(), "Suyog", "456");
		
	}
	@AfterEach
	public void destroy() {
		ticketPojo=null;
		ticket=null;
	}
	@Test
	public void getAllTicketsTest() throws Exception {
		List<Ticket> ticketList = new ArrayList<Ticket>();
		ticketList.add(ticket);
		given(service.Tickets()).willReturn(ticketList);
		ResultActions response = mockMvc.perform(get("/ticket/tickets"));
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(ticketList.size())));
	}
	@Test
	public void getAllTicketsByUserTest() throws Exception {
		User user = new User(1, "test", "test", "admin");
		ua.add(user);
		ticket.setUserName(user.getUserName());ticket.setPassword(user.getPassword());
		List<Ticket> ticketList = new ArrayList<Ticket>();
		ticketList.add(ticket);
		given(service.findTicketsbyUser("test")).willReturn(ticketList);
		ResultActions response = mockMvc.perform(get("/ticket/ticketbyuser/{user}","test"));
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(ticketList.size())));
	}
	@Test
	public void getAllFlights() throws Exception {
		Flight flight = new Flight(1, "AA", "LAX", "JFK", "05SEP", 100, 500, "K");
		List<Flight> fl = new ArrayList<Flight>();
		fl.add(flight);
		given(fs.getAll()).willReturn(fl);
		ResultActions response = mockMvc.perform(get("/ticket/flight/flights"));
		response.andExpect(status().isOk());
		//response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(fl.size())));
	}
//	@Test
//	public void findByFlightIdTest() throws Exception {
//		Flight fl = new Flight(1, "AA", "LAX", "JFK", "05SEP", 100, 500, "K");
//		Integer tid = 1;
//		given(fs.findById(tid)).willReturn(fl);
//		ResultActions response = mockMvc.perform(get("/ticket/flight/{id}",tid));
//		response.andExpect(status().isOk());
//	}
	@Test
	public void addTicketSuccesfulTest() throws Exception {
		given(service.add(any(Ticket.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/ticket/addticket").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticket)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"ticketId\":0,\"flightId\":100,\"numOfPas\":2,\"totalFare\":400.0,\"status\":\"Confirmed\"}"))
		.andReturn();
	}
	@Test
	public void addTicketInvalidAuthorityTest() throws Exception {
		ticket.setUserName("super");ticket.setPassword("admin");
		given(service.add(any(Ticket.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/ticket/addticket").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticket)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"ticketId\":0,\"flightId\":0,\"numOfPas\":0,\"totalFare\":0.0,\"status\":\"Invalid Authority\"}"))
		.andReturn();
	}
	@Test
	public void addTicketFlightNotAvailableTest() throws Exception {
		ticket.setDOJ("06SEP");;
		given(service.add(any(Ticket.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/ticket/addticket").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticket)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"ticketId\":0,\"flightId\":0,\"numOfPas\":0,\"totalFare\":0.0,\"status\":\"Not Available\"}"))
		.andReturn();
	}
	@Test
	public void addTicketSeatNotAvailableTest() throws Exception {
		ticket.setNumOfPas(100);;
		given(service.add(any(Ticket.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/ticket/addticket").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticket)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"ticketId\":0,\"flightId\":0,\"numOfPas\":0,\"totalFare\":0.0,\"status\":\"Not Available\"}"))
		.andReturn();
	}
	@Test
	public void addTicketInvalidUserTest() throws Exception {
		ticket.setUserName(null);;
		given(service.add(any(Ticket.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/ticket/addticket").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ticket)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"ticketId\":0,\"flightId\":0,\"numOfPas\":0,\"totalFare\":0.0,\"status\":\"In Valid User..\"}"))
		.andReturn();
	}
	
	@Test
	public void test_createaccount() throws Exception {
		User user = new User(1, "test", "test", "");
		given(ua.add(user)).willReturn(user);
		ResultActions response = mockMvc.perform(post("/ticket/createaccount").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)));
		response.andDo(print()).andExpect(status().isOk());
	}

	
}
