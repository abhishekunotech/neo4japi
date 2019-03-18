package com.unotechsoft.neo4japi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.Role;
import com.unotechsoft.neo4japi.domain.User;

@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
public interface RoleRepository extends Neo4jRepository<Role, Long> {

	Role findByRolename(@Param("rolename") String rolename);


    @Query("MATCH (m:Role)-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Role> graph(@Param("limit") int limit);
    
    @Query("MATCH (m:Role {rolename:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Role> graphByRoleName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Role {roleid:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Role> graphByRoleId(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Role {rolename:{name}})-[r:BELONGS_TO]-(a:User) RETURN m,r,a LIMIT {limit}")
    Collection<Role> graphGetRolemappings(@Param("limit") int limit, @Param("name") String name);

    @Query("MATCH (m:Role {rolename:{name}})-[r:MANAGES]-(a:ManagedSystem) RETURN m,r,a LIMIT {limit}")
    Collection<Role> graphGetRolemanagements(@Param("limit") int limit, @Param("name") String name);
    
    @Query("CREATE (m:Role {rolename:{0}, roleid:{1}, description:{2}, managedsysid:{3}})")  
    void createOrUpdate(String rolename, String roleid, String description, String managedsysid);
    
}
