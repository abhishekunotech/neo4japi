package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@RelationshipEntity(type = "IS_STAFF_OF")
public class StaffMapping {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private User user;
	
	@EndNode
	@JsonBackReference
	private User user_staff;
	
	public StaffMapping() {
	}
	
	public StaffMapping(User user, User user_staff) {
		this.user = user;
		this.user_staff = user_staff;
	}
	
	public Long getId() {
	    return id;
	}

	public User getUser() {
	    return user;
	}

	public User getStaff() {
	    return user_staff;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "staffmapping");
		return returnVal;
	}
}
