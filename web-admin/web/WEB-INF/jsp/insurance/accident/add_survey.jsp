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
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<style>
input[type=file] {color: lightgray;}
.files-4 {width: 24%; float: left; margin-right: 5px;}
form table td {padding: 5px;}
</style>

<body style='overflow: auto !important;'>
<form id="formSurvey" class="form-horizontal" action="${ROOT}/accident/survey/-1" method="post" enctype="multipart/form-data">
	<input name="editMode" type="hidden" value="${editMode}">
	<input name='machineryType' type='hidden' value='-1'>
	<input name='status' type='hidden' value='2'>
	<input name='approvalStatus' type='hidden' value='0'>
	<div style='padding-bottom: 10px;'>
		<table width="100%">
			<tr>
				<td valign="top">
					<table cellpadding="3" cellspacing="3" border="1" width="100%" bordercolor="#E6E6E6">
						<thead></thead>
						<tbody>
								<tr>
									<td width='20%'><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">报案单号</span></td>
									<td width='30%'><input type="text" id="accidentId" name="accidentId" placeholder="请选择报案单号" class="form-control input-sm required" readonly onclick="loadReport();" style='cursor: pointer;'></td>
									<td width='20%'></td>
									<td width='30%'></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">联系电话</span></td>
									<td><input type="text" id="caseTel" name="caseTel" class="form-control input-sm required digits" maxlength="11" readonly></td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">车牌号码</span></td>
									<td><input type="text" id="carNum" name="carNum" class="form-control input-sm required" readonly></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">农机类型</span></td>
									<td>
										<select id="machineryTypeSelect" name="machineryTypeSelect" class="form-control input-sm" disabled>
											<option value="0">拖拉机</option>
											<option value="1">收割机</option>
											<option value="2">农业机械</option>
										</select>
									</td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">事故现场整体照片(1-4张)</span></td>
									<td colspan=3>
										<input type="file" name="zhengti" title="整体照片1" class="files-4 form-control input-sm required"><br/><br/>
										<input type="file" name="zhengti" title="整体照片2" class="files-4 form-control input-sm"><input type="file" name="zhengti" title="整体照片3" class="files-4 form-control input-sm"><input type="file" name="zhengti" title="整体照片4" class="files-4 form-control input-sm"></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">事故现场局部照片(1-20张)</span></td>
									<td colspan=3>
										<input type="file" name="jubu" title="局部照片1" class="files-4 form-control input-sm required"><br/><br/>

										<input type="file" name="jubu" title="局部照片2" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片3" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片4" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片5" class="files-4 form-control input-sm"><br/><br/>

										<input type="file" name="jubu" title="局部照片6" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片7" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片8" class="files-4 form-control input-sm">
										<input type="file" name="jubu" title="局部照片9" class="files-4 form-control input-sm"><br/><br/>

										<input type="file" name="jubu" title="局部照片10" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片11" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片12" class="files-4 form-control input-sm">
										<input type="file" name="jubu" title="局部照片13" class="files-4 form-control input-sm"><br/><br/>
										
										<input type="file" name="jubu" title="局部照片14" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片15" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片16" class="files-4 form-control input-sm">
										<input type="file" name="jubu" title="局部照片17" class="files-4 form-control input-sm"><br/><br/>

										<input type="file" name="jubu" title="局部照片18" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片19" class="files-4 form-control input-sm"><input type="file" name="jubu" title="局部照片20" class="files-4 form-control input-sm">
									</td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">行驶证照片(1张)</span></td>
									<td><input type="file" id="xingshi" name="xingshi" title="上传行驶证照片" class="form-control input-sm"></td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">驾驶证照片(1张)</span></td>
									<td><input type="file" id="jiashi" name="jiashi" title="上传驾驶证照片" class="form-control input-sm"></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">机牌合影照片(1张)</span></td>
									<td><input type="file" id="jipai" name="jipai" title="上传机牌合影照片" class="form-control input-sm"></td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">农机机架号照片(1张)</span></td>
									<td><input type="file" id="jijia" name="jijia" title="上传农机机架号照片" class="form-control input-sm"></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">受伤者照片(1张)</span></td>
									<td><input type="file" id="shangzhe" name="shangzhe" title="上传受伤者照片" class="form-control input-sm"></td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">伤者部位照片(5张)</span></td>
									<td colspan=3>
										<input type="file" name="buwei" title="照片1" class="files-4 form-control input-sm"><input type="file" name="buwei" title="照片2" class="files-4 form-control input-sm"><input type="file" name="buwei" title="照片3" class="files-4 form-control input-sm"><input type="file" name="buwei" title="照片4" class="files-4 form-control input-sm"><br/><br/>
										<input type="file" name="buwei" title="照片5" class="files-4 form-control input-sm">
									</td>
								</tr>
							</tbody>
						</table>

						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="50%" height="30" align="right"><input type="submit" class="btn btn-success" name="saveBtn" value="保存"></td>
								<td width="6%" height="30" align="right">&nbsp;</td>
								<td width="44%" height="30"><input type="button" class="btn btn-default" value="取消" onclick="resetForm()"></td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
	</div>
</form>

<div class='hidden'>
	<div id='divLoadTableToolBar'>
		<table width=850>
			<tr>
				<td align=right><b>报案单号：</b></td>
				<td><input id='sAccidentId' name='sAccidentId' class='form-control input-sm' style='font-size: 16px; color: #0342fb;' placeholder='报案单号'></td>
				<td><b>&nbsp;&nbsp;&nbsp;&nbsp;报案人电话：</b></td>
				<td><input id='sInsuranceTel' name='sInsuranceTel' class='form-control input-sm digits' maxlength='15' style='font-size: 16px; color: #0342fb;' placeholder='手机号'></td>
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
$(function ()
{
	$('#formSurvey').submit(saveSurvey);
});

var dlgLoad = null, tableLoad = null;

function loadReport()
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
							 '			<th data-field="id" data-width=30>报案单号</th>' +
							 '			<th data-field="caseName">报案人姓名</th>' +
							 '			<th data-field="caseTel">报案人联系电话</th>' +
							 '			<th data-field="carNum">号牌号码</th>' +
							 '			<th data-field="memberName">出险会员姓名</th>' +
							 '			<th data-field="machineryType" data-formatter="carTypeFormatter">农机类型</th>' +
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
							warn('请选择报案单！');
							return false;
						}
						addReport(rows[0]);
						return true;
					}
				},
				cancle: {val: '取消'}
			}
		});

		// 初始化表格
		tableLoad = $('#tableLoad');
		tableLoad.bootstrapTable();

		dlgLoad.$title.html('<i class="fa fa-plus"></i>&nbsp;报案单');
	}

	dlgLoad.show();
}

function refreshInsurance()
{
	var accidentId = $('input[name="sAccidentId"]').val();
	var insuranceTel = $('input[name="sInsuranceTel"]').val();
	var status = 1;
	var approvalStatus = 2;
	tableLoad.bootstrapTable('load', []);

	$.post
	(
		'${ROOT}/accident/get-accident', {accidentId: accidentId, insuranceTel: insuranceTel, status: status, approvalStatus: approvalStatus}, function(rsp, textStatus, jqXHR)
		{
			tableLoad.bootstrapTable('load', rsp);
		}
	);
}

function addReport(row)
{
	$('input[name="accidentId"]').val(row.id);
	$('input[name="caseTel"]').val(row.caseTel);
	$('input[name="carNum"]').val(row.carNum);
	$('select[name="machineryTypeSelect"]').val(row.machineryType);
	$('input[name="machineryType"]').val(row.machineryType);
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

function search()
{
	if (!checkEmpty("sAccidentId") && !checkEmpty("sInsuranceTel"))
	{
		warn("保单号和手机不能同时为空");
		return;
	}

	refreshInsurance();
}

/**
 * 保存
 */
function saveSurvey()
{
	if (!$("#formSurvey").valid())
	{
		warn('保存失败：查勘信息填写不完整！');
		return false;
	}

	$('#formSurvey').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				info('查勘信息保存成功！');
				resetForm();
			}
			else
				error(rsp.msg);
		}
	});

	return false;
}

//保单重置
function resetForm()
{
	$("#formSurvey")[0].reset();
}
</script>
</html>