package cn.forp.test;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Entity
{
	private String reqParameters;
	private List<RuleSet> ruleSets;
	private String serciceName;

	public Entity()
	{
	}

	public Entity(String reqParameters, List<RuleSet> ruleSets, String serciceName)
	{
		this.reqParameters = reqParameters;
		this.ruleSets = ruleSets;
		this.serciceName = serciceName;
	}

	@XmlElement(name = "reqParameters")
	public String getReqParameters()
	{
		return reqParameters;
	}

	public void setReqParameters(String reqParameters)
	{
		this.reqParameters = reqParameters;
	}

	@XmlElementWrapper(name = "ruleSets")
	@XmlElement(name = "ruleSet")
	public List<RuleSet> getRuleSets()
	{
		return ruleSets;
	}

	public void setRuleSets(List<RuleSet> ruleSets)
	{
		this.ruleSets = ruleSets;
	}

	@XmlElement(name = "serciceName")
	public String getSerciceName()
	{
		return serciceName;
	}

	public void setSerciceName(String serciceName)
	{
		this.serciceName = serciceName;
	}

}
