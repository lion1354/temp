package com.popular.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@JsonInclude(Include.NON_NULL)
public class Friend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2287353007379135509L;

	public Friend() {
		super();
	}

	public Friend(ClientUser owner, ClientUser friend) {
		super();
		this.owner = owner;
		this.friend = friend;
	}

	public Friend(ClientUser owner, ClientUser friend, Date createTime) {
		super();
		this.owner = owner;
		this.friend = friend;
		this.createTime = createTime;
	}

	private Integer id;
	private ClientUser owner;
	private ClientUser friend;
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

	public ClientUser getFriend() {
		return friend;
	}

	public void setFriend(ClientUser friend) {
		this.friend = friend;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
