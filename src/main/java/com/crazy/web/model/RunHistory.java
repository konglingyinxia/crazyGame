package com.crazy.web.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.crazy.util.UKTools;

/**
 * @ClassName: RunHistory
 * @Description: TODO(分润历史实体类)
 * @author dave
 * @date 2017年9月28日 下午2:48:35
 */
@Entity
@Table(name = "bm_run_history")
@org.hibernate.annotations.Proxy(lazy = false)
public class RunHistory implements java.io.Serializable {

	private static final long serialVersionUID = -1252067657318920642L;

	/**
	 * 分润历史id
	 */
	@Id
	private String id = UKTools.getUUID().toLowerCase();

	/**
	 * 用户名
	 */
	@Column(name = "USER_NAME")
	private String userName;

	/**
	 * 邀请码
	 */
	@Column(name = "INVITATION_CODE")
	private String invitationCode;

	/**
	 * 获得分润金额
	 */
	@Column(name = "GET_PROFIT_AMOUNT")
	private BigDecimal getProfitAmount;

	/**
	 * 来源人姓名
	 */
	@Column(name = "SOURCE_NAME")
	private String sourceName;

	/**
	 * 来源人ID
	 */
	@Column(name = "SOURCE_ID")
	private String sourceId;

	/**
	 * 获得分润时间
	 */
	@Column(name = "GET_PROFIT_TIME")
	private Date getProfitTime = new Date();

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

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public BigDecimal getGetProfitAmount() {
		return getProfitAmount;
	}

	public void setGetProfitAmount(BigDecimal getProfitAmount) {
		this.getProfitAmount = getProfitAmount;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public Date getGetProfitTime() {
		return getProfitTime;
	}

	public void setGetProfitTime(Date getProfitTime) {
		this.getProfitTime = getProfitTime;
	}

}
