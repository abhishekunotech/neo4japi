package com.unotechsoft.neo4japi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Group;
import com.unotechsoft.neo4japi.domain.User;

@RepositoryRestResource(collectionResourceRel = "groups", path = "groups")
public interface GroupRepository extends Neo4jRepository<Group, Long> {

	Group findByGrpname(@Param("groupname") String groupname);

    @Query("MATCH (m:Group)-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Group> graph(@Param("limit") int limit);
    
    @Query("MATCH (m:Group {grpname:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Group> graphByGroupName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Group {grpid:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Group> graphByGroupId(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Group {grpname:{name}})-[r:BELONGS_TO]-(a:User) RETURN m,r,a LIMIT {limit}")
    Collection<Group> graphGetGroupings(@Param("limit") int limit, @Param("name") String name);

    @Query("MATCH (m:Group {grpname:{name}})-[r:MANAGES]-(a:ManagedSystem) RETURN m,r,a LIMIT {limit}")
    Collection<Group> graphGetGroupmanagements(@Param("limit") int limit, @Param("name") String name);
    
    @Query("CREATE (m:Group {grpid:{0}, grpname:{1}, managedsysid:{2}, adgrptype:{3}, typeid:{4}})")
	void createOrUpdate(String grpid, String grpname, String managedsysid, String adgrptype, String typeid);
    
}
