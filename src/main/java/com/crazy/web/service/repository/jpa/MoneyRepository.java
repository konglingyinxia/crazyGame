package com.crazy.web.service.repository.jpa;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crazy.web.model.Money;

public abstract interface MoneyRepository extends JpaRepository<Money, String>, JpaSpecificationExecutor<Money> {

	public Money findByIdAndType(Integer id, Integer type);

	@Query(value = "update bm_money set balance = :ye where id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	public void setBalanceById(@Param("ye") BigDecimal ye, @Param("id") Integer id);

	public Money findById(Integer id);

}
