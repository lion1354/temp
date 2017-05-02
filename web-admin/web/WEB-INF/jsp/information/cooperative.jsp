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
		<div class="form-group">&nbsp;服务站名称：<input name='sName' class="form-control input-sm" placeholder='服务站名称' title='服务站名称'>&nbsp;</div>
		<div class="form-group">负责人姓名：<input name='sContactPerson' class="form-control input-sm" placeholder='负责人姓名' title='负责人姓名'>&nbsp;</div>
		<div class="form-group">状态：<select name="sStatus" class="form-control input-sm" onChange='search();'><option value="-1" selected>所有</option><option value="1">正常</option><option value="0">已停用</option></select>&nbsp</div>
		<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableCooperative' data-toggle="table" data-url="${ROOT}/information/cooperative" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="name" data-sort-order="asc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="name" data-halign='left' data-align='left' data-formatter="nameFormatter">服务站名称</th>
			<th data-field="contactPerson" data-width='150'>负责人姓名</th>
			<th data-field="mobilePhone" data-width='150'>联系电话</th>
			<th data-field="address" data-halign='left' data-align='left'>地址</th>
			<th data-field="status" data-width='60' data-formatter="stateFormatter">状态</th>
			<th data-field="remark" data-halign='left' data-align='left'>简介</th>
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formCooperative' class='form-horizontal' enctype="multipart/form-data" action='' method='post'>
		<input name='longitude' type='hidden' value='-1'>
		<input name='latitude' type='hidden' value='-1'>
		<table width='100%'>
			<tr>
				<td align='right'>服务站名称：</td>
				<td colspan=3><input name='name' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td width='90' align='right'>负责人姓名：</td>
				<td><input name='contactPerson' class='form-control input-sm required' maxlength=50></td>
				<td width='100' align='right'>联系电话：</td>
				<td><input name='mobilePhone' class='form-control input-sm required digits' maxlength=15></td>
			</tr>

			<tr>
				<td align='right'>地址：</td>
				<td colspan=3><input name='address' class='form-control input-sm required' readonly placeholder=' 点击选择百度地图位置 ' style='cursor: pointer;' onclick='chooseAddress();'></td>
			</tr>

			<tr>
				<td align='right'>状态：</td>
				<td><select name='status' class='form-control input-sm'><option value="1">正常</option><option value="0">已停用</option></select></td>	
				<td align='right'>照片：</td>
				<td><input type='file' name='image' class='form-control input-sm'></td>
			</tr>

			<tr>
				<td align='right' valign='top'>简介：</td>
				<td colspan=3><textarea name='remark' class='form-control input-sm' rows='3' maxlength='40'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableCooperative = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableCooperative = $('#tableCooperative');
})

/**
 * 查询列表数据
 */
function search()
{
	 tableCooperative.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.name = $('input[name="sName"]').val();
	params.contactPerson = $('input[name="sContactPerson"]').val();
	params.status = $('select[name="sStatus"]').val();

	return params;
}

/**
 * Formatter - 名称
 */
function nameFormatter(val, r)
{
	if (r.imageId)
	{
		var url = $QiNiuDomainName + r.imageId;
		return '<a target="_blank" href="' + url + '" title="点击查看大图"><img src="' + url + '?imageView2/1/w/30/h/30"/></a>&nbsp;' + val;
	}
	else
		return val;
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

var dlgCooperative = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgCooperative)
	{
		dlgCooperative = iDialog(
		{
			content: $('#formCooperative')[0], lock: true, effect: 'i-super-scale', width: 650, 
			btn: {ok: {val: '保存', type: 'green', click: saveCooperative}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formCooperative").validate();
		// Ajax Form提交
		$('#formCooperative').submit(saveCooperative);
	}

	dlgCooperative.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formCooperative")[0].reset();
	$('#formCooperative').attr('action', '${ROOT}/information/cooperative/' + id);
	$("#formCooperative").clearValidate();

	dlgCooperative.show();
}

// 保存提交
function saveCooperative()
{
	if (!$("#formCooperative").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}
	
	$('#formCooperative').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableCooperative.bootstrapTable('refresh');
				info('合作社展台“' + $('input[name="name"]').val() + '”保存成功！');
				dlgCooperative.hide();
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
	var r = tableCooperative.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的合作社展台！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formCooperative', r[0]);
	$('select[name="status"]').val(r[0].status);
}
</script>
</html