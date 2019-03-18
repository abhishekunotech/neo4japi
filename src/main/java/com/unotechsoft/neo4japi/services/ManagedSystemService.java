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
import com.unotechsoft.neo4japi.domain.ManagedSystem;
import com.unotechsoft.neo4japi.domain.ManagedSystemMapping;
import com.unotechsoft.neo4japi.domain.RoleManaging;
import com.unotechsoft.neo4japi.domain.RoleMapping;
import com.unotechsoft.neo4japi.domain.StaffMapping;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.GroupManaging;
import com.unotechsoft.neo4japi.repositories.CompanyRepository;
import com.unotechsoft.neo4japi.repositories.ManagedSystemRepository;
import com.unotechsoft.neo4japi.repositories.UserRepository;

@Service
public class ManagedSystemService {
    private final static Logger LOG = LoggerFactory.getLogger(ManagedSystemService.class);
    
	private final ManagedSystemRepository managedsystemRepository;
	public ManagedSystemService(ManagedSystemRepository managedsystemRepository) {
		this.managedsystemRepository = managedsystemRepository;
	}

	
	private List<Object> createNodesRelsGraph(ManagedSystem managedsystem, int i, int source, Map<Long,Integer> nodeIDs, List<Map<String, Object>> rels, List<Map<String, Object>> nodes) {
		List<Object> returnObjectForGraph = new ArrayList<>();
		
		try {
		for (ManagedSystemMapping managedsystemmapping : managedsystem.getManagedsystemmappings()) {
				int target;
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(managedsystemmapping.getUser().getId())) {
					target = nodeIDs.get(managedsystemmapping.getUser().getId());
					//rels.add(map("startNode", source, "endNode", nodeIDs.get(managedsystemmapping.getUser().getId())));
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"user"});
					actor.put("properties", managedsystemmapping.getUser().getProperties());
					target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					nodeIDs.put(managedsystemmapping.getUser().getId(), target);
				}
				Map<String, Object> zmap = map("startNode", source, "endNode", target);
				zmap.put("id", managedsystemmapping.getId());
				zmap.put("type", "managedsystemmapping");
				zmap.put("labels", new String[]{"managedsystemmapping"});
				rels.add(zmap);
			}
		} catch(Exception e) {
			LOG.info("Managed System Mappings are missing for the Managed System with ID as "+managedsystem.getManagedsysid());
		}
		
		
		//TODO FOR MS-Role mapping
		
		try {
			for (RoleManaging rolemanaging : managedsystem.getRolemanagements()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(rolemanaging.getRole().getId())) {
						target = nodeIDs.get(rolemanaging.getRole().getId());
						//rels.add(map("startNode", source, "endNode", nodeIDs.get(managedsystemmapping.getUser().getId())));
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"role"});
						actor.put("properties", rolemanaging.getRole().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(rolemanaging.getRole().getId(), target);
					}
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", rolemanaging.getId());
					zmap.put("type", "rolemanagement");
					zmap.put("labels", new String[]{"rolemanagement"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.info("Managed System Mappings are missing for the Managed System with ID as "+managedsystem.getManagedsysid());
			}
		
		
		//TODO For MS-Group mapping
		
		try {
			for (GroupManaging groupmanagement: managedsystem.getGroupmanagements()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(groupmanagement.getGroup().getId())) {
						target = nodeIDs.get(groupmanagement.getGroup().getId());
						//rels.add(map("startNode", source, "endNode", nodeIDs.get(managedsystemmapping.getUser().getId())));
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"group"});
						actor.put("properties", groupmanagement.getGroup().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(groupmanagement.getGroup().getId(), target);
					}
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", groupmanagement.getId());
					zmap.put("type", "groupmanagement");
					zmap.put("labels", new String[]{"groupmanagement"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.info("Managed System Mappings are missing for the Managed System with ID as "+managedsystem.getManagedsysid());
			}
		
		
		
		
		
		
		
		returnObjectForGraph.add(managedsystem);
		returnObjectForGraph.add(i);
		returnObjectForGraph.add(source);
		returnObjectForGraph.add(nodeIDs);
		returnObjectForGraph.add(rels);
		returnObjectForGraph.add(nodes);
		return returnObjectForGraph;
	}
	
	private Map<String, Object> toNeo4JFormat(Collection<ManagedSystem> managedsystems){
		Map<String, Object> finalObject = new HashMap<String, Object>();
		Map<String,Object> d3formattedObject = toD3Format(managedsystems);
		
		Map<String, Object> graphObject = new HashMap<String, Object>();
		graphObject.put("graph", d3formattedObject);
		
		
		List<Map<String, Object>> dataObject = new ArrayList<>();
		dataObject.add(graphObject);
		
		
		Map<String, Object> resultsInnerObject = new HashMap<String, Object>();
		resultsInnerObject.put("data", dataObject);
		resultsInnerObject.put("columns", new String[] {"user","managedsystem"});
		
		List<Map<String, Object>> resultsObject = new ArrayList<>();
		resultsObject.add(resultsInnerObject);
		finalObject.put("results", resultsObject);
		return finalObject;
	 }
	
	
	
	private Map<String, Object> toD3Format(Collection<ManagedSystem> managedsystems) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		Map<Long,Integer> nodeIDs = new HashMap<Long, Integer>();
		
		int i = 0;
		Iterator<ManagedSystem> result = managedsystems.iterator();
		while (result.hasNext()) {
			ManagedSystem managedsystem = result.next();
			int source = i;
			if(nodeIDs.size()>0) {
				
			} else {
				nodeIDs.put(managedsystem.getId(), new Integer(i));
			
				Map<String, Object> nodeData = map("id", i, "labels", new String[] {"managedsystem"});
				nodeData.put("properties", managedsystem.getProperties());
				nodes.add(nodeData);
				i++;
			}
			List<Object> tmp = createNodesRelsGraph(managedsystem, i, source, nodeIDs, rels, nodes);
			managedsystem = (ManagedSystem)tmp.get(0);
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
    public ManagedSystem findByName(String name) {
        ManagedSystem result = managedsystemRepository.findByName(name);
        return result;
    }

	@Transactional(readOnly = true)
	public Map<String, Object>  graph(int limit) {
		Collection<ManagedSystem> result = managedsystemRepository.graph(limit);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByName(int limit, String name) {
		Collection<ManagedSystem> result = managedsystemRepository.graphByName(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphById(int limit, String name) {
		Collection<ManagedSystem> result = managedsystemRepository.graphById(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetManagedsystemmappings(int limit, String name) {
		Collection<ManagedSystem> result = managedsystemRepository.graphGetManagedsystemmappings(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetRolemanagements(int limit, String name) {
		Collection<ManagedSystem> result = managedsystemRepository.graphGetRolesManaged(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetGroupmanagements(int limit, String name) {
		Collection<ManagedSystem> result = managedsystemRepository.graphGetGroupsManaged(limit, name);
		return toNeo4JFormat(result);
	}
	
	
	@Transactional(readOnly = true)
	public void createOrUpdate(String managedsysid, String name, String status){
		managedsystemRepository.createOrUpdate(managedsysid, name, status);
	}
}
