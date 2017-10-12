package com.crazy.web.model;

import org.hibernate.annotations.GenericGenerator;

import com.crazy.util.UKTools;

import javax.persistence.*;
import java.util.Date;

/**
 * 描述：房卡使用记录 创建人：田帅
 */
@Entity
@Table(name = "bm_room_touse_record")
@org.hibernate.annotations.Proxy(lazy = false)
public class RoomTouseRecord implements java.io.Serializable {

	private static final long serialVersionUID = -9129265144867143069L;

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
	 * 使用时间
	 */
	@Column(name = "use_time")
	private Date useTime = new Date();

	/**
	 * 使用房卡数量
	 */
	@Column(name = "use_room_count")
	private Integer useRoomCount;

	/**
	 * 剩余房卡数量
	 */
	@Column(name = "surplus_room_count")
	private Integer surplusRoomCount;

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

	/**
	 * 使用时间
	 */
	@Column(name = "use_times")
	private String useTimes;

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

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public Integer getUseRoomCount() {
		return useRoomCount;
	}

	public void setUseRoomCount(Integer useRoomCount) {
		this.useRoomCount = useRoomCount;
	}

	public Integer getSurplusRoomCount() {
		return surplusRoomCount;
	}

	public void setSurplusRoomCount(Integer surplusRoomCount) {
		this.surplusRoomCount = surplusRoomCount;
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

	public String getUseTimes() {
		return useTimes;
	}

	public void setUseTimes(String useTimes) {
		this.useTimes = useTimes;
	}
}
