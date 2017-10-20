package com.crazy.web.service.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.crazy.web.model.Record;

public abstract interface RecordRepository  extends JpaRepository<Record, String>, JpaSpecificationExecutor<Record> {

}
