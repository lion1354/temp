package cn.forp.stastics.service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.forp.framework.core.BaseService;
import cn.forp.stastics.vo.NongJiStastics;
import cn.forp.stastics.vo.SGJStastics;
import cn.forp.stastics.vo.TractorStastics;

@Service
public class StasticsService extends BaseService
{
	public List<NongJiStastics> getNongjiStastics(String year, String beginDate, String endDate, int regionsId) throws Exception
	{
		String condition = " and t.formStatus <> 3 ";
		String xianCondition = "";
		if ((year != null) && (!year.equals("")) && (!year.equalsIgnoreCase("-1")))
		{
			condition = condition + "and t.qiandanYear ='" + year + "'";
		}
		condition = condition + getTimeCondition(beginDate, endDate);

		if (regionsId != 0)
		{
			xianCondition = xianCondition + " and x.xianId ='" + regionsId + "' ";
		}
		List<NongJiStastics> list = new ArrayList<NongJiStastics>();
		List<NongJiStastics> results = new ArrayList<NongJiStastics>();
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select nj.xian,nj.diqu,nj.fst_50,nj.fst_100,nj.fst_jifen,nj.fst_fenshu,nj.fst_money-nj.fst_jifen as fst_money,nj.trd_100,nj.trd_200,nj.trd_500,nj.trd_600,nj.trd_700,nj.trd_jifen,nj.trd_fenshu,nj.trd_money-nj.trd_jifen as trd_money,nj.little_jifen ,nj.fst_fenshu+nj.trd_fenshu as little_fenshu,nj.fst_money+nj.trd_money-nj.fst_jifen-nj.trd_jifen as little_money,nj.driver_50,nj.driver_100,nj.driver_jifen,nj.driver_fenshu,nj.driver_money-nj.driver_jifen as driver_money,nj.fst_jifen+nj.trd_jifen+nj.driver_jifen as totle_jifen ,nj.totle_fenshu,nj.fst_money-nj.fst_jifen+nj.trd_money-nj.trd_jifen+nj.driver_money-nj.driver_jifen as totle_money from ");

		sb.append(
				"(select xianName as xian,areaName as diqu,ifnull(js_50,0)as fst_50,ifnull(js_100,0) as fst_100,ifnull(jsDkSum,0) as fst_jifen,ifnull(js_50,0)+ifnull(js_100,0) as fst_fenshu,50*ifnull(js_50,0)+100*ifnull(js_100,0) as fst_money,ifnull(js_34_100,0)as trd_100,ifnull(js_34_200,0)as trd_200,ifnull(js_34_500,0) as trd_500,ifnull(js_34_600,0)as trd_600,ifnull(js_34_700,0)as trd_700,ifnull(jsDkSum_34,0) as trd_jifen,ifnull(js_34_100,0)+ifnull(js_34_200,0)+ifnull(js_34_500,0)+ifnull(js_34_600,0)+ifnull(js_34_700,0) as trd_fenshu,100*ifnull(js_34_100,0)+200*ifnull(js_34_200,0)+500*ifnull(js_34_500,0)+600*ifnull(js_34_600,0)+700*ifnull(js_34_700,0) as trd_money,ifnull(jsDkSum,0)+ifnull(jsDkSum_34,0) as little_jifen,ifnull(dri_50,0) as driver_50,ifnull(dri_100,0) as driver_100,ifnull(driDkSum,0) as driver_jifen,ifnull(dri_100,0)+ifnull(dri_50,0) as driver_fenshu,ifnull(dri_100,0)*100+ifnull(dri_50,0)*50 as driver_money,ifnull(totle_fenshu,0) as totle_fenshu ");
		sb.append(
				"from ((select * from (select * from(select * from (select * from (select * from(select * from (select * from (select * from (select * from (select * from (select * from (select * from (select x.*,a.areaName from sys_xian as x,sys_area as a where x.areaId=a.areaId "
						+ xianCondition + ") as x_a ");
		sb.append(
				"left join (select count(t.xianId) as js_50,t.xianId as id_50 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=50 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (1,2))"
						+ condition + " group by t.xianId) as t1 ");
		sb.append(
				"on x_a.xianId=t1.id_50) as table_50 left join (select count(t.xianId) as js_100,t.xianId as id_100 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=100 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (1,2)) "
						+ condition + "group by t.xianId) as t2 ");
		sb.append(
				"on table_50.xianId = t2.id_100) as table_100 left join (select SUM(t.jishenDikouMoney)as jsDkSum,t.xianId as id_js_dksum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (1,2)) "
						+ condition + "group by t.xianId) as t3 ");
		sb.append("on table_100.xianId = t3.id_js_dksum) as table_jsdksum left join ");
		sb.append(
				"(select count(t.xianId) as js_34_100,t.xianId as id_34_100 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=100 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (3,4)) "
						+ condition + "group by t.xianId) as t4 ");
		sb.append("on table_jsdksum.xianId = t4.id_34_100) as table_js_34_100 left join ");
		sb.append(
				"(select count(t.xianId) as js_34_200,t.xianId as id_34_200 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=200 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (3,4)) "
						+ condition + "group by t.xianId) as t5 ");
		sb.append("on table_js_34_100.xianId = t5.id_34_200) as table_js_34_200 left join ");
		sb.append(
				"(select count(t.xianId) as js_34_500,t.xianId as id_34_500 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=500 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (3,4)) "
						+ condition + "group by t.xianId) as t5 ");
		sb.append("on table_js_34_200.xianId = t5.id_34_500) as table_js_34_500 left join ");
		sb.append(
				"(select count(t.xianId) as js_34_600,t.xianId as id_34_600 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=600 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (3,4)) "
						+ condition + "group by t.xianId) as t5 ");
		sb.append("on table_js_34_500.xianId = t5.id_34_600) as table_js_34_600 left join ");
		sb.append(
				"(select count(t.xianId) as js_34_700,t.xianId as id_34_700 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=700 and a.xianId=t.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (3,4)) "
						+ condition + "group by t.xianId) as t5 ");
		sb.append("on table_js_34_600.xianId = t5.id_34_700)as table_js_34_700 left join ");
		sb.append(
				"(select SUM(t.jishenDikouMoney)as jsDkSum_34,t.xianId as id_js_dksum_34 from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=2 and t.insuranceTractorId in (select tt.insuranceTractorId from sys_type_tractor as tt where tt.typeId in (3,4)) "
						+ condition + "group by t.xianId) as t3 ");
		sb.append("on table_js_34_700.xianId = t3.id_js_dksum_34) as table_jsdksum34 left join ");
		sb.append("(select count(t.xianId) as dri_50,t.xianId as id_dri_50 from sys_tractor as t,sys_xian as a where t.driverHuiFeiMoney=50 and a.xianId=t.xianId and t.type=2  "
				+ condition + "group by t.xianId) as t5 ");
		sb.append("on table_jsdksum34.xianId = t5.id_dri_50) as table_dri_50 left join ");
		sb.append("(select count(t.xianId) as dri_100,t.xianId as id_dri_100 from sys_tractor as t,sys_xian as a where t.driverHuiFeiMoney=100 and a.xianId=t.xianId and t.type=2  "
				+ condition + "group by t.xianId) as t5 ");
		sb.append("on table_dri_50.xianId = t5.id_dri_100) as table_dri_100 left join ");
		sb.append("(select count(*) as totle_fenshu,t.xianId as id_totle_fenshu from sys_tractor as t,sys_xian as a where a.xianId=t.xianId and t.type=2  " + condition
				+ "group by t.xianId) as t5 ");
		sb.append("on table_dri_100.xianId = t5.id_totle_fenshu) as table_totle_fenshu left join ");
		sb.append("(select SUM(t.driverDiKouMoney)as driDkSum,t.xianId as id_dri_dksum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=2  " + condition
				+ "group by t.xianId) as t3 ");
		sb.append("on table_totle_fenshu.xianId = t3.id_dri_dksum)) as nj");

		final String sql = sb.toString();

		list = findByList(sql, NongJiStastics.class);

		int rowspan = 0;
		NongJiStastics recordTemp = list.get(0);
		String areaName = recordTemp.getDiqu();
		int startRow = 0;

		for (int i = 0; i < list.size(); i++)
		{
			NongJiStastics entity = list.get(i);
			entity.setColspan(Integer.valueOf(0));
			if (entity.getDiqu().equalsIgnoreCase(areaName))
			{
				rowspan++;
			}
			else
			{
				results.get(startRow).setColspan(Integer.valueOf(rowspan));
				startRow = i;
				areaName = entity.getDiqu();
				rowspan = 1;
			}
			results.add(entity);
		}
		results.get(startRow).setColspan(Integer.valueOf(rowspan));
		NongJiStastics totalEntity = (NongJiStastics) sumEntities(results, NongJiStastics.class.getName());
		if (totalEntity != null)
		{
			totalEntity.setDiqu("所有地区");
			totalEntity.setXian("合计");
			totalEntity.setColspan(Integer.valueOf(1));
			results.add(totalEntity);
		}
		return results;
	}

	public List<SGJStastics> getSGJStastics(String year, String beginDate, String endDate, int regionsId) throws Exception
	{
		String condition = " and t.formStatus <> 3 ";
		String xianCondition = "";
		if ((year != null) && (!year.equals("")) && (!year.equalsIgnoreCase("-1")))
		{
			condition = condition + "and t.qiandanYear ='" + year + "'";
		}
		condition = condition + getTimeCondition(beginDate, endDate);

		if (regionsId != 0)
		{
			xianCondition = xianCondition + " and x.xianId ='" + regionsId + "' ";
		}
		List<SGJStastics> results = new ArrayList<SGJStastics>();
		List<SGJStastics> list = new ArrayList<SGJStastics>();
		List<SGJStastics> sublist = new ArrayList<SGJStastics>();
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select nj.xian,nj.diqu,nj.jishen_300,nj.jishen_400,nj.jishen_600,nj.jishen_700,nj.jishen_800,nj.jishen_850,nj.jishen_900,nj.jishen_950,nj.jishen_fenshu,nj.jishen_money, ");
		sb.append("nj.third_600,nj.third_700 ,nj.third_800,nj.third_fenshu,nj.third_money as third_money,  ");
		sb.append(
				"nj.driver_operate_100,nj.driver_operate_200,nj.combine_xianbu_money,nj.combine_xianbu_fenshu,nj.combine_shengbu_fenshu,nj.driver_operate_fenshu,nj.driver_operate_money,nj.driver_mainten_50,nj.driver_mainten_100,nj.driver_mainten_fenshu,nj.driver_mainten_money, ");
		sb.append("nj.driver_operate_fenshu+nj.driver_mainten_fenshu as little_driver_fenshu,nj.driver_operate_money+nj.driver_mainten_money as little_driver_money, ");
		sb.append(
				"nj.combine_400,nj.combine_600,nj.combine_800,nj.combine_950,nj.combine_1000,nj.combine_1200,nj.little_combine_fenshu,nj.combine_money-nj.combine_xianbu_money as little_combine_money, ");
		sb.append("nj.jishen_fenshu+nj.driver_operate_fenshu+nj.driver_mainten_fenshu+nj.little_combine_fenshu as totle_fenshu,nj.totle_jifen, ");
		sb.append("nj.jishen_money+nj.third_money+nj.driver_operate_money+nj.driver_mainten_money+nj.combine_money-nj.totle_jifen as totle_money,nj.totle_jifen_fenshu from ");
		sb.append(
				"(select xianName as xian,areaName as diqu,ifnull(js_300,0)as jishen_300,ifnull(js_400,0) as jishen_400,ifnull(js_600,0) as jishen_600,ifnull(js_700,0) as jishen_700,ifnull(js_800,0) as jishen_800, ");
		sb.append(
				"ifnull(js_850,0) as jishen_850,ifnull(js_900,0) as jishen_900,ifnull(js_950,0) as jishen_950,ifnull(dkSum,0) as totle_jifen,ifnull(js_300,0)+ifnull(js_400,0)+ifnull(js_600,0)+ifnull(js_700,0)+ifnull(js_800,0)+ifnull(js_850,0)+ifnull(js_900,0)+ifnull(js_950,0) as jishen_fenshu, ");
		sb.append(
				"300*ifnull(js_300,0)+400*ifnull(js_400,0)+600*ifnull(js_600,0)+700*ifnull(js_700,0)+800*ifnull(js_800,0)+850*ifnull(js_850,0)+900*ifnull(js_900,0)+950*ifnull(js_950,0) as jishen_money, ");
		sb.append(
				"ifnull(trd_600,0)as third_600,ifnull(trd_700,0)as third_700,ifnull(trd_800,0) as third_800,ifnull(totle_jifen_fenshu,0)as totle_jifen_fenshu,ifnull(trd_600,0)+ifnull(trd_700,0)+IFNULL(trd_800,0) as third_fenshu,600*ifnull(trd_600,0)+700*ifnull(trd_700,0)+800*IFNULL(trd_800,0) as third_money, ");
		sb.append(
				"IFNULL(dri_opr_100,0) as driver_operate_100,IFNULL(dri_opr_200,0) as driver_operate_200,IFNULL(xianbu_fenshu,0) as combine_xianbu_fenshu,IFNULL(dri_opr_100,0)+IFNULL(dri_opr_200,0) as driver_operate_fenshu,100*IFNULL(dri_opr_100,0)+200*IFNULL(dri_opr_200,0) as driver_operate_money, ");
		sb.append(
				"IFNULL(dri_mt_50,0) as driver_mainten_50,IFNULL(dri_mt_100,0) as driver_mainten_100,IFNULL(shengbu_fenshu,0) as combine_shengbu_fenshu,IFNULL(dri_mt_50,0)+IFNULL(dri_mt_100,0) as driver_mainten_fenshu,50*IFNULL(dri_mt_50,0)+100*IFNULL(dri_mt_100,0) as driver_mainten_money, ");
		sb.append(
				"ifnull(com_400,0) as combine_400,ifnull(xianSum,0) as combine_xianbu_money,ifnull(com_600,0) as combine_600,ifnull(com_700,0) as combine_700,ifnull(com_800,0) as combine_800,ifnull(com_950,0) as combine_950,ifnull(com_1000,0) as combine_1000,ifnull(com_1200,0) as combine_1200, ");
		sb.append(
				"ifnull(com_600,0)+ifnull(com_800,0)+ifnull(com_950,0)+ifnull(com_1000,0)+ifnull(com_1200,0) as little_combine_fenshu,400*ifnull(com_400,0)+600*ifnull(com_600,0)+800*ifnull(com_800,0)+950*ifnull(com_950,0)+1000*ifnull(com_1000,0)+1200*ifnull(com_1200,0) as combine_money ");
		sb.append(
				"from ((select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from ");
		sb.append(
				"(select * from(select * from (select * from (select * from(select * from (select * from (select * from (select * from (select * from (select * from (select * from (select x.*,a.areaName from sys_xian as x,sys_area as a where x.areaId=a.areaId "
						+ xianCondition + ") as x_a ");
		sb.append(
				"left join (select count(t.xianId) as js_300,t.xianId as id_300 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=300 and a.xianId=t.xianId and t.type=1 "
						+ condition + "group by t.xianId) as ts on x_a.xianId=ts.id_300) as table_300 left join ");
		sb.append("(select count(t.xianId) as js_400,t.xianId as id_400 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=400 and a.xianId=t.xianId and t.type=1 "
				+ condition + "group by t.xianId) as ts on table_300.xianId = ts.id_400) as table_400 left join ");

		sb.append("(select count(t.xianId) as js_600,t.xianId as id_600 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=600 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_400.xianId= ts.id_600) as table_600 left join");
		sb.append("(select count(t.xianId) as js_700,t.xianId as id_700 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=700 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_600.xianId = ts.id_700) as table_700 left join ");
		sb.append("(select count(t.xianId) as js_800,t.xianId as id_800 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=800 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_700.xianId = ts.id_800) as table_800 left join ");
		sb.append("(select count(t.xianId) as js_850,t.xianId as id_850 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=850 and a.xianId=t.xianId and t.type=1 "
				+ condition + "group by t.xianId) as ts on table_800.xianId = ts.id_850) as table_850 left join ");
		sb.append("(select count(t.xianId) as js_900,t.xianId as id_900 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=900 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_850.xianId = ts.id_900)as table_900 left join ");
		sb.append("(select count(t.xianId) as js_950,t.xianId as id_950 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=950 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_900.xianId = ts.id_950) as table_950 left join ");
		sb.append("(select SUM(t.totalDikouMoney)as dkSum,t.xianId as id_js_dksum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  " + condition
				+ "group by t.xianId) as ts on table_950.xianId = ts.id_js_dksum) as table_js_dksum left join ");
		sb.append("(select count(t.xianId) as trd_600,t.xianId as id_trd_600 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=600 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_js_dksum.xianId = ts.id_trd_600) as table_trd_600 left join ");
		sb.append("(select count(t.xianId) as trd_700,t.xianId as id_trd_700 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=700 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_trd_600.xianId = ts.id_trd_700) as table_trd_700 left join ");
		sb.append("(select count(t.xianId) as trd_800,t.xianId as id_trd_800 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=800 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_trd_700.xianId = ts.id_trd_800) as table_trd_800 left join ");
		sb.append("(select count(t.totalDikouMoney)as totle_jifen_fenshu,t.xianId as id_trd_dksum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_trd_800.xianId = ts.id_trd_dksum) as table_trd_dksum left join ");
		sb.append(
				"(select count(t.xianId) dri_opr_100,t.xianId as id_dri_opr_as_100 from sys_tractor as t,sys_xian as a where t.driverOperateHuiFeiMoney=100 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_trd_dksum.xianId = ts.id_dri_opr_as_100) as table_dri_opr_as_100 left join ");
		sb.append(
				"(select count(t.xianId) dri_opr_200,t.xianId as id_dri_opr_as_200 from sys_tractor as t,sys_xian as a where t.driverOperateHuiFeiMoney=200 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_dri_opr_as_100.xianId = ts.id_dri_opr_as_200) as table_dri_opr_as_100 left join ");
		sb.append("(select SUM(t.combineXianMoney)as xianSum,t.xianId as id_trd_opr_dksum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  " + condition
				+ "group by t.xianId) as ts on table_dri_opr_as_100.xianId = ts.id_trd_opr_dksum) as table_trd_opr_dksum left join ");
		sb.append(
				"(select count(t.xianId) dri_mt_50,t.xianId as id_dri_mt_as_50 from sys_tractor as t,sys_xian as a where t.driverMaintenHuiFeiMoney=50 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_trd_opr_dksum.xianId = ts.id_dri_mt_as_50) as table_dri_mt_as_50 left join ");
		sb.append(
				"(select count(t.xianId) dri_mt_100,t.xianId as id_dri_mt_as_100 from sys_tractor as t,sys_xian as a where t.driverMaintenHuiFeiMoney=100 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_dri_mt_as_50.xianId = ts.id_dri_mt_as_100) as table_dri_mt_as_100 left join ");
		sb.append(
				"(select count(t.combineProvinceMoney)as shengbu_fenshu,t.xianId as id_trd_mt_dksum from sys_tractor as t,sys_xian as a where t.combineProvinceMoney <>0 and t.xianId=a.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_dri_mt_as_100.xianId = ts.id_trd_mt_dksum) as table_trd_opr_dksum left join ");
		sb.append("(select count(t.xianId) com_400,t.xianId as id_com_400 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=400 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_trd_opr_dksum.xianId = ts.id_com_400) as table_com_400 left join ");
		sb.append(
				"(select count(t.combineXianMoney) xianbu_fenshu,t.xianId as id_com_500 from sys_tractor as t,sys_xian as a where t.combineXianMoney<>0 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_com_400.xianId = ts.id_com_500) as table_com_500 left join ");
		sb.append("(select count(t.xianId) com_600,t.xianId as id_com_600 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=600 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_com_500.xianId = ts.id_com_600) as table_com_600 left join ");
		sb.append("(select count(t.xianId) com_700,t.xianId as id_com_700 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=700 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_com_600.xianId = ts.id_com_700) as table_com_700 left join ");
		sb.append("(select count(t.xianId) com_800,t.xianId as id_com_800 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=800 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_com_700.xianId = ts.id_com_800) as table_com_800 left join ");
		sb.append("(select count(t.xianId) com_900,t.xianId as id_com_900 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=900 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_com_800.xianId = ts.id_com_900) as table_com_900 left join ");
		sb.append("(select count(t.xianId) com_950,t.xianId as id_com_950 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=950 and a.xianId=t.xianId and t.type=1  "
				+ condition + "group by t.xianId) as ts on table_com_900.xianId = ts.id_com_950) as table_com_950 left join ");
		sb.append(
				"(select count(t.xianId) com_1000,t.xianId as id_com_1000 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=1000 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_com_950.xianId = ts.id_com_1000) as table_com_1000 left join ");
		sb.append(
				"(select count(t.xianId) com_1200,t.xianId as id_com_1200 from sys_tractor as t,sys_xian as a where t.combineHuiFeiMoney=1200 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_com_1000.xianId = ts.id_com_1200)) as nj");

		final String sql = sb.toString();

		list = findByList(sql, SGJStastics.class);

		StringBuffer subSb = new StringBuffer();
		subSb.append(
				"select nj.xianId,nj.xianName,ifnull(nj.third_shengbu,0) as third_shengbu ,ifnull(nj.combine_jifen,0) as combine_jifen,ifnull(nj.combine_shengbu,0) as combine_shengbu,ifnull(nj.trd_300,0)+ifnull(nj.trd_400,0)+ifnull(nj.trd_500,0) as third_fenshu,ifnull(nj.trd_300,0) as third_300,ifnull(nj.trd_400,0) as third_400,ifnull(nj.trd_500,0) as third_500,ifnull(trd_combine_400,0) as third_combine_400,ifnull(trd_combine_500,0) as third_combine_500,ifnull(trd_combine_600,0) as third_combine_600,ifnull(trd_combine_700,0) as third_combine_700,ifnull(trd_combine_800,0) as third_combine_800,ifnull(trd_combine_900,0) as third_combine_900, ");
		subSb.append(
				"ifnull(nj.trd_combine_400,0)+ifnull(nj.trd_combine_500,0)+ifnull(nj.trd_combine_600,0)+ifnull(nj.trd_combine_700,0)+ifnull(nj.trd_combine_800,0)+ifnull(nj.trd_combine_900,0) as third_combine_fenshu,ifnull(nj.trd_combine_400,0)*400+ifnull(nj.trd_combine_500,0)*500+ifnull(nj.trd_combine_600,0)*600+ifnull(nj.trd_combine_700,0)*700+ifnull(nj.trd_combine_800,0)*800+ifnull(nj.trd_combine_900,0)*900 as third_combine_money from (select * from (select * from (select * from(select * from(select * from(select * from(select * from(select * from (select * from (select * from (select * from (select * from (select * from (select x.*,a.areaName from sys_xian as x,sys_area as a where x.areaId=a.areaId "
						+ xianCondition + ") as x_a ");
		subSb.append(
				"left join (select SUM(t.thirdProvinceMoney)as third_shengbu,t.xianId as id_trd_pro_sum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on x_a.xianId=ts.id_trd_pro_sum) as table_third_shengbu left join (select SUM(t.combineDikouMoney)as combine_jifen,t.xianId as id_com_dksum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_third_shengbu.xianId=ts.id_com_dksum) as table_third_c_jifen left join (select count(*)as totle_fenshu,t.xianId as id_totle_fenshu from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_third_c_jifen.xianId=ts.id_totle_fenshu) as table_totle_fenshu left join (select count(t.xianId) as trd_300,t.xianId as id_trd_300 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=300 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_totle_fenshu.xianId=ts.id_trd_300) as table_trd_300 left join (select count(t.xianId) as trd_400,t.xianId as id_trd_400 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=400 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_300.xianId=ts.id_trd_400) as table_trd_400 left join (select count(t.xianId) as trd_500,t.xianId as id_trd_500 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=500 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_400.xianId=ts.id_trd_500) as table_trd_500 left join (select count(t.xianId) as trd_combine_400,t.xianId as id_trd_combine_400 from sys_tractor as t,sys_xian as a where t.thridPeopleCombineHuiFeiMoney=400 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_500.xianId=ts.id_trd_combine_400) as table_trd_combine_400 left join (select count(t.xianId) as trd_combine_500,t.xianId as id_trd_combine_500 from sys_tractor as t,sys_xian as a where t.thridPeopleCombineHuiFeiMoney=500 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_combine_400.xianId=ts.id_trd_combine_500) as table_trd_combine_500 left join (select count(t.xianId) as trd_combine_600,t.xianId as id_trd_combine_600 from sys_tractor as t,sys_xian as a where t.thridPeopleCombineHuiFeiMoney=600 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_combine_500.xianId=ts.id_trd_combine_600) as table_trd_combine_600 left join (select count(t.xianId) as trd_combine_700,t.xianId as id_trd_combine_700 from sys_tractor as t,sys_xian as a where t.thridPeopleCombineHuiFeiMoney=700 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_combine_600.xianId=ts.id_trd_combine_700) as table_trd_combine_700 left join (select count(t.xianId) as trd_combine_800,t.xianId as id_trd_combine_800 from sys_tractor as t,sys_xian as a where t.thridPeopleCombineHuiFeiMoney=800 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");
		subSb.append(
				"on table_trd_combine_700.xianId=ts.id_trd_combine_800) as table_trd_combine_800 left join (select count(t.xianId) as trd_combine_900,t.xianId as id_trd_combine_900 from sys_tractor as t,sys_xian as a where t.thridPeopleCombineHuiFeiMoney=900 and a.xianId=t.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts ");

		subSb.append(
				"on table_trd_combine_800.xianId = ts.id_trd_combine_900) as table_trd_combine_900 left join(select SUM(t.combineProvinceMoney)as combine_shengbu,t.xianId as id_com_pro_sum from sys_tractor as t,sys_xian as a where t.xianId=a.xianId and t.type=1  "
						+ condition + "group by t.xianId) as ts on table_trd_combine_900.xianId = ts.id_com_pro_sum) as nj");
		final String subSql = subSb.toString();

		sublist = findByList(subSql, SGJStastics.class);

		int rowspan = 0;

		SGJStastics recordTemp = list.get(0);
		String areaName = recordTemp.getDiqu();
		int startRow = 0;
		for (int i = 0; i < list.size(); i++)
		{
			SGJStastics entity = list.get(i);
			SGJStastics subEntity = sublist.get(i);
			entity.setThird_300(subEntity.getThird_300());
			entity.setThird_400(subEntity.getThird_400());
			entity.setThird_500(subEntity.getThird_500());
			entity.setCombine_shengbu(subEntity.getCombine_shengbu());
			entity.setThird_combine_400(subEntity.getThird_combine_400());
			entity.setThird_combine_500(subEntity.getThird_combine_500());
			entity.setThird_combine_600(subEntity.getThird_combine_600());
			entity.setThird_combine_700(subEntity.getThird_combine_700());
			entity.setThird_combine_800(subEntity.getThird_combine_800());
			entity.setThird_combine_900(subEntity.getThird_combine_900());
			entity.setThird_combine_fenshu(subEntity.getThird_combine_fenshu());
			entity.setThird_combine_money(subEntity.getThird_combine_money());
			Double little_combine_money = Double.valueOf(getDoubleNum(entity.getLittle_combine_money()) - getDoubleNum(entity.getCombine_shengbu()));
			entity.setLittle_combine_money(String.valueOf(little_combine_money));
			Double third_money = Double.valueOf(getDoubleNum(entity.getThird_money()) + 300.0D * getDoubleNum(entity.getThird_300()) + 400.0D * getDoubleNum(entity.getThird_400())
					+ 500.0D * getDoubleNum(entity.getThird_500()));

			entity.setThird_money(String.valueOf(third_money));
			Integer totle_fenshu = Integer
					.valueOf(Integer.parseInt(entity.getTotle_fenshu()) + Integer.parseInt(subEntity.getThird_fenshu()) + Integer.parseInt(subEntity.getThird_combine_fenshu()));
			entity.setTotle_fenshu(String.valueOf(totle_fenshu));

			Double totle_money = Double.valueOf(getDoubleNum(entity.getJishen_money()) + getDoubleNum(entity.getThird_money()) + getDoubleNum(entity.getLittle_driver_money())
					+ getDoubleNum(entity.getLittle_combine_money()) + getDoubleNum(entity.getThird_combine_money()) - getDoubleNum(entity.getTotle_jifen()));
			entity.setTotle_money(String.valueOf(totle_money));

			entity.setRowspan(Integer.valueOf(0));
			if (entity.getDiqu().equalsIgnoreCase(areaName))
			{
				rowspan++;
			}
			else
			{
				results.get(startRow).setRowspan(Integer.valueOf(rowspan));
				startRow = i;
				areaName = entity.getDiqu();
				rowspan = 1;
			}
			results.add(entity);
		}
		results.get(startRow).setRowspan(Integer.valueOf(rowspan));

		SGJStastics totalEntity = (SGJStastics) sumEntities(results, SGJStastics.class.getName());
		if (totalEntity != null)
		{
			totalEntity.setDiqu("所有地区");
			totalEntity.setXian("合计");
			totalEntity.setRowspan(Integer.valueOf(1));
			results.add(totalEntity);
		}

		return results;
	}

	public List<TractorStastics> getTractorStastics(String year, String beginDate, String endDate, int regionsId) throws Exception
	{
		String condition = " and t.formStatus <> 3 ";
		String xianCondition = "";
		if ((year != null) && (!year.equals("")) && (!year.equalsIgnoreCase("-1")))
		{
			condition = condition + "and t.qiandanYear ='" + year + "'";
		}
		condition = condition + getTimeCondition(beginDate, endDate);

		if (regionsId != 0)
		{
			xianCondition = xianCondition + " and x.xianId ='" + regionsId + "' ";
		}
		List<TractorStastics> list = new ArrayList<TractorStastics>();
		List<TractorStastics> results = new ArrayList<TractorStastics>();
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select tlj.xian,tlj.diqu,tlj.jishen_100,tlj.jishen_150,tlj.jishen_200,tlj.jishen_250,tlj.jishen_300,tlj.jishen_350,tlj.jishen_400,tlj.jishen_500,tlj.jishen_fenshu,tlj.jishen_money,");
		sb.append(
				"tlj.driver_operate_100,tlj.driver_operate_200,tlj.driver_operate_fenshu,tlj.driver_operate_money,tlj.driver_mainten_50,tlj.driver_mainten_100,tlj.third_driver_xianbu_fenshu,tlj.third_driver_xianbu_money,tlj.third_driver_350*350-tlj.third_driver_xianbu_money-tlj.third_driver_shengbu_money as little_third_driver_money,tlj.driver_mainten_fenshu,tlj.driver_mainten_money,");
		sb.append(
				"tlj.driver_operate_fenshu+tlj.driver_mainten_fenshu as little_driver_fenshu,tlj.driver_operate_money+tlj.driver_mainten_money as little_driver_money,tlj.trd_100,tlj.trd_200,tlj.trd_300,tlj.trd_400,");
		sb.append(
				"tlj.totle_jifen_fenshu,tlj.totle_jifen_money,tlj.trd_fenshu,tlj.trd_money,tlj.totle_fenshu,tlj.jishen_money+tlj.driver_operate_money+tlj.driver_mainten_money+tlj.trd_money-tlj.third_driver_350*350-tlj.third_driver_shengbu_money-tlj.third_driver_xianbu_money-tlj.totle_jifen_money as totle_money,tlj.third_driver_350,tlj.third_driver_shengbu_fenshu,tlj.third_driver_shengbu_money,tlj.third_driver_xianbu_fenshu,tlj.third_driver_xianbu_money from( ");
		sb.append(
				"(select xianName as xian,areaName as diqu,ifnull(js_100,0) as jishen_100,ifnull(js_150,0) as jishen_150,ifnull(js_200,0) as jishen_200,ifnull(js_250,0) as jishen_250,ifnull(js_300,0) as jishen_300,ifnull(js_350,0) as jishen_350,ifnull(js_400,0) as jishen_400,ifnull(js_500,0) as jishen_500,");
		sb.append(
				"ifnull(trd_driver_350,0) as third_driver_350,ifnull(js_100,0)+ifnull(js_150,0)+ifnull(js_200,0)+ifnull(js_250,0)+ifnull(js_300,0)+ifnull(js_350,0)+ifnull(js_400,0)+ifnull(js_500,0) as jishen_fenshu,ifnull(js_100,0)*100+ifnull(js_150,0)*150+ifnull(js_200,0)*200+ifnull(js_250,0)*250+ifnull(js_300,0)*300+ifnull(js_350,0)*350+ifnull(js_400,0)*400+ifnull(js_500,0)*500 as jishen_money,");
		sb.append(
				"ifnull(dri_per_100,0) as driver_operate_100,ifnull(dri_per_200,0) as driver_operate_200,ifnull(trd_driver_xianbu_fenshu,0)as third_driver_xianbu_fenshu, ifnull(dri_per_100,0)+ifnull(dri_per_200,0) as driver_operate_fenshu,ifnull(dri_per_100,0)*100+ifnull(dri_per_200,0)*200 as driver_operate_money,");
		sb.append(
				"ifnull(dri_main_50,0) as driver_mainten_50,ifnull(dri_main_100,0) as driver_mainten_100,ifnull(trd_driver_xianbu_money,0) as third_driver_xianbu_money,ifnull(dri_main_50,0)+ifnull(dri_main_100,0) as driver_mainten_fenshu,ifnull(dri_main_50,0)*50+ifnull(dri_main_100,0)*100 as driver_mainten_money, ");
		sb.append(
				"ifnull(trd_100,0) as trd_100,ifnull(trd_200,0) as trd_200,ifnull(trd_300,0) as trd_300,ifnull(trd_400,0) as trd_400,ifnull(trd_driver_shengbu_fenshu,0) as third_driver_shengbu_fenshu,ifnull(trd_driver_shengbu_money,0) as third_driver_shengbu_money,ifnull(trd_100,0)+ifnull(trd_200,0)+ifnull(trd_300,0)+ifnull(trd_400,0) as trd_fenshu,ifnull(totle_fenshu,0) as totle_fenshu,ifnull(trd_100,0)*100+ifnull(trd_200,0)*200+ifnull(trd_300,0)*300+ifnull(trd_400,0)*400 as trd_money,ifnull(totle_jifen_fenshu,0) as totle_jifen_fenshu,ifnull(totle_jifen_money,0) as totle_jifen_money ");
		sb.append(
				"from (select * from (select * from (select * from(select * from (select * from (select * from(select* from (select * from(select * from (select * from (select * from(select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from (select * from(select * from (select x.*,a.areaName from sys_xian as x,sys_area as a where x.areaId=a.areaId "
						+ xianCondition + ") as x_a ");
		sb.append("left join (select count(*) as js_100,t.xianId as id_100 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=100 and a.xianId=t.xianId and t.type=0 "
				+ condition + "group by t.jishenHuiFeiMoney,t.xianId) as ts on x_a.xianId=ts.id_100) as table_100 left join ");
		sb.append("(select count(*) as js_150,t.xianId as id_150 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=150 and a.xianId=t.xianId and t.type=0 " + condition
				+ "group by t.xianId) as ts on table_100.xianId =ts.id_150) as table_150 left join ");
		sb.append("(select count(*) as js_200,t.xianId as id_200 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=200 and a.xianId=t.xianId and t.type=0 " + condition
				+ "group by t.xianId) as ts on table_150.xianId = ts.id_200) as table_200 left join ");
		sb.append("(select count(*) as js_300,t.xianId as id_300 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=300 and a.xianId=t.xianId and t.type=0  " + condition
				+ "group by t.xianId) as ts on table_200.xianId= ts.id_300) as table_300 left join ");
		sb.append("(select count(*) as js_350,t.xianId as id_350 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=350 and a.xianId=t.xianId and t.type=0  " + condition
				+ "group by t.xianId) as ts on table_300.xianId = ts.id_350) as table_350 left join ");
		sb.append("(select count(*) as js_400,t.xianId as id_400 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=400 and a.xianId=t.xianId and t.type=0  " + condition
				+ "group by t.xianId) as ts on table_350.xianId = ts.id_400) as table_400 left join ");
		sb.append(
				"(select count(*) as trd_driver_350,t.xianId as id_js_dksum from sys_tractor as t,sys_xian as a where t._350CombineHuifei=350 and t.xianId=a.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_400.xianId = ts.id_js_dksum) as table_js_dksum left join ");
		sb.append(
				"(select count(*) as dri_per_100,t.xianId as id_per_100 from sys_tractor as t,sys_xian as a where t.driverOperateHuiFeiMoney=100 and a.xianId=t.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_js_dksum.xianId = ts.id_per_100)as table_dri_per_100 left join ");
		sb.append(
				"(select count(*) as dri_per_200,t.xianId as id_per_200 from sys_tractor as t,sys_xian as a where t.driverOperateHuiFeiMoney=200 and a.xianId=t.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_dri_per_100.xianId = ts.id_per_200) as table_dri_per_200 left join ");
		sb.append(
				"(select count(*)as trd_driver_xianbu_fenshu,t.xianId as id_dri_per_dksum from sys_tractor as t,sys_xian as a where t._350CombineHuifei=350 and t.xianId=a.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_dri_per_200.xianId = ts.id_dri_per_dksum) as table_dri_per_dksum left join ");
		sb.append(
				"(select count(*) as dri_main_50,t.xianId as id_dri_main_50 from sys_tractor as t,sys_xian as a where t.driverMaintenHuiFeiMoney=50 and a.xianId=t.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_dri_per_dksum.xianId = ts.id_dri_main_50) as table_dri_main_50 left join ");
		sb.append(
				"(select count(*) as dri_main_100,t.xianId as id_dri_main_100 from sys_tractor as t,sys_xian as a where t.driverMaintenHuiFeiMoney=100 and a.xianId=t.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_dri_main_50.xianId = ts.id_dri_main_100) as table_dri_main_100 left join ");
		sb.append(
				"(select SUM(t._350CombineXianMoney)as trd_driver_xianbu_money,t.xianId as id_dri_main_dksum from sys_tractor as t,sys_xian as a where t._350CombineHuifei=350 and t.xianId=a.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_dri_main_100.xianId = ts.id_dri_main_dksum) as table_dri_main_dksum left join ");
		sb.append("(select count(*) as trd_100,t.xianId as id_trd_100 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=100 and a.xianId=t.xianId and t.type=0  "
				+ condition + "group by t.xianId) as ts on table_dri_main_dksum.xianId=ts.id_trd_100) as table_trd_100 left join ");
		sb.append("(select count(*) as trd_200,t.xianId as id_trd_200 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=200 and a.xianId=t.xianId and t.type=0  "
				+ condition + "group by t.xianId) as ts on table_trd_100.xianId=ts.id_trd_200) as table_trd_200 left join ");
		sb.append("(select count(*) as trd_300,t.xianId as id_trd_300 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=300 and a.xianId=t.xianId and t.type=0  "
				+ condition + "group by t.xianId) as ts on table_trd_200.xianId=ts.id_trd_300) as table_trd_300 left join ");
		sb.append("(select count(*) as trd_400,t.xianId as id_trd_400 from sys_tractor as t,sys_xian as a where t.thridHuiFeiMoney=400 and a.xianId=t.xianId and t.type=0  "
				+ condition + "group by t.xianId) as ts on table_trd_300.xianId = ts.id_trd_400) as table_trd_400 left join ");
		sb.append(
				"(select count(*)as trd_driver_shengbu_fenshu,t.xianId as id_trd_dksum from sys_tractor as t,sys_xian as a where t._350CombineHuifei=350 and t.xianId=a.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_trd_400.xianId=ts.id_trd_dksum ) as table_trd_dksum left join ");
		sb.append("(select count(*) as js_250,t.xianId as id_250 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=250 and a.xianId=t.xianId and t.type=0 " + condition
				+ "group by t.xianId) as ts on table_trd_dksum.xianId =ts.id_250) as table_250 left join ");
		sb.append("(select count(*) as js_500,t.xianId as id_500 from sys_tractor as t,sys_xian as a where t.jishenHuiFeiMoney=500 and a.xianId=t.xianId and t.type=0 " + condition
				+ "group by t.xianId) as ts on table_250.xianId = ts.id_500) as table_500 left join ");
		sb.append("(select count(*) as totle_fenshu,t.xianId as id_totle_fenshu from sys_tractor as t,sys_xian as a where a.xianId=t.xianId and t.type=0 " + condition
				+ "group by t.xianId) as ts on table_500.xianId = ts.id_totle_fenshu) as table_totle_fenshu left join ");
		sb.append(
				"(select count(t.totalDikouMoney) as totle_jifen_fenshu,t.xianId as id_totle_jifen_fenshu from sys_tractor as t,sys_xian as a where a.xianId=t.xianId and t.type=0 "
						+ condition + "group by t.xianId) as ts on table_totle_fenshu.xianId = ts.id_totle_jifen_fenshu) as table_totle_jifen_fenshu left join ");
		sb.append("(select SUM(t.totalDikouMoney) as totle_jifen_money,t.xianId as id_totle_jifen_money from sys_tractor as t,sys_xian as a where a.xianId=t.xianId and t.type=0 "
				+ condition + "group by t.xianId) as ts on table_totle_jifen_fenshu.xianId = ts.id_totle_jifen_money) as table_totle_jifen_money left join ");
		sb.append(
				"(select SUM(t._350CombineProvinceMoney)as trd_driver_shengbu_money,t.xianId as id_trd_shengbu from sys_tractor as t,sys_xian as a where t._350CombineHuifei=350 and t.xianId=a.xianId and t.type=0  "
						+ condition + "group by t.xianId) as ts on table_totle_jifen_money.xianId=ts.id_trd_shengbu)) as tlj");

		final String sql = sb.toString();

		list = findByList(sql, TractorStastics.class);

		int rowspan = 0;
		TractorStastics recordTemp = list.get(0);
		String areaName = recordTemp.getDiqu();
		int startRow = 0;

		for (int i = 0; i < list.size(); i++)
		{
			TractorStastics entity = list.get(i);
			Double totle_money = getDoubleNum(entity.getJishen_money()) + getDoubleNum(entity.getLittle_driver_money()) + getDoubleNum(entity.getTrd_money())
					+ getDoubleNum(entity.getLittle_third_driver_money()) - getDoubleNum(entity.getTotle_jifen_money());
			entity.setTotle_money(String.valueOf(totle_money));
			entity.setRowspan(Integer.valueOf(0));
			if (entity.getDiqu().equalsIgnoreCase(areaName))
			{
				rowspan++;
			}
			else
			{
				results.get(startRow).setRowspan(Integer.valueOf(rowspan));
				startRow = i;
				areaName = entity.getDiqu();
				rowspan = 1;
			}
			results.add(entity);
		}
		results.get(startRow).setRowspan(Integer.valueOf(rowspan));
		TractorStastics totalEntity = (TractorStastics) sumEntities(results, TractorStastics.class.getName());
		if (totalEntity != null)
		{
			totalEntity.setDiqu("所有地区");
			totalEntity.setXian("合计");
			totalEntity.setRowspan(Integer.valueOf(1));
			results.add(totalEntity);
		}

		return results;
	}

	private String getTimeCondition(String beginDate, String endDate)
	{
		String condition = "";
		if ((beginDate != null) && (!beginDate.equals("")))
		{
			if ((endDate == null) || (endDate.equals("")))
			{
				Date date = new Date();
				endDate = dateToString(date);
			}
			condition = condition + " and ( t.insuranceStart>= '" + beginDate + " 00:00:00' and t.insuranceStart<= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart2>= '" + beginDate + " 00:00:00' and t.insuranceStart2<= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart3>= '" + beginDate + " 00:00:00' and t.insuranceStart3<= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceEnd>= '" + beginDate + " 00:00:00' and t.insuranceEnd<= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceEnd2>= '" + beginDate + " 00:00:00' and t.insuranceEnd2<= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceEnd3>= '" + beginDate + " 00:00:00' and t.insuranceEnd3<= " + "'" + endDate + " 23:59:59' ";
			condition = condition + " or t.insuranceStart<= '" + beginDate + " 00:00:00' and t.insuranceEnd>= " + "'" + beginDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart<= '" + endDate + " 00:00:00' and t.insuranceEnd>= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart2<= '" + beginDate + " 00:00:00' and t.insuranceEnd2>= " + "'" + beginDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart2<= '" + endDate + " 00:00:00' and t.insuranceEnd2>= " + "'" + endDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart3<= '" + beginDate + " 00:00:00' and t.insuranceEnd3>= " + "'" + beginDate + " 23:59:59'";
			condition = condition + " or t.insuranceStart3= '" + endDate + " 00:00:00' and t.insuranceEnd3>= " + "'" + endDate + " 23:59:59' )";
		}
		return condition;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object sumEntities(List entities, String classType)
	{
		try
		{
			Class entityClass = Class.forName(classType);
			Object obj = entityClass.newInstance();

			for (Method method : entityClass.getMethods())
			{
				Object returnValue = null;
				Double total = Double.valueOf(0.0D);
				String fieldName = "";
				if ((method.getName().startsWith("get")) && (method.getReturnType().equals(String.class)))
				{
					fieldName = method.getName().substring(3);
					for (int i = 0; i < entities.size(); i++)
					{
						Object data = entities.get(i);
						returnValue = method.invoke(data, null);
						if (returnValue != null)
						{
							Double getDouble = parseStringToDouble(returnValue.toString());
							if ((!method.getReturnType().equals(String.class)) || (getDouble == null))
								continue;
							total = Double.valueOf(getDouble.doubleValue() + total.doubleValue());
						}
					}
				}

				if ((fieldName.length() > 0) && (!fieldName.equals("Class")) && (returnValue != null))
				{
					Method methodSet = entityClass.getMethod("set" + fieldName, new Class[] { returnValue.getClass() });
					methodSet.invoke(obj, new Object[] { total.toString() });
				}
			}
			return obj;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String dateToString(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null)
			return "";
		return sdf.format(date);
	}

	private Double parseStringToDouble(String num)
	{
		Double d = null;
		try
		{
			d = Double.valueOf(Double.parseDouble(num));
		}
		catch (Exception localException)
		{
		}
		return d;
	}

	private double getDoubleNum(String number)
	{
		if (StringUtils.isBlank(number))
		{
			return 0d;
		}
		else
		{
			return Double.parseDouble(number);
		}
	}
}