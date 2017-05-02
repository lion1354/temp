<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<body>
<div id="table-toolbar">
	<div class="btn-group">
		<button class='btn btn-primary' onclick='add();'><i class="fa fa-plus"></i>&nbsp;新建</button>
		<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
	</div>

	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">&nbsp;名称：<input name='sName' class="form-control input-sm" placeholder='名称' title='名称'>&nbsp;</div>
		<div class="form-group">状态：<select name="sState" class="form-control input-sm" onChange='search();'><option value="-1" selected>所有</option><option value="1">正常</option><option value="0">已停用</option></select>&nbsp</div>
		<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableStation' data-toggle="table" data-url="${ROOT}/information/station" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="name" data-sort-order="asc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="name" data-width='200' data-halign='left' data-align='left'>名称</th>
			<th data-field="mobilePhone" data-width='150'>联系电话</th>
			<th data-field="address" data-halign='left' data-align='left'>地址</th>
			<th data-field="status" data-width='60' data-formatter="stateFormatter">状态</th>
			<th data-field="remark" data-halign='left' data-align='left'>备注</th>
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formStation' class='form-horizontal' action='' method='post'>
		<input name='longitude' type='hidden' value='-1'><input name='latitude' type='hidden' value='-1'>
		<table width='100%'>
			<tr>
				<td width='80' align='right'>名称：</td>
				<td colspan=3><input name='name' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td width='80' align='right'>地址：</td>
				<td colspan=3><input name='address' class='form-control input-sm required' readonly placeholder=' 点击选择百度地图位置 ' style='cursor: pointer;' onclick='chooseAddress();'></td>
			</tr>

			<tr>
				<td width='80' align='right'>联系电话：</td>
				<td><input name='mobilePhone' class='form-control input-sm required digits' maxlength=15></td>
				<td width='100' align='right'>状态：</td>
				<td><select name='status' class='form-control input-sm'><option value="1">正常</option><option value="0">已停用</option></select></td>
			</tr>

			<tr>
				<td width='80' align='right'>备注：</td>
				<td colspan=3><textarea name='remark' class='form-control input-sm' rows='2' maxlength='200'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableStation = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableStation = $('#tableStation');
})

/**
 * 查询列表数据
 */
function search()
{
	 tableStation.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.name = $('input[name="sName"]').val();
	params.state = $('select[name="sState"]').val();

	return params;
}

/**
 * Formatter - 状态
 */
function stateFormatter(val, r)
{
	if (1 == val)
		return '<i class="fa fa-check fa-lg" aria-hidden="true" title=" 正常 " style="color: #00cc00;"></i>';
	else
		return '<i class="fa fa-ban fa-lg" aria-hidden="true" title=" 已停用 " style="color: #ff0066;"></i>';
}

var dlgStation = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgStation)
	{
		dlgStation = iDialog(
		{
			content: $('#formStation')[0], lock: true, effect: 'i-super-scale', width: 600, 
			btn: {ok: {val: '保存', type: 'green', click: saveStation}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formStation").validate();
		// Ajax Form提交
		$('#formStation').submit(saveStation);
	}

	dlgStation.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formStation")[0].reset();
	$('#formStation').attr('action', '${ROOT}/information/station/' + id);
	$("#formStation").clearValidate();

	dlgStation.show();
}

// 保存提交
function saveStation()
{
	if (!$("#formStation").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formStation').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableStation.bootstrapTable('refresh');
				info('监理站“' + $('input[name="name"]').val() + '”保存成功！');
				dlgStation.hide();
			}
			else
				error(rsp.msg);
		}
	});

	// 取消默认submit方式 && 不关闭对话框
	return false;
}

/**
 * 选择百度地图位置
 */
function chooseAddress()
{
	showBaiduMap
	(
		$('input[name="longitude"]').val(), $('input[name="latitude"]').val(), $('input[name="address"]').val(),
		function(lng, lat, address)
		{
			$('input[name="longitude"]').val(lng);
			$('input[name="latitude"]').val(lat);
			$('input[name="address"]').val(address);
		}
	);
}

/**
 * 新建
 */
function add()
{
	initFormDialog(-1);
}

/**
 * 修改
 */
function edit()
{
	var r = tableStation.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的监理站！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formStation', r[0]);
	$('select[name="status"]').val(r[0].status);
}
</script>
</html