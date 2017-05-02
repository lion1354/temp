<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>

	<link  rel="stylesheet" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css" >
	<script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>
	
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<body>
<div id="table-toolbar">
	<div class="btn-group">
		<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
<!-- 		<button class='btn btn-danger' onclick='removeIt();'><i class="fa fa-times"></i>&nbsp;删除</button> -->
	</div>
	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">&nbsp;模糊搜索：<input name='contentQuery' class="form-control input-sm" style='width: 550px;' placeholder='账号(手机号码)，真实姓名，身份证号，企业名称, 法人姓名, 组织机构码, 统一社会信用码' title='账号(手机号码)，真实姓名，身份证号，企业名称, 法人姓名, 组织机构码, 统一社会信用码'>&nbsp;</div>
		<div class="form-group">用户类型：<select name="typeQuery" class="form-control input-sm" onChange='search();'><option value="-1" selected>所有</option><option value="1">个人用户</option><option value="2">企业用户</option></select>&nbsp</div>
		<div><button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableMember' data-toggle="table" data-url="${ROOT}/member/members" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="id" data-sort-order="asc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="mobilePhone" data-width='120' data-halign='left' data-align='left'>账号(手机号码)</th>
			<th data-field="type" data-width='60' data-formatter="typeFormatter">用户类型</th>
			<th data-field="userName" data-width='100'>真实姓名</th>
			<th data-field="idNumber" data-width='120' data-halign='left' data-align='left'>身份证号</th>
			<th data-field="enterpriseName" data-width='150' data-halign='left' data-align='left'>企业名称</th>
			<th data-field="corporateName" data-width='100'>法人姓名</th>
			<th data-field="organizationCode" data-width='90' data-halign='left' data-align='left'>组织机构码</th>
			<th data-field="creditCode" data-width='100'>统一社会信用码</th>
			<th data-field="createDate" data-width='90' data-formatter="commDateColumnFormatter">创建日期</th>
			<th data-field="createUserName" data-width='100'>创建人</th>
			<th data-field="lastModifyDate" data-width='90' data-formatter="commDateColumnFormatter">修改日期</th>
			<th data-field="lastModifyUserName" data-width='100'>修改人</th>
			<th data-field="remark" data-sortable='false' data-halign='left' data-align='left'>备注</th>
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formMember' class='form-horizontal' action='' method='post'>
		<input name='id' type='hidden' value='-1'>
		<input name='type' type='hidden' value='-1'>
		<table width='100%'>
			<tr>
				<td width='90' align='right'>账号：</td>
				<td><input name='mobilePhone' placeholder='请输入手机号码' class='form-control input-sm required'></td>
				<td width='130' align='right'>用户类型：</td>
				<td><select name='type' onChange='switchType(this.value);' class='form-control input-sm'></select></td>
			</tr>

			<tr class='personal' stlyle='display: none;'>
				<td align='right'>真实姓名：</td>
				<td><input name='userName' placeholder='请输入您的真实姓名' class='form-control input-sm required' maxlength='50'></td>
				<td align='right'>身份证号：</td>
				<td><input name='idNumber' placeholder='请输入您的身份证号' class='form-control input-sm required' maxlength='50'></td>
			</tr>

			<tr class='enterprise' stlyle='display: none;'>
				<td align='right'>企业名称：</td>
				<td><input name='enterpriseName' placeholder='请输入企业名称' class='form-control input-sm required' maxlength='50'></td>
				<td align='right'>法人姓名：</td>
				<td><input name='corporateName' placeholder='请输入法人姓名' class='form-control input-sm required' maxlength='50'></td>
			</tr>

			<tr class='enterprise' stlyle='display: none;'>
				<td align='right'>组织机构码：</td>
				<td><input name='organizationCode' placeholder='请输入组织机构码' class='form-control input-sm required' maxlength='50'></td>
				<td align='right'>统一社会信用码：</td>
				<td><input name='creditCode' placeholder='请输入统一社会信用码' class='form-control input-sm required' maxlength='50'></td>
			</tr>

			<tr>
				<td align='right'>密码：</td>
				<td><input id='password'  name='password' type='password' class='form-control input-sm required' minlength=6></td>
				<td align='right'>确认密码：</td>
				<td><input name='confirmPassword' type='password' class='form-control input-sm required' equalTo='#password' data-msg-equalTo='您2次输入的密码不一致！' minlength=6></td>
			</tr>

			<tr>
				<td align='right'>备注：</td>
				<td colspan=3><textarea name='remark' class='form-control input-sm' rows='3' maxlength='200'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableMember = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableMember = $('#tableMember');
})

/**
 * 查询列表数据
 */
function search()
{
	tableMember.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.contentQuery = $('input[name="contentQuery"]').val();
	params.typeQuery = $('select[name="typeQuery"]').val();

	return params;
}

/**
 * Formatter - 用户类型
 */
function typeFormatter(val, r)
{
	if (1 == val)
		return '个人用户';
	else (2 == val)
		return '企业用户';
}

var dlgMember = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgMember)
	{
		dlgMember = iDialog(
		{
			content: $('#formMember')[0], lock: true, effect: 'i-super-scale', width: 700, 
			btn: {ok: {val: '保存', type: 'green', click: saveMember}, cancle: {val: '取消'}}
		});
		
		$('select[name="type"]').html(getSelectOptions([[1, '个人用户'], [2, '企业用户']], 1));
		var val = $('select[name="type"]').val();
		switchType(val);
		// 初始化校验规则
		$("#formMember").validate();
		// Ajax Form提交
		$('#formMember').submit(saveMember);
	}

	dlgMember.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formMember")[0].reset();
	$('#formMember').attr('action', '${ROOT}/member/member/' + id);
	$("#formMember").clearValidate();

	dlgMember.show();
}

// 保存提交
function saveMember()
{
	if (!$("#formMember").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formMember').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableMember.bootstrapTable('refresh');
				info('系统账号“' + $('input[name="mobilePhone"]').val() + '”保存成功！');
				dlgMember.hide();
			}
			else
				error(rsp.msg);
		}
	});

	// 取消默认submit方式 && 不关闭对话框
	return false;
}

// 动态显示、隐藏
function switchType(val)
{
	if (1 == val)
	{
		$(".enterprise").hide();
		$(".personal").show();
	}
	else if (2 == val)	
	{
		$(".personal").hide();
		$(".enterprise").show();
	}
}

/**
 * 修改
 */
function edit()
{
	var r = tableMember.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的系统账号！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formMember', r[0]);
	$('#formMember').find('input[name="confirmPassword"]').val(r[0].password);
}

/**
 * 删除
 */
function removeIt()
{
	var r = tableMember.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要删除的会员！');
		return;
	}

	confirm('您确定要删除该会员吗？', function()
	{
		$.ajax(
		{
			url: '${ROOT}/member/member/' + r[0].id + '?mobilePhone=' + r[0].mobilePhone, type: 'DELETE',
			success: function(rsp, status)
			{
				if (rsp.success)
				{
					info('会员“' + r[0].mobilePhone + '”删除成功！');
					tableMember.bootstrapTable('refresh');
				}
				else
					warn('删除失败：该会员正在使用中，不能删除！', function(){});
			}
		});
	});
}
</script>
</html>