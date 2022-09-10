package com.web.spring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.web.spring.model.Flight;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class FlightRepositoryTest {

	@Autowired
	private FlightRepository flightrepo;

	private Flight flight;

	@BeforeEach
	public void setup() {
		flight = new Flight("AA", "LAX", "JFK", "05SEP", 100, 500, "K");
		flightrepo.save(flight);
	}

	@AfterEach
	public void destroy() {
		flight = null;
	}

//	@Test
//	public void givenFlightsList_whenFindAll_thenFlightList() {
//		Flight flight1 = new Flight("LA", "JFK", "LAX", "05SEP", 100, 500, "L");
//		flightrepo.save(flight1);
//		List<Flight> fList = flightrepo.findAll();
//		assertThat(fList).size().isGreaterThan(0);
//	}

	@Test
	public void givenFlightObject_whenSave_thenReturnSavedFlight() {
		Flight flight1 = new Flight("LA", "JFK", "LAX", "05SEP", 100, 500, "L");
		Flight f = flightrepo.save(flight1);
		assertThat(f).isNotNull();
	}

	@Test
	public void givenFlightObject_whenFindById_thenReturnFlightObject() {
		// when - action or the behaviour that we are going test
		Flight flDB = flightrepo.findById(flight.getFlightId()).get();

		// then - verify the output
		assertThat(flDB).isNotNull();
	}
	@Test
    public void givenFlightObject_whenUpdateFlight_thenReturnUpdatedFlight(){
		 // when -  action or the behaviour that we are going test
        Flight savedFlight = flightrepo.findById(flight.getFlightId()).get();
        savedFlight.setDestination("DEL");
        savedFlight.setFclass("M");
        Flight updatedFlight =  flightrepo.save(savedFlight);

        // then - verify the output
        assertThat(updatedFlight.getDestination()).isEqualTo("DEL");
        assertThat(updatedFlight.getFclass()).isEqualTo("M");
    }
	
	 @Test
	 public void givenFlightObject_whenDelete_thenRemoveFlight(){
		// when -  action or the behaviour that we are going test
	        flightrepo.deleteById(flight.getFlightId());
	        Optional<Flight> flightOptional = flightrepo.findById(flight.getFlightId());

	        // then - verify the output
	        assertThat(flightOptional).isEmpty();
	 }
}
