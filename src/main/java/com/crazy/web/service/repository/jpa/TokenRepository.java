package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface TokenRepository extends JpaRepository<Token, String>
{
	public abstract Token findById(String id);
	
	public abstract List<Token> findByUserid(String userid);
}
