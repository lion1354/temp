package cn.forp.stastics.vo;

import java.io.Serializable;

import cn.forp.framework.core.rowmapper.DBTable;

@DBTable(name = "tlj")
public class TractorStastics implements Serializable
{
	private static final long serialVersionUID = -4570405645600786301L;

	private Integer rowspan;
	private String diqu;
	private String xian;
	private String jishen_100;
	private String jishen_150;
	private String jishen_200;
	private String jishen_250;
	private String jishen_300;
	private String jishen_400;
	private String jishen_500;
	private String jishen_jifen;
	private String jishen_fenshu;
	private String jishen_money;
	private String driver_operate_100;
	private String driver_operate_200;
	private String driver_operate_jifen;
	private String driver_operate_fenshu;
	private String driver_operate_money;
	private String driver_mainten_50;
	private String driver_mainten_100;
	private String driver_mainten_jifen;
	private String driver_mainten_fenshu;
	private String driver_mainten_money;
	private String little_driver_jifen;
	private String little_driver_fenshu;
	private String little_driver_money;
	private String trd_100;
	private String trd_200;
	private String trd_300;
	private String trd_400;
	private String trd_jifen;
	private String trd_fenshu;
	private String trd_money;
	private String trd_shengbu;
	private String totle_shengbu;
	private String totle_jifen;
	private String totle_fenshu;
	private String totle_money;
	private String third_driver_350;
	private String third_driver_xianbu_fenshu;
	private String third_driver_shengbu_fenshu;
	private String third_driver_xianbu_money;
	private String third_driver_shengbu_money;
	private String little_third_driver_fenshu;
	private String little_third_driver_money;
	private String totle_jifen_fenshu;
	private String totle_jifen_money;

	public String getTotle_jifen_fenshu()
	{
		return totle_jifen_fenshu;
	}

	public void setTotle_jifen_fenshu(String totle_jifen_fenshu)
	{
		this.totle_jifen_fenshu = totle_jifen_fenshu;
	}

	public String getTotle_jifen_money()
	{
		return totle_jifen_money;
	}

	public void setTotle_jifen_money(String totle_jifen_money)
	{
		this.totle_jifen_money = totle_jifen_money;
	}

	public String getThird_driver_350()
	{
		return third_driver_350;
	}

	public void setThird_driver_350(String third_driver_350)
	{
		this.third_driver_350 = third_driver_350;
	}

	public String getThird_driver_xianbu_fenshu()
	{
		return third_driver_xianbu_fenshu;
	}

	public void setThird_driver_xianbu_fenshu(String third_driver_xianbu_fenshu)
	{
		this.third_driver_xianbu_fenshu = third_driver_xianbu_fenshu;
	}

	public String getThird_driver_shengbu_fenshu()
	{
		return third_driver_shengbu_fenshu;
	}

	public void setThird_driver_shengbu_fenshu(String third_driver_shengbu_fenshu)
	{
		this.third_driver_shengbu_fenshu = third_driver_shengbu_fenshu;
	}

	public String getThird_driver_xianbu_money()
	{
		return third_driver_xianbu_money;
	}

	public void setThird_driver_xianbu_money(String third_driver_xianbu_money)
	{
		this.third_driver_xianbu_money = third_driver_xianbu_money;
	}

	public String getThird_driver_shengbu_money()
	{
		return third_driver_shengbu_money;
	}

	public void setThird_driver_shengbu_money(String third_driver_shengbu_money)
	{
		this.third_driver_shengbu_money = third_driver_shengbu_money;
	}

	public String getLittle_third_driver_fenshu()
	{
		return little_third_driver_fenshu;
	}

	public void setLittle_third_driver_fenshu(String little_third_driver_fenshu)
	{
		this.little_third_driver_fenshu = little_third_driver_fenshu;
	}

	public String getLittle_third_driver_money()
	{
		return little_third_driver_money;
	}

	public void setLittle_third_driver_money(String little_third_driver_money)
	{
		this.little_third_driver_money = little_third_driver_money;
	}

	public Integer getRowspan()
	{
		return rowspan;
	}

	public void setRowspan(Integer rowspan)
	{
		this.rowspan = rowspan;
	}

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

	public String getJishen_100()
	{
		return jishen_100;
	}

	public void setJishen_100(String jishen_100)
	{
		this.jishen_100 = jishen_100;
	}

	public String getJishen_150()
	{
		return jishen_150;
	}

	public void setJishen_150(String jishen_150)
	{
		this.jishen_150 = jishen_150;
	}

	public String getJishen_200()
	{
		return jishen_200;
	}

	public void setJishen_200(String jishen_200)
	{
		this.jishen_200 = jishen_200;
	}

	public String getJishen_300()
	{
		return jishen_300;
	}

	public void setJishen_300(String jishen_300)
	{
		this.jishen_300 = jishen_300;
	}

	public String getJishen_400()
	{
		return jishen_400;
	}

	public void setJishen_400(String jishen_400)
	{
		this.jishen_400 = jishen_400;
	}

	public String getJishen_jifen()
	{
		return jishen_jifen;
	}

	public void setJishen_jifen(String jishen_jifen)
	{
		this.jishen_jifen = jishen_jifen;
	}

	public String getJishen_fenshu()
	{
		return jishen_fenshu;
	}

	public void setJishen_fenshu(String jishen_fenshu)
	{
		this.jishen_fenshu = jishen_fenshu;
	}

	public String getJishen_money()
	{
		return jishen_money;
	}

	public void setJishen_money(String jishen_money)
	{
		this.jishen_money = jishen_money;
	}

	public String getDriver_operate_100()
	{
		return driver_operate_100;
	}

	public void setDriver_operate_100(String driver_operate_100)
	{
		this.driver_operate_100 = driver_operate_100;
	}

	public String getDriver_operate_200()
	{
		return driver_operate_200;
	}

	public void setDriver_operate_200(String driver_operate_200)
	{
		this.driver_operate_200 = driver_operate_200;
	}

	public String getDriver_operate_jifen()
	{
		return driver_operate_jifen;
	}

	public void setDriver_operate_jifen(String driver_operate_jifen)
	{
		this.driver_operate_jifen = driver_operate_jifen;
	}

	public String getDriver_operate_fenshu()
	{
		return driver_operate_fenshu;
	}

	public void setDriver_operate_fenshu(String driver_operate_fenshu)
	{
		this.driver_operate_fenshu = driver_operate_fenshu;
	}

	public String getDriver_operate_money()
	{
		return driver_operate_money;
	}

	public void setDriver_operate_money(String driver_operate_money)
	{
		this.driver_operate_money = driver_operate_money;
	}

	public String getDriver_mainten_50()
	{
		return driver_mainten_50;
	}

	public void setDriver_mainten_50(String driver_mainten_50)
	{
		this.driver_mainten_50 = driver_mainten_50;
	}

	public String getDriver_mainten_100()
	{
		return driver_mainten_100;
	}

	public void setDriver_mainten_100(String driver_mainten_100)
	{
		this.driver_mainten_100 = driver_mainten_100;
	}

	public String getDriver_mainten_jifen()
	{
		return driver_mainten_jifen;
	}

	public void setDriver_mainten_jifen(String driver_mainten_jifen)
	{
		this.driver_mainten_jifen = driver_mainten_jifen;
	}

	public String getDriver_mainten_fenshu()
	{
		return driver_mainten_fenshu;
	}

	public void setDriver_mainten_fenshu(String driver_mainten_fenshu)
	{
		this.driver_mainten_fenshu = driver_mainten_fenshu;
	}

	public String getDriver_mainten_money()
	{
		return driver_mainten_money;
	}

	public void setDriver_mainten_money(String driver_mainten_money)
	{
		this.driver_mainten_money = driver_mainten_money;
	}

	public String getLittle_driver_jifen()
	{
		return little_driver_jifen;
	}

	public void setLittle_driver_jifen(String little_driver_jifen)
	{
		this.little_driver_jifen = little_driver_jifen;
	}

	public String getLittle_driver_fenshu()
	{
		return little_driver_fenshu;
	}

	public void setLittle_driver_fenshu(String little_driver_fenshu)
	{
		this.little_driver_fenshu = little_driver_fenshu;
	}

	public String getLittle_driver_money()
	{
		return little_driver_money;
	}

	public void setLittle_driver_money(String little_driver_money)
	{
		this.little_driver_money = little_driver_money;
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

	public String getTrd_300()
	{
		return trd_300;
	}

	public void setTrd_300(String trd_300)
	{
		this.trd_300 = trd_300;
	}

	public String getTrd_400()
	{
		return trd_400;
	}

	public void setTrd_400(String trd_400)
	{
		this.trd_400 = trd_400;
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

	public String getTotle_money()
	{
		return totle_money;
	}

	public void setTotle_money(String totle_money)
	{
		this.totle_money = totle_money;
	}

	public String getTrd_shengbu()
	{
		return trd_shengbu;
	}

	public void setTrd_shengbu(String trd_shengbu)
	{
		this.trd_shengbu = trd_shengbu;
	}

	public String getTotle_shengbu()
	{
		return totle_shengbu;
	}

	public void setTotle_shengbu(String totle_shengbu)
	{
		this.totle_shengbu = totle_shengbu;
	}

	public String getJishen_250()
	{
		return jishen_250;
	}

	public void setJishen_250(String jishen_250)
	{
		this.jishen_250 = jishen_250;
	}

	public String getJishen_500()
	{
		return jishen_500;
	}

	public void setJishen_500(String jishen_500)
	{
		this.jishen_500 = jishen_500;
	}

}
