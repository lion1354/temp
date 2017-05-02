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
	<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
</div>

<table id='tableParameter' data-toggle="table" data-url='${ROOT}/platform/setting/parameter' data-method='get' data-pagination='false' data-side-pagination='client' data-toolbar="#table-toolbar" data-sortable='false'>
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="name" data-width='300' data-halign='left' data-align='left'>参数名称</th>
			<th data-field="value" data-width='250' data-halign='left' data-align='left'>参数值</th>
			<th data-field="remark" data-halign='left' data-align='left'>参数说明</th>
		</tr>
	</thead>
</table>
</body>

<div class="hidden">
	<form id='formParameter' class='form-horizontal' action='' method='post'>
		<input name='sn' type='hidden'/><input name='name' type='hidden'/><input name='remark' type='hidden'/>
		<table width='100%'>
			<tr>
				<td width='80' align='right'>参数名称：</td>
				<td><p name='name' class="form-control-static"></p></td>
			</tr>

			<tr>
				<td width='80' align='right'>参数值：</td>
				<td><input name='value' class='form-control input-sm required' maxlength=100></td>
			</tr>

			<tr>
				<td align='right'>参数说明：</td>
				<td><p name='remark' class="form-control-static"></p></td>
			</tr>
		</table>
	</form>
</div>

<script>
var tableParameter = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableParameter = $('#tableParameter');
})

var dlgParameter = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgParameter)
	{
		dlgParameter = iDialog(
		{
			content: $('#formParameter')[0], lock: true, effect: 'i-super-scale', width: 500, 
			btn: {ok: {val: '保存', type: 'green', click: saveParameter}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formParameter").validate();
		// Ajax Form提交
		$('#formParameter').submit(saveParameter);
	}

	dlgParameter.$title.html('<i class="fa fa-pencil"></i>&nbsp;修改参数');
	$("#formParameter")[0].reset();
	$('#formParameter').attr('action', '${ROOT}/platform/setting/parameter/' + id);

	dlgParameter.show();
}

// 保存提交
function saveParameter()
{
	if (!$("#formParameter").valid())
	{
		warn('保存失败：请填写正确的参数信息！');
		return false;
	}

	$('#formParameter').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableParameter.bootstrapTable('refresh');
				info('参数保存成功！');
				dlgParameter.hide();
			}
			else
				error(rsp.msg);
		}
	});

	// 取消默认submit方式 && 不关闭对话框
	return false;
}

/**
 * 修改参数 TODO 每个参数单独设定合适的输入框。
 */
function edit()
{
	var r = tableParameter.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的参数！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填Form
	fillFormData('formParameter', r[0]);
	$('p[name="name"]').html(r[0].name);
	$('p[name="remark"]').html(r[0].remark);
}
</script>

</html>