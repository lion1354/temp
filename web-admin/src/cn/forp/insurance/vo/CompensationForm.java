package cn.forp.insurance.vo;

import java.io.Serializable;
import java.util.Date;

import cn.forp.member.vo.Xian;

public class CompensationForm implements Serializable
{
	private static final long serialVersionUID = 7719494234133832037L;
	private String compensationFormId;

	private Integer compensationType;
	private Integer carType;
	private Date accidentDate;
	private String accidentAddress;
	private String driverName;
	private String driverLience;
	private String accidentDeiscription;
	private String jiShenExpression;
	private Double jishencompensationMoney;
	private Double jishenChangeMoney;
	private Double jishenMentainMoney;
	private Double jishenSupportMoney;

	private String driverExpression;
	private Double drivercompensationMoney;
	private Double driverDeathMoney;
	private Double driverMedicalMoney;
	private Double driverSupportMoney;

	private String thirdExpression;
	private Double thirdcompensationMoney;
	private Double thirdPropertyMoney;
	private Double thirdMedicalMoney;
	private Double thirdSupportMoney;

	private Double totalMoney;
	private Double leftMoney;
	// 0:invalid 1:normal 2:fast track
	private Integer status;
	private String accidentMemberName;
	private Date memberSignDate;
	private String checkOrg;
	private String checkAgent;
	private Date checkerApproveDate;
	private String provinceAgent;
	private Date provinceAgentSignDate;
	private String compensationYear;
	private Tractor tractor;

	private Xian xian;

	public String getCompensationFormId()
	{
		return compensationFormId;
	}

	public void setCompensationFormId(String compensationFormId)
	{
		this.compensationFormId = compensationFormId;
	}

	public Integer getCompensationType()
	{
		return compensationType;
	}

	public void setCompensationType(Integer compensationType)
	{
		this.compensationType = compensationType;
	}

	public Integer getCarType()
	{
		return carType;
	}

	public void setCarType(Integer carType)
	{
		this.carType = carType;
	}

	public Date getAccidentDate()
	{
		return accidentDate;
	}

	public void setAccidentDate(Date accidentDate)
	{
		this.accidentDate = accidentDate;
	}

	public String getAccidentAddress()
	{
		return accidentAddress;
	}

	public void setAccidentAddress(String accidentAddress)
	{
		this.accidentAddress = accidentAddress;
	}

	public String getDriverName()
	{
		return driverName;
	}

	public void setDriverName(String driverName)
	{
		this.driverName = driverName;
	}

	public String getDriverLience()
	{
		return driverLience;
	}

	public void setDriverLience(String driverLience)
	{
		this.driverLience = driverLience;
	}

	public String getAccidentDeiscription()
	{
		return accidentDeiscription;
	}

	public void setAccidentDeiscription(String accidentDeiscription)
	{
		this.accidentDeiscription = accidentDeiscription;
	}

	public String getJiShenExpression()
	{
		return jiShenExpression;
	}

	public void setJiShenExpression(String jiShenExpression)
	{
		this.jiShenExpression = jiShenExpression;
	}

	public Double getJishencompensationMoney()
	{
		return jishencompensationMoney;
	}

	public void setJishencompensationMoney(Double jishencompensationMoney)
	{
		this.jishencompensationMoney = jishencompensationMoney;
	}

	public String getDriverExpression()
	{
		return driverExpression;
	}

	public void setDriverExpression(String driverExpression)
	{
		this.driverExpression = driverExpression;
	}

	public Double getDrivercompensationMoney()
	{
		return drivercompensationMoney;
	}

	public void setDrivercompensationMoney(Double drivercompensationMoney)
	{
		this.drivercompensationMoney = drivercompensationMoney;
	}

	public String getThirdExpression()
	{
		return thirdExpression;
	}

	public void setThirdExpression(String thirdExpression)
	{
		this.thirdExpression = thirdExpression;
	}

	public Double getTotalMoney()
	{
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney)
	{
		this.totalMoney = totalMoney;
	}

	public Double getLeftMoney()
	{
		return leftMoney;
	}

	public void setLeftMoney(Double leftMoney)
	{
		this.leftMoney = leftMoney;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getAccidentMemberName()
	{
		return accidentMemberName;
	}

	public void setAccidentMemberName(String accidentMemberName)
	{
		this.accidentMemberName = accidentMemberName;
	}

	public Date getMemberSignDate()
	{
		return memberSignDate;
	}

	public void setMemberSignDate(Date memberSignDate)
	{
		this.memberSignDate = memberSignDate;
	}

	public String getCheckOrg()
	{
		return checkOrg;
	}

	public void setCheckOrg(String checkOrg)
	{
		this.checkOrg = checkOrg;
	}

	public String getCheckAgent()
	{
		return checkAgent;
	}

	public void setCheckAgent(String checkAgent)
	{
		this.checkAgent = checkAgent;
	}

	public Date getCheckerApproveDate()
	{
		return checkerApproveDate;
	}

	public void setCheckerApproveDate(Date checkerApproveDate)
	{
		this.checkerApproveDate = checkerApproveDate;
	}

	public String getProvinceAgent()
	{
		return provinceAgent;
	}

	public void setProvinceAgent(String provinceAgent)
	{
		this.provinceAgent = provinceAgent;
	}

	public Date getProvinceAgentSignDate()
	{
		return provinceAgentSignDate;
	}

	public void setProvinceAgentSignDate(Date provinceAgentSignDate)
	{
		this.provinceAgentSignDate = provinceAgentSignDate;
	}

	public Tractor getTractor()
	{
		return tractor;
	}

	public void setTractor(Tractor tractor)
	{
		this.tractor = tractor;
	}

	public Xian getXian()
	{
		return xian;
	}

	public void setXian(Xian xian)
	{
		this.xian = xian;
	}

	public String getCompensationYear()
	{
		return compensationYear;
	}

	public void setCompensationYear(String compensationYear)
	{
		this.compensationYear = compensationYear;
	}

	public Double getJishenChangeMoney()
	{
		return jishenChangeMoney;
	}

	public void setJishenChangeMoney(Double jishenChangeMoney)
	{
		this.jishenChangeMoney = jishenChangeMoney;
	}

	public Double getJishenMentainMoney()
	{
		return jishenMentainMoney;
	}

	public void setJishenMentainMoney(Double jishenMentainMoney)
	{
		this.jishenMentainMoney = jishenMentainMoney;
	}

	public Double getJishenSupportMoney()
	{
		return jishenSupportMoney;
	}

	public void setJishenSupportMoney(Double jishenSupportMoney)
	{
		this.jishenSupportMoney = jishenSupportMoney;
	}

	public Double getDriverDeathMoney()
	{
		return driverDeathMoney;
	}

	public void setDriverDeathMoney(Double driverDeathMoney)
	{
		this.driverDeathMoney = driverDeathMoney;
	}

	public Double getDriverMedicalMoney()
	{
		return driverMedicalMoney;
	}

	public void setDriverMedicalMoney(Double driverMedicalMoney)
	{
		this.driverMedicalMoney = driverMedicalMoney;
	}

	public Double getDriverSupportMoney()
	{
		return driverSupportMoney;
	}

	public void setDriverSupportMoney(Double driverSupportMoney)
	{
		this.driverSupportMoney = driverSupportMoney;
	}

	public Double getThirdcompensationMoney()
	{
		return thirdcompensationMoney;
	}

	public void setThirdcompensationMoney(Double thirdcompensationMoney)
	{
		this.thirdcompensationMoney = thirdcompensationMoney;
	}

	public Double getThirdPropertyMoney()
	{
		return thirdPropertyMoney;
	}

	public void setThirdPropertyMoney(Double thirdPropertyMoney)
	{
		this.thirdPropertyMoney = thirdPropertyMoney;
	}

	public Double getThirdMedicalMoney()
	{
		return thirdMedicalMoney;
	}

	public void setThirdMedicalMoney(Double thirdMedicalMoney)
	{
		this.thirdMedicalMoney = thirdMedicalMoney;
	}

	public Double getThirdSupportMoney()
	{
		return thirdSupportMoney;
	}

	public void setThirdSupportMoney(Double thirdSupportMoney)
	{
		this.thirdSupportMoney = thirdSupportMoney;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

}
