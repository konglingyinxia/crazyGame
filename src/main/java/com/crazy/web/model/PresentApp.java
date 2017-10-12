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
 * @ClassName: PresentApp
 * @Description: TODO(提现记录表)
 * @author dave
 * @date 2017年9月25日 下午7:39:01
 */
@Entity
@Table(name = "bm_present_app")
@org.hibernate.annotations.Proxy(lazy = false)
public class PresentApp implements java.io.Serializable {

	private static final long serialVersionUID = -5507168273437101066L;

	/**
	 * 提现主键id
	 */
	@Id
	private String id = UKTools.getUUID().toLowerCase();

	/**
	 * 申请单号
	 */
	@Column(name = "APPLICATION_NUM")
	private String applicationNum;

	/**
	 * 人员表主键id
	 */
	@Column(name = "PLAY_USER_ID")
	private String playUserId;

	/**
	 * 用户名
	 */
	@Column(name = "USER_NAME")
	private String userName;

	/**
	 * 微信id
	 */
	private String openid;

	/**
	 * 邀请码
	 */
	@Column(name = "INVITATION_CODE")
	private String invitationCode;

	/**
	 * 申请提现时间
	 */
	@Column(name = "PRESENT_APP_TIME")
	private Date presentAppTime = new Date();

	/**
	 * 提现金额
	 */
	@Column(name = "AMOUNT_MONEY")
	private BigDecimal amountMoney;

	/**
	 * 提现状态0未1已2拒绝
	 */
	@Column(name = "PRE_STATE")
	private int preState;

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

	public String getApplicationNum() {
		return applicationNum;
	}

	public void setApplicationNum(String applicationNum) {
		this.applicationNum = applicationNum;
	}

	public String getPlayUserId() {
		return playUserId;
	}

	public void setPlayUserId(String playUserId) {
		this.playUserId = playUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public Date getPresentAppTime() {
		return presentAppTime;
	}

	public void setPresentAppTime(Date presentAppTime) {
		this.presentAppTime = presentAppTime;
	}

	public BigDecimal getAmountMoney() {
		return amountMoney;
	}

	public void setAmountMoney(BigDecimal amountMoney) {
		this.amountMoney = amountMoney;
	}

	public int getPreState() {
		return preState;
	}

	public void setPreState(int preState) {
		this.preState = preState;
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
