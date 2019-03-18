package com.unotechsoft.neo4japi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Resource;
import com.unotechsoft.neo4japi.domain.Role;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.services.CompanyService;
import com.unotechsoft.neo4japi.services.ResourceService;
import com.unotechsoft.neo4japi.services.RoleService;

@RestController
@RequestMapping("/resource")
public class ResourceController {
	private final ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	@GetMapping("/findByResourcename")
	public Resource findByResourcename(@RequestParam(value = "limit",required = false) Integer limit, @RequestParam(value = "rolename", required = true) String rolename) {
		return resourceService.findByResourcename(rolename);
	}

    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return resourceService.graph(limit == null ? 100 : limit);
	}
  
    @GetMapping("/graphByResourceName")
	public Map<String, Object> graphByResourceName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return resourceService.graphByResourceName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphByResourceId")
	public Map<String, Object> graphByResourceId(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String id) {
		return resourceService.graphByResourceId(limit == null ? 100 : limit, id);
	}
    
    @GetMapping("/graphGetEntitlements")
    public Map<String, Object> graphGetEntitlements(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return resourceService.graphGetEntitlements(limit == null ? 100 : limit, name);
    }
    
    @PostMapping(path="/create",consumes="application/json",produces="application/json")
    public void addMember(@RequestBody Resource resource) {
    	resourceService.createOrUpdate(resource.getResourcename(),resource.getResourceid(),resource.getResourcetypeid(), resource.getResourceurl());
    }
}
