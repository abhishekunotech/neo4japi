package com.unotechsoft.neo4japi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Group;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.services.CompanyService;
import com.unotechsoft.neo4japi.services.GroupService;

@RestController
@RequestMapping("/group")
public class GroupController {
	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}
	
	@GetMapping("/findByGroupname")
	public Group findByGroupname(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "groupname", required = true) String groupname) {
		return groupService.findByGrpname(groupname);
	}

    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return groupService.graph(limit == null ? 100 : limit);
	}
  
    @GetMapping("/graphByGroupName")
	public Map<String, Object> graphByGroupName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return groupService.graphByGroupName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphGetGroupings")
    public Map<String, Object> graphGetGroupings(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return groupService.graphGetGroupings(limit == null ? 100 : limit, name);
    }
    
    @GetMapping("/graphGetGroupManagements")
    public Map<String, Object> graphGetGroupManagements(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return groupService.graphGetGroupmanagements(limit == null ? 100 : limit, name);
    }
    
    @PostMapping(path="/create",consumes="application/json",produces="application/json")
    public void addMember(@RequestBody Group group) {
    	groupService.createOrUpdate(group.getGrpid(), group.getGrpname(), group.getManagedsysid(), group.getAdgrptype(),group.getTypeid());
    }
}
