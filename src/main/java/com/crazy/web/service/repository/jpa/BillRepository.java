package com.crazy.web.service.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.crazy.web.model.Bill;

public interface BillRepository extends JpaRepository<Bill, String>, JpaSpecificationExecutor<Bill> {

}
