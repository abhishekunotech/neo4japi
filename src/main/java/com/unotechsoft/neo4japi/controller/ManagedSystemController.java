package com.unotechsoft.neo4japi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.ManagedSystem;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.services.CompanyService;
import com.unotechsoft.neo4japi.services.ManagedSystemService;

@RestController
@RequestMapping("/mngdsystem")
public class ManagedSystemController {
	private final ManagedSystemService managedSystemService;

	public ManagedSystemController(ManagedSystemService managedSystemService) {
		this.managedSystemService = managedSystemService;
	}
	
	@GetMapping("/findByName")
	public ManagedSystem findByName(@RequestParam(value = "limit",required = false) Integer limit, @RequestParam(value = "name", required = true) String name) {
		return managedSystemService.findByName(name);
	}

    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return managedSystemService.graph(limit == null ? 100 : limit);
	}
  
    @GetMapping("/graphByName")
	public Map<String, Object> graphByName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return managedSystemService.graphByName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphGetManagedsystemmapping")
    public Map<String, Object> graphGetManagedsystemmappings(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return managedSystemService.graphGetManagedsystemmappings(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetRolemanagements")
    public Map<String, Object> graphGetRolemanagements(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return managedSystemService.graphGetRolemanagements(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetGroupmanagements")
    public Map<String, Object> graphGetGroupmanagements(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return managedSystemService.graphGetGroupmanagements(limit == null ? 100 : limit, name);
    }
    
    @PostMapping(path="/create",consumes="application/json",produces="application/json")
    public void addMember(@RequestBody ManagedSystem mngdsystem) {
    	managedSystemService.createOrUpdate(mngdsystem.getManagedsysid(), mngdsystem.getName(), mngdsystem.getStatus());
    }
}
