package com.web.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.spring.model.User;
import com.web.spring.repository.UserRepo;


@Service
public class UserService {

	@Autowired
	private UserRepo repo;
	
	public void addUser(User user) {
		
		repo.save(user);
		System.out.println("User Successfully Added......"+user);
	}

	
	public boolean login(User user) {
		String username = user.getUserName();
		List<User> users = repo.findByUserName(username);
		boolean status=false ;
		
		if (users == null) {
			status = false;
			
		} else {
			for (User u : users) {
				if (u.getPassword().equals(user.getPassword())) {
					status = true;
				}
			}
		}
		return status;
	}

}
