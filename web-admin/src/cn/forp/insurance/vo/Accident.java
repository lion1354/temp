/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.vo;

import java.io.Serializable;
import java.util.Date;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

/**
 * 事故信息（报案，查勘，理赔）
 *
 * @author Apple
 */
@DBTable(name = "Sys_Accident_Info")
public class Accident implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6491416538077386216L;
	private Long id;
	@DBColumn(name = "FK_InsuranceId")
	private String insuranceId; // 保单ID(保单号)
	private String caseName; // 报案人姓名
	private String caseTel; // 报案人联系电话
	private String memberName; // 出险会员姓名
	private Integer machineryType; // 农机类型 0 拖拉机.1 收割机.2 农业机械
	private String carNum; // 号牌号码
	private Date dangerTime; // 出险时间
	private String dangerAddress; // 出险地点
	private String dangerLongitude; // 出险地点经度
	private String dangerLatitude; // 出险地点纬度
	private Integer reason; // 出险原因 1操作失误、2环境因素、3第三方原因、4机件失灵、5其它
	private Integer responsibility; // 责任类型 1全责，2主责，3同责，4次责，5无责
	private Integer accident; // 案件情形 1翻机、2坠落、3碰撞、4玻璃单独破碎翻机、5自燃、6其它
	private String situation; // 损失情况 1本机损失、2驾驶人伤、3随机人伤（只有机械类型为收割机才有该选项）、4第三方财产损失、5第三方人伤 用,分割，如1,2,3,4
	@DBColumn(name = "")
	private Integer situation1 = 0;
	@DBColumn(name = "")
	private Integer situation2 = 0;
	@DBColumn(name = "")
	private Integer situation3 = 0;
	@DBColumn(name = "")
	private Integer situation4 = 0;
	@DBColumn(name = "")
	private Integer situation5 = 0;
	private String driverName; // 驾驶员姓名
	private Date reportTime; // 报案时间
	private String reportAddress = ""; // 报案地点
	private String reportrLongitude; // 报案地点经度
	private String reportLatitude; // 报案地点纬度
	private String paymentBank = ""; // 理赔款接收账户银行
	private String paymentName = ""; // 理赔款接收账户名
	private String paymentAccount = ""; // 理赔款接收账账号
	private Integer status; // 事故类型 1.报案 2.查勘 3.理赔
	private Integer approvalStatus = 0; // 审批状态 0未审批，1未通过，2已通过，3关闭
	private String approvalRemark = ""; // 审批原因
	private String createUserName; // 创建人
	private Date lastModifyDate; // 修改日期
	private String lastModifyUserName; // 修改人

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the insuranceId
	 */
	public String getInsuranceId()
	{
		return insuranceId;
	}

	/**
	 * @param insuranceId
	 *            the insuranceId to set
	 */
	public void setInsuranceId(String insuranceId)
	{
		this.insuranceId = insuranceId;
	}

	/**
	 * @return the caseName
	 */
	public String getCaseName()
	{
		return caseName;
	}

	/**
	 * @param caseName
	 *            the caseName to set
	 */
	public void setCaseName(String caseName)
	{
		this.caseName = caseName;
	}

	/**
	 * @return the caseTel
	 */
	public String getCaseTel()
	{
		return caseTel;
	}

	/**
	 * @param caseTel
	 *            the caseTel to set
	 */
	public void setCaseTel(String caseTel)
	{
		this.caseTel = caseTel;
	}

	/**
	 * @return the memberName
	 */
	public String getMemberName()
	{
		return memberName;
	}

	/**
	 * @param memberName
	 *            the memberName to set
	 */
	public void setMemberName(String memberName)
	{
		this.memberName = memberName;
	}

	/**
	 * @return the machineryType
	 */
	public Integer getMachineryType()
	{
		return machineryType;
	}

	/**
	 * @param machineryType
	 *            the machineryType to set
	 */
	public void setMachineryType(Integer machineryType)
	{
		this.machineryType = machineryType;
	}

	/**
	 * @return the carNum
	 */
	public String getCarNum()
	{
		return carNum;
	}

	/**
	 * @param carNum
	 *            the carNum to set
	 */
	public void setCarNum(String carNum)
	{
		this.carNum = carNum;
	}

	/**
	 * @return the dangerTime
	 */
	public Date getDangerTime()
	{
		return dangerTime;
	}

	/**
	 * @param dangerTime
	 *            the dangerTime to set
	 */
	public void setDangerTime(Date dangerTime)
	{
		this.dangerTime = dangerTime;
	}

	/**
	 * @return the dangerAddress
	 */
	public String getDangerAddress()
	{
		return dangerAddress;
	}

	/**
	 * @param dangerAddress
	 *            the dangerAddress to set
	 */
	public void setDangerAddress(String dangerAddress)
	{
		this.dangerAddress = dangerAddress;
	}

	/**
	 * @return the dangerLongitude
	 */
	public String getDangerLongitude()
	{
		return dangerLongitude;
	}

	/**
	 * @param dangerLongitude
	 *            the dangerLongitude to set
	 */
	public void setDangerLongitude(String dangerLongitude)
	{
		this.dangerLongitude = dangerLongitude;
	}

	/**
	 * @return the dangerLatitude
	 */
	public String getDangerLatitude()
	{
		return dangerLatitude;
	}

	/**
	 * @param dangerLatitude
	 *            the dangerLatitude to set
	 */
	public void setDangerLatitude(String dangerLatitude)
	{
		this.dangerLatitude = dangerLatitude;
	}

	/**
	 * @return the reason
	 */
	public Integer getReason()
	{
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(Integer reason)
	{
		this.reason = reason;
	}

	/**
	 * @return the responsibility
	 */
	public Integer getResponsibility()
	{
		return responsibility;
	}

	/**
	 * @param responsibility
	 *            the responsibility to set
	 */
	public void setResponsibility(Integer responsibility)
	{
		this.responsibility = responsibility;
	}

	/**
	 * @return the accident
	 */
	public Integer getAccident()
	{
		return accident;
	}

	/**
	 * @param accident
	 *            the accident to set
	 */
	public void setAccident(Integer accident)
	{
		this.accident = accident;
	}

	/**
	 * @return the situation
	 */
	public String getSituation()
	{
		return situation;
	}

	/**
	 * @param situation
	 *            the situation to set
	 */
	public void setSituation(String situation)
	{
		this.situation = situation;
	}

	/**
	 * @return the situation1
	 */
	public Integer getSituation1()
	{
		return situation1;
	}

	/**
	 * @param situation1
	 *            the situation1 to set
	 */
	public void setSituation1(Integer situation1)
	{
		this.situation1 = situation1;
	}

	/**
	 * @return the situation2
	 */
	public Integer getSituation2()
	{
		return situation2;
	}

	/**
	 * @param situation2
	 *            the situation2 to set
	 */
	public void setSituation2(Integer situation2)
	{
		this.situation2 = situation2;
	}

	/**
	 * @return the situation3
	 */
	public Integer getSituation3()
	{
		return situation3;
	}

	/**
	 * @param situation3
	 *            the situation3 to set
	 */
	public void setSituation3(Integer situation3)
	{
		this.situation3 = situation3;
	}

	/**
	 * @return the situation4
	 */
	public Integer getSituation4()
	{
		return situation4;
	}

	/**
	 * @param situation4
	 *            the situation4 to set
	 */
	public void setSituation4(Integer situation4)
	{
		this.situation4 = situation4;
	}

	/**
	 * @return the situation5
	 */
	public Integer getSituation5()
	{
		return situation5;
	}

	/**
	 * @param situation5
	 *            the situation5 to set
	 */
	public void setSituation5(Integer situation5)
	{
		this.situation5 = situation5;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName()
	{
		return driverName;
	}

	/**
	 * @param driverName
	 *            the driverName to set
	 */
	public void setDriverName(String driverName)
	{
		this.driverName = driverName;
	}

	/**
	 * @return the reportTime
	 */
	public Date getReportTime()
	{
		return reportTime;
	}

	/**
	 * @param reportTime
	 *            the reportTime to set
	 */
	public void setReportTime(Date reportTime)
	{
		this.reportTime = reportTime;
	}

	/**
	 * @return the reportAddress
	 */
	public String getReportAddress()
	{
		return reportAddress;
	}

	/**
	 * @param reportAddress
	 *            the reportAddress to set
	 */
	public void setReportAddress(String reportAddress)
	{
		this.reportAddress = reportAddress;
	}

	/**
	 * @return the reportrLongitude
	 */
	public String getReportrLongitude()
	{
		return reportrLongitude;
	}

	/**
	 * @param reportrLongitude
	 *            the reportrLongitude to set
	 */
	public void setReportrLongitude(String reportrLongitude)
	{
		this.reportrLongitude = reportrLongitude;
	}

	/**
	 * @return the reportLatitude
	 */
	public String getReportLatitude()
	{
		return reportLatitude;
	}

	/**
	 * @param reportLatitude
	 *            the reportLatitude to set
	 */
	public void setReportLatitude(String reportLatitude)
	{
		this.reportLatitude = reportLatitude;
	}

	/**
	 * @return the paymentBank
	 */
	public String getPaymentBank()
	{
		return paymentBank;
	}

	/**
	 * @param paymentBank
	 *            the paymentBank to set
	 */
	public void setPaymentBank(String paymentBank)
	{
		this.paymentBank = paymentBank;
	}

	/**
	 * @return the paymentName
	 */
	public String getPaymentName()
	{
		return paymentName;
	}

	/**
	 * @param paymentName
	 *            the paymentName to set
	 */
	public void setPaymentName(String paymentName)
	{
		this.paymentName = paymentName;
	}

	/**
	 * @return the paymentAccount
	 */
	public String getPaymentAccount()
	{
		return paymentAccount;
	}

	/**
	 * @param paymentAccount
	 *            the paymentAccount to set
	 */
	public void setPaymentAccount(String paymentAccount)
	{
		this.paymentAccount = paymentAccount;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status)
	{
		this.status = status;
	}

	/**
	 * @return the approvalStatus
	 */
	public Integer getApprovalStatus()
	{
		return approvalStatus;
	}

	/**
	 * @param approvalStatus
	 *            the approvalStatus to set
	 */
	public void setApprovalStatus(Integer approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return the approvalRemark
	 */
	public String getApprovalRemark()
	{
		return approvalRemark;
	}

	/**
	 * @param approvalRemark
	 *            the approvalRemark to set
	 */
	public void setApprovalRemark(String approvalRemark)
	{
		this.approvalRemark = approvalRemark;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName()
	{
		return createUserName;
	}

	/**
	 * @param createUserName
	 *            the createUserName to set
	 */
	public void setCreateUserName(String createUserName)
	{
		this.createUserName = createUserName;
	}

	/**
	 * @return the lastModifyDate
	 */
	public Date getLastModifyDate()
	{
		return lastModifyDate;
	}

	/**
	 * @param lastModifyDate
	 *            the lastModifyDate to set
	 */
	public void setLastModifyDate(Date lastModifyDate)
	{
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * @return the lastModifyUserName
	 */
	public String getLastModifyUserName()
	{
		return lastModifyUserName;
	}

	/**
	 * @param lastModifyUserName
	 *            the lastModifyUserName to set
	 */
	public void setLastModifyUserName(String lastModifyUserName)
	{
		this.lastModifyUserName = lastModifyUserName;
	}

}