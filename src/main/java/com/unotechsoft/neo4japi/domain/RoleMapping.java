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

@RelationshipEntity(type = "BELONGS_TO")
public class RoleMapping {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private User user;

	@EndNode
	@JsonBackReference
	private Role role;
	
	public RoleMapping() {
	}
	
	public RoleMapping(User user, Role role) {
		this.user = user;
		this.role = role;
	}
	
	public Long getId() {
	    return id;
	}

	public User getUser() {
	    return user;
	}

	public Role getRole() {
	    return role;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "rolemapping");
		return returnVal;
	}
}
