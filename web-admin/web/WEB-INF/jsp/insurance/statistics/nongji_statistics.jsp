<%@include file="/header.jsp"%>

<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>农业机械保单统计</title>
    <%@include file="/common.jsp" %>

    <link rel="stylesheet" type="text/css" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css">
    <script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>

    <link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/public.css"/>
    <link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/displaytag.css"/>
    <link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/alternative.css"/>

    <!-- Forp通用css和js补丁 -->
    <link rel="stylesheet" href="${ROOT}/css/common.css"/>
    <script src="${ROOT}/js/common.js"></script>
    <script src="${ROOT}/js/business.js"></script>
</head>
<body style='overflow: auto !important;'>
	<table width="100%" border="1" cellpadding="0" cellspacing="0">
		<form id="nongjiStastics" name="nongjiStastics" onsubmit="return true;" action="${ROOT}/stastics/nongji" method="post">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top">
						<table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>
									<table width="100%">
										<tr>
											<td width="33%">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td width="30%" height="30" align="right">互助单年份：&nbsp;</td>
														<td width="70%" height="30"><select name='year' id="nongjiStastics_year" class='form-control input-sm' style='width: 110px;'></select></td>
													</tr>
												</table>
											</td>
											<td width="33%">
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<tr>
														<td width="30%" height="30" align="right">&nbsp;互助单起始日期：&nbsp;</td>
														<td width="70%" height="30"><input id="beginDate" name="beginDate" type="text" class="form-control input-sm" onClick="WdatePicker()" style="width: 110px;"></td>
													</tr>
												</table>
											</td>
											<td width="33%">
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<tr>
														<td width="30%" height="30" align="right">&nbsp;互助单终止日期：&nbsp;</td>
														<td width="70%" height="30"><input id="endDate" name="endDate" type="text" class="form-control input-sm" onClick="WdatePicker()" style="width: 110px;"></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan=4 height="30" align="center"><input type="submit" id="formSerachBtn" name="searchBtn" value="提交"/></td>
				</tr>
			</table>
		</form>
		
		<tr>
			<td>
				<table class="ReportTable"  border="1">
					<tr class="TableHeader">
						<th rowspan="3" width="80">地区</th>
						<th rowspan="3" width="210">县</th>
						<th colspan="16">机身损失互助</th>
						<th rowspan="2" colspan="5">驾驶人伤害互助</th>
						<th rowspan="2" colspan="3">总计</th>
					</tr>
					<tr class="TableHeader">
						<th colspan="5">一类二类机具</th>
						<th colspan="8">三类四类机具</th>
						<th colspan="3">小计</th>
					</tr>
					<tr class="TableHeader">
						<td>50</td>
						<td>100</td>
						<td>积分抵扣</td>
						<td>份数</td>
						<td>金额</td>

						<td>100</td>
						<td>200</td>
						<td>500</td>
						<td>600</td>
						<td>700</td>
						<td>积分抵扣</td>
						<td>份数</td>
						<td>金额</td>

						<td>积分抵扣</td>
						<td>份数</td>
						<td>金额</td>

						<td>50</td>
						<td>100</td>
						<td>积分抵扣</td>
						<td>份数</td>
						<td>金额</td>

						<td>积分抵扣</td>
						<td>份数</td>
						<td>金额</td>
					</tr>
					<c:forEach items="${list}" var="nongji_stastics_entity">
						<tr>
							<c:if test="${nongji_stastics_entity.colspan != 0}">
								<td rowspan="${nongji_stastics_entity.colspan}">${nongji_stastics_entity.diqu}</td>
							</c:if>
							<td>${nongji_stastics_entity.xian}</td>
							<td>${nongji_stastics_entity.fst_50}</td>
							<td>${nongji_stastics_entity.fst_100}</td>
							<td>${nongji_stastics_entity.fst_jifen}</td>
							<td>${nongji_stastics_entity.fst_fenshu}</td>
							<td>${nongji_stastics_entity.fst_money}</td>
							<td>${nongji_stastics_entity.trd_100}</td>
							<td>${nongji_stastics_entity.trd_200}</td>
							<td>${nongji_stastics_entity.trd_500}</td>
							<td>${nongji_stastics_entity.trd_600}</td>
							<td>${nongji_stastics_entity.trd_700}</td>
							<td>${nongji_stastics_entity.trd_jifen}</td>
							<td>${nongji_stastics_entity.trd_fenshu}</td>
							<td>${nongji_stastics_entity.trd_money}</td>
							<td>${nongji_stastics_entity.little_jifen}</td>
							<td>${nongji_stastics_entity.little_fenshu}</td>
							<td>${nongji_stastics_entity.little_money}</td>
							<td>${nongji_stastics_entity.driver_50}</td>
							<td>${nongji_stastics_entity.driver_100}</td>
							<td>${nongji_stastics_entity.driver_jifen}</td>
							<td>${nongji_stastics_entity.driver_fenshu}</td>
							<td>${nongji_stastics_entity.driver_money}</td>
							<td>${nongji_stastics_entity.totle_jifen}</td>
							<td>${nongji_stastics_entity.totle_fenshu}</td>
							<td>${nongji_stastics_entity.totle_money}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
</body>
<script>
/**
 * jQuery初始化
 */
var yearStr = '${year}';
var beginDateStr = '${beginDate}';
var endDateStr = '${endDate}';
$(function()
{
	var years = getQueryYearArray(10);
	$('select[name="year"]').html(getSelectOptions(years, 0));
	if (yearStr != null && yearStr != '') {
		$('select[name="year"]').val(yearStr);
	}
	if (beginDateStr != '') {
		$("#beginDate").attr("value", beginDateStr);
	}
	if (endDateStr != '') {
		$("#endDate").attr("value", endDateStr);
	}
})

function getQueryYearArray(years)
{
	var ya = [], count = 0, y = 0;
	var thisYear = (new Date).getFullYear();
	ya.push([-1, '所有年份']);
	while (count < years)
	{
		y = thisYear - count;
		ya.push([y, y + ' 年']);

		count++;
	}
	return ya;
}
</script>
<style type="text/css">
    .ReportTable {
        border: 1px solid #999999;
        font-family: verdana, arial, sans-serif;
        font-size: 11px;
        border-collapse: collapse;
        width: 3000px;
        overflow: scroll;
    }

    .ReportTable tr {
        background-color: #ffffff;
        font-family: verdana, arial, sans-serif;
        font-size: 11px;
        font-weight: normal;
    }

    .ReportTable tr.TableHeader {
        background-color: #045FB4;
        font-family: verdana, arial, sans-serif;
        height: 20px;
        color: #F0F0F0;
        font-size: 11px;
        font-weight: bold
    }

    .ReportTable tr.DeptHeader {
        background-color: #DDDDDD;
        font-family: verdana, arial, sans-serif;
        height: 20px;
        font-size: 11px;
        font-weight: bold
    }

    .ReportTable tr.DeptTotal {
        background-color: #cccccc;
        font-family: verdana, arial, sans-serif;
        height: 20px;
        font-size: 11px;
        font-weight: bold
    }

    .ReportTable tr.StoreTotal {
        background-color: #571b7e;
        font-family: verdana, arial, sans-serif;
        height: 25px;
        color: white;
        font-size: 11px;
        font-weight: bold
    }

    .ReportTable tr {
        background-color: #ffffff;
        font-family: verdana, arial, sans-serif;
        font-size: 11px;
        font-weight: normal
    }

    .ReportTable td {
        border: #aaaaaa 1px solid;
        padding: 4px;
        table-layout: fixed;
        word-break: break-all;
    }
</style>
</html>
