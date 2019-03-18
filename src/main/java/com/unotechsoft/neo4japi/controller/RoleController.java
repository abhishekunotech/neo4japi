package com.unotechsoft.neo4japi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Role;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.services.CompanyService;
import com.unotechsoft.neo4japi.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@GetMapping("/findByRolename")
	public Role findByRolename(@RequestParam(value = "limit",required = false) Integer limit, @RequestParam(value = "rolename", required = true) String rolename) {
		return roleService.findByRolename(rolename);
	}

    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return roleService.graph(limit == null ? 100 : limit);
	}
  
    @GetMapping("/graphByRoleName")
	public Map<String, Object> graphByRoleName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return roleService.graphByRoleName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphByRoleId")
	public Map<String, Object> graphByRoleId(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String id) {
		return roleService.graphByRoleId(limit == null ? 100 : limit, id);
	}
    
    @GetMapping("/graphGetRolemappings")
    public Map<String, Object> graphGetRolemappings(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return roleService.graphGetRolemappings(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetRolemanagements")
    public Map<String, Object> graphGetRolemanagements(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return roleService.graphGetRolemanagements(limit == null ? 100 : limit, name);
    }
    
    @PostMapping(path="/create",consumes="application/json",produces="application/json")
    public void addMember(@RequestBody Role role) {
    	roleService.createOrUpdate(role.getRolename(),role.getRoleid(),role.getDescription(), role.getManagedsysid());
    }
}
