package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, String> {
	
	public Template findByIdAndOrgi(String id, String orgi);
	public List<Template> findByTemplettypeAndOrgi(String templettype, String orgi);
	public List<Template> findByOrgi(String orgi) ;
}
