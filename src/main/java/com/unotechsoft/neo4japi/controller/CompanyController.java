package com.unotechsoft.neo4japi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unotechsoft.neo4japi.domain.Company;
import com.unotechsoft.neo4japi.domain.User;
import com.unotechsoft.neo4japi.services.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {
	private final CompanyService companyService;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	@GetMapping("/findByCompanyname")
	public Company findByCompanyname(@RequestParam(value = "limit",required = false) Integer limit, @RequestParam(value = "companyname", required = true) String companyname) {
		return companyService.findByCompanyname(companyname);
	}

    @GetMapping("/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return companyService.graph(limit == null ? 100 : limit);
	}
  
    @GetMapping("/graphByCompanyName")
	public Map<String, Object> graphByCompanyName(@RequestParam(value = "limit",required = false) Integer limit,
			@RequestParam(value = "name", required = false) String name) {
		return companyService.graphByCompanyName(limit == null ? 100 : limit, name);
	}
    
    @GetMapping("/graphGetAffiliations")
    public Map<String, Object> graphGetAffiliations(@RequestParam(value = "limit", required = false) Integer limit,
    		@RequestParam(value = "name", required = true) String name){
    	return companyService.graphGetAffiliations(limit == null ? 100 : limit, name);
    }
    
    @PostMapping(path="/create",consumes="application/json",produces="application/json")
    public void addMember(@RequestBody Company company) {
    	companyService.createOrUpdate(company.getAlias(),company.getCompanyid(),company.getCompanyname(), company.getDescription(), company.getInternalcompanyid(), company.getOrgtypeid());
    }
}
