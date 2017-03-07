package cn.forp.test;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "service")
public class Service
{
	public Service()
	{
	}

	public Service(List<ServiceEntity> serviceEntitys)
	{
		this.serviceEntitys = serviceEntitys;
	}

	private List<ServiceEntity> serviceEntitys;

	@XmlElement(name = "serviceEntity")
	public List<ServiceEntity> getServiceEntitys()
	{
		return serviceEntitys;
	}

	public void setServiceEntitys(List<ServiceEntity> serviceEntitys)
	{
		this.serviceEntitys = serviceEntitys;
	}

}
