package cn.forp.member.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable
{
	private static final long serialVersionUID = 2216858418865532067L;

	private Integer userid;
	private String userName;
	private String passWord;
	private String email;
	private Date createDate;
	private Date modifyDate;
	private Integer isValid;
	private Area area;
	private Xian xian;
	private Set<Group> group = new HashSet<Group>();

	private Set<Role> role = new HashSet<Role>();

	public User()
	{
	}

	public User(Integer userid, String userName, String passWord, String email, Date createDate, Integer isValid)
	{
		super();
		this.userid = userid;
		this.userName = userName;
		this.passWord = passWord;
		this.email = email;
		this.createDate = createDate;
		this.isValid = isValid;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Integer getIsValid()
	{
		return isValid;
	}

	public void setIsValid(Integer isValid)
	{
		this.isValid = isValid;
	}

	public Integer getUserid()
	{
		return userid;
	}

	public void setUserid(Integer userid)
	{
		this.userid = userid;
	}

	public String getUserName()
	{
		return this.userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassWord()
	{
		return this.passWord;
	}

	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}

	public Area getArea()
	{
		return area;
	}

	public void setArea(Area area)
	{
		this.area = area;
	}

	public Set<Group> getGroup()
	{
		return group;
	}

	public void setGroup(Set<Group> group)
	{
		this.group = group;
	}

	public Date getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public Set<Role> getRole()
	{
		return role;
	}

	public void setRole(Set<Role> role)
	{
		this.role = role;
	}

	public Xian getXian()
	{
		return xian;
	}

	public void setXian(Xian xian)
	{
		this.xian = xian;
	}

}