package cn.forp.stastics.vo;

import java.io.Serializable;

import cn.forp.framework.core.rowmapper.DBTable;

@DBTable(name = "nj")
public class NongJiStastics implements Serializable
{
	private static final long serialVersionUID = 5762150192590630091L;

	private Integer rowspan;
	private String diqu;
	private String xian;
	private String fst_50;
	private String fst_100;
	private String fst_jifen;
	private String fst_fenshu;
	private String fst_money;
	private String trd_100;
	private String trd_200;
	private String trd_500;
	private String trd_600;
	private String trd_700;
	private String trd_jifen;
	private String trd_fenshu;
	private String trd_money;
	private String little_jifen;
	private String little_fenshu;
	private String little_money;
	private String driver_50;
	private String driver_100;
	private String driver_jifen;
	private String driver_fenshu;
	private String driver_money;
	private String totle_jifen;
	private String totle_fenshu;
	private String totle_money;

	public String getDiqu()
	{
		return diqu;
	}

	public void setDiqu(String diqu)
	{
		this.diqu = diqu;
	}

	public String getXian()
	{
		return xian;
	}

	public void setXian(String xian)
	{
		this.xian = xian;
	}

	public String getFst_50()
	{
		return fst_50;
	}

	public void setFst_50(String fst_50)
	{
		this.fst_50 = fst_50;
	}

	public String getFst_100()
	{
		return fst_100;
	}

	public void setFst_100(String fst_100)
	{
		this.fst_100 = fst_100;
	}

	public String getFst_jifen()
	{
		return fst_jifen;
	}

	public void setFst_jifen(String fst_jifen)
	{
		this.fst_jifen = fst_jifen;
	}

	public String getFst_fenshu()
	{
		return fst_fenshu;
	}

	public void setFst_fenshu(String fst_fenshu)
	{
		this.fst_fenshu = fst_fenshu;
	}

	public String getFst_money()
	{
		return fst_money;
	}

	public void setFst_money(String fst_money)
	{
		this.fst_money = fst_money;
	}

	public String getTrd_100()
	{
		return trd_100;
	}

	public void setTrd_100(String trd_100)
	{
		this.trd_100 = trd_100;
	}

	public String getTrd_200()
	{
		return trd_200;
	}

	public void setTrd_200(String trd_200)
	{
		this.trd_200 = trd_200;
	}

	public String getTrd_500()
	{
		return trd_500;
	}

	public void setTrd_500(String trd_500)
	{
		this.trd_500 = trd_500;
	}

	public String getTrd_600()
	{
		return trd_600;
	}

	public void setTrd_600(String trd_600)
	{
		this.trd_600 = trd_600;
	}

	public String getTrd_700()
	{
		return trd_700;
	}

	public void setTrd_700(String trd_700)
	{
		this.trd_700 = trd_700;
	}

	public String getTrd_jifen()
	{
		return trd_jifen;
	}

	public void setTrd_jifen(String trd_jifen)
	{
		this.trd_jifen = trd_jifen;
	}

	public String getTrd_fenshu()
	{
		return trd_fenshu;
	}

	public void setTrd_fenshu(String trd_fenshu)
	{
		this.trd_fenshu = trd_fenshu;
	}

	public String getTrd_money()
	{
		return trd_money;
	}

	public void setTrd_money(String trd_money)
	{
		this.trd_money = trd_money;
	}

	public String getDriver_50()
	{
		return driver_50;
	}

	public void setDriver_50(String driver_50)
	{
		this.driver_50 = driver_50;
	}

	public String getDriver_100()
	{
		return driver_100;
	}

	public void setDriver_100(String driver_100)
	{
		this.driver_100 = driver_100;
	}

	public String getDriver_jifen()
	{
		return driver_jifen;
	}

	public void setDriver_jifen(String driver_jifen)
	{
		this.driver_jifen = driver_jifen;
	}

	public String getDriver_fenshu()
	{
		return driver_fenshu;
	}

	public void setDriver_fenshu(String driver_fenshu)
	{
		this.driver_fenshu = driver_fenshu;
	}

	public String getDriver_money()
	{
		return driver_money;
	}

	public void setDriver_money(String driver_money)
	{
		this.driver_money = driver_money;
	}

	public String getTotle_jifen()
	{
		return totle_jifen;
	}

	public void setTotle_jifen(String totle_jifen)
	{
		this.totle_jifen = totle_jifen;
	}

	public String getTotle_fenshu()
	{
		return totle_fenshu;
	}

	public void setTotle_fenshu(String totle_fenshu)
	{
		this.totle_fenshu = totle_fenshu;
	}

	public String getTotle_money()
	{
		return totle_money;
	}

	public void setTotle_money(String totle_money)
	{
		this.totle_money = totle_money;
	}

	public Integer getColspan()
	{
		return rowspan;
	}

	public void setColspan(Integer colspan)
	{
		this.rowspan = colspan;
	}

	public String getLittle_jifen()
	{
		return little_jifen;
	}

	public void setLittle_jifen(String little_jifen)
	{
		this.little_jifen = little_jifen;
	}

	public String getLittle_fenshu()
	{
		return little_fenshu;
	}

	public void setLittle_fenshu(String little_fenshu)
	{
		this.little_fenshu = little_fenshu;
	}

	public String getLittle_money()
	{
		return little_money;
	}

	public void setLittle_money(String little_money)
	{
		this.little_money = little_money;
	}

}
