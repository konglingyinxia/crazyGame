package com.crazy.web.service.repository.es;

import com.crazy.web.model.Token;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public abstract interface TokenESRepository extends ElasticsearchCrudRepository<Token, String>
{
	public abstract Token findById(String id);
	
	public abstract List<Token> findByUserid(String userid);
}
