package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@RelationshipEntity(type = "MANAGES")
public class GroupManaging {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private ManagedSystem managedsystem;
	
	@EndNode
	@JsonBackReference
	private Group group;
	
	public GroupManaging() {
	}
	
	public GroupManaging(ManagedSystem managedsystem, Group group) {
		this.managedsystem = managedsystem;
		this.group = group;
	}
	
	public Long getId() {
	    return id;
	}

	public ManagedSystem getManagedsystem() {
	    return managedsystem;
	}

	public Group getGroup() {
	    return group;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "groupmanagement");
		return returnVal;
	}
}
