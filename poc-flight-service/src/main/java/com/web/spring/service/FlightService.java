package com.web.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.spring.model.Flight;
import com.web.spring.repository.FlightRepository;

@Service
public class FlightService {
	@Autowired
	private FlightRepository fr;

	public Flight add(Flight flight)
	{
		fr.save(flight);
		System.out.println("Flight Details Saved:"+flight);
		return flight;
		
	}
	
	public List<Flight> flights()
	{
		return fr.findAll();
	}
	
	public Flight flightById(int id)
	{
		return fr.findById(id).get();
	}
	
	public void delete(int id)
	{
		fr.deleteById(id);
	}
	
	public Flight update(Flight f)
	{
		System.out.println("Flight="+f);
		
		fr.save(f);
		//return fr.findById(f.getFlightId()).get();
		return f;
	}


}
