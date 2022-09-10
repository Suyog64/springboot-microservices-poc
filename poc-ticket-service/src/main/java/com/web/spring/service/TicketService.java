package com.web.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.spring.model.Ticket;
import com.web.spring.repository.TicketRepo;

@Service
public class TicketService {

	@Autowired
	private TicketRepo tr;
	
	public Ticket add(Ticket f)
	{
		tr.save(f);
		return f;
	}
	
	public List<Ticket> Tickets()
	{
		return tr.findAll();
	}
	public List<Ticket> findTicketsbyUser(String t)
	{
		return tr.findByUser(t);
	}
	public Ticket TicketById(int id)
	{
		return tr.findById(id).get();
	}
	
	public void delete(int id)
	{
		tr.deleteById(id);
	}
	
	public Ticket update(Ticket f)
	{
		tr.save(f);
		return tr.findById(f.getTicketId()).get();
	}
	
	public void updateCancel(int id)
	{
		tr.updateCancel(id);
	}


	
}
