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
		<div class="form-group">&nbsp;标题：<input name='sTitle' class="form-control input-sm" placeholder='标题' title='标题'>&nbsp;</div>
		<div class="form-group">类别：<select name="sCategory" class="form-control input-sm" onChange='search();'><option value="-1" selected>所有</option><option value="0">拖拉机</option><option value="1">收割机</option><option value="2">农业机械</option></select>&nbsp</div>
		<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableTechnology' data-toggle="table" data-url="${ROOT}/information/technology" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="lastModifyDate" data-sort-order="desc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="title" data-halign='left' data-align='left' data-sortable='false' data-formatter="titleFormatter">标题</th>
			<th data-field="source" ddata-halign='left' data-align='left' data-sortable='false'>来源</th>
			<th data-field="lastModifyDate" data-width='120' data-formatter="commDateColumnFormatter" data-sortable='false'>发布时间</th>
			<th data-field="category" data-width='150' data-formatter="categoryFormatter" data-sortable='false'>类别</th>
			<!-- th data-field="info" data-halign='left' data-align='left' data-sortable='false'>详情</th -->
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formTechnology' class='form-horizontal' enctype="multipart/form-data" action='' method='post'>
		<table width='100%'>
			<tr>
				<td align='right'>标题：</td>
				<td colspan='3'><input name='title' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td width='60' align='right'>类别：</td>
				<td><select name='category' class='form-control input-sm'><option value="0">拖拉机</option><option value="1">收割机</option><option value="2">农业机械</option></select></td>
				<td width='100' align='right'>来源：</td>
				<td><input name='source' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td align='right'>图片：</td>
				<td colspan='3'><input type="file" name='image' class='form-control input-sm'></td>
			</tr>

			<tr>
				<td align='right' valign='top'>详情：</td>
				<td colspan='3'><textarea name='info' class='form-control input-sm' rows='10'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableTechnology = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableTechnology = $('#tableTechnology');
})

/**
 * 查询列表数据
 */
function search()
{
	 tableTechnology.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.title = $('input[name="sTitle"]').val();
	params.category = $('select[name="sCategory"]').val();

	return params;
}

/**
 * Formatter - 标题
 */
function titleFormatter(val, r)
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
function categoryFormatter(val, r)
{
	if (0 == val)
		return '拖拉机';
	else if (1 == val)
		return '收割机';
	else
		return '农业机械';
}

var dlgTechnology = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgTechnology)
	{
		dlgTechnology = iDialog(
		{
			content: $('#formTechnology')[0], lock: true, effect: 'i-super-scale', width: 700, 
			btn: {ok: {val: '保存', type: 'green', click: saveTechnology}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formTechnology").validate();
		// Ajax Form提交
		$('#formTechnology').submit(saveTechnology);
	}

	dlgTechnology.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formTechnology")[0].reset();
	$('#formTechnology').attr('action', '${ROOT}/information/technology/' + id);
	$("#formTechnology").clearValidate();

	dlgTechnology.show();
}

// 保存提交
function saveTechnology()
{
	if (!$("#formTechnology").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formTechnology').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableTechnology.bootstrapTable('refresh');
				info('新技术“' + $('input[name="title"]').val() + '”保存成功！');
				dlgTechnology.hide();
			}
			else
				error(rsp.msg);
		}
	});

	// 取消默认submit方式 && 不关闭对话框
	return false;
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
	var r = tableTechnology.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的新技术信息！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formTechnology', r[0]);
}
</script>
</html>