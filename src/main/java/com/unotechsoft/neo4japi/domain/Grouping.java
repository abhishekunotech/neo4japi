package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@RelationshipEntity(type = "BELONGS_TO")
public class Grouping {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private User user;
	
	@EndNode
	@JsonBackReference
	private Group group;
	
	public Grouping() {
	}
	
	public Grouping(User user, Group group) {
		this.user = user;
		this.group = group;
	}
	
	public Long getId() {
	    return id;
	}

	public User getUser() {
	    return user;
	}

	public Group getGroup() {
	    return group;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "grouping");
		return returnVal;
	}
}
