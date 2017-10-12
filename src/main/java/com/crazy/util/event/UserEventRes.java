package com.crazy.util.event;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Transient;


@SuppressWarnings("rawtypes")
public abstract class UserEventRes {
	
	
	private ElasticsearchCrudRepository esRes ;
	private JpaRepository dbRes ;
	
	@Transient
	public ElasticsearchCrudRepository getEsRes() {
		return esRes;
	}
	public void setEsRes(ElasticsearchCrudRepository esRes) {
		this.esRes = esRes;
	}
	@Transient
	public JpaRepository getDbRes() {
		return dbRes;
	}
	public void setDbRes(JpaRepository dbRes) {
		this.dbRes = dbRes;
	}
	
}
