package com.crazy.web.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.crazy.util.UKTools;

/**
 * @ClassName: Bill
 * @Description: TODO(账单实体类)
 * @author dave
 * @date 2017年10月13日 下午7:47:33
 */

@Entity
@Table(name = "bm_bill")
@org.hibernate.annotations.Proxy(lazy = false)
public class Bill implements java.io.Serializable {

	private static final long serialVersionUID = -7230722488818098146L;

	/**
	 * 账单ID
	 */
	@Id
	private String id = UKTools.getUUID().toLowerCase();

	/**
	 * 用户名
	 */
	@Column(name = "USER_NAME")
	private String userName;

	/**
	 * 账单类型
	 */
	@Column(name = "BILL_TYPE")
	private Integer billType;

	/**
	 * 收入金额
	 */
	@Column(name = "INCOME_AMOUNT")
	private BigDecimal incomeAmount;

	/**
	 * 支出金额
	 */
	@Column(name = "EXP_AMOUNT")
	private BigDecimal expAmount;

	/**
	 * 时间(账单)
	 */
	@Column(name = "BILL_TIME")
	private Date billTime = new Date();

	/**
	 * 方式
	 */
	@Column(name = "BILL_MODE")
	private String billMode;

	/**
	 * 账户金额
	 */
	@Column(name = "ACCOUNT_AMOUNT")
	private BigDecimal accountAmount;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

	/**
	 * 是否删除0未1已
	 */
	@Column(name = "IS_DEL")
	private int isDel;

	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "assigned")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public BigDecimal getExpAmount() {
		return expAmount;
	}

	public void setExpAmount(BigDecimal expAmount) {
		this.expAmount = expAmount;
	}

	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	public String getBillMode() {
		return billMode;
	}

	public void setBillMode(String billMode) {
		this.billMode = billMode;
	}

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

}
