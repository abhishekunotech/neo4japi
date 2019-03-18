package com.unotechsoft.neo4japi.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@NodeEntity
public class Group {

	@GraphId
	private Long id;
	private String grpid;
	private String grpname;
	private String managedsysid;
	private String adgrptype;
	private String typeid;
	public Long getId() {
		return id;
	}
	public String getGrpid() {
		return grpid;
	}
	public String getGrpname() {
		return grpname;
	}
	public String getManagedsysid() {
		return managedsysid;
	}
	public String getAdgrptype() {
		return adgrptype;
	}
	public String getTypeid() {
		return typeid;
	}
	
	@JsonManagedReference
	@Relationship(type = "BELONGS_TO", direction=Relationship.INCOMING)
	private List<Grouping> groupings = new ArrayList<>();

	@JsonManagedReference
	@Relationship(type = "MANAGES", direction=Relationship.INCOMING)
	private List<GroupManaging> groupmanagements = new ArrayList<>();
	
	public List<Grouping> getGroupings() {
		return groupings;
	}
	
	public List<GroupManaging> getGroupmanagements() {
		return groupmanagements;
	}
	
	public Group() {
	}

	/*
	 * private String grpid; private String grpname; private String managedsysid;
	 * private String adgrptype; private String typeid;
	 */
	public Group(String grpid, String grpname, String managedsysid, String adgrptype, String typeid) {
		this.grpid = grpid;
		this.grpname = grpname;
		this.managedsysid = managedsysid;
		this.adgrptype = adgrptype;
		this.typeid = typeid;
	}
	
	public Map<String, String> getProperties(){
		Map<String, String> returnVal = new HashMap<String, String>();
		
		returnVal.put("grpid", this.grpid);
		returnVal.put("typeid", this.typeid);
		returnVal.put("grpname", this.grpname);
		returnVal.put("managedsysid", this.managedsysid);
		returnVal.put("adgrptype", this.adgrptype);
		
		return returnVal;
	}
}
