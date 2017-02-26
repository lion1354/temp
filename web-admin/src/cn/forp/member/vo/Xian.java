package cn.forp.member.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cn.forp.insurance.vo.Accident;
import cn.forp.insurance.vo.CompensationForm;
import cn.forp.insurance.vo.Tractor;

public class Xian implements Serializable
{
	private static final long serialVersionUID = -8835549433046922956L;

	private Integer xianId;
	private String xianName;
	private Area area;
	private Set<Tractor> tractorSet = new HashSet<Tractor>();
	private Set<CompensationForm> compensationSet = new HashSet<CompensationForm>();
	private Set<User> userSet = new HashSet<User>();
	private Set<Accident> accidentSet = new HashSet<Accident>();

	public Integer getXianId()
	{
		return xianId;
	}

	public void setXianId(Integer xianId)
	{
		this.xianId = xianId;
	}

	public String getXianName()
	{
		return xianName;
	}

	public void setXianName(String xianName)
	{
		this.xianName = xianName;
	}

	public Area getArea()
	{
		return area;
	}

	public void setArea(Area area)
	{
		this.area = area;
	}

	public Set<Tractor> getTractorSet()
	{
		return tractorSet;
	}

	public void setTractorSet(Set<Tractor> tractorSet)
	{
		this.tractorSet = tractorSet;
	}

	public Set<CompensationForm> getCompensationSet()
	{
		return compensationSet;
	}

	public void setCompensationSet(Set<CompensationForm> compensationSet)
	{
		this.compensationSet = compensationSet;
	}

	public Set<User> getUserSet()
	{
		return userSet;
	}

	public void setUserSet(Set<User> userSet)
	{
		this.userSet = userSet;
	}

	public Set<Accident> getAccidentSet()
	{
		return accidentSet;
	}

	public void setAccidentSet(Set<Accident> accidentSet)
	{
		this.accidentSet = accidentSet;
	}

}
