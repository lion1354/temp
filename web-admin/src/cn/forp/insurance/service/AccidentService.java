/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.util.MongoDB;
import cn.forp.framework.core.util.QiNiu;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.vo.Accident;

/**
 * 事故信息管理Service
 */
@Service
public class AccidentService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(AccidentService.class);

	/**
	 * 添加
	 */
	public long create(Accident accident, String[] includeFields, String[] excludeFields) throws Exception
	{
		long accidentId = insertIntoTable(accident, includeFields, excludeFields);
		lg.info("新建事故信息信息：{}[{}]", accident.getAccident(), accidentId);
		return accidentId;
	}

	/**
	 * 修改
	 */
	public void update(Accident accident, String[] includeFields, String[] excludeFields) throws Exception
	{
		lg.debug("修改事故信息：" + accident.getId());
		updateTable(accident, includeFields, excludeFields);
	}

	public List<Accident> searchAccident(String id, String caseTel, String status, String approvalStatus) throws Exception
	{
		String sql = "select * from Sys_Accident_Info form where 1=1";
		List<Object> params = new ArrayList<>();
		if (StringUtils.isNotBlank(id))
		{
			sql += " and form.id like ?";
			params.add("%" + id + "%");
		}
		if (StringUtils.isNotBlank(caseTel))
		{
			sql += " and form.caseTel = ?";
			params.add(caseTel);
		}
		if (StringUtils.isNotBlank(status))
		{
			sql += " and form.status = ?";
			params.add(status);
		}
		if (StringUtils.isNotBlank(approvalStatus))
		{
			sql += " and form.approvalStatus = ?";
			params.add(approvalStatus);
		}
		lg.debug("SQL：{}", sql);
		List<Accident> list = findByList(sql, params.toArray(new Object[0]), Accident.class);
		return list;
	}

	public Page<Accident> searchReport(User user, IForm form, String status, PageSort ps) throws Exception
	{
		String sql = "select * from Sys_Accident_Info form where 1=1";
		List<Object> params = new ArrayList<>();
		String queryMachineryType = form.get("queryMachineryType");
		String queryYear = form.get("queryYear");
		String queryMemberName = form.get("queryMemberName");
		String queryCaseTel = form.get("queryCaseTel");
		String queryApprovalStatus = form.get("queryApprovalStatus");
		String diquSelected = form.get("diquSelected");
		String xianSelected = form.get("xianSelected");
		String querySwiftNumber = form.get("querySwiftNumber");
		String queryDamageMoney = form.get("queryDamageMoney");
		String queryPaymentMoney = form.get("queryPaymentMoney");

		lg.debug("searchType status ：{}", status);
		if (StringUtils.isNotBlank(status))
		{
			sql += " and form.status = ?";
			params.add(status);
		}
		if (StringUtils.isNotBlank(queryMachineryType) && !queryMachineryType.equals("-1"))
		{
			sql += " and form.machineryType = ?";
			params.add(queryMachineryType);
		}
		if (StringUtils.isNotBlank(queryYear) && !queryYear.equals("-1"))
		{
			sql += " and form.reportTime like ?";
			params.add("%" + queryYear + "%");
		}
		if (StringUtils.isNotBlank(queryMemberName))
		{
			sql += " and form.memberName like ?";
			params.add("%" + queryMemberName + "%");
		}
		if (StringUtils.isNotBlank(queryCaseTel))
		{
			sql += " and form.caseTel like ?";
			params.add("%" + queryCaseTel + "%");
		}
		if (StringUtils.isNotBlank(queryApprovalStatus) && !queryApprovalStatus.equals("-1"))
		{
			sql += " and form.approvalStatus = ?";
			params.add(queryApprovalStatus);
		}
		if (StringUtils.isNotBlank(diquSelected) && !diquSelected.equals("0"))
		{
			sql += " and form.provinceId = ?";
			params.add(diquSelected);
		}
		if (StringUtils.isNotBlank(xianSelected) && !xianSelected.equals("0"))
		{
			sql += " and form.regionId = ?";
			params.add(xianSelected);
		}
		if (StringUtils.isNotBlank(querySwiftNumber))
		{
			sql += " and form.swiftNumber like ?";
			params.add("%" + querySwiftNumber + "%");
		}
		if (StringUtils.isNotBlank(queryDamageMoney))
		{
			sql += " and form.damageMoney like ?";
			params.add("%" + queryDamageMoney + "%");
		}
		if (StringUtils.isNotBlank(queryPaymentMoney))
		{
			sql += " and form.paymentMoney like ?";
			params.add("%" + queryPaymentMoney + "%");
		}

		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(new Object[0]), Accident.class, ps);
	}

	public Page<Accident> searchSurvey(User user, IForm form, String status, PageSort ps) throws Exception
	{
		String sql = "select * from v_accident_survey form where 1=1";
		List<Object> params = new ArrayList<>();
		String queryMachineryType = form.get("queryMachineryType");
		String queryYear = form.get("queryYear");
		String queryMemberName = form.get("queryMemberName");
		String queryCaseTel = form.get("queryCaseTel");
		String queryApprovalStatus = form.get("queryApprovalStatus");
		String diquSelected = form.get("diquSelected");
		String xianSelected = form.get("xianSelected");
		String querySwiftNumber = form.get("querySwiftNumber");
		String queryDamageMoney = form.get("queryDamageMoney");
		String queryPaymentMoney = form.get("queryPaymentMoney");

		lg.debug("searchType status ：{}", status);
		// if (StringUtils.isNotBlank(status))
		// {
		// sql += " and form.status = ?";
		// params.add(status);
		// }
		if (StringUtils.isNotBlank(queryMachineryType) && !queryMachineryType.equals("-1"))
		{
			sql += " and form.machineryType = ?";
			params.add(queryMachineryType);
		}
		if (StringUtils.isNotBlank(queryYear) && !queryYear.equals("-1"))
		{
			sql += " and form.reportTime like ?";
			params.add("%" + queryYear + "%");
		}
		if (StringUtils.isNotBlank(queryMemberName))
		{
			sql += " and form.memberName like ?";
			params.add("%" + queryMemberName + "%");
		}
		if (StringUtils.isNotBlank(queryCaseTel))
		{
			sql += " and form.caseTel like ?";
			params.add("%" + queryCaseTel + "%");
		}
		if (StringUtils.isNotBlank(queryApprovalStatus) && !queryApprovalStatus.equals("-1"))
		{
			sql += " and form.approvalStatus = ?";
			params.add(queryApprovalStatus);
		}
		if (StringUtils.isNotBlank(diquSelected) && !diquSelected.equals("0"))
		{
			sql += " and form.provinceId = ?";
			params.add(diquSelected);
		}
		if (StringUtils.isNotBlank(xianSelected) && !xianSelected.equals("0"))
		{
			sql += " and form.regionId = ?";
			params.add(xianSelected);
		}
		if (StringUtils.isNotBlank(querySwiftNumber))
		{
			sql += " and form.swiftNumber like ?";
			params.add("%" + querySwiftNumber + "%");
		}
		if (StringUtils.isNotBlank(queryDamageMoney))
		{
			sql += " and form.damageMoney like ?";
			params.add("%" + queryDamageMoney + "%");
		}
		if (StringUtils.isNotBlank(queryPaymentMoney))
		{
			sql += " and form.paymentMoney like ?";
			params.add("%" + queryPaymentMoney + "%");
		}

		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(new Object[0]), Accident.class, ps);
	}

	public Page<Accident> searchSettlement(User user, IForm form, String status, PageSort ps) throws Exception
	{
		String sql = "select * from v_accident_claims form where 1=1";
		List<Object> params = new ArrayList<>();
		String queryMachineryType = form.get("queryMachineryType");
		String queryYear = form.get("queryYear");
		String queryMemberName = form.get("queryMemberName");
		String queryCaseTel = form.get("queryCaseTel");
		String queryApprovalStatus = form.get("queryApprovalStatus");
		String diquSelected = form.get("diquSelected");
		String xianSelected = form.get("xianSelected");
		String querySwiftNumber = form.get("querySwiftNumber");
		String queryDamageMoney = form.get("queryDamageMoney");
		String queryPaymentMoney = form.get("queryPaymentMoney");

		lg.debug("searchType status ：{}", status);
		// if (StringUtils.isNotBlank(status))
		// {
		// sql += " and form.status = ?";
		// params.add(status);
		// }
		if (StringUtils.isNotBlank(queryMachineryType) && !queryMachineryType.equals("-1"))
		{
			sql += " and form.machineryType = ?";
			params.add(queryMachineryType);
		}
		if (StringUtils.isNotBlank(queryYear) && !queryYear.equals("-1"))
		{
			sql += " and form.reportTime like ?";
			params.add("%" + queryYear + "%");
		}
		if (StringUtils.isNotBlank(queryMemberName))
		{
			sql += " and form.memberName like ?";
			params.add("%" + queryMemberName + "%");
		}
		if (StringUtils.isNotBlank(queryCaseTel))
		{
			sql += " and form.caseTel like ?";
			params.add("%" + queryCaseTel + "%");
		}
		if (StringUtils.isNotBlank(queryApprovalStatus) && !queryApprovalStatus.equals("-1"))
		{
			sql += " and form.approvalStatus = ?";
			params.add(queryApprovalStatus);
		}
		if (StringUtils.isNotBlank(diquSelected) && !diquSelected.equals("0"))
		{
			sql += " and form.provinceId = ?";
			params.add(diquSelected);
		}
		if (StringUtils.isNotBlank(xianSelected) && !xianSelected.equals("0"))
		{
			sql += " and form.regionId = ?";
			params.add(xianSelected);
		}
		if (StringUtils.isNotBlank(querySwiftNumber))
		{
			sql += " and form.swiftNumber like ?";
			params.add("%" + querySwiftNumber + "%");
		}
		if (StringUtils.isNotBlank(queryDamageMoney))
		{
			sql += " and form.damageMoney like ?";
			params.add("%" + queryDamageMoney + "%");
		}
		if (StringUtils.isNotBlank(queryPaymentMoney))
		{
			sql += " and form.paymentMoney like ?";
			params.add("%" + queryPaymentMoney + "%");
		}

		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(new Object[0]), Accident.class, ps);
	}

	public void update(Accident accident, String[] includeFields) throws Exception
	{
		lg.debug("修改案件：" + accident.getId());
		updateTable(accident, includeFields, null, "id", String.valueOf(accident.getId()));
	}

	public Accident searchById(String id) throws Exception
	{
		String sql = "select * from Sys_Accident_Info where id=?";
		List<Accident> list = findByList(sql, Accident.class, id);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	// =================================================================
	// 查勘
	// =================================================================

	/**
	 * 添加查勘信息
	 *
	 * @param user
	 *            操作人
	 * @param survey
	 *            查勘信息
	 * @param zhengti
	 *            事故现场整体照片(1-4张)
	 * @param jubu
	 *            事故现场局部照片(1-20张)
	 * @param xingshi
	 *            行驶证照片(0-1张)
	 * @param jiashi
	 *            驾驶证照片(0-1张)
	 * @param jipai
	 *            机牌合影照片(0-1张)
	 * @param jijia
	 *            农机机架号照片(0-1张)
	 * @param shangzhe
	 *            受伤者照片(0-1张)
	 * @param buwei
	 *            伤者部位照片(0-5张)
	 */
	public void createSurvey(User user, Accident survey, MultipartFile[] zhengti, MultipartFile[] jubu, MultipartFile xingshi, MultipartFile jiashi, MultipartFile jipai,
			MultipartFile jijia, MultipartFile shangzhe, MultipartFile[] buwei) throws Exception
	{
		// 1 保存查勘信息
		long id = insertIntoTable(survey);

		// 2 保存照片
		saveSurveyImages(id, user, zhengti, jubu, xingshi, jiashi, jipai, jijia, shangzhe, buwei);

	}

	/**
	 * 修改查勘信息
	 *
	 * @param user
	 *            操作人
	 * @param survey
	 *            查勘信息
	 * @param deletedImages
	 *            删除的图片UUID列表
	 * @param zhengti
	 *            事故现场整体照片(1-4张)
	 * @param jubu
	 *            事故现场局部照片(1-20张)
	 * @param xingshi
	 *            行驶证照片(0-1张)
	 * @param jiashi
	 *            驾驶证照片(0-1张)
	 * @param jipai
	 *            机牌合影照片(0-1张)
	 * @param jijia
	 *            农机机架号照片(0-1张)
	 * @param shangzhe
	 *            受伤者照片(0-1张)
	 * @param buwei
	 *            伤者部位照片(0-5张)
	 */
	public void updateSurvey(User user, Accident survey, String deletedImages, MultipartFile[] zhengti, MultipartFile[] jubu, MultipartFile xingshi, MultipartFile jiashi,
			MultipartFile jipai, MultipartFile jijia, MultipartFile shangzhe, MultipartFile[] buwei) throws Exception
	{
		// 1 更新查勘信息
		updateTable(survey, new String[] { "lastModifyDate", "lastModifyUserName" });

		// 2 删除照片
		String sql = "delete from Sys_Accident_Image where FK_AccidentId=? and ImageId=?";
		String[] images = deletedImages.split(",");
		for (String imageId : images)
		{
			jdbc.update(sql, survey.getId(), imageId);
			lg.debug("删除照片：{}", imageId);

			// 删除MongoDB附件
			MongoDB.deleteFile(imageId);
		}

		// 3 保存新上传的图片
		saveSurveyImages(survey.getId(), user, zhengti, jubu, xingshi, jiashi, jipai, jijia, shangzhe, buwei);
	}

	/**
	 * 保存查勘附件图片列表
	 *
	 * @param id
	 *            查勘ID
	 * @param user
	 *            操作人
	 * @param zhengti
	 *            事故现场整体照片(1-4张)
	 * @param jubu
	 *            事故现场局部照片(1-20张)
	 * @param xingshi
	 *            行驶证照片(0-1张)
	 * @param jiashi
	 *            驾驶证照片(0-1张)
	 * @param jipai
	 *            机牌合影照片(0-1张)
	 * @param jijia
	 *            农机机架号照片(0-1张)
	 * @param shangzhe
	 *            受伤者照片(0-1张)
	 * @param buwei
	 *            伤者部位照片(0-5张)
	 */
	private void saveSurveyImages(Long id, User user, MultipartFile[] zhengti, MultipartFile[] jubu, MultipartFile xingshi, MultipartFile jiashi, MultipartFile jipai,
			MultipartFile jijia, MultipartFile shangzhe, MultipartFile[] buwei) throws Exception
	{
		String imageId, imageType;
		List<Object[]> params = new ArrayList<>();
		String sql = "insert into Sys_Accident_Image (FK_AccidentId, Type, ImageId, UserID) values(?, ?, ?, ?)";

		// 1 事故现场整体照片
		if (null != zhengti && zhengti.length > 0)
		{
			imageType = "1";
			for (MultipartFile f : zhengti)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSurveyImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		// 2 事故现场局部照片
		if (null != jubu && jubu.length > 0)
		{
			imageType = "2";
			for (MultipartFile f : jubu)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSurveyImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		// 3 行驶证照片
		if (null != xingshi && !xingshi.isEmpty())
		{
			imageType = "3";
			// 保存
			imageId = saveSurveyImage(id, xingshi, imageType);
			// 记录SQL批量参数
			params.add(new Object[] { id, imageType, imageId, user.getId() });
		}

		// 4 驾驶证照片
		if (null != jiashi && !jiashi.isEmpty())
		{
			imageType = "4";
			// 保存
			imageId = saveSurveyImage(id, jiashi, imageType);
			// 记录SQL批量参数
			params.add(new Object[] { id, imageType, imageId, user.getId() });
		}

		// 5 机牌合影照片
		if (null != jipai && !jipai.isEmpty())
		{
			imageType = "5";
			// 保存
			imageId = saveSurveyImage(id, jipai, imageType);
			// 记录SQL批量参数
			params.add(new Object[] { id, imageType, imageId, user.getId() });
		}

		// 6 农机机架号照片
		if (null != jijia && !jijia.isEmpty())
		{
			imageType = "6";
			// 保存
			imageId = saveSurveyImage(id, jijia, imageType);
			// 记录SQL批量参数
			params.add(new Object[] { id, imageType, imageId, user.getId() });
		}

		// 7 受伤者照片
		if (null != shangzhe && !shangzhe.isEmpty())
		{
			imageType = "7";
			// 保存
			imageId = saveSurveyImage(id, shangzhe, imageType);
			// 记录SQL批量参数
			params.add(new Object[] { id, imageType, imageId, user.getId() });
		}

		// 8 伤者部位照片
		if (null != buwei && buwei.length > 0)
		{
			imageType = "8";
			for (MultipartFile f : buwei)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSurveyImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		// 9 批量插入数据库
		jdbc.batchUpdate(sql, params);
	}

	/**
	 * 保存查勘图片附件
	 *
	 * @param id
	 *            查勘ID
	 * @param image
	 *            图片文件
	 * @param imageType
	 *            图片类型
	 */
	private String saveSurveyImage(long id, MultipartFile image, String imageType) throws Exception
	{
		// 存入本地MongoDB
		DBObject metaData = new BasicDBObject();
		metaData.put("type", "survey-" + imageType);
		String imageId = MongoDB.saveFile(null, image, metaData);

		// 上传至七牛第三方存储
		QiNiu.upload(image, imageId);

		return imageId;
	}

	/**
	 * 加载查勘详情信息
	 *
	 * @param id
	 *            查勘ID
	 */
	public Accident getSurvey(Long id) throws Exception
	{
		// 查勘信息
		Accident survey = new Accident();
		String sqlSurvey = "select * from V_Accident_Survey where ID=?";
		List<Accident> list = findByList(sqlSurvey, Accident.class, id);
		lg.debug("查勘详情list.size >  " + list.size());
		if (list.size() > 0)
		{
			survey = list.get(0);
		}

		// 图片信息
		String sql = "select * from Sys_Accident_Image where FK_AccidentId=?";
		SqlRowSet rs = jdbc.queryForRowSet(sql, id);
		int type;
		while (rs.next())
		{
			type = rs.getInt("Type");
			switch (type)
			{
			case 1:
			{
				if (null == survey.getZhengtiList())
					survey.setZhengtiList(new ArrayList<>());

				survey.getZhengtiList().add(rs.getString("ImageId"));
				break;
			}
			case 2:
			{
				if (null == survey.getJubuList())
					survey.setJubuList(new ArrayList<>());

				survey.getJubuList().add(rs.getString("ImageId"));
				break;
			}
			case 3:
			{
				if (null == survey.getXingshiList())
					survey.setXingshiList(new ArrayList<>());

				survey.getXingshiList().add(rs.getString("ImageId"));
				break;
			}
			case 4:
			{
				if (null == survey.getJiashiList())
					survey.setJiashiList(new ArrayList<>());

				survey.getJiashiList().add(rs.getString("ImageId"));
				break;
			}
			case 5:
			{
				if (null == survey.getJipaiList())
					survey.setJipaiList(new ArrayList<>());

				survey.getJipaiList().add(rs.getString("ImageId"));
				break;
			}
			case 6:
			{
				if (null == survey.getJijiaList())
					survey.setJijiaList(new ArrayList<>());

				survey.getJijiaList().add(rs.getString("ImageId"));
				break;
			}
			case 7:
			{
				if (null == survey.getShangzheList())
					survey.setShangzheList(new ArrayList<>());

				survey.getShangzheList().add(rs.getString("ImageId"));
				break;
			}
			case 8:
			{
				if (null == survey.getBuweiList())
					survey.setBuweiList(new ArrayList<>());

				survey.getBuweiList().add(rs.getString("ImageId"));
				break;
			}
			}
		}

		return survey;
	}

	// =================================================================
	// 报案
	// =================================================================

	/**
	 * 添加报案信息
	 */
	public void createSettlement(User user, Accident settlement, MultipartFile[] sunshi, MultipartFile[] weixiu, MultipartFile[] zeren, MultipartFile[] panjue,
			MultipartFile[] zhenduan, MultipartFile[] fapiao, MultipartFile[] yongyao) throws Exception
	{
		// 1 保存查勘信息
		long id = insertIntoTable(settlement);

		// 2 保存照片
		saveSettlementImages(id, user, sunshi, weixiu, zeren, panjue, zhenduan, fapiao, yongyao);

	}

	/**
	 * 修改报案信息
	 *
	 */
	public void updateSettlement(User user, Accident settlement, String deletedImages, MultipartFile[] sunshi, MultipartFile[] weixiu, MultipartFile[] zeren,
			MultipartFile[] panjue, MultipartFile[] zhenduan, MultipartFile[] fapiao, MultipartFile[] yongyao) throws Exception
	{
		// 1 更新查勘信息
		updateTable(settlement, new String[] { "lastModifyDate", "lastModifyUserName" });

		// 2 删除照片
		String sql = "delete from Sys_Accident_Image where FK_AccidentId=? and ImageId=?";
		String[] images = deletedImages.split(",");
		for (String imageId : images)
		{
			jdbc.update(sql, settlement.getId(), imageId);
			lg.debug("删除照片：{}", imageId);

			// 删除MongoDB附件
			MongoDB.deleteFile(imageId);
		}

		// 3 保存新上传的图片
		saveSettlementImages(settlement.getId(), user, sunshi, weixiu, zeren, panjue, zhenduan, fapiao, yongyao);
	}

	/**
	 * 保存报案附件图片列表
	 */
	private void saveSettlementImages(Long id, User user, MultipartFile[] sunshi, MultipartFile[] weixiu, MultipartFile[] zeren, MultipartFile[] panjue, MultipartFile[] zhenduan,
			MultipartFile[] fapiao, MultipartFile[] yongyao) throws Exception
	{
		String imageId, imageType;
		List<Object[]> params = new ArrayList<>();
		String sql = "insert into Sys_Accident_Image (FK_AccidentId, Type, ImageId, UserID) values(?, ?, ?, ?)";

		if (null != sunshi && sunshi.length > 0)
		{
			imageType = "9";
			for (MultipartFile f : sunshi)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		if (null != weixiu && weixiu.length > 0)
		{
			imageType = "10";
			for (MultipartFile f : weixiu)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		if (null != zeren && zeren.length > 0)
		{
			imageType = "11";
			for (MultipartFile f : zeren)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		if (null != panjue && panjue.length > 0)
		{
			imageType = "12";
			for (MultipartFile f : panjue)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		if (null != zhenduan && zhenduan.length > 0)
		{
			imageType = "13";
			for (MultipartFile f : zhenduan)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		if (null != fapiao && fapiao.length > 0)
		{
			imageType = "14";
			for (MultipartFile f : fapiao)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		if (null != yongyao && yongyao.length > 0)
		{
			imageType = "15";
			for (MultipartFile f : yongyao)
			{
				if (null != f && !f.isEmpty())
				{
					// 保存
					imageId = saveSettlementImage(id, f, imageType);
					// 记录SQL批量参数
					params.add(new Object[] { id, imageType, imageId, user.getId() });
				}
			}
		}

		// 批量插入数据库
		jdbc.batchUpdate(sql, params);
	}

	/**
	 * 保存报案图片附件
	 */
	private String saveSettlementImage(long id, MultipartFile image, String imageType) throws Exception
	{
		// 存入本地MongoDB
		DBObject metaData = new BasicDBObject();
		metaData.put("type", "survey-" + imageType);
		String imageId = MongoDB.saveFile(null, image, metaData);

		// 上传至七牛第三方存储
		QiNiu.upload(image, imageId);

		return imageId;
	}

	/**
	 * 加载报案详情信息
	 */
	public Accident getSettlement(Long id) throws Exception
	{
		// 查勘信息
		Accident settlement = new Accident();
		String sqlSettlement = "select * from V_Accident_Claims where ID=?";
		List<Accident> list = findByList(sqlSettlement, Accident.class, id);
		lg.debug("理赔详情list.size >  " + list.size());
		if (list.size() > 0)
		{
			settlement = list.get(0);
		}

		// 图片信息
		String sql = "select * from Sys_Accident_Image where FK_AccidentId=?";
		SqlRowSet rs = jdbc.queryForRowSet(sql, id);
		int type;
		while (rs.next())
		{
			type = rs.getInt("Type");
			switch (type)
			{
			case 9:
			{
				if (null == settlement.getSunshiList())
					settlement.setSunshiList(new ArrayList<>());

				settlement.getSunshiList().add(rs.getString("ImageId"));
				break;
			}
			case 10:
			{
				if (null == settlement.getWeixiuList())
					settlement.setWeixiuList(new ArrayList<>());

				settlement.getWeixiuList().add(rs.getString("ImageId"));
				break;
			}
			case 11:
			{
				if (null == settlement.getZerenList())
					settlement.setZerenList(new ArrayList<>());

				settlement.getZerenList().add(rs.getString("ImageId"));
				break;
			}
			case 12:
			{
				if (null == settlement.getPanjueList())
					settlement.setPanjueList(new ArrayList<>());

				settlement.getPanjueList().add(rs.getString("ImageId"));
				break;
			}
			case 13:
			{
				if (null == settlement.getZhenduanList())
					settlement.setZhenduanList(new ArrayList<>());

				settlement.getZhenduanList().add(rs.getString("ImageId"));
				break;
			}
			case 14:
			{
				if (null == settlement.getFapiaoList())
					settlement.setFapiaoList(new ArrayList<>());

				settlement.getFapiaoList().add(rs.getString("ImageId"));
				break;
			}
			case 15:
			{
				if (null == settlement.getYongyaoList())
					settlement.setYongyaoList(new ArrayList<>());

				settlement.getYongyaoList().add(rs.getString("ImageId"));
				break;
			}
			}
		}

		return settlement;
	}
}