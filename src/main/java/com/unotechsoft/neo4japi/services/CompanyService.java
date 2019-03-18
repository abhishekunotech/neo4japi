package com.unotechsoft.neo4japi.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unotechsoft.neo4japi.domain.Affiliation;
import com.unotechsoft.neo4japi.domain.Grouping;
import com.unotechsoft.neo4japi.domain.ManagedSystemMapping;
import com.unotechsoft.neo4japi.domain.RoleMapping;
import com.unotechsoft.neo4japi.domain.StaffMapping;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.DepartmentMapping;
import com.unotechsoft.neo4japi.repositories.CompanyRepository;
import com.unotechsoft.neo4japi.repositories.UserRepository;

@Service
public class CompanyService {
    private final static Logger LOG = LoggerFactory.getLogger(CompanyService.class);
    
	private final CompanyRepository companyRepository;
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	
	private List<Object> createNodesRelsGraph(Company company, int i, int source, Map<Long,Integer> nodeIDs, List<Map<String, Object>> rels, List<Map<String, Object>> nodes) {
		List<Object> returnObjectForGraph = new ArrayList<>();
		
		
		try {
			for (DepartmentMapping deptmapping : company.getDepartmentmappings()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(deptmapping.getDepartment().getId())) {
						target = nodeIDs.get(deptmapping.getDepartment().getId());
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"department"});
						actor.put("properties", deptmapping.getDepartment().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(deptmapping.getDepartment().getId(), target);
					}
					Map<String, Object>zmap = map("startNode", source, "endNode", target);
					zmap.put("id", deptmapping.getId());
					zmap.put("type", "departmentmapping");
					zmap.put("labels", new String[]{"departmentmapping"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.debug("Department Mappings are missing for the company with Company ID as "+company.getCompanyid());
			}
		
		
		try {
		for (Affiliation affiliation : company.getAffiliations()) {
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(affiliation.getUser().getId())) {
					rels.add(map("startNode", source, "endNode", nodeIDs.get(affiliation.getUser().getId())));
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"user"});
					actor.put("properties", affiliation.getUser().getProperties());
					int target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", affiliation.getId());
					zmap.put("type", "affiliation");
					zmap.put("labels", new String[]{"affiliation"});
					rels.add(zmap);
					nodeIDs.put(affiliation.getUser().getId(), target);
				}
			}
		} catch(Exception e) {
			LOG.info("Affiliations are missing for the company with Company ID as "+company.getCompanyid());
		}
		
		returnObjectForGraph.add(company);
		returnObjectForGraph.add(i);
		returnObjectForGraph.add(source);
		returnObjectForGraph.add(nodeIDs);
		returnObjectForGraph.add(rels);
		returnObjectForGraph.add(nodes);
		return returnObjectForGraph;
	}
	
	private Map<String, Object> toNeo4JFormat(Collection<Company> companies){
		Map<String, Object> finalObject = new HashMap<String, Object>();
		Map<String,Object> d3formattedObject = toD3Format(companies);
		
		Map<String, Object> graphObject = new HashMap<String, Object>();
		graphObject.put("graph", d3formattedObject);
		
		
		List<Map<String, Object>> dataObject = new ArrayList<>();
		dataObject.add(graphObject);
		
		
		Map<String, Object> resultsInnerObject = new HashMap<String, Object>();
		resultsInnerObject.put("data", dataObject);
		resultsInnerObject.put("columns", new String[] {"user","company"});
		
		List<Map<String, Object>> resultsObject = new ArrayList<>();
		resultsObject.add(resultsInnerObject);
		finalObject.put("results", resultsObject);
		return finalObject;
	 }
	
	
	
	private Map<String, Object> toD3Format(Collection<Company> companies) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		Map<Long,Integer> nodeIDs = new HashMap<Long, Integer>();
		
		int i = 0;
		Iterator<Company> result = companies.iterator();
		while (result.hasNext()) {
			Company company = result.next();
			int source = i;
			if(nodeIDs.containsKey(company.getId())) {
				
			} else {
				nodeIDs.put(company.getId(), new Integer(i));
			
				Map<String, Object> nodeData = map("id", i, "labels", new String[] {"company"});
				nodeData.put("properties", company.getProperties());
				nodes.add(nodeData);
				i++;
			}
			List<Object> tmp = createNodesRelsGraph(company, i, source, nodeIDs, rels, nodes);
			company = (Company)tmp.get(0);
			i = (int)tmp.get(1);
			source = (int)tmp.get(2);
			nodeIDs = (Map<Long,Integer>)tmp.get(3);
			
			rels = (List<Map<String,Object>>)tmp.get(4);
			nodes = (List<Map<String,Object>>)tmp.get(5);
		}
		return map("nodes", nodes, "relationships", rels);
	}

	private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put(key1, value1);
		result.put(key2, value2);
		return result;
	}

    @Transactional(readOnly = true)
    public Company findByCompanyname(String companyname) {
        Company result = companyRepository.findByCompanyname(companyname);
        return result;
    }

	@Transactional(readOnly = true)
	public Map<String, Object>  graph(int limit) {
		Collection<Company> result = companyRepository.graph(limit);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByCompanyName(int limit, String name) {
		Collection<Company> result = companyRepository.graphByCompanyName(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByCompanyId(int limit, String name) {
		Collection<Company> result = companyRepository.graphByCompanyId(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetAffiliations(int limit, String name) {
		Collection<Company> result = companyRepository.graphGetAffiliations(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public void createOrUpdate(String alias, String companyid, String companyname, String description, String internalcompanyid, String orgtypeid){
		companyRepository.createOrUpdate(alias,companyid, companyname, description, internalcompanyid, orgtypeid);
	}
}
