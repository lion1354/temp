package com.popular.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.popular.common.util.DateUtils;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 *         2016/7/27
 */
@JsonInclude(Include.NON_NULL)
public class Flower implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 972896901544575235L;

	public Flower() {
		super();
	}

	public Flower(JSONObject flower) {
		super();
		this.id = flower.containsKey("id") ? flower.getInt("id") : null;
		this.sender = flower.containsKey("sender") ? flower.getInt("sender") : null;
		this.recipient = flower.containsKey("recipient") ? flower.getInt("recipient") : null;
		try {
			this.sendTime = flower.containsKey("sendTime") ? DateUtils.parse(flower.getString("sendTime")) : new Date();
			this.createTime = flower.containsKey("createTime") ? DateUtils.parse(flower.getString("createTime"))
					: new Date();
			this.updateTime = flower.containsKey("updateTime") ? DateUtils.parse(flower.getString("updateTime"))
					: new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Flower(Integer sender, Integer recipient) {
		super();
		this.sender = sender;
		this.recipient = recipient;
	}

	private Integer id;
	private Integer sender;
	private Integer recipient;
	private Date sendTime;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSender() {
		return sender;
	}

	public void setSender(Integer sender) {
		this.sender = sender;
	}

	public Integer getRecipient() {
		return recipient;
	}

	public void setRecipient(Integer recipient) {
		this.recipient = recipient;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
