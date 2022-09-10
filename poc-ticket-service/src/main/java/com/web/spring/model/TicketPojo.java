package com.web.spring.model;

public class TicketPojo {
	private int ticketId;
	private String DOJ;
	private int flightId;
	private int numOfPas;
	private double totalFare;
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public String getDOJ() {
		return DOJ;
	}

	public void setDOJ(String dOJ) {
		DOJ = dOJ;
	}

	public int getFlightId() {
		return flightId;
	}

	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}

	public int getNumOfPas() {
		return numOfPas;
	}

	public void setNumOfPas(int numOfPas) {
		this.numOfPas = numOfPas;
	}

	public double getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}


	public TicketPojo() {
		// TODO Auto-generated constructor stub
	}

	public TicketPojo(String dOJ, int flightId, int numOfPas, double totalFare, String status) {
		super();
		DOJ = dOJ;
		this.flightId = flightId;
		this.numOfPas = numOfPas;
		this.totalFare = totalFare;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", DOJ=" + DOJ + ", flightId=" + flightId + ", numOfPas=" + numOfPas
				+ ", totalFare=" + totalFare + "]";
	}
}
