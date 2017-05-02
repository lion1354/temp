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
									<input type="text" id="insuranceId" name="insuranceId" placeholder="请选择保单" class="form-control input-sm required" readonly">
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
								<input type="text" id="dangerAddress" name="dangerAddress" class="form-control input-sm required" readonly style='cursor: pointer;'>
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
								<input type="text" id="reportAddress" name="reportAddress" class="form-control input-sm" readonly style='cursor: pointer;'>
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
							<td height="30" align="center">
								<input type="button" class="btn btn-default" value="取消" onclick="goBack()">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</form>
</body>

<script type="text/javascript">
/**
 * jQuery初始化
 */
$(function () {
	getReport();
});

//返回
function goBack()
{
	history.back();
}
</script>
</html>