package cn.forp.test;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ServiceEntity
{
	private String serviceID;
	private Entity entity;

	public ServiceEntity()
	{
	}

	public ServiceEntity(String serviceID, Entity entity)
	{
		this.serviceID = serviceID;
		this.entity = entity;
	}

	@XmlAttribute(name = "serviceID")
	public String getServiceID()
	{
		return serviceID;
	}

	public void setServiceID(String serviceID)
	{
		this.serviceID = serviceID;
	}

	@XmlElement(name = "entity")
	public Entity getEntity()
	{
		return entity;
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}

}
