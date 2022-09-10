package com.web.spring.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import com.web.spring.model.User;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepoTest {
	@Autowired
	private UserRepo repo;
	
	private User user;
	
	@BeforeEach
	public void setup() {
		user = new User(1, "Suyog", "456", "user");
		//repo.save(user);
	}
	@AfterEach
	public void destroy() {
		user = null;
	}
	@Test
	public void findUserByUsernameTest() {
		repo.save(user);
		List<User> ul = repo.findByUserName("Suyog");
		Assertions.assertThat(ul).size().isGreaterThan(0);
	}
}
