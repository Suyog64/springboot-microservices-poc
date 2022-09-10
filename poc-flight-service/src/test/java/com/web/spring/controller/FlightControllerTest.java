package com.web.spring.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.spring.model.Flight;
import com.web.spring.model.FlightPojo;
import com.web.spring.model.User;
import com.web.spring.repository.UserAuth;
import com.web.spring.service.FlightService;

//@WebMvcTest(FlightController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FlightControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FlightService flightService;
	@Mock
	private UserAuth ua;
	@Autowired
	private ObjectMapper objectMapper;

	private FlightPojo flight;
	private Flight savFlight;

	@BeforeEach
	public void mockFeignClient() throws Exception {
		FlightController fs = webApplicationContext.getBean(FlightController.class);
		Field fieldUserApiClient = FlightController.class.getDeclaredField("ua");
		fieldUserApiClient.setAccessible(true);
		fieldUserApiClient.set(fs, this.ua);

		flight = new FlightPojo(1, "AA", "LAX", "JFK", "05SEP", 100, 500, "K", "test", "test");
		// flightRepo.save(flight);
		savFlight = new Flight(flight.getFlightName(), flight.getDestination(), flight.getSource(), flight.getDOJ(),
				flight.getNoOfAvailableSeats(), flight.getTicketPrice(), flight.getFclass());
		// flightService.add(savFlight);
	}
//	@BeforeEach
//	public void setup() {
//		User newUser = new User(1, "test", "test", "admin");
//		flight = new FlightPojo(1,"AA", "LAX", "JFK", "05SEP", 100, 500, "K", newUser.getUserName(), newUser.getPassword());
//		//flightRepo.save(flight);
//		savFlight = new Flight(flight.getFlightName(), flight.getDestination(), flight.getSource(), flight.getDOJ(),
//									flight.getNoOfAvailableSeats(), flight.getTicketPrice(), flight.getFclass());
//		flightService.add(savFlight);
//	}

	@AfterEach
	public void destroy() {
		flight = null;
		// flightService.delete(savFlight.getFlightId());
	}

	@Test
	public void givenFlight_whencreateFlight_thenReturnFlight() throws Exception {
		given(flightService.add(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/flight/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flight)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("Flight Successfully Added."))
				.andReturn();
	}

	@Test
	public void givenFlight_whencreateFlightInvalidUser_thenReturnInvalidUser() throws Exception {
		given(flightService.add(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
		FlightPojo flight1 = new FlightPojo(1, "AA", "LAX", "JFK", "05SEP", 100, 500, "K", "", "");
		ResultActions response = mockMvc.perform(post("/flight/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flight1)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("Invalid User")).andReturn();
	}

//	@Test
//	public void givenFlight_whencreateFlightIncorrectPassword_thenReturnIncorrectPass() throws Exception {
//		given(flightService.add(any(Flight.class))).willAnswer((invocation)-> invocation.getArgument(0));
//		FlightPojo flight1 = new FlightPojo(1,"AA", "LAX", "JFK", "05SEP", 100, 500, "K", "test", "t");
//		ResultActions response = mockMvc.perform(post("/flight/add")
//		            .contentType(MediaType.APPLICATION_JSON)
//		            .content(objectMapper.writeValueAsString(flight1)));
//		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("In Correct Password..")).andReturn();
//	}

	@Test
	public void givenFlight_whencreateFlightNotadmin_thenReturnInvalidAuthority() throws Exception {
		given(flightService.add(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
		FlightPojo flight1 = new FlightPojo(1, "AA", "LAX", "JFK", "05SEP", 100, 500, "K", "user", "123");
		ResultActions response = mockMvc.perform(post("/flight/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flight1)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("In valid Authority"))
				.andReturn();
	}

	@Test
	public void givenFlightList_whenGetAllFlights_thenReturnFlightList() throws Exception {
		// given - precondition or setup
		List<Flight> listFlights = new ArrayList<Flight>();
		listFlights.add(savFlight);
		given(flightService.flights()).willReturn(listFlights);
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/flight/flights"));
		// then - verify the output
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listFlights.size())));
	}

	@Test
	public void givenFlightId_whenGetFlightyId_thenReturnFlight() throws Exception {
		Integer flightId = 1;
		given(flightService.flightById(flightId)).willReturn(savFlight);
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/flight/{id}", flightId));
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.flightName", is("AA")))
				.andExpect(jsonPath("$.source", is("JFK"))).andExpect(jsonPath("$.destination", is("LAX")));

	}

	@Test
	public void givenFlight_whenUpdateFlight_thenReturnUpdateStatus() throws Exception {
		given(flightService.update(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
		FlightPojo flight1 = new FlightPojo(0, "LA", "LAX", "JFK", "05SEP", 200, 500, "K", "test", "test");
		ResultActions response = mockMvc.perform(put("/flight/update").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flight1)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("Update Successfully.."))
				.andReturn();
	}

	@Test
	public void givenFlight_whenUpdateFlightInvalidUser_thenReturnInvalidUser() throws Exception {
		given(flightService.update(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
		FlightPojo flight1 = new FlightPojo(0, "LA", "LAX", "JFK", "05SEP", 200, 500, "K", "", "");
		ResultActions response = mockMvc.perform(put("/flight/update").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flight1)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("In Valid User Right"))
				.andReturn();
	}

	@Test
	public void givenFlight_whenUpdateFlightNotAdmin_thenReturnInvalidAuthority() throws Exception {
		given(flightService.update(any(Flight.class))).willAnswer((invocation) -> invocation.getArgument(0));
		FlightPojo flight1 = new FlightPojo(0, "LA", "LAX", "JFK", "05SEP", 200, 500, "K", "user", "123");
		ResultActions response = mockMvc.perform(put("/flight/update").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flight1)));
		response.andDo(print()).andExpect(status().isOk()).andExpect(content().string("In valid Authority"))
				.andReturn();
	}

//	@Test
//	public void givenFlight_whenDeleteFlight_thenReturnTrue() throws Exception {
//
//		willDoNothing().given(flightService).delete(flight.getFlightId());
//		// when - action or the behaviour that we are going test
//		ResultActions response = mockMvc.perform(delete("/flight/del", flight).accept(MediaType.APPLICATION_JSON));
//		// then - verify the output
//		response.andExpect(status().isOk()).andExpect(content().string("true")).andReturn();
//
//	}

	@Test
	public void test_createaccount() throws Exception {
		User user = new User(1, "test", "test", "");
		given(ua.add(user)).willReturn(user);
		ResultActions response = mockMvc.perform(post("/flight/createaccount").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)));
		response.andDo(print()).andExpect(status().isOk());
	}
}
