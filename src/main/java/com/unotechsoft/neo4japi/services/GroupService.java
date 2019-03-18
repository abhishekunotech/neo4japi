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
import com.unotechsoft.neo4japi.domain.Group;
import com.unotechsoft.neo4japi.domain.GroupManaging;
import com.unotechsoft.neo4japi.repositories.CompanyRepository;
import com.unotechsoft.neo4japi.repositories.GroupRepository;
import com.unotechsoft.neo4japi.repositories.UserRepository;

@Service
public class GroupService {
    private final static Logger LOG = LoggerFactory.getLogger(GroupService.class);
    
	private final GroupRepository groupRepository;
	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	
	private List<Object> createNodesRelsGraph(Group group, int i, int source, Map<Long,Integer> nodeIDs, List<Map<String, Object>> rels, List<Map<String, Object>> nodes) {
		List<Object> returnObjectForGraph = new ArrayList<>();
		
		try {
		for (Grouping grouping : group.getGroupings()) {
				int target;
				if(nodeIDs.size() > 0 && nodeIDs.containsKey(grouping.getUser().getId())) {
					//rels.add(map("startNode", source, "endNode", nodeIDs.get(grouping.getUser().getId())));
					target = nodeIDs.get(grouping.getUser().getId());
				} else {
					Map<String, Object> actor = map("id",i, "labels", new String[]{"user"});
					actor.put("properties", grouping.getUser().getProperties());
					target = nodes.indexOf(actor);
					if (target == -1) {
						nodes.add(actor);
						target = i++;
					}
					nodeIDs.put(grouping.getUser().getId(), target);
				}
				
				Map<String, Object> zmap = map("startNode", source, "endNode", target);
				zmap.put("id", grouping.getId());
				zmap.put("type", "grouping");
				zmap.put("labels", new String[]{"grouping"});
				rels.add(zmap);
			}
		} catch(Exception e) {
			LOG.info("Groupings are missing for the groups with Group ID as "+group.getGrpid());
		}
		
		
		//TODO For MS-Group mapping
		
		
		try {
			for (GroupManaging groupmanagement : group.getGroupmanagements()) {
					int target;
					if(nodeIDs.size() > 0 && nodeIDs.containsKey(groupmanagement.getManagedsystem().getId())) {
						//rels.add(map("startNode", source, "endNode", nodeIDs.get(grouping.getUser().getId())));
						target = nodeIDs.get(groupmanagement.getManagedsystem().getId());
					} else {
						Map<String, Object> actor = map("id",i, "labels", new String[]{"managedsystem"});
						actor.put("properties", groupmanagement.getManagedsystem().getProperties());
						target = nodes.indexOf(actor);
						if (target == -1) {
							nodes.add(actor);
							target = i++;
						}
						nodeIDs.put(groupmanagement.getManagedsystem().getId(), target);
					}
					
					Map<String, Object> zmap = map("startNode", source, "endNode", target);
					zmap.put("id", groupmanagement.getId());
					zmap.put("type", "groupmanagement");
					zmap.put("labels", new String[]{"groupmanagement"});
					rels.add(zmap);
				}
			} catch(Exception e) {
				LOG.info("Groupings are missing for the groups with Group ID as "+group.getGrpid());
			}
			
		
		returnObjectForGraph.add(group);
		returnObjectForGraph.add(i);
		returnObjectForGraph.add(source);
		returnObjectForGraph.add(nodeIDs);
		returnObjectForGraph.add(rels);
		returnObjectForGraph.add(nodes);
		return returnObjectForGraph;
	}
	
	private Map<String, Object> toNeo4JFormat(Collection<Group> groups){
		Map<String, Object> finalObject = new HashMap<String, Object>();
		Map<String,Object> d3formattedObject = toD3Format(groups);
		
		Map<String, Object> graphObject = new HashMap<String, Object>();
		graphObject.put("graph", d3formattedObject);
		
		
		List<Map<String, Object>> dataObject = new ArrayList<>();
		dataObject.add(graphObject);
		
		
		Map<String, Object> resultsInnerObject = new HashMap<String, Object>();
		resultsInnerObject.put("data", dataObject);
		resultsInnerObject.put("columns", new String[] {"user","group"});
		
		List<Map<String, Object>> resultsObject = new ArrayList<>();
		resultsObject.add(resultsInnerObject);
		finalObject.put("results", resultsObject);
		return finalObject;
	 }
	
	
	
	private Map<String, Object> toD3Format(Collection<Group> groups) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		Map<Long,Integer> nodeIDs = new HashMap<Long, Integer>();
		
		int i = 0;
		Iterator<Group> result = groups.iterator();
		while (result.hasNext()) {
			Group grp = result.next();
			int source = i;
			if(nodeIDs.size()>0) {
				
			} else {
				nodeIDs.put(grp.getId(), new Integer(i));
			
				Map<String, Object> nodeData = map("id", i, "labels", new String[] {"group"});
				nodeData.put("properties", grp.getProperties());
				nodes.add(nodeData);
				i++;
			}
			List<Object> tmp = createNodesRelsGraph(grp, i, source, nodeIDs, rels, nodes);
			grp = (Group)tmp.get(0);
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
    public Group findByGrpname(String grpname) {
        Group result = groupRepository.findByGrpname(grpname);
        return result;
    }

	@Transactional(readOnly = true)
	public Map<String, Object>  graph(int limit) {
		Collection<Group> result = groupRepository.graph(limit);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByGroupName(int limit, String name) {
		Collection<Group> result = groupRepository.graphByGroupName(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphByGroupId(int limit, String name) {
		Collection<Group> result = groupRepository.graphByGroupId(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetGroupings(int limit, String name) {
		Collection<Group> result = groupRepository.graphGetGroupings(limit, name);
		return toNeo4JFormat(result);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object>  graphGetGroupmanagements(int limit, String name) {
		Collection<Group> result = groupRepository.graphGetGroupmanagements(limit, name);
		return toNeo4JFormat(result);
	}
	
	/*
	 * graphGetGroupmanagements
	 * private String grpid; private String grpname; private String managedsysid;
	 * private String adgrptype; private String typeid;
	 */
	
	@Transactional(readOnly = true)
	public void createOrUpdate(String grpid, String grpname, String managedsysid, String adgrptype, String typeid){
		groupRepository.createOrUpdate(grpid, grpname, managedsysid, adgrptype, typeid);
	}
}
