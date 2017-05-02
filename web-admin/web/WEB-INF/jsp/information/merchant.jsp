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
		<div class="form-group">入驻到期日期：<input name='sFromDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' onChange='search();'>~<input name='sToDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' onChange='search();'>&nbsp;</div>
		<div class="form-group">类型：<select name="sType" class="form-control input-sm" onChange='search();'><option value="" selected>所有</option><option value="isMachineMerchant">农机经销商</option><option value="isPartsMerchant">配件经销商</option><option value="isCareMerchant">修理厂</option></select>&nbsp</div>
		<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableMerchant' data-toggle="table" data-url="${ROOT}/information/merchant" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="name" data-sort-order="asc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="name" data-width='250' data-halign='left' data-align='left' data-formatter="nameFormatter">名称</th>
			<th data-field="isMachineMerchant" data-width='240' data-formatter="merchantTypeFormatter">类型</th>
			<th data-field="mobilePhone" data-width='150'>联系电话</th>
			<th data-field="createDate" data-width='150' data-formatter="commDateColumnFormatter">首次入驻日期</th>
			<th data-field="endDate"  data-width='150' data-formatter="commDateColumnFormatter">入驻到期结束日期</th>
			<th data-field="weight"  data-width='80'>排序权重</th>
			<th data-field="remark" data-halign='left' data-align='left'>简介</th>
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formMerchant' class='form-horizontal' enctype="multipart/form-data" action='' method='post'>
		<input name='status' type='hidden' value='1'>
		<input name='longitude' type='hidden' value='-1'>
		<input name='latitude' type='hidden' value='-1'>
		<table width='100%'>
			<tr>
				<td align='right'>名称：</td>
				<td colspan=3><input name='name' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td height='35' align='right'>类型：</td>
				<td colspan=3><label><input type='checkbox' name='isMachineMerchant' value=1>&nbsp;农机经销商</label>&nbsp;&nbsp;<label><input type='checkbox' name='isPartsMerchant' value=1>&nbsp;配件经销商</label>&nbsp;&nbsp;<label><input type='checkbox' name='isCareMerchant' value=1>&nbsp;修理厂</label></td>
			</tr>

			<tr>
				<td width='80' align='right'>联系电话：</td>
				<td><input name='mobilePhone' class='form-control input-sm required digits' maxlength=15></td>
				<td width='150' align='right'>入驻到期结束日期：</td>
				<td><input name='endDate1' type='text' class="form-control input-sm required" onClick="WdatePicker()"></td>
			</tr>

			<tr>
				<td align='right'>照片：</td>
				<td><input type='file' name='image' class='form-control input-sm'></td>
				<td align='right'>排序权重：</td>
				<td><input name='weight' class='form-control input-sm digits required' maxlength=3 value='1'></td>
			</tr>

			<tr>
				<td align='right'>地址：</td>
				<td colspan=3><input name='address' class='form-control input-sm required' readonly placeholder=' 点击选择百度地图位置 ' style='cursor: pointer;' onclick='chooseAddress();'></td>
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
var tableMerchant = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableMerchant = $('#tableMerchant');
})

/**
 * 查询列表数据
 */
function search()
{
	 tableMerchant.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.name = $('input[name="sName"]').val();
	params.fromDate = $('input[name="sFromDate"]').val();
	params.toDate = $('input[name="sToDate"]').val();
	params.type = $('select[name="sType"]').val();

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
 * Formatter - 商户类型
 */
function merchantTypeFormatter(val, r)
{
	var names = [];

	if (1 == val)
		names.push('农机经销商');

	if (1 == r.isPartsMerchant)
		names.push('配件经销商');

	if (1 == r.isCareMerchant)
		names.push('修理厂');

	return names.join('，');
}

var dlgMerchant = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgMerchant)
	{
		dlgMerchant = iDialog(
		{
			content: $('#formMerchant')[0], lock: true, effect: 'i-super-scale', width: 650, 
			btn: {ok: {val: '保存', type: 'green', click: saveMerchant}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formMerchant").validate();
		// Ajax Form提交
		$('#formMerchant').submit(saveMerchant);
	}

	dlgMerchant.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formMerchant")[0].reset();
	$('#formMerchant').attr('action', '${ROOT}/information/merchant/' + id);
	$("#formMerchant").clearValidate();

	dlgMerchant.show();
}

// 保存提交
function saveMerchant()
{
	if (!$("#formMerchant").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	// 校验商户类型
	/*
	var types = $('input[name="type"]:checked');
	if (0 == types.length)
	{
		warn('保存失败：请选择商户类型！');
		return false;
	}
	else
	{
		var ids = [];
		$.each(types, function(idx, r)
		{
			ids.push(r.value);
		});

		$('input[name="merchantType"]').val(ids.join(','));
		// alert($('input[name="merchantType"]').val());
	}
	*/

	$('#formMerchant').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableMerchant.bootstrapTable('refresh');
				info('监理站“' + $('input[name="name"]').val() + '”保存成功！');
				dlgMerchant.hide();
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
	var r = tableMerchant.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的商户！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formMerchant', r[0]);
	
	// 回填Checkbox选中
	if (1 == r[0].isMachineMerchant)
		$('input[name="isMachineMerchant"]').prop('checked', true);
	
	if (1 == r[0].isPartsMerchant)
		$('input[name="isPartsMerchant"]').prop('checked', true);
	
	if (1 == r[0].isCareMerchant)
		$('input[name="isCareMerchant"]').prop('checked', true);

	// 回填结束日期
	$('input[name="endDate1"]').val(commDateColumnFormatter(r[0].endDate));
}
</script>
</html