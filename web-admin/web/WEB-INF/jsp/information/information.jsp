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
		<!-- <button class='btn btn-primary' onclick='add();'><i class="fa fa-plus"></i>&nbsp;新建</button> -->
		<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
	</div>

	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">&nbsp;标题：<input name='sTitle' class="form-control input-sm" placeholder='标题' title='标题'>&nbsp;</div>
		<div class="form-group">类别：<select name="sCategory" class="form-control input-sm" onChange='search();'><option value="-1" selected>全部</option><option value="0">找活干</option><option value="1">找机器</option><option value="2">招司机</option><option value="3">二手交易</option><option value="4">其他</option></select>&nbsp</div>		
		<div class="form-group">发布日期：<input name='sFromDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' onChange='search();'>~<input name='sToDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' onChange='search();'>&nbsp;</div>
		<div class="form-group">状态：<select name="sStatus" class="form-control input-sm" onChange='search();'><option value="-1" selected>全部</option><option value="0">待审批</option><option value="1">审批通过</option><option value="2">未通过</option><option value="3">已关闭</option></select>&nbsp</div>
		<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableInformation' data-toggle="table" data-url="${ROOT}/information/information" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="lastModifyDate" data-sort-order="desc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="title" data-halign='left' data-align='left' data-sortable='false' data-formatter="titleFormatter">标题</th>
			<th data-field="category" data-width='80' data-sortable='false' data-formatter="categoryFormatter">类别</th>
			<th data-field="status" data-width='80'  data-sortable='false' data-formatter="statusFormatter">状态</th>
			<th data-field="mobilePhone" data-width='120' data-sortable='false'>联系电话</th>
			<th data-field="lastModifyDate" data-width='120'  data-sortable='false' data-formatter="commDateColumnFormatter">发布时间</th>
			<th data-field="lastModifyUserName"  data-width='120' data-sortable='false'>发布人</th>
			<th data-field="info" data-halign='left' data-align='left' data-sortable='false'>信息详情</th>
			<th data-field="id" data-width='120' data-sortable='false' data-formatter="operatorFormatter">&nbsp;&nbsp;操&nbsp;作&nbsp;&nbsp;</th>
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formInformation' class='form-horizontal' enctype="multipart/form-data" action='' method='post'>
		<table width='100%'>
			<tr>
				<td align='right'>标题：</td>
				<td colspan=3><input name='title' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td width='80' align='right'>类别：</td>
				<td><select name='category' class='form-control input-sm'><option value="0">找活干</option><option value="1">找机器</option><option value="2">招司机</option><option value="3">二手交易</option><option value="4">其他</option></select></td>
				<td width='120' align='right'>联系电话：</td>
				<td><input name='mobilePhone' class='form-control input-sm required digits' maxlength=15></td>
			</tr>

			<tr>
				<td align='right'>图片：</td>
				<td colspan=3><input type="file" name='image' class='form-control input-sm'></td>
			</tr>

			<tr>
				<td align='right' valign='top'>信息详情：</td>
				<td colspan=3><textarea name='info' class='form-control input-sm' rows='3' maxlength='100'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableInformation = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableInformation = $('#tableInformation');
})

/**
 * 查询列表数据
 */
function search()
{
	 tableInformation.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.title = $('input[name="sTitle"]').val();
	params.fromDate = $('input[name="sFromDate"]').val();
	params.toDate = $('input[name="sToDate"]').val();
	params.category = $('select[name="sCategory"]').val();
	params.status = $('select[name="sStatus"]').val();

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
 * Formatter - 信息类别
 */
function categoryFormatter(val, r)
{
	if (0 == val)
		return '找活干';
	else if (1 == val)
		return '找机器';
	else if (2 == val)
		return '招司机';
	else if (3 == val)
		return '二手交易';
	else
		return '其他';
}

/**
 * Formatter - 信息状态
 */
function statusFormatter(val, r)
{
	if (0 == val)
		return '待审批';
	else if (1 == val)
		return '审批通过';
	else if (2 == val)
		return '未通过';
	else
		return '已关闭';
}

/**
 * Formatter - 操作
 */
function operatorFormatter(val, r)
{
	var html = '';
	if(r.status == 0 || r.status == 2)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="#" onclick="confirmChangeStatus(' + val + ', 1)">审批通过</a>';
	}
	if(r.status == 0 || r.status == 1)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="#" onclick="confirmChangeStatus(' + val + ', 2)">未通过</a>';
	}
	if(r.status == 0 || r.status == 1)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="#" onclick="confirmChangeStatus(' + val + ', 3)">关闭</a>';
	}
	return html;
}

/**
 * 改变状态
 */ 
function confirmChangeStatus(id, status)
{
	confirm('您确定要改变该信息状态吗? ', function()
	{
		$.post
		(
			'${ROOT}/information/information/status', {id: id, status: status},
			function(rsp, textStatus, jqXHR)
			{
				if (rsp.success)
				{
					info('改变该信息状态成功！');
					search();
				}
				else
					error(rsp.msg, function(){});
			}
		);
	});
}

var dlgInformation = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgInformation)
	{
		dlgInformation = iDialog(
		{
			content: $('#formInformation')[0], lock: true, effect: 'i-super-scale', width: 600, 
			btn: {ok: {val: '保存', type: 'green', click: saveInformation}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formInformation").validate();
		// Ajax Form提交
		$('#formInformation').submit(saveInformation);
	}

	dlgInformation.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formInformation")[0].reset();
	$('#formInformation').attr('action', '${ROOT}/information/information/' + id);
	$("#formInformation").clearValidate();

	dlgInformation.show();
}

// 保存提交
function saveInformation()
{
	if (!$("#formInformation").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formInformation').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableInformation.bootstrapTable('refresh');
				info('信息发布“' + $('input[name="title"]').val() + '”保存成功！');
				dlgInformation.hide();
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
	var r = tableInformation.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的信息！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formInformation', r[0]);
}
</script>
</html