package cn.forp.insurance.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class HuiFei implements Serializable
{
	private static final long serialVersionUID = -2560423575107645060L;

	private Integer huifeiId;
	private double huifeiMoney;
	private Set<Tractor> tractorSet = new HashSet<Tractor>();

	public double getHuifeiMoney()
	{
		return huifeiMoney;
	}

	public void setHuifeiMoney(double huifeiMoney)
	{
		this.huifeiMoney = huifeiMoney;
	}

	public Integer getHuifeiId()
	{
		return huifeiId;
	}

	public void setHuifeiId(Integer huifeiId)
	{
		this.huifeiId = huifeiId;
	}

	public Set<Tractor> getTractorSet()
	{
		return tractorSet;
	}

	public void setTractorSet(Set<Tractor> tractorSet)
	{
		this.tractorSet = tractorSet;
	}

}
