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
 * @ClassName: ProManagement
 * @Description: TODO(提现历史)
 * @author dave
 * @date 2017年9月25日 下午7:50:24
 */
@Entity
@Table(name = "bm_pro_management")
public class ProManagement implements java.io.Serializable {

	private static final long serialVersionUID = 130219713828003957L;
	/**
	 * 提现历史id
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
	 * 提现金额
	 */
	@Column(name = "AMOUNT_MONEY")
	private BigDecimal amountMoney;

	/**
	 * 提现时间
	 */
	@Column(name = "APP_TIME")
	private Date appTime;

	/**
	 * 类型（打款）
	 */
	@Column(name = "PRO_TYPE")
	private String proType;

	/**
	 * 分润剩余总额
	 */
	@Column(name = "TRT_PROFIT")
	private BigDecimal trtProfit;

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

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public BigDecimal getAmountMoney() {
		return amountMoney;
	}

	public void setAmountMoney(BigDecimal amountMoney) {
		this.amountMoney = amountMoney;
	}

	public Date getAppTime() {
		return appTime;
	}

	public void setAppTime(Date appTime) {
		this.appTime = appTime;
	}

	public BigDecimal getTrtProfit() {
		return trtProfit;
	}

	public void setTrtProfit(BigDecimal trtProfit) {
		this.trtProfit = trtProfit;
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

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

}
