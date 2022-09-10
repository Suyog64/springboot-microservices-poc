package com.web.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ticketId;
	private String DOJ;
	private int flightId;
	private int numOfPas;
	private double totalFare;
	private String status;
	private String userName;
	private String password;
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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


	

	public Ticket(String dOJ, int flightId, int numOfPas, double totalFare, String status, String userName,
			String password) {
		super();
		DOJ = dOJ;
		this.flightId = flightId;
		this.numOfPas = numOfPas;
		this.totalFare = totalFare;
		this.status = status;
		this.userName = userName;
		this.password = password;
	}

	public Ticket() {
		super();
	}

	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", DOJ=" + DOJ + ", flightId=" + flightId + ", numOfPas=" + numOfPas
				+ ", totalFare=" + totalFare + "]";
	}
}
