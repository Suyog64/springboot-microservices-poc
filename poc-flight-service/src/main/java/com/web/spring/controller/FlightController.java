package com.web.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.model.Flight;
import com.web.spring.model.FlightPojo;
import com.web.spring.model.User;
import com.web.spring.repository.UserAuth;
import com.web.spring.service.FlightService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/flight")
public class FlightController {
	@Autowired
	private FlightService flightService;

	@Autowired
	private UserAuth ua;

	private static final String FAILURE_SERVICE = "FailureService";

	@GetMapping("/flights")
	public List<Flight> getAll() {
		return flightService.flights();
	}

	@GetMapping("/{id}")
	public Flight findById(@PathVariable int id) {
		return flightService.flightById(id);
	}

	/*
	 * @CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "delFlightBreak")
	 * 
	 * @DeleteMapping("/del") public boolean delById(@RequestBody Flight flight) {
	 * Flight newflight = flightService.flightById(flight.getFlightId()); if
	 * (newflight == null) { return false; } else {
	 * flightService.delete(flight.getFlightId()); return true; } }
	 */

	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "delFlightBreak")
	@DeleteMapping("/del")
	public boolean delById(@RequestBody FlightPojo flight) {
		User u = new User();
		u.setUserName(flight.getUserName());
		u.setPassword(flight.getPassword());
		User usr = ua.login(u);
		if (usr.getUserName() != null) {
			if (usr.getPassword().equals(flight.getPassword())) {

				if (usr.getRole().equalsIgnoreCase("admin")) {

					Flight newflight = flightService.flightById(flight.getFlightId());
					if (newflight == null) {
						return false;
					} else {
						flightService.delete(flight.getFlightId());
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/*
	 * @CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "updateFlightBreak")
	 * 
	 * @PutMapping("/update") public String update(@RequestBody Flight flight) {
	 * Flight newFlight1 = new Flight(flight.getFlightName(), flight.getSource(),
	 * flight.getDestination(), flight.getDOJ(), flight.getNoOfAvailableSeats(),
	 * flight.getTicketPrice(), flight.getFclass());
	 * newFlight1.setFlightId(flight.getFlightId());
	 * flightService.update(newFlight1); return "Update Successfully.."; }
	 * 
	 * @CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "addFlightBreak")
	 * 
	 * @PostMapping("/add") public String add(@RequestBody Flight flight) { Flight
	 * newFlight1 = new Flight(flight.getFlightName(), flight.getSource(),
	 * flight.getDestination(), flight.getDOJ(), flight.getNoOfAvailableSeats(),
	 * flight.getTicketPrice(), flight.getFclass()); flightService.add(newFlight1);
	 * return "Flight Successfully Added."; }
	 * 
	 * @PutMapping("/updateFromTicket") public String updateFromTicket(@RequestBody
	 * Flight flight) {
	 * 
	 * Flight newFlight1 = new Flight(flight.getFlightName(), flight.getSource(),
	 * flight.getDestination(), flight.getDOJ(), flight.getNoOfAvailableSeats(),
	 * flight.getTicketPrice(), flight.getFclass());
	 * newFlight1.setFlightId(flight.getFlightId());
	 * flightService.update(newFlight1); return "Update Successfully.."; }
	 */

	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "updateFlightBreak")
	@PutMapping("/update")
	public String update(@RequestBody FlightPojo flight) {
		User u = new User();
		u.setUserName(flight.getUserName());
		u.setPassword(flight.getPassword());
		User usr = ua.login(u);
		if (usr.getUserName() != null) {
			if (usr.getPassword().equals(flight.getPassword())) {

				if (usr.getRole().equalsIgnoreCase("admin")) {
					Flight newFlight1 = new Flight(flight.getFlightName(), flight.getSource(), flight.getDestination(),
							flight.getDOJ(), flight.getNoOfAvailableSeats(), flight.getTicketPrice(),
							flight.getFclass());
					newFlight1.setFlightId(flight.getFlightId());
					flightService.update(newFlight1);
					return "Update Successfully..";
				} else {
					return "In valid Authority";
				}
			} else {
				return "In Correct Password..";
			}
		} else {
			return "In Valid User Right";
		}
	}

	@PutMapping("/updateFromTicket")
	public String updateFromTicket(@RequestBody FlightPojo flight) {

		Flight newFlight1 = new Flight(flight.getFlightName(), flight.getSource(), flight.getDestination(),
				flight.getDOJ(), flight.getNoOfAvailableSeats(), flight.getTicketPrice(), flight.getFclass());
		newFlight1.setFlightId(flight.getFlightId());
		flightService.update(newFlight1);
		return "Update Successfully..";
	}

	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "addFlightBreak")
	@PostMapping("/add")
	public String add(@RequestBody FlightPojo flight) {
		User u = new User();
		u.setUserName(flight.getUserName());
		u.setPassword(flight.getPassword());
		User usr = ua.login(u);
		if (usr.getUserName() != null) {
			if (usr.getPassword().equals(flight.getPassword())) {
				if (usr.getRole().equalsIgnoreCase("admin")) {

					Flight newFlight1 = new Flight(flight.getFlightName(), flight.getSource(), flight.getDestination(),
							flight.getDOJ(), flight.getNoOfAvailableSeats(), flight.getTicketPrice(),
							flight.getFclass());
					flightService.add(newFlight1);
					return "Flight Successfully Added.";
				} else {
					return "In valid Authority";
				}
			} else {
				return "In Correct Password..";
			}
		} else {
			return "Invalid User";
		}
	}

	@CircuitBreaker(name = FAILURE_SERVICE, fallbackMethod = "createFlightBreak")
	@PostMapping("/createaccount")
	public User createAccount(@RequestBody User user) {
		user.setRole("admin");
		User u = ua.add(user);
		return u;
	}

	public String updateFlightBreak(Exception e) {

		return "Exception Occurred..";
	}

	public String addFlightBreak(Exception e) {

		return "Exception Occurred..";
	}
	public User createFlightBreak(Exception e) {	
		return new User(0, null, null, null);
	}
	
	public boolean delFlightBreak(Exception e) {
		return false;
	}
}
