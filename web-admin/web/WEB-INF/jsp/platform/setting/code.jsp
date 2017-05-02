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

	<button class='btn btn-danger' onclick='removeIt();'><i class="fa fa-times"></i>&nbsp;删除</button>
</div>

<table id='tableCode' data-toggle="table" data-url="${ROOT}/platform/setting/code/${param.category}" data-method='get' data-toolbar="#table-toolbar" data-sort-name="id" data-sort-order="asc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="name" data-width='200'>${param.name}</th>
			<th data-field="remark" data-sortable='false' data-halign='left' data-align='left' data-formatter="commRemarkColumnFormatter">备注</th>
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formCode' class='form-horizontal' action='' method='post'>
		<input name='id' type='hidden' value='-1'>
		<input name='status' type='hidden' value='1'>
		<input name='categoryName' type='hidden' value='${param.name}'>
		<table width='100%'>
			<tr>
				<td width='80' align='right'>${param.name}：</td>
				<td><input name='name' class='form-control input-sm required' maxlength='20'></td>
			</tr>

			<tr>
				<td align='right'>备注：</td>
				<td><textarea name='remark' class='form-control input-sm' rows='3' maxlength='200'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableCode = null;

/**
 * jQuery初始化
 */
$(function()
{
	tableCode = $('#tableCode');

})

/**
 * 查询列表数据
 */
function search()
{
	 tableCode.bootstrapTable('refresh');
}

var dlgCode = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgCode)
	{
		dlgCode = iDialog(
		{
			content: $('#formCode')[0], lock: true, effect: 'i-super-scale', width: 500, 
			btn: {ok: {val: '保存', type: 'green', click: saveCode}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formCode").validate();
		// Ajax Form提交
		$('#formCode').submit(saveCode);
	}

	dlgCode.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formCode")[0].reset();
	$('#formCode').attr('action', '${ROOT}/platform/setting/code/${param.category}/' + id);
	dlgCode.show();
}

// 保存提交
function saveCode()
{
	if (!$("#formCode").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formCode').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableCode.bootstrapTable('refresh');
				info('${param.name}“' + $('input[name="name"]').val() + '”保存成功！');
				dlgCode.hide();
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
	var r = tableCode.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的${param.name}！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formCode', r[0]);
}


/**
 * 删除
 */
function removeIt()
{
	var r = tableCode.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要删除的${param.name}！');
		return;
	}

	confirm('您确定要删除该${param.name}吗？', function()
	{
		$.ajax(
		{
			url: '${ROOT}/platform/setting/code/' + r[0].id + '?name=' + r[0].name + '&categoryName=${param.name}', type: 'DELETE',
			success: function(rsp, status)
			{
				if (rsp.success)
				{
					info('${param.name}“' + r[0].name + '”删除成功！');
					tableCode.bootstrapTable('refresh');
				}
				else
					warn('删除失败：该${param.name}正在使用中，不能删除！', function(){});
			}
		});
	});
}
</script>

</html>