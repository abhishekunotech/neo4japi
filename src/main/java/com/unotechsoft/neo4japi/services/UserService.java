package com.unotechsoft.neo4japi.services;

import java.util.*;

import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.domain.Affiliation;
import com.unotechsoft.neo4japi.domain.Entitlement;
import com.unotechsoft.neo4japi.domain.Grouping;
import com.unotechsoft.neo4japi.domain.ManagedSystemMapping;
import com.unotechsoft.neo4japi.domain.RoleMapping;
import com.unotechsoft.neo4japi.domain.StaffMapping;
import com.unotechsoft.neo4japi.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
    
	private final UserRepository userRepository;
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	
	private List<Object> createNodesRelsGraph(User user, int i, int source, Map<Long,Integer> nodeIDs, List<Map<String, Object>> rels, List<Map<String, Object>> nodes) {
		List<Object> returnObjectForGraph = new ArrayList<>();
		
		// For Entitlement
		
		try {
			for (Entitlement entitlement : user.getEntitlements()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(entitlement.getResource().getId())) {
						target = nodeIDs.get(entitlement.getResource().getId());
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"resource"});
						actor.put("properties", entitlement.getResource().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(entitlement.getResource().getId(), target);
					}
					Map<String, Object>zmap = map("startNode", source, "endNode", target);
					zmap.put("id", entitlement.getId());
					zmap.put("type", "entitlement");
					zmap.put("labels", new String[]{"entitlement"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.debug("Entitlements are missing for the User with employee ID as "+user.getEmployeeid());
			}
		
		
		
		
			try {
			for (StaffMapping staffmapping : user.getStaffmappings()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(staffmapping.getStaff().getId())) {
						target = nodeIDs.get(staffmapping.getStaff().getId());
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"staff"});
						actor.put("properties", staffmapping.getStaff().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(staffmapping.getStaff().getId(), target);
					}
					Map<String, Object>zmap = map("startNode", source, "endNode", target);
					zmap.put("id", staffmapping.getId());
					zmap.put("type", "staffmapping");
					zmap.put("labels", new String[]{"staffmapping"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.debug("Staff Mappings are missing for the user with employee ID as "+user.getEmployeeid());
			}
		
		
		
		// For Role Mapping
		
		try {
			for (RoleMapping rolemapping : user.getRolemappings()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(rolemapping.getRole().getId())) {
						target = nodeIDs.get(rolemapping.getRole().getId());
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"role"});
						actor.put("properties", rolemapping.getRole().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(rolemapping.getRole().getId(), target);
					}
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", rolemapping.getId());
					zmap.put("type", "rolemapping");
					zmap.put("labels", new String[]{"rolemapping"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.debug("Role Mappings are missing for the user with employee ID as "+user.getEmployeeid());
			}
		
		
		// For Managed System Mapping
		
		
		try {
			for (ManagedSystemMapping managedsystemmapping : user.getManagedsystemmappings()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(managedsystemmapping.getManagedsystem().getId())) {
						target = nodeIDs.get(managedsystemmapping.getManagedsystem().getId());
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"managed system"});
						actor.put("properties", managedsystemmapping.getManagedsystem().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(managedsystemmapping.getManagedsystem().getId(), target);
					}
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", managedsystemmapping.getId());
					zmap.put("type", "managedsystemmapping");
					zmap.put("labels", new String[]{"managedsystemmapping"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.debug("Managed System mappings are missing for the user with employee ID as "+user.getEmployeeid());
			}
		
		try {
		for (Affiliation affiliation : user.getAffiliations()) {
				int target;
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(affiliation.getCompany().getId())) {
					target = nodeIDs.get(affiliation.getCompany().getId());
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"company"});
					actor.put("properties", affiliation.getCompany().getProperties());
					target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					nodeIDs.put(affiliation.getCompany().getId(), target);
				}
				Map<String, Object> zmap = map("startNode", source, "endNode", target);
				zmap.put("id", affiliation.getId());
				zmap.put("type", "affiliation");
				zmap.put("labels", new String[]{"affiliation"});
				rels.add(zmap);
			}
		} catch(Exception e) {
			LOG.debug("Affiliations are missing for the user with employee ID as "+user.getEmployeeid());
		}
		
		try {
		for (Grouping grouping : user.getGroupings()) {
				int target;
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(grouping.getGroup().getId())) {
					target = nodeIDs.get(grouping.getGroup().getId());
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"group"});
					actor.put("properties", grouping.getGroup().getProperties());
					target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					nodeIDs.put(grouping.getGroup().getId(), target);
				}
				Map<String, Object> zmap = map("startNode", source, "endNode", target);
				zmap.put("id", grouping.getId());
				zmap.put("type", "grouping");
				zmap.put("labels", new String[]{"grouping"});
				rels.add(zmap);
			}
		} catch(Exception e) {
			LOG.debug("Groupings are missing for the user with employee ID as "+user.getEmployeeid());
		}
		
		
		returnObjectForGraph.add(user);
		returnObjectForGraph.add(i);
		returnObjectForGraph.add(source);
		returnObjectForGraph.add(nodeIDs);
		returnObjectForGraph.add(rels);
		returnObjectForGraph.add(nodes);
		return returnObjectForGraph;
	}
	
	private Map<String, Object> toNeo4JFormat(Collection<User> users){
		Map<String, Object> finalObject = new HashMap<String, Object>();
		//LOG.error("%d"+users.iterator().next().getLastname());
		Map<String,Object> d3formattedObject = toD3Format(users);
		
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
	
	
	
	private Map<String, Object> toD3Format(Collection<User> users) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		Map<Long,Integer> nodeIDs = new HashMap<Long, Integer>();
		
		int i = 0;
		Iterator<User> result = users.iterator();
		while (result.hasNext()) {
			User user = result.next();
			int source = i;
			if(nodeIDs.containsKey(user.getId())) {
				
			} else {
				nodeIDs.put(user.getId(), new Integer(i));
			
				Map<String, Object> nodeData = map("id", i, "labels", new String[] {"user"});
				nodeData.put("properties", user.getProperties());
				nodes.add(nodeData);
				i++;
			}
			List<Object> tmp = createNodesRelsGraph(user, i, source, nodeIDs, rels, nodes);
			user = (User)tmp.get(0);
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
    public User findByFirstname(String firstname) {
        User result = userRepository.findByFirstname(firstname);
        return result;
    }

    @Transactional(readOnly = true)
    public Collection<User> findByFirstnameLike(String title) {
        Collection<User> result = userRepository.findByFirstnameLike(title);
        return result;
    }

	@Transactional(readOnly = true)
	public Map<String, Object>  graph(int limit) {
		Collection<User> result = userRepository.graph(limit);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByName(int limit, String name) {
		Collection<User> result = userRepository.graphByName(limit, name);
		return toNeo4JFormat(result);
	}
	
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByFirstName(int limit, String name) {
		Collection<User> result = userRepository.graphByFirstName(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByLastName(int limit, String name) {
		Collection<User> result = userRepository.graphByLastName(limit, name);
		return toNeo4JFormat(result);
	}
	
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByUserId(int limit, String name) {
		Collection<User> result = userRepository.graphByUserId(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByPrincipal(int limit, String name) {
		Collection<User> result = userRepository.graphByPrincipal(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetAffiliations(int limit, String name) {
		Collection<User> result = userRepository.graphGetAffiliations(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetRoles(int limit, String name) {
		Collection<User> result = userRepository.graphGetRoles(limit, name);
		return toNeo4JFormat(result);
	}
	
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetGroupings(int limit, String name) {
		Collection<User> result = userRepository.graphGetGroupings(limit, name);
		return toNeo4JFormat(result);
	}
	
	
	@Transactional(readOnly = true)
	public Map<String, Object> graphGetManagedSystem(int limit, String name) {
		Collection<User> result = userRepository.graphGetManagedSystem(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public void createOrUpdate(String principal, String firstname, String lastname, String loginid, String typeid, String title, String classification, String employeeid, String userid, String status){
		userRepository.createOrUpdate(principal,firstname, lastname, loginid, typeid, title, classification, employeeid, userid, status);
	}
}