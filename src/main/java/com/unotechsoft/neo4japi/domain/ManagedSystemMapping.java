package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@RelationshipEntity(type = "PRESENT_IN")
public class ManagedSystemMapping {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private User user;
	
	@EndNode
	@JsonBackReference
	private ManagedSystem managedsystem;
	
	public ManagedSystemMapping() {
	}
	
	public ManagedSystemMapping(User user, ManagedSystem managedsystem) {
		this.user = user;
		this.managedsystem = managedsystem;
	}
	
	public Long getId() {
	    return id;
	}

	public User getUser() {
	    return user;
	}

	public ManagedSystem getManagedsystem() {
	    return managedsystem;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "grouping");
		return returnVal;
	}
}
