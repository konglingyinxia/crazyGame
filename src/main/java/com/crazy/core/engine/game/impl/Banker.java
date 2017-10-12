package com.crazy.core.engine.game.impl;

import com.crazy.core.engine.game.Message;

import java.io.Serializable;

public class Banker implements Message,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9011347288522873348L;
	private String command ;
	private String userid ;
	
	public Banker(String userid){
		this.userid = userid ;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
