package com.unotechsoft.neo4japi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Resource;
import com.unotechsoft.neo4japi.domain.Role;
import com.unotechsoft.neo4japi.domain.User;

@RepositoryRestResource(collectionResourceRel = "resources", path = "resources")
public interface ResourceRepository extends Neo4jRepository<Resource, Long> {

	Resource findByResourcename(@Param("resourcename") String resourcename);

    @Query("MATCH (m:Resource)-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Resource> graph(@Param("limit") int limit);
    
    @Query("MATCH (m:Resource {resourcename:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Resource> graphByResourceName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Resource {resourceid:{id}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Resource> graphByResourceId(@Param("limit") int limit, @Param("id") String id);
    
    @Query("MATCH (m:Resource {resourcename:{name}})-[r:IS_ENTITLED_TO]-(a:User) RETURN m,r,a LIMIT {limit}")
    Collection<Resource> graphGetEntitlements(@Param("limit") int limit, @Param("name") String name);

    @Query("CREATE (m:Resource {resourcename:{0}, resourceid:{1}, resourcetypeid:{2}, resourceurl:{3}})")  
    void createOrUpdate(String resourcename, String resourceid, String resourcetypeid, String resourceurl);
    
}
