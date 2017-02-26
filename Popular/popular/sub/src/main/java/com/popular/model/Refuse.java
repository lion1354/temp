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
public class Refuse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4520337246855637771L;

	public Refuse() {
		super();
	}

	public Refuse(ClientUser owner, ClientUser target) {
		super();
		this.owner = owner;
		this.target = target;
	}

	public Refuse(ClientUser owner, ClientUser target, Integer refuseCount, Date createTime) {
		super();
		this.owner = owner;
		this.target = target;
		this.refuseCount = refuseCount;
		this.createTime = createTime;
	}

	public Refuse(JSONObject refuse) {
		super();
		this.id = refuse.containsKey("id") ? refuse.getInt("id") : null;
		this.owner = refuse.containsKey("ownerId") ? new ClientUser(refuse.getInt("ownerId")) : null;
		this.target = refuse.containsKey("targetId") ? new ClientUser(refuse.getInt("targetId")) : null;
		this.refuseCount = refuse.containsKey("refuseCount") ? refuse.getInt("refuseCount") : null;
		try {
			this.createTime = refuse.containsKey("createTime") ? DateUtils.parse(refuse.getString("createTime"))
					: new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Integer id;
	private ClientUser owner;
	private ClientUser target;
	private Integer refuseCount;
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

	public Integer getRefuseCount() {
		return refuseCount;
	}

	public void setRefuseCount(Integer refuseCount) {
		this.refuseCount = refuseCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
