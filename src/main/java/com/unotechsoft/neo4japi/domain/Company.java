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
 * @author Mark Angrish
 */
@NodeEntity
public class Company {
	
	@GraphId
	private Long id;
	private String alias;
	private String companyid;
	private String companyname;
	private String description;
	private String internalcompanyid;
	private String orgtypeid;

	public String getAlias() {
		return alias;
	}

	public String getCompanyid() {
		return companyid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public String getDescription() {
		return description;
	}

	public String getInternalcompanyid() {
		return internalcompanyid;
	}

	public String getOrgtypeid() {
		return orgtypeid;
	}

	//Code to be boo-bood
	@JsonIgnoreProperties("company")
	@JsonManagedReference
	@Relationship(type = "IS_A_DEPARTMENT_OF", direction=Relationship.OUTGOING)
	private List<DepartmentMapping> departmentmappings = new ArrayList<>();
	
	
	@JsonManagedReference
	@Relationship(type = "WORKS_IN", direction=Relationship.INCOMING)
	private List<Affiliation> affiliations = new ArrayList<>();

	
	public List<DepartmentMapping> getDepartmentmappings() {
		return departmentmappings;
	}
	
	
	public void addDepartmentmapping(DepartmentMapping departmentmapping) {
		if(this.departmentmappings == null) {
			this.departmentmappings = new ArrayList<>();
		}
		this.departmentmappings.add(departmentmapping);
	}
	
	public Company() {
	}

	public Company(String alias, String companyid, String companyname, String description, String internalcompanyid, String orgtypeid) {
		this.alias = alias;
		this.companyid = companyid;
		this.companyname = companyname;
		this.description = description;
		this.internalcompanyid = internalcompanyid;
		this.orgtypeid = orgtypeid;
	}

	public Long getId() {
		return id;
	}

	public List<Affiliation> getAffiliations() {
		return affiliations;
	}
	
	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		/*
		 * this.alias = alias; this.companyid = companyid; this.companyname =
		 * companyname; this.description = description; this.internalcompanyid =
		 * internalcompanyid; this.orgtypeid = orgtypeid;
		 */
		returnVal.put("alias", this.alias);
		returnVal.put("companyid", this.companyid);
		returnVal.put("companyname", this.companyname);
		returnVal.put("description", this.description);
		returnVal.put("orgtypeid", this.orgtypeid);
		returnVal.put("internalcompanyid", this.internalcompanyid);
		
		return returnVal;
	}
}