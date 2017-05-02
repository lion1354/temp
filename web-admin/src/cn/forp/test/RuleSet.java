package cn.forp.test;

import javax.xml.bind.annotation.XmlAttribute;

public class RuleSet
{
	private String type;
	private String fields;
	private String associateFields;

	public RuleSet()
	{
	}

	public RuleSet(String type, String fields, String associateFields)
	{
		this.type = type;
		this.fields = fields;
		this.associateFields = associateFields;
	}

	@XmlAttribute(name = "type")
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@XmlAttribute(name = "fields")
	public String getFields()
	{
		return fields;
	}

	public void setFields(String fields)
	{
		this.fields = fields;
	}

	@XmlAttribute(name = "associateFields")
	public String getAssociateFields()
	{
		return associateFields;
	}

	public void setAssociateFields(String associateFields)
	{
		this.associateFields = associateFields;
	}

}
