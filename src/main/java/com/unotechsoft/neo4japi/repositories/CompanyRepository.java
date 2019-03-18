package com.unotechsoft.neo4japi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.User;

@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepository extends Neo4jRepository<Company, Long> {

	Company findByCompanyname(@Param("companyname") String companyname);


    @Query("MATCH (m:Company)-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Company> graph(@Param("limit") int limit);
    
    @Query("MATCH (m:Company {companyname:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Company> graphByCompanyName(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Company {companyid:{name}})-[r]-(a) RETURN m,r,a LIMIT {limit}")
	Collection<Company> graphByCompanyId(@Param("limit") int limit, @Param("name") String name);
    
    @Query("MATCH (m:Company {companyname:{name}})-[r:WORKS_IN]-(a:User) RETURN m,r,a LIMIT {limit}")
    Collection<Company> graphGetAffiliations(@Param("limit") int limit, @Param("name") String name);

    @Query("MATCH (m:Company {companyname:{name}})-[r:IS_A_DEPARTMENT_OF]-(a:Company) RETURN m,r,a LIMIT {limit}")
    Collection<Company> graphGetDepartments(@Param("limit") int limit, @Param("name") String name);
    
    @Query("CREATE (m:Company {alias:{0}, companyid:{1}, companyname:{2}, description:{3}, internalcompanyid:{4}, orgtypeid:{5}})")  
    void createOrUpdate(String alias, String companyid, String companyname, String description, String internalcompanyid, String orgtypeid);
    
}
