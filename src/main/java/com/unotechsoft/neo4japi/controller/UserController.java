package com.unotechsoft.neo4japi.controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/findByFirstname")
	public User findByFirstname(@RequestParam(value = "limit",required = false) Integer limit, @RequestParam(value = "firstname", required = true) String firstname) {
		return userService.findByFirstname(firstname);
	}
	
    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return userService.graph(limit == null ? 100 : limit);
	}
    
    @GetMapping("/graphByLastName")
	public Map<String, Object> graphByLastName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return userService.graphByLastName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphByFirstName")
	public Map<String, Object> graphByFirstName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return userService.graphByFirstName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphByUserId")
	public Map<String, Object> graphByUserId(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return userService.graphByUserId(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphByPrincipal")
    public Map<String, Object> graphByPrincipal(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return userService.graphByPrincipal(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetGroupings")
    public Map<String, Object> graphGetGroupings(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return userService.graphGetGroupings(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetRoles")
    public Map<String, Object> graphGetRoles(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return userService.graphGetRoles(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetManagedSystem")
    public Map<String, Object> graphGetManagedSystem(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return userService.graphGetManagedSystem(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetAffiliations")
    public Map<String, Object> graphGetAffiliations(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return userService.graphGetAffiliations(limit == null ? 100 : limit, name);
    }
    
    @PostMapping(path="/create",consumes="application/json",produces="application/json")
    public void addMember(@RequestBody User user) {
    	userService.createOrUpdate(user.getPrincipal(),user.getFirstname(), user.getLastname(), user.getLoginid(), user.getTypeid(), user.getTitle(), user.getClassification(), user.getEmployeeid(), user.getUserid(), user.getStatus());
    }
    
}