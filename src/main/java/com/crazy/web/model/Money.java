package com.crazy.web.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bm_money")
@org.hibernate.annotations.Proxy(lazy = false)
public class Money implements java.io.Serializable {

	private static final long serialVersionUID = -4485563057289933297L;

	/**
	 * 账户余额id
	 */
	private Integer id;

	/**
	 * 账户余额
	 */
	private BigDecimal balance;

	/**
	 * 类别1微信2支付宝...
	 */
	private Integer type;

	@Id
	@Column(length = 10)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
