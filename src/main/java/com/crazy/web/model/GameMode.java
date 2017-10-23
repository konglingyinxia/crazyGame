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
@Table(name = "bm_game_mode")
@org.hibernate.annotations.Proxy(lazy = false)
public class GameMode implements java.io.Serializable {

	private static final long serialVersionUID = -1786131925741615325L;

	/**
	 * 赛制id
	 */
	private String id = UKTools.getUUID();

	/**
	 * 玩家id
	 */
	@Column(name = "playuser_id")
	private String playUserId;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 是否在线0否1是
	 */
	private Integer online;

	/**
	 * 是否在游戏中0否1是
	 */
	private Integer ingame;

	/**
	 * 周赛卡
	 */
	@Column(name = "week_game_card")
	private String weekGameCard;

	/**
	 * 月赛卡
	 */
	@Column(name = "month_game_card")
	private String monthGameCard;

	/**
	 * 获胜次数
	 */
	@Column(name = "win_num")
	private Integer winNum;

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

	public String getPlayUserId() {
		return playUserId;
	}

	public void setPlayUserId(String playUserId) {
		this.playUserId = playUserId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public String getWeekGameCard() {
		return weekGameCard;
	}

	public void setWeekGameCard(String weekGameCard) {
		this.weekGameCard = weekGameCard;
	}

	public String getMonthGameCard() {
		return monthGameCard;
	}

	public void setMonthGameCard(String monthGameCard) {
		this.monthGameCard = monthGameCard;
	}

	public Integer getWinNum() {
		return winNum;
	}

	public void setWinNum(Integer winNum) {
		this.winNum = winNum;
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

	public Integer getIngame() {
		return ingame;
	}

	public void setIngame(Integer ingame) {
		this.ingame = ingame;
	}

}
