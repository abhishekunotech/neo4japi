package com.unotechsoft.neo4japi.domain;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.annotation.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "WORKS_IN")
public class Affiliation {

    @GraphId
	private Long id;

	@StartNode
	@JsonBackReference
	private User user;

	@EndNode
	@JsonBackReference
	private Company company;

	public Affiliation() {
	}

	public Affiliation(User user, Company company) {
		this.user = user;
		this.company = company;
	}

	public Long getId() {
	    return id;
	}


	public User getUser() {
	    return user;
	}

	public Company getCompany() {
	    return company;
	}

	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		returnVal.put("typename", "affiliation");
		return returnVal;
	}
    
}
