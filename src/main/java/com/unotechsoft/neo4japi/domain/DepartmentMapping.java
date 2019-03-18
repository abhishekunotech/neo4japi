package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@RelationshipEntity(type = "IS_A_DEPARTMENT_OF")
public class DepartmentMapping {
	@GraphId
	private Long id;
	
	@StartNode
	@JsonBackReference
	private Company company;
	
	@EndNode
	@JsonBackReference
	private Company department;
	
	public DepartmentMapping() {
	}
	
	public DepartmentMapping(Company company, Company department) {
		this.company = company;
		this.department = department;
	}
	
	public Long getId() {
	    return id;
	}

	public Company getCompany() {
	    return company;
	}

	public Company getDepartment() {
	    return department;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "departmentmapping");
		return returnVal;
	}
}
