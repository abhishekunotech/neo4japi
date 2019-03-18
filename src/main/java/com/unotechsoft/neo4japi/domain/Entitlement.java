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

@RelationshipEntity(type = "IS_ENTITLED_TO")
public class Entitlement {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private User user;

	@EndNode
	@JsonBackReference
	private Resource resource;
	
	public Entitlement() {
	}
	
	public Entitlement(User user, Resource resource) {
		this.user = user;
		this.resource = resource;
	}
	
	public Long getId() {
	    return id;
	}

	public User getUser() {
	    return user;
	}

	public Resource getResource() {
	    return resource;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "entitlement");
		return returnVal;
	}
}
