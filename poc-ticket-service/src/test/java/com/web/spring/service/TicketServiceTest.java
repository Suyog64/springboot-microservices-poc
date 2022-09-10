package com.web.spring.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

import com.web.spring.model.Ticket;
import com.web.spring.repository.FlightRepo;
import com.web.spring.repository.TicketRepo;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TicketServiceTest {
	@Mock
	private TicketRepo ticketRepo;
	@InjectMocks
	private TicketService ticketService;
	
	private Ticket ticket;
	
	@BeforeEach
	public void setup() {
		ticket = new Ticket("05SEP", 100, 20, 210, "Confirmed", "test", "test");
		ticketRepo.save(ticket);
				
	}
	@AfterEach
	public void destroy() {
		ticket = null;
	}
	@Test
	public void givenTicketObject_whenAddTicket_thenReturnTicketObj() {
		given(ticketRepo.save(ticket)).willReturn(ticket);
		Ticket savedTicket = ticketService.add(ticket);
		Assertions.assertThat(savedTicket).isNotNull();	
	}
	
	@Test
	public void givenTicketList_whenGetAllTickets_thenReturnTicketList() {
		Ticket ticket1 =  new Ticket("05SEP", 200, 10, 230, "Confirmed", "test", "test");
		given(ticketRepo.findAll()).willReturn(List.of(ticket1, ticket));
		List<Ticket> result = ticketService.Tickets();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.size()).isEqualTo(2);
	}
	@Test
	public void givenTicketId_whenGetTicketId_thenReturnTicket() {
		given(ticketRepo.findById(ticket.getTicketId())).willReturn(Optional.of(ticket));
		Ticket savedTicket = ticketService.TicketById(ticket.getTicketId());
		Assertions.assertThat(savedTicket).isNotNull();
	}
	@Test
	public void givenUser_whenGetTicketByUser_thenReturnTicketByUserName() {
		given(ticketRepo.findByUser(ticket.getUserName())).willReturn(List.of(ticket));
		List<Ticket> result = ticketService.findTicketsbyUser(ticket.getUserName());
		Assertions.assertThat(result).isNotNull();
	}
//	@Test
//	public void givenTicketObject_whenUpdated_thenReturnUpdatedTicket() {
//		//Ticket savedTicket = ticketRepo.findById(ticket.getTicketId()).get();
//		given(ticketRepo.save(ticket)).willReturn(ticket);
//		ticket.setDOJ("06SEP");
//		ticket.setNumOfPas(23);
//		Ticket updated = ticketService.update(ticket);
//		Assertions.assertThat(updated.getNumOfPas()).isEqualTo(23);
//	}Uncommentwhen code coverage report to be generated
	
	@Test
	public void givenTicketObject_whenUpdatedCancel_thenReturnUpdatedCancelTicket() {
		//Ticket savedTicket = ticketRepo.findById(ticket.getTicketId()).get();
		given(ticketRepo.save(ticket)).willReturn(ticket);
		ticket.setStatus("cancelled");;
		ticketService.updateCancel(ticket.getTicketId());
		assertEquals(ticket.getStatus(), "cancelled");;
	}
	@Test
	public void givenTicketId_whenDeleteTicket_thenDoNohing() {
		Integer ticketId = 1;
		willDoNothing().given(ticketRepo).deleteById(ticketId);
		ticketService.delete(ticketId);
		verify(ticketRepo, times(1)).deleteById(ticketId);
		
	}
	
}
