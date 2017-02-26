package cn.forp.insurance.vo;

import java.io.Serializable;
import java.util.Date;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;
import cn.forp.member.vo.Xian;

/**
 * 拖拉机保单信息
 * 
 * @author Apple
 *
 */
@DBTable(name = "Sys_Tractor")
public class Tractor implements Serializable
{
	private static final long serialVersionUID = -7488123737018182206L;

	private String insuranceTractorId;// 保单NO编号
	private String owner;// 机主姓名（单位）
	private Integer count = 0;
	private String idCard;// 身份证号码（机构代码）
	private String address;// 地址
	private String telNum;// 电话
	private String carNum;// 号牌号码
	private String factoryNum;// 厂牌 型号
	private String engineNum;// 发动机号
	private String jijiaNum;// 机架号

	private Date buyDate;// 购置日期
	private Double broughtPrice = 0d;// 新机购置价
	private Date regDate;// 初次登记日期
	private Double jishenHuiFeiMoney = 0d;// 机身损失互助，会费
	private Double jishenDikouMoney = 0d;
	private Double jishenHighestPay = 0d;// 机身损失互助，会费，最高补偿限额
	private Double driverHuiFeiMoney = 0d;// 驾驶人意外伤害互助总会费
	private Double driverDiKouMoney = 0d;
	private Double driverHighestPay = 0d;
	private Double driverOperateHuiFeiMoney = 0d;// 驾驶人意外伤害互助，驾驶操作会费
	private Double driverOperateDiKouMoney = 0d;
	// TODO comment
	private Double driverOperateHighestPay = 0d;
	private Double driverOperateDeathPay = 0d;// 驾驶人意外伤害互助，驾驶操作会费，身故补偿限额
	private Double driverOperateMedicalPay = 0d;// 驾驶人意外伤害互助，驾驶操作会费，医疗补偿限额
	private Double driverMaintenHuiFeiMoney = 0d;// 驾驶人意外伤害互助，维护保养会费
	private Double driverMaintenDiKouMoney = 0d;
	private Double driverMaintenHighestPay = 0d;
	private Double driverMaintenDeathPay = 0d;// 驾驶人意外伤害互助、维护保养会费，身故补偿限额
	private Double driverMaintenMedicalPay = 0d;// 驾驶人意外伤害互助，维护保养会费，医疗补偿限额
	private String driverName;
	private String driverLience;
	private String secondDirverName;// 驾驶人意外伤害互助，驾驶操作人姓名
	private String secondDriverLience;// 驾驶人意外伤害互助，身份证号码，驾驶证号
	private Double thridHuiFeiMoney = 0d;// 第三者损害责任互助，会费
	private Double thridDikouMoney = 0d;
	private Double thridDeathPay = 0d;// 第三者损失责任互助，会费，身故补偿限额
	private Double thirdProvinceMoney = 0d;
	private Double thridMedicalMoney = 0d;// 第三者损害责任互助，会费，医疗伤残补偿限额
	private Double thridPropertyMoney = 0d;// 第三者损失责任互助，会费，财产损失补偿限额
	private Double thridPeopleCombineHuiFeiMoney = 0d;
	private Double thridPeopleCombineDikouMoney = 0d;
	private Double thridPeopleCombineDeathPay = 0d;
	private Double thirdPeopleCombineProvinceMoney = 0d;
	private Double thridPeopleCombineMedicalMoney = 0d;
	private Double thridPeopleCombinePropertyMoney = 0d;
	// added for sgj
	private Double thridPeopleCombineChengkeDeathMoney = 0d;
	private Double thridPeopleCombineChengkeMedicalMoney = 0d;
	private Double combineDriverDeathPay = 0d;
	private Double combineDriverMedicalPay = 0d;
	private Double combineXianMoney = 0d;
	// end for sgj
	private Double thridPeopleCombineChengkeMoney = 0d;
	private String thridPeopleCombineDriverName;
	private String thridPeopleCombineDriverLience;
	// add for tractor
	private Double _350CombineHuifei = 0d;// 驾驶人及第三者组合互助会费
	private Double _350CombineProvinceMoney = 0d;// 驾驶人及第三者组合互助省补
	private Double _350CombineXianMoney = 0d;// 驾驶人及第三者组合互助县补
	// end for tractor
	private Double totalDikouMoney = 0d;// 积分抵扣
	private String formDriverName;// 驾驶人姓名
	private String formDriverLience;// 驾驶证号

	private Double totalMoney = 0d;
	private Date insuranceStart; // 互保期限开始时间
	private Date insuranceEnd;// 互保期限结束时间
	private Date insuranceStart2;
	private Date insuranceEnd2;
	private Date insuranceStart3;
	private Date insuranceEnd3;
	private Date qiandanDate;// 签单日期
	private String verifyPerson;// 审核人
	private String secondVerifyPerson;// 复核人
	private Date secondVerifyDate;// 经办人这行后面的日期
	private Date submitDate;
	private Integer type = 0;// 0 拖拉机.1 收割机.2 农业机械.
	// private Set<ClassType> classTypeSet = new HashSet<ClassType>();
	// private Set<CompensationForm> compensationForm = new HashSet<CompensationForm>();
	private Integer formStatus = 0;
	private String qiandanYear;// 签单年份
	private String qiandanPerson;// 经办人
	private Integer qiandanPersonType;// 经办人类型 0:管理员,1:会员
	private String trafficInsuranceNum;
	private Double trafficInsuranceMoney = 0d;
	private Double combineHuiFeiMoney = 0d;
	private Double combineDikouMoney = 0d;
	private Double combineDriverHighestPay = 0d;
	private Double combineMachineHighestPay = 0d;
	private Double combineProvinceMoney = 0d;
	private Double combineThridDeathHighestPay = 0d;
	private Double combineThridMedicineHighestPay = 0d;
	private Double combinePropertyHighestPay = 0d;
	private String combineDriverName;
	private String combineDriverLience;
	@DBColumn(name = "")
	private Xian xian;
	@DBColumn(name = "")
	private HuiFei huifei;
	private Integer xianId;
	@DBColumn(name = "")
	private String xianName;
	private Integer huifeiId;

	private String fileName;
	@DBColumn(name = "")
	private String fileName2;

	// 保费
	private Double huBaoZuHeZongBaoFei = 0d;// 互保组合总保费
	private Double diSanZheZeRenBaoFei = 0d;// 第三者责任
	private Double jiaShiRenYiWaiShangHaiBaoFei = 0d;// 驾驶人意外伤害
	private Double duZhuZuoYeRenYuanShangHaiBaoFei = 0d;// 辅助作业人员意外伤害
	private Double boLiDanDuPoSuiBaoFei = 0d;// 玻璃单独破碎
	private Double ziRanSunShiBaoFei = 0d;// 自燃损失保费
	private Double weiXiuBaoYangZuoYeBaoFei = 0d;// 维修保养作业保费
	private Double jiJuTuoLuoSunShiBaoFei = 0d;// 机具脱落损失保费
	private Double yunZhuanYiWaiShangHaiBaoFei = 0d;// 运转意外伤害保费
	private Double buJiMianBuLvBaoFei = 0d;// 不计免补率

	private Double jiShenSunShiBaoFei = 0d;// 机身损失保费
	private Double nongJiHuoTuoCheBaoFei = 0d;// 农机或拖车保费
	private Double feiShiGuBuJianSunShiBaoFei = 0d;// 非事故部件损失
	private Double zhuangYunSunShiBaoFei = 0d;// 装运损失

	private String remark;// 备注

	@DBColumn(name = "charging_status")
	private String chargingStatus;// 收费状态
	private Double caizhengbutie = 0d;// 市县财政补贴（元）

	/**
	 * @return the insuranceTractorId
	 */
	public String getInsuranceTractorId()
	{
		return insuranceTractorId;
	}

	/**
	 * @param insuranceTractorId
	 *            the insuranceTractorId to set
	 */
	public void setInsuranceTractorId(String insuranceTractorId)
	{
		this.insuranceTractorId = insuranceTractorId;
	}

	/**
	 * @return the owner
	 */
	public String getOwner()
	{
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * @return the count
	 */
	public Integer getCount()
	{
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count)
	{
		this.count = count;
	}

	/**
	 * @return the idCard
	 */
	public String getIdCard()
	{
		return idCard;
	}

	/**
	 * @param idCard
	 *            the idCard to set
	 */
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}

	/**
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * @return the telNum
	 */
	public String getTelNum()
	{
		return telNum;
	}

	/**
	 * @param telNum
	 *            the telNum to set
	 */
	public void setTelNum(String telNum)
	{
		this.telNum = telNum;
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
	 * @return the factoryNum
	 */
	public String getFactoryNum()
	{
		return factoryNum;
	}

	/**
	 * @param factoryNum
	 *            the factoryNum to set
	 */
	public void setFactoryNum(String factoryNum)
	{
		this.factoryNum = factoryNum;
	}

	/**
	 * @return the engineNum
	 */
	public String getEngineNum()
	{
		return engineNum;
	}

	/**
	 * @param engineNum
	 *            the engineNum to set
	 */
	public void setEngineNum(String engineNum)
	{
		this.engineNum = engineNum;
	}

	/**
	 * @return the jijiaNum
	 */
	public String getJijiaNum()
	{
		return jijiaNum;
	}

	/**
	 * @param jijiaNum
	 *            the jijiaNum to set
	 */
	public void setJijiaNum(String jijiaNum)
	{
		this.jijiaNum = jijiaNum;
	}

	/**
	 * @return the buyDate
	 */
	public Date getBuyDate()
	{
		return buyDate;
	}

	/**
	 * @param buyDate
	 *            the buyDate to set
	 */
	public void setBuyDate(Date buyDate)
	{
		this.buyDate = buyDate;
	}

	/**
	 * @return the broughtPrice
	 */
	public Double getBroughtPrice()
	{
		return broughtPrice;
	}

	/**
	 * @param broughtPrice
	 *            the broughtPrice to set
	 */
	public void setBroughtPrice(Double broughtPrice)
	{
		this.broughtPrice = broughtPrice;
	}

	/**
	 * @return the regDate
	 */
	public Date getRegDate()
	{
		return regDate;
	}

	/**
	 * @param regDate
	 *            the regDate to set
	 */
	public void setRegDate(Date regDate)
	{
		this.regDate = regDate;
	}

	/**
	 * @return the jishenHuiFeiMoney
	 */
	public Double getJishenHuiFeiMoney()
	{
		return jishenHuiFeiMoney;
	}

	/**
	 * @param jishenHuiFeiMoney
	 *            the jishenHuiFeiMoney to set
	 */
	public void setJishenHuiFeiMoney(Double jishenHuiFeiMoney)
	{
		this.jishenHuiFeiMoney = jishenHuiFeiMoney;
	}

	/**
	 * @return the jishenDikouMoney
	 */
	public Double getJishenDikouMoney()
	{
		return jishenDikouMoney;
	}

	/**
	 * @param jishenDikouMoney
	 *            the jishenDikouMoney to set
	 */
	public void setJishenDikouMoney(Double jishenDikouMoney)
	{
		this.jishenDikouMoney = jishenDikouMoney;
	}

	/**
	 * @return the jishenHighestPay
	 */
	public Double getJishenHighestPay()
	{
		return jishenHighestPay;
	}

	/**
	 * @param jishenHighestPay
	 *            the jishenHighestPay to set
	 */
	public void setJishenHighestPay(Double jishenHighestPay)
	{
		this.jishenHighestPay = jishenHighestPay;
	}

	/**
	 * @return the driverHuiFeiMoney
	 */
	public Double getDriverHuiFeiMoney()
	{
		return driverHuiFeiMoney;
	}

	/**
	 * @param driverHuiFeiMoney
	 *            the driverHuiFeiMoney to set
	 */
	public void setDriverHuiFeiMoney(Double driverHuiFeiMoney)
	{
		this.driverHuiFeiMoney = driverHuiFeiMoney;
	}

	/**
	 * @return the driverDiKouMoney
	 */
	public Double getDriverDiKouMoney()
	{
		return driverDiKouMoney;
	}

	/**
	 * @param driverDiKouMoney
	 *            the driverDiKouMoney to set
	 */
	public void setDriverDiKouMoney(Double driverDiKouMoney)
	{
		this.driverDiKouMoney = driverDiKouMoney;
	}

	/**
	 * @return the driverHighestPay
	 */
	public Double getDriverHighestPay()
	{
		return driverHighestPay;
	}

	/**
	 * @param driverHighestPay
	 *            the driverHighestPay to set
	 */
	public void setDriverHighestPay(Double driverHighestPay)
	{
		this.driverHighestPay = driverHighestPay;
	}

	/**
	 * @return the driverOperateHuiFeiMoney
	 */
	public Double getDriverOperateHuiFeiMoney()
	{
		return driverOperateHuiFeiMoney;
	}

	/**
	 * @param driverOperateHuiFeiMoney
	 *            the driverOperateHuiFeiMoney to set
	 */
	public void setDriverOperateHuiFeiMoney(Double driverOperateHuiFeiMoney)
	{
		this.driverOperateHuiFeiMoney = driverOperateHuiFeiMoney;
	}

	/**
	 * @return the driverOperateDiKouMoney
	 */
	public Double getDriverOperateDiKouMoney()
	{
		return driverOperateDiKouMoney;
	}

	/**
	 * @param driverOperateDiKouMoney
	 *            the driverOperateDiKouMoney to set
	 */
	public void setDriverOperateDiKouMoney(Double driverOperateDiKouMoney)
	{
		this.driverOperateDiKouMoney = driverOperateDiKouMoney;
	}

	/**
	 * @return the driverOperateHighestPay
	 */
	public Double getDriverOperateHighestPay()
	{
		return driverOperateHighestPay;
	}

	/**
	 * @param driverOperateHighestPay
	 *            the driverOperateHighestPay to set
	 */
	public void setDriverOperateHighestPay(Double driverOperateHighestPay)
	{
		this.driverOperateHighestPay = driverOperateHighestPay;
	}

	/**
	 * @return the driverOperateDeathPay
	 */
	public Double getDriverOperateDeathPay()
	{
		return driverOperateDeathPay;
	}

	/**
	 * @param driverOperateDeathPay
	 *            the driverOperateDeathPay to set
	 */
	public void setDriverOperateDeathPay(Double driverOperateDeathPay)
	{
		this.driverOperateDeathPay = driverOperateDeathPay;
	}

	/**
	 * @return the driverOperateMedicalPay
	 */
	public Double getDriverOperateMedicalPay()
	{
		return driverOperateMedicalPay;
	}

	/**
	 * @param driverOperateMedicalPay
	 *            the driverOperateMedicalPay to set
	 */
	public void setDriverOperateMedicalPay(Double driverOperateMedicalPay)
	{
		this.driverOperateMedicalPay = driverOperateMedicalPay;
	}

	/**
	 * @return the driverMaintenHuiFeiMoney
	 */
	public Double getDriverMaintenHuiFeiMoney()
	{
		return driverMaintenHuiFeiMoney;
	}

	/**
	 * @param driverMaintenHuiFeiMoney
	 *            the driverMaintenHuiFeiMoney to set
	 */
	public void setDriverMaintenHuiFeiMoney(Double driverMaintenHuiFeiMoney)
	{
		this.driverMaintenHuiFeiMoney = driverMaintenHuiFeiMoney;
	}

	/**
	 * @return the driverMaintenDiKouMoney
	 */
	public Double getDriverMaintenDiKouMoney()
	{
		return driverMaintenDiKouMoney;
	}

	/**
	 * @param driverMaintenDiKouMoney
	 *            the driverMaintenDiKouMoney to set
	 */
	public void setDriverMaintenDiKouMoney(Double driverMaintenDiKouMoney)
	{
		this.driverMaintenDiKouMoney = driverMaintenDiKouMoney;
	}

	/**
	 * @return the driverMaintenHighestPay
	 */
	public Double getDriverMaintenHighestPay()
	{
		return driverMaintenHighestPay;
	}

	/**
	 * @param driverMaintenHighestPay
	 *            the driverMaintenHighestPay to set
	 */
	public void setDriverMaintenHighestPay(Double driverMaintenHighestPay)
	{
		this.driverMaintenHighestPay = driverMaintenHighestPay;
	}

	/**
	 * @return the driverMaintenDeathPay
	 */
	public Double getDriverMaintenDeathPay()
	{
		return driverMaintenDeathPay;
	}

	/**
	 * @param driverMaintenDeathPay
	 *            the driverMaintenDeathPay to set
	 */
	public void setDriverMaintenDeathPay(Double driverMaintenDeathPay)
	{
		this.driverMaintenDeathPay = driverMaintenDeathPay;
	}

	/**
	 * @return the driverMaintenMedicalPay
	 */
	public Double getDriverMaintenMedicalPay()
	{
		return driverMaintenMedicalPay;
	}

	/**
	 * @param driverMaintenMedicalPay
	 *            the driverMaintenMedicalPay to set
	 */
	public void setDriverMaintenMedicalPay(Double driverMaintenMedicalPay)
	{
		this.driverMaintenMedicalPay = driverMaintenMedicalPay;
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
	 * @return the driverLience
	 */
	public String getDriverLience()
	{
		return driverLience;
	}

	/**
	 * @param driverLience
	 *            the driverLience to set
	 */
	public void setDriverLience(String driverLience)
	{
		this.driverLience = driverLience;
	}

	/**
	 * @return the secondDirverName
	 */
	public String getSecondDirverName()
	{
		return secondDirverName;
	}

	/**
	 * @param secondDirverName
	 *            the secondDirverName to set
	 */
	public void setSecondDirverName(String secondDirverName)
	{
		this.secondDirverName = secondDirverName;
	}

	/**
	 * @return the secondDriverLience
	 */
	public String getSecondDriverLience()
	{
		return secondDriverLience;
	}

	/**
	 * @param secondDriverLience
	 *            the secondDriverLience to set
	 */
	public void setSecondDriverLience(String secondDriverLience)
	{
		this.secondDriverLience = secondDriverLience;
	}

	/**
	 * @return the thridHuiFeiMoney
	 */
	public Double getThridHuiFeiMoney()
	{
		return thridHuiFeiMoney;
	}

	/**
	 * @param thridHuiFeiMoney
	 *            the thridHuiFeiMoney to set
	 */
	public void setThridHuiFeiMoney(Double thridHuiFeiMoney)
	{
		this.thridHuiFeiMoney = thridHuiFeiMoney;
	}

	/**
	 * @return the thridDikouMoney
	 */
	public Double getThridDikouMoney()
	{
		return thridDikouMoney;
	}

	/**
	 * @param thridDikouMoney
	 *            the thridDikouMoney to set
	 */
	public void setThridDikouMoney(Double thridDikouMoney)
	{
		this.thridDikouMoney = thridDikouMoney;
	}

	/**
	 * @return the thridDeathPay
	 */
	public Double getThridDeathPay()
	{
		return thridDeathPay;
	}

	/**
	 * @param thridDeathPay
	 *            the thridDeathPay to set
	 */
	public void setThridDeathPay(Double thridDeathPay)
	{
		this.thridDeathPay = thridDeathPay;
	}

	/**
	 * @return the thirdProvinceMoney
	 */
	public Double getThirdProvinceMoney()
	{
		return thirdProvinceMoney;
	}

	/**
	 * @param thirdProvinceMoney
	 *            the thirdProvinceMoney to set
	 */
	public void setThirdProvinceMoney(Double thirdProvinceMoney)
	{
		this.thirdProvinceMoney = thirdProvinceMoney;
	}

	/**
	 * @return the thridMedicalMoney
	 */
	public Double getThridMedicalMoney()
	{
		return thridMedicalMoney;
	}

	/**
	 * @param thridMedicalMoney
	 *            the thridMedicalMoney to set
	 */
	public void setThridMedicalMoney(Double thridMedicalMoney)
	{
		this.thridMedicalMoney = thridMedicalMoney;
	}

	/**
	 * @return the thridPropertyMoney
	 */
	public Double getThridPropertyMoney()
	{
		return thridPropertyMoney;
	}

	/**
	 * @param thridPropertyMoney
	 *            the thridPropertyMoney to set
	 */
	public void setThridPropertyMoney(Double thridPropertyMoney)
	{
		this.thridPropertyMoney = thridPropertyMoney;
	}

	/**
	 * @return the thridPeopleCombineHuiFeiMoney
	 */
	public Double getThridPeopleCombineHuiFeiMoney()
	{
		return thridPeopleCombineHuiFeiMoney;
	}

	/**
	 * @param thridPeopleCombineHuiFeiMoney
	 *            the thridPeopleCombineHuiFeiMoney to set
	 */
	public void setThridPeopleCombineHuiFeiMoney(Double thridPeopleCombineHuiFeiMoney)
	{
		this.thridPeopleCombineHuiFeiMoney = thridPeopleCombineHuiFeiMoney;
	}

	/**
	 * @return the thridPeopleCombineDikouMoney
	 */
	public Double getThridPeopleCombineDikouMoney()
	{
		return thridPeopleCombineDikouMoney;
	}

	/**
	 * @param thridPeopleCombineDikouMoney
	 *            the thridPeopleCombineDikouMoney to set
	 */
	public void setThridPeopleCombineDikouMoney(Double thridPeopleCombineDikouMoney)
	{
		this.thridPeopleCombineDikouMoney = thridPeopleCombineDikouMoney;
	}

	/**
	 * @return the thridPeopleCombineDeathPay
	 */
	public Double getThridPeopleCombineDeathPay()
	{
		return thridPeopleCombineDeathPay;
	}

	/**
	 * @param thridPeopleCombineDeathPay
	 *            the thridPeopleCombineDeathPay to set
	 */
	public void setThridPeopleCombineDeathPay(Double thridPeopleCombineDeathPay)
	{
		this.thridPeopleCombineDeathPay = thridPeopleCombineDeathPay;
	}

	/**
	 * @return the thirdPeopleCombineProvinceMoney
	 */
	public Double getThirdPeopleCombineProvinceMoney()
	{
		return thirdPeopleCombineProvinceMoney;
	}

	/**
	 * @param thirdPeopleCombineProvinceMoney
	 *            the thirdPeopleCombineProvinceMoney to set
	 */
	public void setThirdPeopleCombineProvinceMoney(Double thirdPeopleCombineProvinceMoney)
	{
		this.thirdPeopleCombineProvinceMoney = thirdPeopleCombineProvinceMoney;
	}

	/**
	 * @return the thridPeopleCombineMedicalMoney
	 */
	public Double getThridPeopleCombineMedicalMoney()
	{
		return thridPeopleCombineMedicalMoney;
	}

	/**
	 * @param thridPeopleCombineMedicalMoney
	 *            the thridPeopleCombineMedicalMoney to set
	 */
	public void setThridPeopleCombineMedicalMoney(Double thridPeopleCombineMedicalMoney)
	{
		this.thridPeopleCombineMedicalMoney = thridPeopleCombineMedicalMoney;
	}

	/**
	 * @return the thridPeopleCombinePropertyMoney
	 */
	public Double getThridPeopleCombinePropertyMoney()
	{
		return thridPeopleCombinePropertyMoney;
	}

	/**
	 * @param thridPeopleCombinePropertyMoney
	 *            the thridPeopleCombinePropertyMoney to set
	 */
	public void setThridPeopleCombinePropertyMoney(Double thridPeopleCombinePropertyMoney)
	{
		this.thridPeopleCombinePropertyMoney = thridPeopleCombinePropertyMoney;
	}

	/**
	 * @return the thridPeopleCombineChengkeDeathMoney
	 */
	public Double getThridPeopleCombineChengkeDeathMoney()
	{
		return thridPeopleCombineChengkeDeathMoney;
	}

	/**
	 * @param thridPeopleCombineChengkeDeathMoney
	 *            the thridPeopleCombineChengkeDeathMoney to set
	 */
	public void setThridPeopleCombineChengkeDeathMoney(Double thridPeopleCombineChengkeDeathMoney)
	{
		this.thridPeopleCombineChengkeDeathMoney = thridPeopleCombineChengkeDeathMoney;
	}

	/**
	 * @return the thridPeopleCombineChengkeMedicalMoney
	 */
	public Double getThridPeopleCombineChengkeMedicalMoney()
	{
		return thridPeopleCombineChengkeMedicalMoney;
	}

	/**
	 * @param thridPeopleCombineChengkeMedicalMoney
	 *            the thridPeopleCombineChengkeMedicalMoney to set
	 */
	public void setThridPeopleCombineChengkeMedicalMoney(Double thridPeopleCombineChengkeMedicalMoney)
	{
		this.thridPeopleCombineChengkeMedicalMoney = thridPeopleCombineChengkeMedicalMoney;
	}

	/**
	 * @return the combineDriverDeathPay
	 */
	public Double getCombineDriverDeathPay()
	{
		return combineDriverDeathPay;
	}

	/**
	 * @param combineDriverDeathPay
	 *            the combineDriverDeathPay to set
	 */
	public void setCombineDriverDeathPay(Double combineDriverDeathPay)
	{
		this.combineDriverDeathPay = combineDriverDeathPay;
	}

	/**
	 * @return the combineDriverMedicalPay
	 */
	public Double getCombineDriverMedicalPay()
	{
		return combineDriverMedicalPay;
	}

	/**
	 * @param combineDriverMedicalPay
	 *            the combineDriverMedicalPay to set
	 */
	public void setCombineDriverMedicalPay(Double combineDriverMedicalPay)
	{
		this.combineDriverMedicalPay = combineDriverMedicalPay;
	}

	/**
	 * @return the combineXianMoney
	 */
	public Double getCombineXianMoney()
	{
		return combineXianMoney;
	}

	/**
	 * @param combineXianMoney
	 *            the combineXianMoney to set
	 */
	public void setCombineXianMoney(Double combineXianMoney)
	{
		this.combineXianMoney = combineXianMoney;
	}

	/**
	 * @return the thridPeopleCombineChengkeMoney
	 */
	public Double getThridPeopleCombineChengkeMoney()
	{
		return thridPeopleCombineChengkeMoney;
	}

	/**
	 * @param thridPeopleCombineChengkeMoney
	 *            the thridPeopleCombineChengkeMoney to set
	 */
	public void setThridPeopleCombineChengkeMoney(Double thridPeopleCombineChengkeMoney)
	{
		this.thridPeopleCombineChengkeMoney = thridPeopleCombineChengkeMoney;
	}

	/**
	 * @return the thridPeopleCombineDriverName
	 */
	public String getThridPeopleCombineDriverName()
	{
		return thridPeopleCombineDriverName;
	}

	/**
	 * @param thridPeopleCombineDriverName
	 *            the thridPeopleCombineDriverName to set
	 */
	public void setThridPeopleCombineDriverName(String thridPeopleCombineDriverName)
	{
		this.thridPeopleCombineDriverName = thridPeopleCombineDriverName;
	}

	/**
	 * @return the thridPeopleCombineDriverLience
	 */
	public String getThridPeopleCombineDriverLience()
	{
		return thridPeopleCombineDriverLience;
	}

	/**
	 * @param thridPeopleCombineDriverLience
	 *            the thridPeopleCombineDriverLience to set
	 */
	public void setThridPeopleCombineDriverLience(String thridPeopleCombineDriverLience)
	{
		this.thridPeopleCombineDriverLience = thridPeopleCombineDriverLience;
	}

	/**
	 * @return the _350CombineHuifei
	 */
	public Double get_350CombineHuifei()
	{
		return _350CombineHuifei;
	}

	/**
	 * @param _350CombineHuifei
	 *            the _350CombineHuifei to set
	 */
	public void set_350CombineHuifei(Double _350CombineHuifei)
	{
		this._350CombineHuifei = _350CombineHuifei;
	}

	/**
	 * @return the _350CombineProvinceMoney
	 */
	public Double get_350CombineProvinceMoney()
	{
		return _350CombineProvinceMoney;
	}

	/**
	 * @param _350CombineProvinceMoney
	 *            the _350CombineProvinceMoney to set
	 */
	public void set_350CombineProvinceMoney(Double _350CombineProvinceMoney)
	{
		this._350CombineProvinceMoney = _350CombineProvinceMoney;
	}

	/**
	 * @return the _350CombineXianMoney
	 */
	public Double get_350CombineXianMoney()
	{
		return _350CombineXianMoney;
	}

	/**
	 * @param _350CombineXianMoney
	 *            the _350CombineXianMoney to set
	 */
	public void set_350CombineXianMoney(Double _350CombineXianMoney)
	{
		this._350CombineXianMoney = _350CombineXianMoney;
	}

	/**
	 * @return the totalDikouMoney
	 */
	public Double getTotalDikouMoney()
	{
		return totalDikouMoney;
	}

	/**
	 * @param totalDikouMoney
	 *            the totalDikouMoney to set
	 */
	public void setTotalDikouMoney(Double totalDikouMoney)
	{
		this.totalDikouMoney = totalDikouMoney;
	}

	/**
	 * @return the formDriverName
	 */
	public String getFormDriverName()
	{
		return formDriverName;
	}

	/**
	 * @param formDriverName
	 *            the formDriverName to set
	 */
	public void setFormDriverName(String formDriverName)
	{
		this.formDriverName = formDriverName;
	}

	/**
	 * @return the formDriverLience
	 */
	public String getFormDriverLience()
	{
		return formDriverLience;
	}

	/**
	 * @param formDriverLience
	 *            the formDriverLience to set
	 */
	public void setFormDriverLience(String formDriverLience)
	{
		this.formDriverLience = formDriverLience;
	}

	/**
	 * @return the totalMoney
	 */
	public Double getTotalMoney()
	{
		return totalMoney;
	}

	/**
	 * @param totalMoney
	 *            the totalMoney to set
	 */
	public void setTotalMoney(Double totalMoney)
	{
		this.totalMoney = totalMoney;
	}

	/**
	 * @return the insuranceStart
	 */
	public Date getInsuranceStart()
	{
		return insuranceStart;
	}

	/**
	 * @param insuranceStart
	 *            the insuranceStart to set
	 */
	public void setInsuranceStart(Date insuranceStart)
	{
		this.insuranceStart = insuranceStart;
	}

	/**
	 * @return the insuranceEnd
	 */
	public Date getInsuranceEnd()
	{
		return insuranceEnd;
	}

	/**
	 * @param insuranceEnd
	 *            the insuranceEnd to set
	 */
	public void setInsuranceEnd(Date insuranceEnd)
	{
		this.insuranceEnd = insuranceEnd;
	}

	/**
	 * @return the insuranceStart2
	 */
	public Date getInsuranceStart2()
	{
		return insuranceStart2;
	}

	/**
	 * @param insuranceStart2
	 *            the insuranceStart2 to set
	 */
	public void setInsuranceStart2(Date insuranceStart2)
	{
		this.insuranceStart2 = insuranceStart2;
	}

	/**
	 * @return the insuranceEnd2
	 */
	public Date getInsuranceEnd2()
	{
		return insuranceEnd2;
	}

	/**
	 * @param insuranceEnd2
	 *            the insuranceEnd2 to set
	 */
	public void setInsuranceEnd2(Date insuranceEnd2)
	{
		this.insuranceEnd2 = insuranceEnd2;
	}

	/**
	 * @return the insuranceStart3
	 */
	public Date getInsuranceStart3()
	{
		return insuranceStart3;
	}

	/**
	 * @param insuranceStart3
	 *            the insuranceStart3 to set
	 */
	public void setInsuranceStart3(Date insuranceStart3)
	{
		this.insuranceStart3 = insuranceStart3;
	}

	/**
	 * @return the insuranceEnd3
	 */
	public Date getInsuranceEnd3()
	{
		return insuranceEnd3;
	}

	/**
	 * @param insuranceEnd3
	 *            the insuranceEnd3 to set
	 */
	public void setInsuranceEnd3(Date insuranceEnd3)
	{
		this.insuranceEnd3 = insuranceEnd3;
	}

	/**
	 * @return the qiandanDate
	 */
	public Date getQiandanDate()
	{
		return qiandanDate;
	}

	/**
	 * @param qiandanDate
	 *            the qiandanDate to set
	 */
	public void setQiandanDate(Date qiandanDate)
	{
		this.qiandanDate = qiandanDate;
	}

	/**
	 * @return the verifyPerson
	 */
	public String getVerifyPerson()
	{
		return verifyPerson;
	}

	/**
	 * @param verifyPerson
	 *            the verifyPerson to set
	 */
	public void setVerifyPerson(String verifyPerson)
	{
		this.verifyPerson = verifyPerson;
	}

	/**
	 * @return the secondVerifyPerson
	 */
	public String getSecondVerifyPerson()
	{
		return secondVerifyPerson;
	}

	/**
	 * @param secondVerifyPerson
	 *            the secondVerifyPerson to set
	 */
	public void setSecondVerifyPerson(String secondVerifyPerson)
	{
		this.secondVerifyPerson = secondVerifyPerson;
	}

	/**
	 * @return the secondVerifyDate
	 */
	public Date getSecondVerifyDate()
	{
		return secondVerifyDate;
	}

	/**
	 * @param secondVerifyDate
	 *            the secondVerifyDate to set
	 */
	public void setSecondVerifyDate(Date secondVerifyDate)
	{
		this.secondVerifyDate = secondVerifyDate;
	}

	/**
	 * @return the submitDate
	 */
	public Date getSubmitDate()
	{
		return submitDate;
	}

	/**
	 * @param submitDate
	 *            the submitDate to set
	 */
	public void setSubmitDate(Date submitDate)
	{
		this.submitDate = submitDate;
	}

	/**
	 * @return the type
	 */
	public Integer getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type)
	{
		this.type = type;
	}

	/**
	 * @return the formStatus
	 */
	public Integer getFormStatus()
	{
		return formStatus;
	}

	/**
	 * @param formStatus
	 *            the formStatus to set
	 */
	public void setFormStatus(Integer formStatus)
	{
		this.formStatus = formStatus;
	}

	/**
	 * @return the qiandanYear
	 */
	public String getQiandanYear()
	{
		return qiandanYear;
	}

	/**
	 * @param qiandanYear
	 *            the qiandanYear to set
	 */
	public void setQiandanYear(String qiandanYear)
	{
		this.qiandanYear = qiandanYear;
	}

	/**
	 * @return the qiandanPerson
	 */
	public String getQiandanPerson()
	{
		return qiandanPerson;
	}

	/**
	 * @param qiandanPerson
	 *            the qiandanPerson to set
	 */
	public void setQiandanPerson(String qiandanPerson)
	{
		this.qiandanPerson = qiandanPerson;
	}

	/**
	 * @return the qiandanPersonType
	 */
	public Integer getQiandanPersonType()
	{
		return qiandanPersonType;
	}

	/**
	 * @param qiandanPersonType
	 *            the qiandanPersonType to set
	 */
	public void setQiandanPersonType(Integer qiandanPersonType)
	{
		this.qiandanPersonType = qiandanPersonType;
	}

	/**
	 * @return the trafficInsuranceNum
	 */
	public String getTrafficInsuranceNum()
	{
		return trafficInsuranceNum;
	}

	/**
	 * @param trafficInsuranceNum
	 *            the trafficInsuranceNum to set
	 */
	public void setTrafficInsuranceNum(String trafficInsuranceNum)
	{
		this.trafficInsuranceNum = trafficInsuranceNum;
	}

	/**
	 * @return the trafficInsuranceMoney
	 */
	public Double getTrafficInsuranceMoney()
	{
		return trafficInsuranceMoney;
	}

	/**
	 * @param trafficInsuranceMoney
	 *            the trafficInsuranceMoney to set
	 */
	public void setTrafficInsuranceMoney(Double trafficInsuranceMoney)
	{
		this.trafficInsuranceMoney = trafficInsuranceMoney;
	}

	/**
	 * @return the combineHuiFeiMoney
	 */
	public Double getCombineHuiFeiMoney()
	{
		return combineHuiFeiMoney;
	}

	/**
	 * @param combineHuiFeiMoney
	 *            the combineHuiFeiMoney to set
	 */
	public void setCombineHuiFeiMoney(Double combineHuiFeiMoney)
	{
		this.combineHuiFeiMoney = combineHuiFeiMoney;
	}

	/**
	 * @return the combineDikouMoney
	 */
	public Double getCombineDikouMoney()
	{
		return combineDikouMoney;
	}

	/**
	 * @param combineDikouMoney
	 *            the combineDikouMoney to set
	 */
	public void setCombineDikouMoney(Double combineDikouMoney)
	{
		this.combineDikouMoney = combineDikouMoney;
	}

	/**
	 * @return the combineDriverHighestPay
	 */
	public Double getCombineDriverHighestPay()
	{
		return combineDriverHighestPay;
	}

	/**
	 * @param combineDriverHighestPay
	 *            the combineDriverHighestPay to set
	 */
	public void setCombineDriverHighestPay(Double combineDriverHighestPay)
	{
		this.combineDriverHighestPay = combineDriverHighestPay;
	}

	/**
	 * @return the combineMachineHighestPay
	 */
	public Double getCombineMachineHighestPay()
	{
		return combineMachineHighestPay;
	}

	/**
	 * @param combineMachineHighestPay
	 *            the combineMachineHighestPay to set
	 */
	public void setCombineMachineHighestPay(Double combineMachineHighestPay)
	{
		this.combineMachineHighestPay = combineMachineHighestPay;
	}

	/**
	 * @return the combineProvinceMoney
	 */
	public Double getCombineProvinceMoney()
	{
		return combineProvinceMoney;
	}

	/**
	 * @param combineProvinceMoney
	 *            the combineProvinceMoney to set
	 */
	public void setCombineProvinceMoney(Double combineProvinceMoney)
	{
		this.combineProvinceMoney = combineProvinceMoney;
	}

	/**
	 * @return the combineThridDeathHighestPay
	 */
	public Double getCombineThridDeathHighestPay()
	{
		return combineThridDeathHighestPay;
	}

	/**
	 * @param combineThridDeathHighestPay
	 *            the combineThridDeathHighestPay to set
	 */
	public void setCombineThridDeathHighestPay(Double combineThridDeathHighestPay)
	{
		this.combineThridDeathHighestPay = combineThridDeathHighestPay;
	}

	/**
	 * @return the combineThridMedicineHighestPay
	 */
	public Double getCombineThridMedicineHighestPay()
	{
		return combineThridMedicineHighestPay;
	}

	/**
	 * @param combineThridMedicineHighestPay
	 *            the combineThridMedicineHighestPay to set
	 */
	public void setCombineThridMedicineHighestPay(Double combineThridMedicineHighestPay)
	{
		this.combineThridMedicineHighestPay = combineThridMedicineHighestPay;
	}

	/**
	 * @return the combinePropertyHighestPay
	 */
	public Double getCombinePropertyHighestPay()
	{
		return combinePropertyHighestPay;
	}

	/**
	 * @param combinePropertyHighestPay
	 *            the combinePropertyHighestPay to set
	 */
	public void setCombinePropertyHighestPay(Double combinePropertyHighestPay)
	{
		this.combinePropertyHighestPay = combinePropertyHighestPay;
	}

	/**
	 * @return the combineDriverName
	 */
	public String getCombineDriverName()
	{
		return combineDriverName;
	}

	/**
	 * @param combineDriverName
	 *            the combineDriverName to set
	 */
	public void setCombineDriverName(String combineDriverName)
	{
		this.combineDriverName = combineDriverName;
	}

	/**
	 * @return the combineDriverLience
	 */
	public String getCombineDriverLience()
	{
		return combineDriverLience;
	}

	/**
	 * @param combineDriverLience
	 *            the combineDriverLience to set
	 */
	public void setCombineDriverLience(String combineDriverLience)
	{
		this.combineDriverLience = combineDriverLience;
	}

	/**
	 * @return the xian
	 */
	public Xian getXian()
	{
		return xian;
	}

	/**
	 * @param xian
	 *            the xian to set
	 */
	public void setXian(Xian xian)
	{
		this.xian = xian;
	}

	/**
	 * @return the huifei
	 */
	public HuiFei getHuifei()
	{
		return huifei;
	}

	/**
	 * @param huifei
	 *            the huifei to set
	 */
	public void setHuifei(HuiFei huifei)
	{
		this.huifei = huifei;
	}

	/**
	 * @return the xianId
	 */
	public Integer getXianId()
	{
		return xianId;
	}

	/**
	 * @param xianId
	 *            the xianId to set
	 */
	public void setXianId(Integer xianId)
	{
		this.xianId = xianId;
	}

	/**
	 * @return the xianName
	 */
	public String getXianName()
	{
		return xianName;
	}

	/**
	 * @param xianName
	 *            the xianName to set
	 */
	public void setXianName(String xianName)
	{
		this.xianName = xianName;
	}

	/**
	 * @return the huifeiId
	 */
	public Integer getHuifeiId()
	{
		return huifeiId;
	}

	/**
	 * @param huifeiId
	 *            the huifeiId to set
	 */
	public void setHuifeiId(Integer huifeiId)
	{
		this.huifeiId = huifeiId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the fileName2
	 */
	public String getFileName2()
	{
		return fileName2;
	}

	/**
	 * @param fileName2
	 *            the fileName2 to set
	 */
	public void setFileName2(String fileName2)
	{
		this.fileName2 = fileName2;
	}

	/**
	 * @return the huBaoZuHeZongBaoFei
	 */
	public Double getHuBaoZuHeZongBaoFei()
	{
		return huBaoZuHeZongBaoFei;
	}

	/**
	 * @param huBaoZuHeZongBaoFei
	 *            the huBaoZuHeZongBaoFei to set
	 */
	public void setHuBaoZuHeZongBaoFei(Double huBaoZuHeZongBaoFei)
	{
		this.huBaoZuHeZongBaoFei = huBaoZuHeZongBaoFei;
	}

	/**
	 * @return the diSanZheZeRenBaoFei
	 */
	public Double getDiSanZheZeRenBaoFei()
	{
		return diSanZheZeRenBaoFei;
	}

	/**
	 * @param diSanZheZeRenBaoFei
	 *            the diSanZheZeRenBaoFei to set
	 */
	public void setDiSanZheZeRenBaoFei(Double diSanZheZeRenBaoFei)
	{
		this.diSanZheZeRenBaoFei = diSanZheZeRenBaoFei;
	}

	/**
	 * @return the jiaShiRenYiWaiShangHaiBaoFei
	 */
	public Double getJiaShiRenYiWaiShangHaiBaoFei()
	{
		return jiaShiRenYiWaiShangHaiBaoFei;
	}

	/**
	 * @param jiaShiRenYiWaiShangHaiBaoFei
	 *            the jiaShiRenYiWaiShangHaiBaoFei to set
	 */
	public void setJiaShiRenYiWaiShangHaiBaoFei(Double jiaShiRenYiWaiShangHaiBaoFei)
	{
		this.jiaShiRenYiWaiShangHaiBaoFei = jiaShiRenYiWaiShangHaiBaoFei;
	}

	/**
	 * @return the duZhuZuoYeRenYuanShangHaiBaoFei
	 */
	public Double getDuZhuZuoYeRenYuanShangHaiBaoFei()
	{
		return duZhuZuoYeRenYuanShangHaiBaoFei;
	}

	/**
	 * @param duZhuZuoYeRenYuanShangHaiBaoFei
	 *            the duZhuZuoYeRenYuanShangHaiBaoFei to set
	 */
	public void setDuZhuZuoYeRenYuanShangHaiBaoFei(Double duZhuZuoYeRenYuanShangHaiBaoFei)
	{
		this.duZhuZuoYeRenYuanShangHaiBaoFei = duZhuZuoYeRenYuanShangHaiBaoFei;
	}

	/**
	 * @return the boLiDanDuPoSuiBaoFei
	 */
	public Double getBoLiDanDuPoSuiBaoFei()
	{
		return boLiDanDuPoSuiBaoFei;
	}

	/**
	 * @param boLiDanDuPoSuiBaoFei
	 *            the boLiDanDuPoSuiBaoFei to set
	 */
	public void setBoLiDanDuPoSuiBaoFei(Double boLiDanDuPoSuiBaoFei)
	{
		this.boLiDanDuPoSuiBaoFei = boLiDanDuPoSuiBaoFei;
	}

	/**
	 * @return the ziRanSunShiBaoFei
	 */
	public Double getZiRanSunShiBaoFei()
	{
		return ziRanSunShiBaoFei;
	}

	/**
	 * @param ziRanSunShiBaoFei
	 *            the ziRanSunShiBaoFei to set
	 */
	public void setZiRanSunShiBaoFei(Double ziRanSunShiBaoFei)
	{
		this.ziRanSunShiBaoFei = ziRanSunShiBaoFei;
	}

	/**
	 * @return the weiXiuBaoYangZuoYeBaoFei
	 */
	public Double getWeiXiuBaoYangZuoYeBaoFei()
	{
		return weiXiuBaoYangZuoYeBaoFei;
	}

	/**
	 * @param weiXiuBaoYangZuoYeBaoFei
	 *            the weiXiuBaoYangZuoYeBaoFei to set
	 */
	public void setWeiXiuBaoYangZuoYeBaoFei(Double weiXiuBaoYangZuoYeBaoFei)
	{
		this.weiXiuBaoYangZuoYeBaoFei = weiXiuBaoYangZuoYeBaoFei;
	}

	/**
	 * @return the jiShenSunShiBaoFei
	 */
	public Double getJiShenSunShiBaoFei()
	{
		return jiShenSunShiBaoFei;
	}

	/**
	 * @param jiShenSunShiBaoFei
	 *            the jiShenSunShiBaoFei to set
	 */
	public void setJiShenSunShiBaoFei(Double jiShenSunShiBaoFei)
	{
		this.jiShenSunShiBaoFei = jiShenSunShiBaoFei;
	}

	/**
	 * @return the nongJiHuoTuoCheBaoFei
	 */
	public Double getNongJiHuoTuoCheBaoFei()
	{
		return nongJiHuoTuoCheBaoFei;
	}

	/**
	 * @param nongJiHuoTuoCheBaoFei
	 *            the nongJiHuoTuoCheBaoFei to set
	 */
	public void setNongJiHuoTuoCheBaoFei(Double nongJiHuoTuoCheBaoFei)
	{
		this.nongJiHuoTuoCheBaoFei = nongJiHuoTuoCheBaoFei;
	}

	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	/**
	 * @return the chargingStatus
	 */
	public String getChargingStatus()
	{
		return chargingStatus;
	}

	/**
	 * @param chargingStatus
	 *            the chargingStatus to set
	 */
	public void setChargingStatus(String chargingStatus)
	{
		this.chargingStatus = chargingStatus;
	}

	/**
	 * @return the caizhengbutie
	 */
	public Double getCaizhengbutie()
	{
		return caizhengbutie;
	}

	/**
	 * @param caizhengbutie
	 *            the caizhengbutie to set
	 */
	public void setCaizhengbutie(Double caizhengbutie)
	{
		this.caizhengbutie = caizhengbutie;
	}

	/**
	 * @return the jiJuTuoLuoSunShiBaoFei
	 */
	public Double getJiJuTuoLuoSunShiBaoFei()
	{
		return jiJuTuoLuoSunShiBaoFei;
	}

	/**
	 * @param jiJuTuoLuoSunShiBaoFei
	 *            the jiJuTuoLuoSunShiBaoFei to set
	 */
	public void setJiJuTuoLuoSunShiBaoFei(Double jiJuTuoLuoSunShiBaoFei)
	{
		this.jiJuTuoLuoSunShiBaoFei = jiJuTuoLuoSunShiBaoFei;
	}

	/**
	 * @return the yunZhuanYiWaiShangHaiBaoFei
	 */
	public Double getYunZhuanYiWaiShangHaiBaoFei()
	{
		return yunZhuanYiWaiShangHaiBaoFei;
	}

	/**
	 * @param yunZhuanYiWaiShangHaiBaoFei
	 *            the yunZhuanYiWaiShangHaiBaoFei to set
	 */
	public void setYunZhuanYiWaiShangHaiBaoFei(Double yunZhuanYiWaiShangHaiBaoFei)
	{
		this.yunZhuanYiWaiShangHaiBaoFei = yunZhuanYiWaiShangHaiBaoFei;
	}

	/**
	 * @return the buJiMianBuLvBaoFei
	 */
	public Double getBuJiMianBuLvBaoFei()
	{
		return buJiMianBuLvBaoFei;
	}

	/**
	 * @param buJiMianBuLvBaoFei
	 *            the buJiMianBuLvBaoFei to set
	 */
	public void setBuJiMianBuLvBaoFei(Double buJiMianBuLvBaoFei)
	{
		this.buJiMianBuLvBaoFei = buJiMianBuLvBaoFei;
	}

	/**
	 * @return the feiShiGuBuJianSunShiBaoFei
	 */
	public Double getFeiShiGuBuJianSunShiBaoFei()
	{
		return feiShiGuBuJianSunShiBaoFei;
	}

	/**
	 * @param feiShiGuBuJianSunShiBaoFei
	 *            the feiShiGuBuJianSunShiBaoFei to set
	 */
	public void setFeiShiGuBuJianSunShiBaoFei(Double feiShiGuBuJianSunShiBaoFei)
	{
		this.feiShiGuBuJianSunShiBaoFei = feiShiGuBuJianSunShiBaoFei;
	}

	/**
	 * @return the zhuangYunSunShiBaoFei
	 */
	public Double getZhuangYunSunShiBaoFei()
	{
		return zhuangYunSunShiBaoFei;
	}

	/**
	 * @param zhuangYunSunShiBaoFei
	 *            the zhuangYunSunShiBaoFei to set
	 */
	public void setZhuangYunSunShiBaoFei(Double zhuangYunSunShiBaoFei)
	{
		this.zhuangYunSunShiBaoFei = zhuangYunSunShiBaoFei;
	}

}
