package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.MetadataTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface MetadataRepository extends JpaRepository<MetadataTable, String>{
	public abstract MetadataTable findById(String id);
	
	public abstract MetadataTable findByTablename(String tablename);

	public abstract Page<MetadataTable> findAll(Pageable paramPageable);
	
	public abstract int countByTablename(String tableName) ;
}
