package com.web.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.model.User;
import com.web.spring.repository.UserRepo;
import com.web.spring.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo repo;

	@PostMapping("/signup")
	public User add(@RequestBody User user) {
		userService.addUser(user);
		return user;
	}

	@PostMapping("/login")
	public User login(@RequestBody User user) {
		boolean res = userService.login(user);
		if (res) {
			List<User> u = repo.findByUserName(user.getUserName());
			User usr = new User();
			for (User uu : u) {
				usr = uu;
				break;
			}
			return usr;
		} else {
			return new User();
		}
	}
}
