package com.unotechsoft.neo4japi.repositories;

import java.util.Collection;

import com.unotechsoft.neo4japi.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends Neo4jRepository<User, Long> {

	User findByFirstname(@Param("firstname") String firstname);

	Collection<User> findByFirstnameLike(@Param("firstname") String firstname);

    @Query("MATCH (m:User)-[r]->(a) RETURN m,r,a LIMIT {limit}")
	Collection<User> graph(@Param("limit") int limit);
    
    @Query("MATCH (m:User {firstname:{name}})-[r]->(a) RETURN m,r,a LIMIT {limit}")
	Collection<User> graphByName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {principal:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
    Collection<User> graphByPrincipal(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {firstname:{name}})-[r]->(a) RETURN m,r,a LIMIT {limit}")
	Collection<User> graphByFirstName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {lastname:{name}})-[r]->(a) RETURN m,r,a LIMIT {limit}")
	Collection<User> graphByLastName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {userid:{name}})-[r]->(a) RETURN m,r,a LIMIT {limit}")
	Collection<User> graphByUserId(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {principal:{name}})-[r:WORKS_IN]->(a:Company) RETURN m,r,a LIMIT {limit}")
    Collection<User> graphGetAffiliations(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {principal:{name}})-[r:BELONGS_TO]->(a:Group) RETURN m,r,a LIMIT {limit}")
    Collection<User> graphGetGroupings(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {principal:{name}})-[r:BELONGS_TO]->(a:Role) RETURN m,r,a LIMIT {limit}")
    Collection<User> graphGetRoles(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {principal:{name}})-[r:PRESENT_IN]->(a:ManagedSystem) RETURN m,r,a LIMIT {limit}")
    Collection<User> graphGetManagedSystem(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:User {principal:{name}})-[r:IS_STAFF_OF]-(a:User) RETURN m,r,a LIMIT {limit}")
    Collection<User> graphGetStaffs(@Param("limit") int limit, @Param("name") String name);
    
    @Query("CREATE (m:User {principal:{0}, firstname:{1}, lastname:{2}, loginid:{3}, typeid:{4}, title:{5}, classification:{6}, employeeid:{7}, userid:{8}, status:{9}})")  
    void createOrUpdate(String principal, String firstname, String lastname, String loginid, String typeid, String title, String classification, String employeeid, String userid, String status);
    
}
