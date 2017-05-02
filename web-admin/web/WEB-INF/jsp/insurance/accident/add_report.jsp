<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/insurance/util.js"></script>
	<script src="${ROOT}/js/insurance/zy_Library.js"></script>
	<script src="${ROOT}/js/insurance/getAccident.js"></script>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<body style='overflow: auto !important;'>
<form id="formReport" class="form-horizontal" action="${ROOT}/accident/report" method="post" onsubmit="return saveForm();">
	<input name="editMode" type="hidden" value="${editMode}">
	<input id="id" name="id" type="hidden" value="${id}">
	<input name='dangerLongitude' type='hidden' value='-1'>
	<input name='dangerLatitude' type='hidden' value='-1'>
	<input name='reportLongitude' type='hidden' value='-1'>
	<input name='reportLatitude' type='hidden' value='-1'>
	<input name='machineryType' type='hidden' value='-1'>
	<input name='status' type='hidden' value='1'>
	<input name='approvalStatus' type='hidden' value='0'>
	
	<div style='padding-bottom: 10px;'>
		<table width="100%">
			<tr>
				<td valign="top">
					<table cellpadding="0" cellspacing="0" border="1" width="100%" bordercolor="#E6E6E6">
						<thead></thead>
						<tbody>
						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">保单号</span>
							</td>
							<td colspan="9" style="width: 25%; height: 20px;">
								<span style="display: inline-block;">
									<input type="text" id="insuranceId" name="insuranceId" placeholder="请选择保单" class="form-control input-sm required" readonly onclick="loadInsurance();">
								</span>
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">报案人姓名</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="caseName" name="caseName" class="form-control input-sm required">
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">报案人联系电话</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="caseTel" name="caseTel" class="form-control input-sm required digits" maxlength="11">
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">出险会员姓名</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="memberName" name="memberName" class="form-control input-sm required" readonly maxlength="20">
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">农机类型</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<select id="machineryTypeSelect" name="machineryTypeSelect" class="form-control input-sm" disabled>
									<option value="0">拖拉机</option>
									<option value="1">收割机</option>
									<option value="2">农业机械</option>
								</select>
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">号牌号码</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="carNum" name="carNum" class="form-control input-sm required" readonly>
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">出险时间</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="dangerTime" name="dangerTimeStr" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" class="form-control input-sm required">
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">出险地点</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="dangerAddress" name="dangerAddress" class="form-control input-sm required" readonly placeholder=' 点击选择百度地图位置 ' style='cursor: pointer;' onclick='chooseDangerAddress();'>
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">出险原因</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<select id="reason" name="reason" class="form-control input-sm">
									<option value="1">操作失误</option>
									<option value="2">环境因素</option>
									<option value="3">第三方原因</option>
									<option value="4">机件失灵</option>
									<option value="5">其它</option>
								</select>
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">责任类型</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<select id="responsibility" name="responsibility" class="form-control input-sm">
									<option value="1">全责</option>
									<option value="2">主责</option>
									<option value="3">同责</option>
									<option value="4">次责</option>
									<option value="4">无责</option>
								</select>
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">案件情形</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<select id="accident" name="accident" class="form-control input-sm">
									<option value="1">翻机</option>
									<option value="2">坠落</option>
									<option value="3">碰撞</option>
									<option value="4">玻璃单独破碎</option>
									<option value="5">自燃</option>
									<option value="6">其它</option>
								</select>
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">损失情况</span>
							</td>
							<td colspan="9" style="width: 25%; height: 20px;">
								<div>
									<input type="checkbox" name="situation1" value="1"><span>本机损失</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="checkbox" name="situation2" value="2"><span>驾驶人伤</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <div id="hiddenDiv"><input type="checkbox" name="situation3" value="3"><span>随机人伤</span>&nbsp;&nbsp;&nbsp;&nbsp;</div>
									<input type="checkbox" name="situation4" value="4"><span>第三方财产损失</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="checkbox" name="situation5" value="5"><span>第三方人伤</span>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">驾驶员姓名</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="driverName" name="driverName" class="form-control input-sm">
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">报案地点</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<input type="text" id="reportAddress" name="reportAddress" class="form-control input-sm" readonly placeholder=' 点击选择百度地图位置 ' style='cursor: pointer;' onclick='chooseReportAddress();'>
							</td>
						</tr>
						</tbody>
					</table>
					<div id="editModeDiv" style="display: none">
					<table cellpadding="0" cellspacing="0" border="1" width="100%" bordercolor="#E6E6E6">
						<tr>
							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">报案单状态</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<select id="approvalStatus" name="approvalStatus" class="form-control input-sm" disabled>
									<option value="0">未审批</option>
									<option value="1">未通过</option>
									<option value="2">已通过</option>
									<option value="3">关闭</option>
								</select>
							</td>

							<td colspan="3" style="width: 25%; height: 20px;">
								<span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">案件异常</span>
							</td>
							<td colspan="3" style="width: 25%; height: 20px;">
								<select id="unusual" name="unusual" class="form-control input-sm" disabled>
									<option value="0">否</option>
									<option value="1">是</option>
								</select>
							</td>
						</tr>
					</table>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="50%" height="30" align="right">
								<input type="submit" class="btn btn-success" name="saveBtn" value="保存">
							</td>
							<td width="6%" height="30" align="right">&nbsp;</td>
							<td width="44%" height="30">
								<input type="button" class="btn btn-default" value="取消" onclick="resetForm()">
							</td>
						</tr>
					</table>
					<!--
					<div id="approveDiv" style="display: none">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="50%" height="30" align="right"></td>
							<td width="6%" height="30" align="right"><input type="button" class="btn btn-default" value="审核通过" onclick="approve()"></td>
							<td width="44%" height="30"></td>
						</tr>
					</table>
					</div>
					-->
				</td>
			</tr>
		</table>
	</div>
</form>

<div class='hidden'>
	<div id='divLoadTableToolBar'>
		<table width=850>
			<tr>
				<td align=right><b>保单号：</b></td>
				<td><input id='sInsuranceId' name='sInsuranceId' class='form-control input-sm' style='font-size: 16px; color: #0342fb;' placeholder='保单号'></td>
				<td><b>&nbsp;&nbsp;&nbsp;&nbsp;电话：</b></td>
				<td><input id='sInsuranceTel' name='sInsuranceTel' class='form-control input-sm digits' maxlength='15' style='font-size: 16px; color: #0342fb;' placeholder='会员保单中的手机号'></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></td>
			</tr>
		</table>
	</div>
</div>
</body>

<script type="text/javascript">
/**
 * jQuery初始化
 */
$(function () {
	var editMode = '${editMode}';
	if('true' == editMode) {
		getReport();
	} else {
		showAndHide();
	}
});

/**
 * 选择百度地图位置
 */
function chooseDangerAddress()
{
	showBaiduMap
	(
		$('input[name="dangerLongitude"]').val(), $('input[name="dangerLatitude"]').val(), $('input[name="dangerAddress"]').val(),
		function(lng, lat, address)
		{
			$('input[name="dangerLongitude"]').val(lng);
			$('input[name="dangerLatitude"]').val(lat);
			$('input[name="dangerAddress"]').val(address);
		}
	);
}

function chooseReportAddress()
{
	showBaiduMap
	(
		$('input[name="reportLongitude"]').val(), $('input[name="reportLatitude"]').val(), $('input[name="reportAddress"]').val(),
		function(lng, lat, address)
		{
			$('input[name="reportLongitude"]').val(lng);
			$('input[name="reportLatitude"]').val(lat);
			$('input[name="reportAddress"]').val(address);
		}
	);
}

var dlgLoad = null, tableLoad = null;

function loadInsurance()
{
	if (null == dlgLoad)
	{
		dlgLoad = iDialog(
		{
			lock: true, effect: 'i-super-scale', width: 1200, height: 500,
			content: '<table id="tableLoad" data-toggle="table" data-pagination="false" data-side-pagination="client" data-single-select="false" data-height="405" width="100%" data-toolbar="#divLoadTableToolBar" data-sortable="false">' +
							 '	<thead>' +
							 '		<tr>' +
							 '			<th data-radio="true" data-visible="true"></th>' +
							 '			<th data-field="insuranceTractorId" data-width=30>编号</th>' +
							 '			<th data-field="owner">名称</th>' +
							 '			<th data-field="address">地址</th>' +
							 '			<th data-field="telNum">电话</th>' +
							 '			<th data-field="carNum">牌号</th>' +
							 '			<th data-field="factoryNum">机型</th>' +
							 '			<th data-field="xianName">签单县</th>' +
							 '			<th data-field="type" data-formatter="carTypeFormatter">农机类型</th>' +
							 '		</tr>' +
							 '	</thead>' +
							 '</table>',
			btn:
			{
				ok:
				{
					val: '确定', type: 'green', click: function()
					{
						var rows = tableLoad.bootstrapTable('getSelections');
						if (!rows || 0 == rows.length)
						{
							warn('请选择保单！');
							return false;
						}
						addInsurance(rows[0]);
						return true;
					}
				},
				cancle: {val: '取消'}
			}
		});

		// 初始化表格
		tableLoad = $('#tableLoad');
		tableLoad.bootstrapTable();

		dlgLoad.$title.html('<i class="fa fa-plus"></i>&nbsp;保单');
	}

	dlgLoad.show();
}

function refreshInsurance()
{
	var insuranceId = $('input[name="sInsuranceId"]').val();
	var insuranceTel = $('input[name="sInsuranceTel"]').val();
	tableLoad.bootstrapTable('load', []);

	$.post
	(
		'${ROOT}/insurance/report-insurance-query', {insuranceId: insuranceId, insuranceTel: insuranceTel}, function(rsp, textStatus, jqXHR)
		{
			tableLoad.bootstrapTable('load', rsp);
		}
	);
}

function addInsurance(row)
{
	$('input[name="insuranceId"]').val(row.insuranceTractorId);
	$('input[name="memberName"]').val(row.owner);
	$('input[name="carNum"]').val(row.carNum);
	$('select[name="machineryTypeSelect"]').val(row.type);
	$('input[name="machineryType"]').val(row.type);
	showAndHide();
}

/**
 * Formatter - 机型状态
 */
function carTypeFormatter(val, r)
{
	if (0 == val)
		return '拖拉机';
	else if (1 == val)
		return '联合收割机';
	else
		return '农业机械';
}
function search() {
	if(!checkEmpty("sInsuranceId") && !checkEmpty("sInsuranceTel")) {
		warn("保单号和手机不能同时为空");
        return;
	}
	refreshInsurance();
}

function showAndHide() {
	var i = $('select[name="machineryTypeSelect"]').val();
	if(i == 1) {
		$('#hiddenDiv').show();
	} else {
		$('input[name="situation3"]').prop('checked', false);
		$('#hiddenDiv').hide();
	}
} 
/**
 * 保存
 */
function saveForm() {
    if (!$("#formReport").valid()) {
        warn("保存失败：表单信息填写不完整！");
        return false;
    }
    return true;
}

//保单重置
function resetForm() {
    $("#formReport")[0].reset();
}

// function approve()
// {
// 	window.location.href = '${ROOT}/accident/approve/${id}/${status}';
// }
</script>
</html>