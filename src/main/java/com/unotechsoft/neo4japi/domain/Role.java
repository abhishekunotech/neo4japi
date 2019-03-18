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
public class Role {

	@GraphId
	private Long id;
	private String roleid;
	private String rolename;
	private String managedsysid;
	private String description;
	public Long getId() {
		return id;
	}
	public String getRoleid() {
		return roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public String getManagedsysid() {
		return managedsysid;
	}
	public String getDescription() {
		return description;
	}
	
	@JsonIgnoreProperties("role")
	@JsonManagedReference
	@Relationship(type = "BELONGS_TO", direction=Relationship.INCOMING)
	private List<RoleMapping> rolemappings = new ArrayList<>();

	@JsonIgnoreProperties("role")
	@JsonManagedReference
	@Relationship(type = "MANAGES", direction=Relationship.INCOMING)
	private List<RoleManaging> rolemanagements = new ArrayList<>();
	
	public List<RoleMapping> getRolemappings() {
		return rolemappings;
	}
	
	public List<RoleManaging> getRolemanagements(){
		return rolemanagements;
	}
	
	public Role() {
	}

	/*
	 * private String grpid; private String grpname; private String managedsysid;
	 * private String adgrptype; private String typeid;
	 */
	public Role(String roleid, String rolename, String managedsysid, String description) {
		this.roleid = roleid;
		this.rolename = rolename;
		this.managedsysid = managedsysid;
		this.description = description;
	}
	
	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		
		returnVal.put("roleid", this.roleid);
		returnVal.put("rolename", this.rolename);
		returnVal.put("description", this.description);
		returnVal.put("managedsysid", this.managedsysid);
		
		return returnVal;
	}
}
