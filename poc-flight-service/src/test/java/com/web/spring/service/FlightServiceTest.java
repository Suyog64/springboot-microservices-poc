package com.web.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.web.spring.model.Flight;
import com.web.spring.repository.FlightRepository;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

	@Mock
	private FlightRepository flightRepo;

	@InjectMocks
	private FlightService flightService;

	private Flight flight;

	@BeforeEach
	public void setup() {
		flight = new Flight("AA", "LAX", "JFK", "05SEP", 100, 500, "K");
		flightRepo.save(flight);
	}

	@AfterEach
	public void destroy() {
		flight = null;
	}

	@Test
	public void givenFlightObject_whenSavFlight_thenReturnFlightObj() {
		// given(flightRepo.findById(flight.getFlightId())).willReturn(Optional.empty());
		given(flightRepo.save(flight)).willReturn(flight);

		// when - action or the behaviour that we are going test
		Flight savedFlight = flightService.add(flight);
		assertThat(savedFlight).isNotNull();
	}

	@Test
	public void givenFlightList_whenGetAllFlights_thenReturnFlightList() {
		Flight flight1 = new Flight("LA", "JFK", "LAX", "05SEP", 100, 500, "L");

		given(flightRepo.findAll()).willReturn(List.of(flight, flight1));

		// when - action or the behaviour that we are going test
		List<Flight> fList = flightService.flights();
		assertThat(fList).isNotNull();
		assertThat(fList.size()).isEqualTo(2);
	}

	@Test
	public void givenEmptyFlightList_whenGetAllFlights_thenReturnEmptyFlightList() {
		Flight flight1 = new Flight("LA", "JFK", "LAX", "05SEP", 100, 500, "L");

		given(flightRepo.findAll()).willReturn(Collections.emptyList());

		// when - action or the behaviour that we are going test
		List<Flight> fList = flightService.flights();
		assertThat(fList).isEmpty();
		assertThat(fList.size()).isEqualTo(0);
	}

	@Test
	public void givenFlightId_whenGetFlightyId_thenReturnFlight() {
		// given
		given(flightRepo.findById(flight.getFlightId())).willReturn(Optional.of(flight));

		// when - action or the behaviour that we are going test
		Flight savedflight = flightService.flightById(flight.getFlightId());
		assertThat(savedflight).isNotNull();
	}

	@Test
	public void givenFlightObject_whenUpdated_thenReturnUpdatedFlight() {
		// given
		given(flightRepo.save(flight)).willReturn(flight);
		flight.setDestination("DUB");
		flight.setTicketPrice(1499);
		System.out.println(flight);
		// when - action or the behaviour that we are going test
		Flight updatedflight = flightService.update(flight);
		assertThat(updatedflight.getDestination()).isEqualTo("DUB");
		assertThat(updatedflight.getTicketPrice()).isEqualTo(1499);
	}

	@Test
	public void givenFlightId_whenDeleteFlight_thenDoNohing() {
		Integer flightId = 1;
		willDoNothing().given(flightRepo).deleteById(flightId);

		flightService.delete(flightId);

		verify(flightRepo, times(1)).deleteById(flightId);
	}

}
