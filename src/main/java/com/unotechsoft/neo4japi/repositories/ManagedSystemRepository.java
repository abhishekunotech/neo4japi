package com.unotechsoft.neo4japi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.ManagedSystem;
import com.unotechsoft.neo4japi.domain.User;

@RepositoryRestResource(collectionResourceRel = "mngdsystems", path = "mngdsystems")
public interface ManagedSystemRepository extends Neo4jRepository<ManagedSystem, Long> {

	ManagedSystem findByName(@Param("name") String name);

    @Query("MATCH (m:ManagedSystem)-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<ManagedSystem> graph(@Param("limit") int limit);
    
    @Query("MATCH (m:ManagedSystem {name:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<ManagedSystem> graphByName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:ManagedSystem {managedsysid:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<ManagedSystem> graphById(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:ManagedSystem {name:{name}})-[r:PRESENT_IN]-(a:User) RETURN m,r,a LIMIT {limit}")
    Collection<ManagedSystem> graphGetManagedsystemmappings(@Param("limit") int limit, @Param("name") String name);

    @Query("MATCH (m:ManagedSystem {name:{name}})-[r:MANAGES]->(a:Group) RETURN m,r,a LIMIT {limit}")
    Collection<ManagedSystem> graphGetGroupsManaged(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:ManagedSystem {name:{name}})-[r:MANAGES]->(a:Role) RETURN m,r,a LIMIT {limit}")
    Collection<ManagedSystem> graphGetRolesManaged(@Param("limit") int limit, @Param("name") String name);
    
    @Query("CREATE (m:ManagedSystem {managedsysid:{0}, name:{1}, status:{2}})")  
    void createOrUpdate(String managedsysid, String name, String status);
    
}
