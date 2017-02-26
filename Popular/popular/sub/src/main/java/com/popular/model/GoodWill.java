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
 * @Date 2016年7月28日
 */
@JsonInclude(Include.NON_NULL)
public class GoodWill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5750168958969007816L;

	public GoodWill() {
		super();
	}

	public GoodWill(Integer id) {
		super();
		this.id = id;
	}

	public GoodWill(JSONObject goodWill) {
		super();
		this.id = goodWill.containsKey("id") ? goodWill.getInt("id") : null;
		this.owner = goodWill.containsKey("ownerId") ? new ClientUser(goodWill.getInt("ownerId")) : null;
		this.target = goodWill.containsKey("targetId") ? new ClientUser(goodWill.getInt("targetId")) : null;
		try {
			this.createTime = goodWill.containsKey("createTime") ? DateUtils.parse(goodWill.getString("createTime")) : new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public GoodWill(ClientUser owner, ClientUser target) {
		super();
		this.owner = owner;
		this.target = target;
	}

	public GoodWill(ClientUser owner, ClientUser target, Date createTime) {
		super();
		this.owner = owner;
		this.target = target;
		this.createTime = createTime;
	}

	private Integer id;
	private ClientUser owner;
	private ClientUser target;
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ClientUser getOwner() {
		return owner;
	}

	public void setOwner(ClientUser owner) {
		this.owner = owner;
	}

	public ClientUser getTarget() {
		return target;
	}

	public void setTarget(ClientUser target) {
		this.target = target;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
