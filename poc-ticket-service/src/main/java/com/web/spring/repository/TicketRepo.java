package com.web.spring.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.spring.model.Ticket;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Integer>{

	
	@Transactional
	@Query(value = "update ticket set status = 'cancelled' where ticket_id=?1",nativeQuery = true)
	void updateCancel(int id);
	
	@Transactional
	@Query(value = "select * from ticket where user_name=?1",nativeQuery = true)
	List<Ticket> findByUser(String name);
}
