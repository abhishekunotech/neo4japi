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
import com.unotechsoft.neo4japi.domain.Role;
import com.unotechsoft.neo4japi.domain.RoleManaging;
import com.unotechsoft.neo4japi.domain.RoleMapping;
import com.unotechsoft.neo4japi.domain.StaffMapping;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.domain.Company;

import com.unotechsoft.neo4japi.repositories.CompanyRepository;
import com.unotechsoft.neo4japi.repositories.RoleRepository;
import com.unotechsoft.neo4japi.repositories.UserRepository;

@Service
public class RoleService {
    private final static Logger LOG = LoggerFactory.getLogger(RoleService.class);
    
	private final RoleRepository roleRepository;
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	
	private List<Object> createNodesRelsGraph(Role role, int i, int source, Map<Long,Integer> nodeIDs, List<Map<String, Object>> rels, List<Map<String, Object>> nodes) {
		List<Object> returnObjectForGraph = new ArrayList<>();
		
		try {
		for (RoleMapping rolemapping : role.getRolemappings()) {
				int target;
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(rolemapping.getUser().getId())) {
					target = nodeIDs.get(rolemapping.getUser().getId());
					//rels.add(map("startNode", source, "endNode", nodeIDs.get(rolemapping.getUser().getId())));
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"user"});
					actor.put("properties", rolemapping.getUser().getProperties());
					target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					
					nodeIDs.put(rolemapping.getUser().getId(), target);
				}
				
				Map<String, Object> zmap = map("startNode", source, "endNode", target);
				zmap.put("id", rolemapping.getId());
				zmap.put("type", "rolemapping");
				zmap.put("labels", new String[]{"rolemapping"});
				rels.add(zmap);
			}
		} catch(Exception e) {
			LOG.info("Role Mappings are missing for the role with Role ID as "+role.getRoleid());
		}
		
		
		//TODO For Role-MS mapping
		
		try {
			for (RoleManaging rolemanagement: role.getRolemanagements()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(rolemanagement.getManagedsystem().getId())) {
						target = nodeIDs.get(rolemanagement.getManagedsystem().getId());
						//rels.add(map("startNode", source, "endNode", nodeIDs.get(rolemapping.getUser().getId())));
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"managedsystem"});
						actor.put("properties", rolemanagement.getManagedsystem().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						
						nodeIDs.put(rolemanagement.getManagedsystem().getId(), target);
					}
					
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", rolemanagement.getId());
					zmap.put("type", "rolemanagement");
					zmap.put("labels", new String[]{"rolemanagement"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.info("Role Mappings are missing for the role with Role ID as "+role.getRoleid());
			}
		
		
		returnObjectForGraph.add(role);
		returnObjectForGraph.add(i);
		returnObjectForGraph.add(source);
		returnObjectForGraph.add(nodeIDs);
		returnObjectForGraph.add(rels);
		returnObjectForGraph.add(nodes);
		return returnObjectForGraph;
	}
	
	private Map<String, Object> toNeo4JFormat(Collection<Role> roles){
		Map<String, Object> finalObject = new HashMap<String, Object>();
		Map<String,Object> d3formattedObject = toD3Format(roles);
		
		Map<String, Object> graphObject = new HashMap<String, Object>();
		graphObject.put("graph", d3formattedObject);
		
		
		List<Map<String, Object>> dataObject = new ArrayList<>();
		dataObject.add(graphObject);
		
		
		Map<String, Object> resultsInnerObject = new HashMap<String, Object>();
		resultsInnerObject.put("data", dataObject);
		resultsInnerObject.put("columns", new String[] {"user","role"});
		
		List<Map<String, Object>> resultsObject = new ArrayList<>();
		resultsObject.add(resultsInnerObject);
		finalObject.put("results", resultsObject);
		return finalObject;
	 }
	
	
	
	private Map<String, Object> toD3Format(Collection<Role> roles) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		Map<Long,Integer> nodeIDs = new HashMap<Long, Integer>();
		
		int i = 0;
		Iterator<Role> result = roles.iterator();
		while (result.hasNext()) {
			Role role = result.next();
			int source = i;
			if(nodeIDs.containsKey(role.getId())) {
				
			} else {
				nodeIDs.put(role.getId(), new Integer(i));
			
				Map<String, Object> nodeData = map("id", i, "labels", new String[] {"role"});
				nodeData.put("properties", role.getProperties());
				nodes.add(nodeData);
				i++;
			}
			List<Object> tmp = createNodesRelsGraph(role, i, source, nodeIDs, rels, nodes);
			role = (Role)tmp.get(0);
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
    public Role findByRolename(String rolename) {
        Role result = roleRepository.findByRolename(rolename);
        return result;
    }

	@Transactional(readOnly = true)
	public Map<String, Object>  graph(int limit) {
		Collection<Role> result = roleRepository.graph(limit);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByRoleName(int limit, String name) {
		Collection<Role> result = roleRepository.graphByRoleName(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByRoleId(int limit, String name) {
		Collection<Role> result = roleRepository.graphByRoleId(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetRolemappings(int limit, String name) {
		Collection<Role> result = roleRepository.graphGetRolemappings(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetRolemanagements(int limit, String name) {
		Collection<Role> result = roleRepository.graphGetRolemanagements(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public void createOrUpdate(String rolename, String roleid, String description, String managedsysid){
		roleRepository.createOrUpdate(rolename,roleid, description, managedsysid);
	}
}
