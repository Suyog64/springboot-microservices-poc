package com.web.spring.model;

public class Flight {
	int flightId;
	String flightName;
	String source;
	String destination;
	String DOJ;
	int NoOfAvailableSeats;
	int ticketPrice;
	String Fclass;
	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	public String getFlightName() {
		return flightName;
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDOJ() {
		return DOJ;
	}
	public void setDOJ(String dOJ) {
		DOJ = dOJ;
	}
	public int getNoOfAvailableSeats() {
		return NoOfAvailableSeats;
	}
	public void setNoOfAvailableSeats(int noOfAvailableSeats) {
		NoOfAvailableSeats = noOfAvailableSeats;
	}
	public int getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public String getFclass() {
		return Fclass;
	}
	public void setFclass(String fclass) {
		Fclass = fclass;
	}
	public Flight(int flightId, String flightName, String source, String destination, String dOJ,
			int noOfAvailableSeats, int ticketPrice, String fclass) {
		super();
		this.flightId = flightId;
		this.flightName = flightName;
		this.source = source;
		this.destination = destination;
		DOJ = dOJ;
		NoOfAvailableSeats = noOfAvailableSeats;
		this.ticketPrice = ticketPrice;
		Fclass = fclass;
	}
	public Flight() {
		super();
	}
	@Override
	public String toString() {
		return "Flight [flightId=" + flightId + ", flightName=" + flightName + ", source=" + source + ", destination="
				+ destination + ", DOJ=" + DOJ + ", NoOfAvailableSeats=" + NoOfAvailableSeats + ", ticketPrice="
				+ ticketPrice + ", Fclass=" + Fclass + "]";
	}

}
