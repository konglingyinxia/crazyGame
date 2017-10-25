package com.crazy.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.crazy.util.UKTools;

@Entity
@Table(name = "bm_record")
@org.hibernate.annotations.Proxy(lazy = false)
public class Record implements java.io.Serializable {

	private static final long serialVersionUID = 2253884699081642810L;

	/**
	 * 战绩id
	 */
	private String id = UKTools.getUUID();

	/**
	 * 玩家id
	 */
	@Column(name = "playuser_id")
	private String playuserId;

	/**
	 * 房间号
	 */
	@Column(name = "room_num")
	private String roomNum;

	/**
	 * 局数
	 */
	@Column(name = "game_num")
	private String gameNum;

	/**
	 * 时间
	 */
	private Date time;

	/**
	 * 玩家游戏信息
	 */
	@Column(name = "gamer_info")
	private String gamerInfo;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime = new Date();

	/**
	 * 是否删除0未1已
	 */
	@Column(name = "is_del")
	private Integer isDel;

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

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getGameNum() {
		return gameNum;
	}

	public void setGameNum(String gameNum) {
		this.gameNum = gameNum;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getGamerInfo() {
		return gamerInfo;
	}

	public void setGamerInfo(String gamerInfo) {
		this.gamerInfo = gamerInfo;
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

	public String getPlayuserId() {
		return playuserId;
	}

	public void setPlayuserId(String playuserId) {
		this.playuserId = playuserId;
	}

}
