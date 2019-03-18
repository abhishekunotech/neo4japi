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
public class Resource {

	@GraphId
	private Long id;
	private String resourcename;
	private String resourceid;
	private String resourcetypeid;
	private String resourceurl;
	public Long getId() {
		return id;
	}
	
	public String getResourcename() {
		return resourcename;
	}
	
	public String getResourceid() {
		return resourceid;
	}

	public String getResourcetypeid() {
		return resourcetypeid;
	}

	public String getResourceurl() {
		return resourceurl;
	}

	@JsonIgnoreProperties("resource")
	@JsonManagedReference
	@Relationship(type = "IS_ENTITLED_TO", direction=Relationship.INCOMING)
	private List<Entitlement> entitlements = new ArrayList<>();

	public List<Entitlement> getEntitlements() {
		return entitlements;
	}
	
	public Resource() {
	}

	/*
	 * private String grpid; private String grpname; private String managedsysid;
	 * private String adgrptype; private String typeid;
	 */
	public Resource(String resourceid, String resourcename, String resourcetypeid, String resourceurl) {
		this.resourceid = resourceid;
		this.resourcename = resourcename;
		this.resourcetypeid = resourcetypeid;
		this.resourceurl = resourceurl;
	}
	
	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		
		returnVal.put("resourceid", this.resourceid);
		returnVal.put("resourcename", this.resourcename);
		returnVal.put("resourcetypeid", this.resourcetypeid);
		returnVal.put("resourceurl", this.resourceurl);
		
		return returnVal;
	}
}
