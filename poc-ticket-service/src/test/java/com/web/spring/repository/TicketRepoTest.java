package com.web.spring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.web.spring.model.Flight;
import com.web.spring.model.Ticket;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TicketRepoTest {
	@Autowired
	private TicketRepo repo;
	
	private Ticket ticket;
	
	@BeforeEach
	public void setup() {
		ticket = new Ticket("05SEp", 100, 20, 210, "Confirmed", "test", "test");
		repo.save(ticket);
				
	}
	@AfterEach
	public void destroy() {
		ticket = null;
	}


	@Test
	public void givenFlightsList_whenFindAll_thenFlightList() {
		Ticket ticket1 = new Ticket("05SEP", 100, 20, 210, "Confirmed", "test", "test");
		repo.save(ticket1);
		List<Ticket> fList = repo.findAll();
		assertThat(fList).size().isGreaterThan(0);
	}

	@Test
	public void givenFlightObject_whenSave_thenReturnSavedFlight() {
		Ticket ticket1 = new Ticket("05SEP", 100, 20, 210, "Confirmed", "test", "test");
		Ticket f = repo.save(ticket1);
		assertThat(f).isNotNull();
	}

	@Test
	public void givenFlightObject_whenFindById_thenReturnFlightObject() {
		// when - action or the behaviour that we are going test
		Ticket flDB = repo.findById(ticket.getTicketId()).get();

		// then - verify the output
		assertThat(flDB).isNotNull();
	}
	
	@Test
	public void givenFlightObject_whenFindByUser_thenReturnFlightObject() {
		// when - action or the behaviour that we are going test
		List<Ticket> flDB = repo.findByUser(ticket.getUserName());

		// then - verify the output
		assertThat(flDB).isNotNull();
	}

	/*
	 * @Test public void
	 * givenFlightObject_whenUpdateCancelFlight_thenReturnUpdatedFlight(){ // when -
	 * action or the behaviour that we are going test Ticket savedTicket =
	 * repo.findById(ticket.getTicketId()).get();
	 * repo.updateCancel(savedTicket.getTicketId());;
	 * 
	 * // then - verify the output
	 * Assertions.assertThat(savedTicket.getStatus()).isEqualTo("cancelled"); }
	 */
	@Test
    public void givenFlightObject_whenUpdateFlight_thenReturnUpdatedFlight(){
		 // when -  action or the behaviour that we are going test
        Ticket savedTicket = repo.findById(ticket.getTicketId()).get();
        savedTicket.setNumOfPas(12);;
        savedTicket.setStatus("Cancelled");;
        Ticket updatedTicket =  repo.save(savedTicket);

        // then - verify the output
        assertThat(updatedTicket.getNumOfPas()).isEqualTo(12);
        assertThat(updatedTicket.getStatus()).isEqualTo("Cancelled");
    }
	
	 @Test
	 public void givenFlightObject_whenDelete_thenRemoveFlight(){
		// when -  action or the behaviour that we are going test
	        repo.deleteById(ticket.getTicketId());
	        Optional<Ticket> flightOptional = repo.findById(ticket.getTicketId());

	        // then - verify the output
	        assertThat(flightOptional).isEmpty();
	 }

}
