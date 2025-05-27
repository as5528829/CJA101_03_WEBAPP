package com.foodtimetest.direct_message;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class DirectMessageVO implements Serializable{
	private Integer dmId;
	private Integer memId;
	private Integer smgrId;
	private String messContent;
	private String messTime;
	private Integer messDirection;
	public Integer getDmId() {
		return dmId;
	}
	public void setDmId(Integer dmId) {
		this.dmId = dmId;
	}
	public Integer getMemId() {
		return memId;
	}
	public void setMemId(Integer memId) {
		this.memId = memId;
	}
	public Integer getSmgrId() {
		return smgrId;
	}
	public void setSmgrId(Integer smgrId) {
		this.smgrId = smgrId;
	}
	public String getMessContent() {
		return messContent;
	}
	public void setMessContent(String messContent) {
		this.messContent = messContent;
	}
	public String getMessTime() {
		return messTime;
	}
	public void setMessTime(String messTime) {
		this.messTime = messTime;
	}
	public Integer getMessDirection() {
		return messDirection;
	}
	public void setMessDirection(Integer messDirection) {
		this.messDirection = messDirection;
	}
	
}
