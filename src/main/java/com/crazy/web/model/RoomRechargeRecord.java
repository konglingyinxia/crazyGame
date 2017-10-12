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
 * 描述：房卡充值记录 创建人：田帅
 */
@Entity
@Table(name = "bm_room_recharge_record")
@org.hibernate.annotations.Proxy(lazy = false)
public class RoomRechargeRecord implements java.io.Serializable {

	private static final long serialVersionUID = 4221247375708688312L;

	@Id
	private String id = UKTools.getUUID().toLowerCase();

	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 邀请码
	 */
	@Column(name = "invitation_code")
	private String invitationCode;

	/**
	 * 充值时间
	 */
	@Column(name = "recharge_time")
	private Date rechargeTime = new Date();

	/**
	 * 房卡数量
	 */
	@Column(name = "room_count")
	private Integer roomCount;

	/**
	 * 支付金额
	 */
	@Column(name = "pay_amount")
	private BigDecimal payAmount;

	/**
	 * 直接上家分润金额
	 */
	@Column(name = "directly_the_last_amount")
	private BigDecimal directlyTheLastAmount;

	/**
	 * 间接上家分润金额
	 */
	@Column(name = "indirect_the_last_amount")
	private BigDecimal indirectTheLastAmount;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime = new Date();

	/**
	 * 是否删除 0：未删除 1：已删除
	 */
	@Column(name = "is_Del")
	private Integer isDel;

	@Id
	@Column(length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
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
		this.userName = userName == null ? null : userName.trim();
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode == null ? null : invitationCode.trim();
	}

	public Date getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getDirectlyTheLastAmount() {
		return directlyTheLastAmount;
	}

	public void setDirectlyTheLastAmount(BigDecimal directlyTheLastAmount) {
		this.directlyTheLastAmount = directlyTheLastAmount;
	}

	public BigDecimal getIndirectTheLastAmount() {
		return indirectTheLastAmount;
	}

	public void setIndirectTheLastAmount(BigDecimal indirectTheLastAmount) {
		this.indirectTheLastAmount = indirectTheLastAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

}
