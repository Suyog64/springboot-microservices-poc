package com.web.spring.repository;

import java.util.List;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.spring.model.Flight;

@FeignClient(name="flight-service",url = "localhost:8089")
@LoadBalancerClient(name="flight-service")
public interface FlightRepo {
	
	@GetMapping(path="/flight/{id}",produces="application/json")
	Flight findById(@PathVariable("id") int id);
	
	@GetMapping(path="/flight/flights",produces="application/json")
	List<Flight> getAll();
	
	@PutMapping(path="/flight/updateFromTicket",produces="application/json")
	public String updateFromTicket(@RequestBody Flight flight);

}
