package com.web.spring.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.web.spring.model.User;
import com.web.spring.repository.UserRepo;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepo repo;
	@InjectMocks
	private UserService service;
	
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
	public void addUserTest() {
		given(repo.save(user)).willReturn(user);
		service.addUser(user);
		verify(repo, times(1)).save(user);
	}
	@Test
	public void test_ifSuccesslogin() {
		given(repo.findByUserName("Suyog")).willReturn(List.of(user));
		boolean actual = service.login(user);
		assertEquals(true, actual);
	}
	@Test
	public void test_ifNotSuccesslogin() {
		User newUser = new User(2, "", "s", "user");
//		repo.save(newUser);
//		//newUser.setPassword("t");
		//List<User> newUser = repo.findByUserName("");
		given(repo.findByUserName("")).willReturn(Collections.emptyList());
		boolean actual = service.login(newUser);
		assertEquals(false, actual);
	}

}
