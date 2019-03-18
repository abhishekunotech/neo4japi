package com.unotechsoft.neo4japi.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@NodeEntity
public class ManagedSystem {

	@GraphId
	private Long id;
	private String managedsysid;
	private String name;
	private String status;
	public Long getId() {
		return id;
	}
	public String getManagedsysid() {
		return managedsysid;
	}
	public String getName() {
		return name;
	}
	public String getStatus() {
		return status;
	}
	
	@JsonIgnoreProperties("managedsystem")
	@JsonManagedReference
	@Relationship(type = "PRESENT_IN", direction=Relationship.INCOMING)
	private List<ManagedSystemMapping> managedsystemmappings = new ArrayList<>();

	@JsonIgnoreProperties("managedsystem")
	@JsonManagedReference
	@Relationship(type = "MANAGES", direction = Relationship.OUTGOING)
	private List<RoleManaging> rolemanagements;
	
	@JsonIgnoreProperties("managedsystem")
	@JsonManagedReference
	@Relationship(type = "MANAGES", direction = Relationship.OUTGOING)
	private List<GroupManaging> groupmanagements;
	
	public List<ManagedSystemMapping> getManagedsystemmappings() {
		return managedsystemmappings;
	}
	
	public List<RoleManaging> getRolemanagements(){
		return rolemanagements;
	}
	
	public List<GroupManaging> getGroupmanagements(){
		return groupmanagements;
	}
	
	
	public void addGroupmanagement(GroupManaging groupmanagement) {
		if (this.groupmanagements == null) {
			this.groupmanagements = new ArrayList<>();
		}
		this.groupmanagements.add(groupmanagement);
	}
	
	
	public void addRolemanagement(RoleManaging rolemanagement) {
		if (this.rolemanagements == null) {
			this.rolemanagements = new ArrayList<>();
		}
		this.rolemanagements.add(rolemanagement);
	}
	
	public ManagedSystem() {
	}

	/*
	private String managedsysid;
	private String name;
	private String status;
	 */
	public ManagedSystem(String name, String status, String managedsysid) {
		this.name = name;
		this.status = status;
		this.managedsysid = managedsysid;
	}
	
	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		
		returnVal.put("name", this.name);
		returnVal.put("status", this.status);
		returnVal.put("managedsysid", this.managedsysid);
		
		return returnVal;
	}
}
