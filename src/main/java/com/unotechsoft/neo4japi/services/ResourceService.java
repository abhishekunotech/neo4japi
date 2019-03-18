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
import com.unotechsoft.neo4japi.domain.Resource;
import com.unotechsoft.neo4japi.domain.Role;
import com.unotechsoft.neo4japi.domain.RoleMapping;
import com.unotechsoft.neo4japi.domain.StaffMapping;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Entitlement;
import com.unotechsoft.neo4japi.repositories.CompanyRepository;
import com.unotechsoft.neo4japi.repositories.ResourceRepository;
import com.unotechsoft.neo4japi.repositories.RoleRepository;
import com.unotechsoft.neo4japi.repositories.UserRepository;

@Service
public class ResourceService {
    private final static Logger LOG = LoggerFactory.getLogger(ResourceService.class);
    
	private final ResourceRepository resourceRepository;
	public ResourceService(ResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}

	
	private List<Object> createNodesRelsGraph(Resource resource, int i, int source, Map<Long,Integer> nodeIDs, List<Map<String, Object>> rels, List<Map<String, Object>> nodes) {
		List<Object> returnObjectForGraph = new ArrayList<>();
		
		try {
		for (Entitlement entitlement : resource.getEntitlements()) {
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(entitlement.getUser().getId())) {
					rels.add(map("startNode", source, "endNode", nodeIDs.get(entitlement.getUser().getId())));
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"user"});
					actor.put("properties", entitlement.getUser().getProperties());
					int target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", entitlement.getId());
					zmap.put("type", "entitlement");
					zmap.put("labels", new String[]{"entitlement"});
					rels.add(zmap);
					nodeIDs.put(entitlement.getUser().getId(), target);
				}
			}
		} catch(Exception e) {
			LOG.info("Entitlements are missing for the resource with Resource ID as "+resource.getResourcename());
		}
		
		returnObjectForGraph.add(resource);
		returnObjectForGraph.add(i);
		returnObjectForGraph.add(source);
		returnObjectForGraph.add(nodeIDs);
		returnObjectForGraph.add(rels);
		returnObjectForGraph.add(nodes);
		return returnObjectForGraph;
	}
	
	private Map<String, Object> toNeo4JFormat(Collection<Resource> resources){
		Map<String, Object> finalObject = new HashMap<String, Object>();
		Map<String,Object> d3formattedObject = toD3Format(resources);
		
		Map<String, Object> graphObject = new HashMap<String, Object>();
		graphObject.put("graph", d3formattedObject);
		
		
		List<Map<String, Object>> dataObject = new ArrayList<>();
		dataObject.add(graphObject);
		
		
		Map<String, Object> resultsInnerObject = new HashMap<String, Object>();
		resultsInnerObject.put("data", dataObject);
		resultsInnerObject.put("columns", new String[] {"user","resource"});
		
		List<Map<String, Object>> resultsObject = new ArrayList<>();
		resultsObject.add(resultsInnerObject);
		finalObject.put("results", resultsObject);
		return finalObject;
	 }
	
	
	
	private Map<String, Object> toD3Format(Collection<Resource> resources) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		Map<Long,Integer> nodeIDs = new HashMap<Long, Integer>();
		
		int i = 0;
		Iterator<Resource> result = resources.iterator();
		while (result.hasNext()) {
			Resource resource = result.next();
			int source = i;
			if(nodeIDs.containsKey(resource.getId())) {
				
			} else {
				nodeIDs.put(resource.getId(), new Integer(i));
			
				Map<String, Object> nodeData = map("id", i, "labels", new String[] {"resource"});
				nodeData.put("properties", resource.getProperties());
				nodes.add(nodeData);
				i++;
			}
			List<Object> tmp = createNodesRelsGraph(resource, i, source, nodeIDs, rels, nodes);
			resource = (Resource)tmp.get(0);
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
    public Resource findByResourcename(String resourcename) {
        Resource result = resourceRepository.findByResourcename(resourcename);
        return result;
    }

	@Transactional(readOnly = true)
	public Map<String, Object>  graph(int limit) {
		Collection<Resource> result = resourceRepository.graph(limit);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByResourceName(int limit, String name) {
		Collection<Resource> result = resourceRepository.graphByResourceName(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByResourceId(int limit, String name) {
		Collection<Resource> result = resourceRepository.graphByResourceId(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetEntitlements(int limit, String name) {
		Collection<Resource> result = resourceRepository.graphGetEntitlements(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public void createOrUpdate(String resourcename, String resourceid, String resourcetypeid, String resourceurl){
		resourceRepository.createOrUpdate(resourcename,resourceid, resourcetypeid, resourceurl);
	}
}
