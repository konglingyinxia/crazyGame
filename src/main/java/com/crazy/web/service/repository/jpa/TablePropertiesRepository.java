package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.TableProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface TablePropertiesRepository extends JpaRepository<TableProperties, String>{
	
	public abstract TableProperties findById(String id);

	public abstract List<TableProperties> findByDbtableid(String dbtableid) ;
}
