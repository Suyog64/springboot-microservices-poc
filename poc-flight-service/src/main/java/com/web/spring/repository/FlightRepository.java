package com.web.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.spring.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer>{

}
