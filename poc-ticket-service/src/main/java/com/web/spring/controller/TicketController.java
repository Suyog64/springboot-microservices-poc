package com.web.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.model.Flight;
import com.web.spring.model.Ticket;
import com.web.spring.model.TicketPojo;
import com.web.spring.model.UpdatePojo;
import com.web.spring.model.User;
import com.web.spring.repository.FlightRepo;
import com.web.spring.repository.UserAuth;
import com.web.spring.service.TicketService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	@Autowired
	private TicketService ts;

	@Autowired
	private FlightRepo fs;
	
	@Autowired
	private UserAuth us;
	
	private static final String FAILURE_SERVICE = "FailureService";
	
	@GetMapping("tickets")
	public List<Ticket> getTickets() {
		return ts.Tickets();
	}
	
	@GetMapping("ticketbyuser/{user}")
	public List<Ticket> getTicketsByUser(@PathVariable("user") String t){
		return ts.findTicketsbyUser(t);
	}
	
	@GetMapping("ticket/{id}")
	public Ticket getTicketById(@PathVariable("id") int id) {
		return ts.TicketById(id);
	}
	@GetMapping("flight/flights") 
	public List<Flight> allFlights() { 
		return fs.getAll(); 
	}
	
	/*
	 * @PostMapping("/addticket") public TicketPojo addTicket(@RequestBody Ticket t)
	 * { TicketPojo newTicket = null; Flight f; f = fs.findById(t.getFlightId()); if
	 * (f.getNoOfAvailableSeats() >= t.getNumOfPas()) {
	 * 
	 * if (t.getDOJ().equals(f.getDOJ())) { newTicket = new TicketPojo(t.getDOJ(),
	 * t.getFlightId(), t.getNumOfPas(), t.getNumOfPas() * f.getTicketPrice(),
	 * "Confirmed"); Ticket newTicket1 = new Ticket(t.getDOJ(), t.getFlightId(),
	 * t.getNumOfPas(), t.getNumOfPas() * f.getTicketPrice(), "Confirmed"); Ticket
	 * f1 = ts.add(newTicket1); newTicket.setTicketId(f1.getTicketId()); int
	 * newAvailable = f.getNoOfAvailableSeats() - t.getNumOfPas();
	 * f.setNoOfAvailableSeats(newAvailable); fs.updateFromTicket(f); } else {
	 * newTicket = new TicketPojo(); newTicket.setStatus("Not Available"); }
	 * 
	 * } else { newTicket = new TicketPojo(); newTicket.setStatus("Not Available");
	 * } return newTicket; }
	 * 
	 * @GetMapping("flight/{id}") public Flight fdetails(@PathVariable("id") int id)
	 * { return fs.findById(id); }
	 * 
	 * @GetMapping("flight/flights") public List<Flight> allFlights() { return
	 * fs.getAll(); }
	 * 
	 * @PostMapping("cancel/{id}") public ResponseEntity<Boolean>
	 * cancelTicket(@PathVariable("id") int id){ Ticket t = ts.TicketById(id); if (t
	 * == null) {
	 * 
	 * return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST); } else {
	 * 
	 * t.setStatus("cancelled"); ts.add(t); Flight f; f =
	 * fs.findById(t.getFlightId());
	 * f.setNoOfAvailableSeats(f.getNoOfAvailableSeats() + t.getNumOfPas());
	 * fs.update(f); return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
	 * }
	 * 
	 * }
	 */	
	
	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "addTicketBreak")
	@PostMapping("addticket")
	public TicketPojo addTicket(@RequestBody Ticket t) {

		TicketPojo newTicket = null;

		User u = new User();
		u.setUserName(t.getUserName());
		u.setPassword(t.getPassword());

		User res = us.login(u);

		if (res.getUserName() != null) {
			if (!res.getUserName().equals(null)) {

				if (res.getRole().equalsIgnoreCase("user")) {
					Flight f;
					f = fs.findById(t.getFlightId());
					if (f.getNoOfAvailableSeats() >= t.getNumOfPas()) {

						if (t.getDOJ().equals(f.getDOJ())) {
							newTicket = new TicketPojo(t.getDOJ(), t.getFlightId(), t.getNumOfPas(),
									t.getNumOfPas() * f.getTicketPrice(), "Confirmed");
							Ticket newTicket1 = new Ticket(t.getDOJ(), t.getFlightId(), t.getNumOfPas(),
									t.getNumOfPas() * f.getTicketPrice(), "Confirmed",t.getUserName(),t.getPassword());
							Ticket f1 = ts.add(newTicket1);
							newTicket.setTicketId(f1.getTicketId());
							int newAvailable = f.getNoOfAvailableSeats() - t.getNumOfPas();
							f.setNoOfAvailableSeats(newAvailable);
							fs.updateFromTicket(f);
						} else {
							newTicket = new TicketPojo();
							newTicket.setStatus("Not Available");
						}

					} else {
						newTicket = new TicketPojo();
						newTicket.setStatus("Not Available");
					}

				} else {
					newTicket = new TicketPojo();
					newTicket.setStatus("Invalid Authority");

				}
			} else {
				newTicket = new TicketPojo();
				newTicket.setStatus("In Valid User..");
			}
		} else {
			newTicket = new TicketPojo();
			newTicket.setStatus("In Valid User..");
		}

		return newTicket;
	}
	@GetMapping("flight/{id}")
	public Flight fdetails(@PathVariable("id") int id) {
		return fs.findById(id);
	}

	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "deleteBreak")
	@DeleteMapping("delete")
	public ResponseEntity<Boolean> delete(@RequestBody UpdatePojo pojo) {
		Ticket t = ts.TicketById(pojo.getId());

		if (t == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
		User u = new User();
		u.setUserName(pojo.getUserName());
		u.setPassword(pojo.getPassword());

		User res = us.login(u);

		if (res.getUserName() != null) {
			ts.delete(pojo.getId());
			return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "cancelBreak")
	@PostMapping("/cancel")
	public ResponseEntity<Boolean> cancelTicket(@RequestBody UpdatePojo pojo) {

		Ticket t = ts.TicketById(pojo.getId());

		if (t == null) {

			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {

			User u = new User();
			u.setUserName(pojo.getUserName());
			u.setPassword(pojo.getPassword());

			User res = us.login(u);

			if (res.getUserName() != null) {
				t.setStatus("cancelled");
				ts.add(t);
				Flight f;
				f = fs.findById(t.getFlightId());
				System.out.println(f.getNoOfAvailableSeats());
				f.setNoOfAvailableSeats(f.getNoOfAvailableSeats() + t.getNumOfPas());
				fs.updateFromTicket(f);
				System.out.println(f.getNoOfAvailableSeats());
				return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
			}

		}
	}

	@PostMapping("/createaccount")
	public User createAccount(@RequestBody User user) {
		user.setRole("user");
		User u = us.add(user);
		return u;
	}
	
	public TicketPojo addTicketBreak(Exception e) {
		TicketPojo pojo = new TicketPojo();
		pojo.setStatus("Exception..");
		return pojo;
	}

	public ResponseEntity<Boolean> deleteBreak(Exception e) {
		return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Boolean> cancelBreak(Exception e) {
		return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
	}

}
