package com.web.spring.repository;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.spring.model.User;


@FeignClient(name="user-service")
@LoadBalancerClient(name="user-service")
public interface UserAuth {
	
	@PostMapping(path="/login",produces="application/json")
	public User login(@RequestBody User user) ;
	
	@PostMapping(path="/signup",produces="application/json")
	public User add(@RequestBody User user) ;
	


}
