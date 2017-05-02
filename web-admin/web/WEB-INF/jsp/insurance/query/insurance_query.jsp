<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>

	<link  rel="stylesheet" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css" >
	<script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/public.css" />
	
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<body>
<div id="table-toolbar">
	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">
	        <table>
		        <tr>
		            <td width="5%" class="left_txt3">编号</td>
		            <td width="20%" class="left_txt3"><input type="text" id="querryFormid" name="querryFormid" class="form-control input-sm"></td>
		            <td width="8%" class="left_txt3">机主姓名</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormOwner" name="querryFormOwner" class="form-control input-sm" maxlength="12"></td>
		            <td width="8%" class="left_txt3">机主地址</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormAddress" name="querryFormAddress" class="form-control input-sm" maxlength="12"></td>
		            <td width="8%" class="left_txt3">机主电话</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormOwnerTel" name="querryFormOwnerTel" class="form-control input-sm" maxlength="12"></td>
		        </tr>
		        <tr>
		            <td width="8%" class="left_txt3">号码牌号</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormCarNum" name="querryFormCarNum" class="form-control input-sm"></td>
		            <td width="8%" class="left_txt3">厂牌型号</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormFactoryNum" name="querryFormFactoryNum" class="form-control input-sm" maxlength="12"></td>
		            <td width="8%" class="left_txt3">发动机号</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormEigineNum" name="querryFormEigineNum" class="form-control input-sm" maxlength="12"></td>
		            <td width="8%" class="left_txt3">机架号</td>
		            <td width="12%" class="left_txt3"><input type="text" id="querryFormJiJiaNum" name="querryFormJiJiaNum" class="form-control input-sm" maxlength="12"></td>
		        </tr>
		        <tr>
		            <td width="5%" class="left_txt3">机型</td>
		            <td width="95%" colspan="7" class="left_txt3"><select id="carType" name="carType" class="form-control input-sm"><option value="0" selected>拖拉机</option><option value="1">联合收割机</option><option value="2">农业机械</option></select></td>
		        </tr>
		        <tr>
		            <td width="8%" class="left_txt3">互助项目</td>
		            <td width="92%" colspan="7" class="left_txt3">
		            	<input type="checkbox" id="projectTypeList1" name="projectTypeList" value="5"><label for="projectTypeList1" class="checkboxLabel">所有互助项目</label>&nbsp;&nbsp;&nbsp;
		            	<input type="checkbox" id="projectTypeList2" name="projectTypeList" value="0"><label for="projectTypeList2" class="checkboxLabel">机身损失互助</label>&nbsp;&nbsp;&nbsp;
		            	<input type="checkbox" id="projectTypeList3" name="projectTypeList" value="1"><label for="projectTypeList3" class="checkboxLabel">驾驶人意外伤害互助</label>&nbsp;&nbsp;&nbsp;
		            	<input type="checkbox" id="projectTypeList4" name="projectTypeList" value="2"><label for="projectTypeList4" class="checkboxLabel">第三者损害责任互助</label>&nbsp;&nbsp;&nbsp;
		            	<input type="checkbox" id="projectTypeList5" name="projectTypeList" value="3"><label for="projectTypeList5" class="checkboxLabel">第三者损害及机上人员伤害组合互助</label>&nbsp;&nbsp;&nbsp;
		            	<input type="checkbox" id="projectTypeList6" name="projectTypeList" value="4"><label for="projectTypeList6" class="checkboxLabel">驾驶人、机身、第三者伤害责任组合互助</label>&nbsp;&nbsp;&nbsp;
		            	<input type="checkbox" id="projectTypeList7" name="projectTypeList" value="6"><label for="projectTypeList7" class="checkboxLabel">驾驶人及第三者组合互助</label>
		            </td>
		        </tr>
		        <tr>
		            <td width="8%" class="left_txt3">保单类型</td>
		            <td width="17%" class="left_txt3">
		            	<select id="formType" name="formType" class="form-control input-sm">
		            		<option value="-1" selected>所有保单</option>
		            		<option value="0">新建保单</option>
		            		<option value="1">审查通过</option>
		            		<option value="2">需要修改</option>
		            		<option value="3">作废保单</option>
		            		<option value="4">修改成功</option>
		            		<option value="5">收费完成待打印</option>
		            		<option value="6">打印完成</option>
		            	</select>
		            </td>
		            <td width="5%" class="left_txt3">收费状态</td>
		            <td width="95%" colspan="3" class="left_txt3">
		            	<input type="radio" name="chargingStatus" id="chargingStatusA" value="A" checked><label for="chargingStatusA">所有</label>&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="chargingStatus" id="chargingStatusN" value="N"><label for="chargingStatusN">未收费</label>&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="chargingStatus" id="chargingStatusY" value="Y"><label for="chargingStatusY">已收费</label>
		            </td>
		            <td width="8%" class="left_txt3">保单年份</td>
		            <td width="13%" class="left_txt3"><select name='formYear' id="formYear" class='form-control input-sm'></select></td>
		        </tr>
		        <tr>
		            <td width="8%" class="left_txt3">会员所属区县</td>
		            <td width="20%" colspan="3" class="left_txt3 nobr">
		            	<select name='diquSelected' class='form-control input-sm' onchange='refreshRegion();' style='width: 30%; float: left;'></select>
		            	<select name='xianSelected' class='form-control input-sm' style='width: 30%; float: left; margin-left: 8px;'></select>
		            </td>
		            <td width="8%" class="left_txt3">收费方式</td>
		            <td width="13%" class="left_txt3">
		            	<select id="chargingType" name="chargingType" class="form-control input-sm">
		            		<option value="-1" selected>请选择</option>
		            		<option value="A">现金收费</option>
		            		<option value="B">刷卡收费</option>
		            		<option value="C">微信收费</option>
		            		<option value="D">支付宝收费</option>
		            		<option value="E">银联收费</option>
		            	</select>
		            </td>
		        </tr>
		        <!-- 
		        <tr>
		            <td width="8%" class="left_txt3">排序方式</td>
		            <td width="92%" colspan="7" class="left_txt3">
		            	<input type="radio" name="orderType" id="orderType0" value="0" checked><label for="orderType0">签单日期</label>&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="orderType" id="orderType1" value="1"><label for="orderType1">编号</label>&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="orderType" id="orderType2" value="2"><label for="orderType2">机主地址</label>&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="orderType" id="orderType3" value="3"><label for="orderType3">厂牌型号</label>&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="orderType" id="orderType4" value="4"><label for="orderType4">签单人</label>
	                </td>
		        </tr>
		        -->
		        <tr>
		            <td colspan=8 height="30" align="center"><button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></td>
		        </tr>
	        </table>
		</div>
	</div>
</div>

<table id='tableInsurance' data-toggle="table" data-url="${ROOT}/insurance/insurance-query" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="qiandanYear" data-sort-order="desc">
	<thead>
		<tr>
			<th data-field="insuranceTractorId" data-width='120' data-halign='left' data-align='left'>编号</th>
			<th data-field="owner" data-width='120'>名称</th>
			<th data-field="address" data-width='120' data-halign='left' data-align='left'>地址</th>
			<th data-field="telNum" data-width='120' data-halign='left' data-align='left'>电话</th>
			<th data-field="carNum" data-width='120' data-halign='left' data-align='left'>牌号</th>
			<th data-field="factoryNum" data-width='120' data-halign='left' data-align='left'>机型</th>
			<th data-field="xianName" data-width='120' data-sortable="false">签单县</th>
			<th data-field="huBaoZuHeZongBaoFei" data-width='120'>互保组合<br>总保费</th>
			
			<th data-field="jiaShiRenYiWaiShangHaiBaoFei" data-width='120'>驾驶人<br>意外伤害</th>
			<th data-field="diSanZheZeRenBaoFei" data-width='120'>第三者<br>责任</th>
			<th data-field="boLiDanDuPoSuiBaoFei" data-width='120'>玻璃<br>单独破碎</th>
			
			<th data-field="jiShenSunShiBaoFei" data-width='120'>机身<br>损失</th>
			<th data-field="nongJiHuoTuoCheBaoFei" data-width='120'>农具或<br>拖车</th>
			<th data-field="jiJuTuoLuoSunShiBaoFei" data-width='120'>机具<br>脱落损失</th>

			<th data-field="duZhuZuoYeRenYuanShangHaiBaoFei" data-width='120'>辅助作业人员<br>意外伤害</th>
			<th data-field="ziRanSunShiBaoFei" data-width='120'>自燃<br>损失</th>
			<th data-field="zhuangYunSunShiBaoFei" data-width='120'>装运<br>损失</th>
			<th data-field="feiShiGuBuJianSunShiBaoFei" data-width='120'>非事故<br>部件损失</th>
			
			<th data-field="yunZhuanYiWaiShangHaiBaoFei" data-width='120'>运转意<br>外伤害</th>
			<th data-field="weiXiuBaoYangZuoYeBaoFei" data-width='120'>维修保<br>养作业</th>
			<th data-field="buJiMianBuLvBaoFei" data-width='120'>不计免<br>补率</th>
			<th data-field="qiandanYear" data-width='120'>保单年份</th>
			<th data-field="qiandanPerson" data-width='120'>签单人</th>
			
			<th data-field="insuranceStart" data-width='120' data-formatter="dateFormatter">互助起止<br>日期</th>
			<th data-field="formStatus" data-width='120' data-formatter="statusFormatter">保单状态</th>
			<th data-field="insuranceTractorId" data-width='120' data-formatter="operatorFormatter" data-sortable="false">&nbsp;&nbsp;操&nbsp;作&nbsp;&nbsp;</th>
<!-- 			<th data-field="formStatus" data-width='120' data-formatter="compensationFormatter">补偿单操作</th> -->
		</tr>
	</thead>
</table>
</body>

<script>
var tableInsurance = null;
var regions = null;
var provinces = [];
var years = [];

/**
 * jQuery初始化
 */
$(function()
{
	tableInsurance = $('#tableInsurance');
	showOrHideCoulumn();
	
	$.post
	(
		'${ROOT}/insurance/get-region', function(rsp, textStatus, jqXHR)
		{
			regions = rsp.regions;
			// 省市
				
			$.each(rsp.provinces, function(index, r)
			{
				provinces.push([r.id, r.name]);
			});
			$('select[name="diquSelected"]').html(getSelectOptions(provinces, 0));
			$('select[name="diquSelected"]').trigger('change');
		}
	);
	var years = getQueryYearArray(10);
	$('select[name="formYear"]').html(getSelectOptions(years, 0));
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

function showOrHideCoulumn()
{
	var params = $('select[name="carType"]').val();
	if(0 == params) // tractor
	{
		tableInsurance.bootstrapTable('showColumn', 'jiaShiRenYiWaiShangHaiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'diSanZheZeRenBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'boLiDanDuPoSuiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'jiShenSunShiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'nongJiHuoTuoCheBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'jiJuTuoLuoSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'duZhuZuoYeRenYuanShangHaiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'ziRanSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'zhuangYunSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'feiShiGuBuJianSunShiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'yunZhuanYiWaiShangHaiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'weiXiuBaoYangZuoYeBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'buJiMianBuLvBaoFei');
	}
	else if(1 == params) // Sgj
	{
		tableInsurance.bootstrapTable('showColumn', 'jiaShiRenYiWaiShangHaiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'diSanZheZeRenBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'boLiDanDuPoSuiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'jiShenSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'nongJiHuoTuoCheBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'jiJuTuoLuoSunShiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'duZhuZuoYeRenYuanShangHaiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'ziRanSunShiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'zhuangYunSunShiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'feiShiGuBuJianSunShiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'yunZhuanYiWaiShangHaiBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'weiXiuBaoYangZuoYeBaoFei');
		tableInsurance.bootstrapTable('showColumn', 'buJiMianBuLvBaoFei');
		
	}
	else if(2 == params) // Nongji
	{
		tableInsurance.bootstrapTable('hideColumn', 'jiaShiRenYiWaiShangHaiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'diSanZheZeRenBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'boLiDanDuPoSuiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'jiShenSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'nongJiHuoTuoCheBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'jiJuTuoLuoSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'duZhuZuoYeRenYuanShangHaiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'ziRanSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'zhuangYunSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'feiShiGuBuJianSunShiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'yunZhuanYiWaiShangHaiBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'weiXiuBaoYangZuoYeBaoFei');
		tableInsurance.bootstrapTable('hideColumn', 'buJiMianBuLvBaoFei');
	}
}

//动态刷新区县列表
function refreshRegion()
{
	var provinceId = $('select[name="diquSelected"]').val() + '';
	var selectOptions = [];

	$.each(regions, function(index, r)
	{
		if (provinceId == r.remark)
			selectOptions.push([r.id, r.name]);
	});

	$('select[name="xianSelected"]').html(getSelectOptions(selectOptions, 0));
}

/**
 * 查询列表数据
 */
function search()
{
	tableInsurance.bootstrapTable('refresh');
	showOrHideCoulumn();
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.querryFormid = $('input[name="querryFormid"]').val();
	params.querryFormOwner = $('input[name="querryFormOwner"]').val();
	params.querryFormAddress = $('input[name="querryFormAddress"]').val();
	params.querryFormOwnerTel = $('input[name="querryFormOwnerTel"]').val();
	params.querryFormCarNum = $('input[name="querryFormCarNum"]').val();
	params.querryFormFactoryNum = $('input[name="querryFormFactoryNum"]').val();
	params.querryFormEigineNum = $('input[name="querryFormEigineNum"]').val();
	params.querryFormJiJiaNum = $('input[name="querryFormJiJiaNum"]').val();
	params.carType = $('select[name="carType"]').val();
	var querryProjectTypeList = "";
    $('input[name="projectTypeList"]:checked').each(function(index) {
    	if(index == 0) {
    		querryProjectTypeList += $(this).val();
    	} else {
    		querryProjectTypeList += "," + $(this).val();
    	}
    });
    params.projectTypeList = querryProjectTypeList;
    params.formType = $('select[name="formType"]').val();
    params.chargingStatus = $("input[name='chargingStatus']:checked").val();
    params.formYear = $('select[name="formYear"]').val();
    params.diquSelected = $('select[name="diquSelected"]').val();
    params.xianSelected = $('select[name="xianSelected"]').val();
    params.chargingType = $('select[name="chargingType"]').val();
    params.orderType = $("input[name='orderType']:checked").val();
	return params;
}

/**
 * Formatter - 互助起止日期
 */
function dateFormatter(val, r)
{
	var sdate = commDateColumnFormatter(val);
	var edate = commDateColumnFormatter(r.insuranceEnd);
	return sdate + '<br>-----<br>' + edate;
}

/**
 * Formatter - 保单状态
 */
function statusFormatter(val, r)
{
	if (0 == val)
		return '新建表单';
	else if (1 == val && r.chargingStatus == 'N')
		return '审批通过表单待收费';
	else if (1 == val && r.chargingStatus != 'N')
		return '已审批，可修改或者重新打印';
	else if (2 == val)
		return '审批后待修改';
	else if (3 == val)
		return '作废表单';
	else if (4 == val)
		return '修改成功';
	else if (5 == val)
		return '收费完成待打印';
	else if (6 == val)
		return '已打印完成';
}

/**
 * Formatter - 操作
 */
function operatorFormatter(val, r)
{
	var html = '';
	html += '<a href="${ROOT}/insurance/show/' + val + '/' + r.type + '/false">查看</a>';
	if(r.formStatus != 1  && r.formStatus != 5 && r.formStatus != 6)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="${ROOT}/insurance/show/' + val + '/' + r.type + '/true">编辑</a>';
	}
	if(r.formStatus != 1  && r.formStatus != 5)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="${ROOT}/insurance/approve/' + val + '">审查通过</a>';
	}
	if(r.formStatus != 2)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="${ROOT}/insurance/disapprove/' + val + '">表单有错误</a>';
	}
	if(r.formStatus != 3 && r.formStatus != 5 && r.formStatus != 6)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="${ROOT}/insurance/drop/' + val + '">作废</a>';
	}
	return html;
}

/**
 * Formatter - 补偿单操作
 */
function compensationFormatter(val, r)
{
	return '新建补偿单 ';
}
</script>
</html>