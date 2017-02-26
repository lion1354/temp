package cn.forp.insurance.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Apple
 *
 */
public class ClassType implements Serializable
{
	private static final long serialVersionUID = -6090857469638112871L;
	private Integer typeId;
	private String typeName;
	private Set<Tractor> tractorSet = new HashSet<Tractor>();

	public Set<Tractor> getTractorSet()
	{
		return tractorSet;
	}

	public void setTractorSet(Set<Tractor> tractorSet)
	{
		this.tractorSet = tractorSet;
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

}
