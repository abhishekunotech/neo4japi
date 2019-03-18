package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@RelationshipEntity(type = "MANAGES")
public class RoleManaging {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private ManagedSystem managedsystem;

	@EndNode
	@JsonBackReference
	private Role role;
	
	public RoleManaging() {
	}
	
	public RoleManaging(ManagedSystem managedsystem, Role role) {
		this.managedsystem = managedsystem;
		this.role = role;
	}
	
	public Long getId() {
	    return id;
	}

	public ManagedSystem getManagedsystem() {
	    return managedsystem;
	}

	public Role getRole() {
	    return role;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "rolemanagement");
		return returnVal;
	}
}
