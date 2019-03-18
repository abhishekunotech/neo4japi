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

/**
 * @author Abhishek Kulkarni
 */
@NodeEntity
public class User {

	@GraphId
	private Long id;
	private String title;
	private String classification;
	private String employeeid;
	private String firstname;
	private String principal;
	private String lastname;
	private String loginid;
	private String status;
	private String typeid;
	private String userid;

	@JsonIgnoreProperties("user")
	@JsonManagedReference
	@Relationship(type = "WORKS_IN", direction = Relationship.OUTGOING)
	private List<Affiliation> affiliations;

	@JsonIgnoreProperties("user")
	@JsonManagedReference
	@Relationship(type = "BELONGS_TO", direction = Relationship.OUTGOING)
	private List<Grouping> groupings;

	@JsonIgnoreProperties("user")
	@JsonManagedReference
	@Relationship(type = "BELONGS_TO", direction = Relationship.OUTGOING)
	private List<RoleMapping> rolemappings;
	
	
	@JsonIgnoreProperties("user")
	@JsonManagedReference
	@Relationship(type = "PRESENT_IN", direction = Relationship.OUTGOING)
	private List<ManagedSystemMapping> managedsystemmappings;
	
	
	//Code to be boo-bood
	@JsonIgnoreProperties("user")
	@JsonManagedReference
	@Relationship(type = "IS_STAFF_OF", direction=Relationship.OUTGOING)
	private List<StaffMapping> staffmappings = new ArrayList<>();
	
	@JsonIgnoreProperties("user")
	@JsonManagedReference
	@Relationship(type = "IS_ENTITLED_TO", direction=Relationship.OUTGOING)
	private List<Entitlement> entitlements = new ArrayList<>();
	
	
	public List<StaffMapping> getStaffmappings() {
		return staffmappings;
	}
	
	// End of code to be boo-bood
	
	public User() {
	}
	//String classification, String employeeid, String firstname, String lastname, String loginid, String status, String title, String typeid, String userid
	public User(String principal, String firstname, String lastname, String loginid, String typeid, String title, String classification, String employeeid, String userid, String status) {
		this.classification = classification;
		this.employeeid = employeeid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.loginid = loginid;
		this.status = status;
		this.typeid = typeid;
		this.userid = userid;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public String getPrincipal() {
		return principal;
	}
	
	public String getTitle() {
		return title;
	}


	public List<Affiliation> getAffiliations() {
		return affiliations;
	}
	
	public List<Grouping> getGroupings() {
		return groupings;
	}

	public List<RoleMapping> getRolemappings() {
		return rolemappings;
	}
	
	public List<ManagedSystemMapping> getManagedsystemmappings() {
		return managedsystemmappings;
	}
	
	public List<Entitlement> getEntitlements(){
		return entitlements;
	}
	
	public String getClassification() {
		return classification;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getLoginid() {
		return loginid;
	}

	public String getStatus() {
		return status;
	}

	public String getTypeid() {
		return typeid;
	}

	public String getUserid() {
		return userid;
	}

	public void addGrouping(Grouping grouping) {
		if (this.groupings == null) {
			this.groupings = new ArrayList<>();
		}
		this.groupings.add(grouping);
	}
	
	public void addAffiliation(Affiliation affiliation) {
		if (this.affiliations == null) {
			this.affiliations = new ArrayList<>();
		}
		this.affiliations.add(affiliation);
	}
	
	public void addRolemapping(RoleMapping rolemapping) {
		if (this.rolemappings == null) {
			this.rolemappings = new ArrayList<>();
		}
		this.rolemappings.add(rolemapping);
	}
	
	public void addManagedsystemmapping(ManagedSystemMapping managedsystemmapping) {
		if (this.managedsystemmappings == null) {
			this.managedsystemmappings = new ArrayList<>();
		}
		this.managedsystemmappings.add(managedsystemmapping);
	}
	
	public void addStaffmapping(StaffMapping staffmapping) {
		if(this.staffmappings == null) {
			this.staffmappings = new ArrayList<>();
		}
		this.staffmappings.add(staffmapping);
	}
	
	public void addEntitlement(Entitlement entitlement) {
		if(this.entitlements == null) {
			this.entitlements = new ArrayList<>();
		}
		this.entitlements.add(entitlement);
	}
	
	/*
	 * private String title; private String classification; private String
	 * employeeid; private String firstname; private String lastname; private String
	 * loginid; private String status; private String typeid; private String userid;
	 */
	
	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("title", this.title);
		returnVal.put("classification", this.classification);
		returnVal.put("employeeid", this.employeeid);
		returnVal.put("firstname", this.firstname);
		returnVal.put("lastname", this.lastname);
		returnVal.put("loginid", this.loginid);
		returnVal.put("status", this.status);
		returnVal.put("typeid", this.typeid);
		returnVal.put("userid", this.userid);
		returnVal.put("principal", this.principal);
		return returnVal;
	}
}