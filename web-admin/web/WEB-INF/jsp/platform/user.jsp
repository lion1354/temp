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
		<button class='btn btn-primary' onclick='add();'><i class="fa fa-plus"></i>&nbsp;新建</button>
		<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
	</div>

	<div class="btn-group">
		<button class='btn btn-info' onclick='enableUser();'><i class="fa fa-check"></i>&nbsp;启用</button>
		<button class='btn btn-info' onclick='disableUser();'><i class="fa fa-ban"></i>&nbsp;停用</button>
	</div>

	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">&nbsp;模糊搜索：<input name='content' class="form-control input-sm" style='width: 400px;' placeholder='登录账号，员工姓名，部门名称，角色名称' title='用户姓名，登录账号，部门名称，角色名称'>&nbsp;</div>
		<div class="form-group">账号状态：<select name="state" class="form-control input-sm" onChange='search();'><option value="-1" selected>所有</option><option value="1">正常</option><option value="2">已停用</option></select>&nbsp</div>
		<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableUser' data-toggle="table" data-url="${ROOT}/platform/user" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="loginName" data-sort-order="asc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="loginName" data-width='120' data-formatter="loginNameFormatter" data-halign='left' data-align='left'>登录账号</th>
			<th data-field="userName" data-width='100'>员工姓名</th>
			<th data-field="gender" data-width='60' data-formatter="genderFormatter">性别</th>
			<th data-field="deptName" data-width='150'>部门名称</th>
			<th data-field="roleName" data-width='150'>角色名称</th>
			<!--th data-field="mobilePhone" data-width='100'>手机号码</th>
			<th data-field="email" data-width='100'>电子邮件</th-->
			<th data-field="createDate" data-width='90' data-formatter="commDateColumnFormatter">创建日期</th>
			<th data-field="createUserName" data-width='100'>创建人</th>
			<th data-field="lastModifyDate" data-width='90' data-formatter="commDateColumnFormatter">修改日期</th>
			<th data-field="lastModifyUserName" data-width='100'>修改人</th>
			<th data-field="state" data-width='60' data-formatter="stateFormatter">状态</th>
			<!--th data-field="remark" data-sortable='false' data-halign='left' data-align='left'>备注</th-->
		</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formUser' class='form-horizontal' action='' method='post'>
		<input name='id' type='hidden' value='-1'><input name='deptId' type='hidden' value='1,001'><input name='bindType' type='hidden' value='1'>
		<input name='state' type='hidden' value='1'><input name='headImg' type='hidden' value='default.png'>
		<table width='100%'>
			<tr>
				<td>
					<table width='100%'>
						<tr>
							<td width='80' align='right'>登录账号：</td>
							<td><input name='loginName' class='form-control input-sm required' maxlength='50'></td>
							<td width='80' align='right'>员工姓名：</td>
							<td><input name='userName' class='form-control input-sm required' maxlength='50'></td>
						</tr>

						<tr>
							<td align='right'>性别：</td>
							<td><select name='gender' class='form-control input-sm'></select></td>
							<td align='right'>部门：</td>
							<td><input name='deptName' class='form-control input-sm required' readonly onclick='changeDept();' style='cursor: pointer;' title='点击选择部门'></td>
						</tr>

						<tr>
							<td align='right'>角色：</td>
							<td colspan=3><select name='roleId' class='form-control input-sm required'></select></td>
						</tr>

						<tr>
							<td align='right'>地区：</td>
							<td colspan=3><select name='item1' class='form-control input-sm required' onchange='refreshRegion();' style='width: 50%; float: left;'></select><select name='item2' class='form-control input-sm required' style='width: 48%; float: left; margin-left: 8px;'></select></td>
						</tr>

						<tr>
							<td align='right'>密码：</td>
							<td><input id='password'  name='password' type='password' class='form-control input-sm required' minlength=6></td>
							<td align='right'>确认密码：</td>
							<td><input name='confirmPassword' type='password' class='form-control input-sm required' equalTo='#password' data-msg-equalTo='您2次输入的密码不一致！' minlength=6></td>
						</tr>

						<tr>
							<td align='right'>手机号码：</td>
							<td><input name='mobilePhone' class='form-control input-sm' pattern='1\d{10}' data-msg-pattern='无效的手机号码！'></td>
							<td align='right'>电子邮件：</td>
							<td><input name='email' class='form-control input-sm email' maxlength='30'></td>
						</tr>
					</table>
				</td>
				<td width='160' align='right'><img id='userHeadImg' src='' style='cursor: pointer; border: solid 1px #c0c0c0; margin: 5 0 0 5; width: 150px; height: 170px;' onClick='changeHeadImage();'></td>
			</tr>
		</table>

		<table width='100%'>
			<!--
			<tr>
				<td width='80' align='right'>账号状态：</td>
				<td width='39%'><select name='state' class='form-control input-sm'>" + getSelectOptions([[1, '正常'], [2, '停用']], 1) + "</select></td>
				<td width='80' align='right'>账号安全：</td>
				<td width='39%'><select name='bindType' class='form-control input-sm' onChange='switchIpAddress(this.value);'>" + getSelectOptions([[-1, '正常'], [1, '绑定IP地址']], -1) + "</select></td>
			</tr>

			<tr id='trIpAddress' style='display: none;'>
				<td align='right'>IP地址：</td>
				<td colspan=3><input name='ipOrMAC' class='form-control input-sm' maxlength='100'></td>
			</tr>
			-->

			<tr>
				<td width='80' align='right'>备注：</td>
				<td colspan=3><textarea name='remark' class='form-control input-sm' rows='3' maxlength='200'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
// TODO 角色多选功能。

var tableUser = null, regions = null;
var roles = [], provinces = [];
/**
 * jQuery初始化
 */
$(function()
{
	tableUser = $('#tableUser');

	// 一次性查询列表和下拉框等初始化数据，动态填充。
	$.post
	(
		'${ROOT}/platform/user/init', function(rsp, textStatus, jqXHR)
		{
			regions = rsp.regions;

			// 角色
			$.each(rsp.roles, function(index, r)
			{
				roles.push([r.id, r.name]);
			});

			// 省市
			$.each(rsp.provinces, function(index, r)
			{
				provinces.push([r.id, r.name]);
			});
		}
	);
})

/**
 * 查询列表数据
 */
function search()
{
	 tableUser.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.content = $('input[name="content"]').val();
	params.state = $('select[name="state"]').val();

	return params;
}

/**
 * Formatter - 用户姓名
 */
function loginNameFormatter(val, r)
{
	// TODO 查找tooltip插件显示大图
	return '<img width=24 height=24 src="' + getUserAvatarUrl(r.headImg, r.domainId) + '" style="vertical-align: middle;"\'>&nbsp;' + val;
}

/**
 * Formatter - 性别
 */
function genderFormatter(val, r)
{
	if (1 == val)
		return '<i class="fa fa-male fa-lg" aria-hidden="true" title=" 男 "></i>';
	else
		return '<i class="fa fa-female fa-lg" aria-hidden="true" title=" 女 "></i>';
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

// 动态刷新区县列表
function refreshRegion()
{
	var provinceId = $('select[name="item1"]').val() + '';
	var selectOptions = [];

	$.each(regions, function(index, r)
	{
		if (provinceId == r.remark)
			selectOptions.push([r.id, r.name]);
	});

	$('select[name="item2"]').html(getSelectOptions(selectOptions, 0));
}

var dlgUser = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgUser)
	{
		dlgUser = iDialog(
		{
			content: $('#formUser')[0], lock: true, effect: 'i-super-scale', width: 750, 
			btn: {ok: {val: '保存', type: 'green', click: saveUser}, cancle: {val: '取消'}}
		});

		// 初始化下拉框选项
		$('select[name="gender"]').html(getSelectOptions([[1, '男'], [2, '女']], 1));
		$('select[name="roleId"]').html(getSelectOptions(roles, -1));
		$('select[name="item1"]').html(getSelectOptions(provinces, 0));
		$('select[name="item1"]').trigger('change');

		// 初始化校验规则
		$("#formUser").validate();
		// Ajax Form提交
		$('#formUser').submit(saveUser);
	}

	dlgUser.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formUser")[0].reset();
	$('#formUser').attr('action', '${ROOT}/platform/user/' + id);
	$("#formUser").clearValidate();

	dlgUser.show();
}

// 保存提交
function saveUser()
{
	if (!$("#formUser").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formUser').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableUser.bootstrapTable('refresh');
				info('系统账号“' + $('input[name="userName"]').val() + '”保存成功！');
				dlgUser.hide();
			}
			else
				error(rsp.msg);
		}
	});

	// 取消默认submit方式 && 不关闭对话框
	return false;
}

// 动态显示、隐藏IpAddress框
function switchIpAddress(val)
{
	if (-1 == val)
		$("#trIpAddress").hide();
	else
		$("#trIpAddress").show();
}

// 更换新头像
function changeHeadImage()
{
	uploadNewHeadImage({}, function(newFileName)
	{
		$('#userHeadImg').prop('src', '${ROOT}/third/avatar/temp/' + newFileName);
		$('input[name="headImg"]').val(newFileName);
	});
}

// 更换部门
function changeDept()
{
	chooseDept(function(id,name)
	{	
		$('input[name="deptId"]').val(id);
		$('input[name="deptName"]').val(name);
	});
}

/**
 * 新建
 */
function add()
{
	initFormDialog(-1);
	$('#userHeadImg').prop('src', getUserAvatarUrl('default.png', 1));
	// 同步区县下拉框
	$('select[name="item1"]').trigger('change');
}

/**
 * 修改
 */
function edit()
{
	var r = tableUser.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的系统账号！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填表单信息。
	fillFormData('formUser', r[0]);
	$('#formUser').find('input[name="confirmPassword"]').val(r[0].password);
	$('#userHeadImg').prop('src', getUserAvatarUrl(r[0].headImg, r[0].domainId));

	// 同步选中区县下拉框
	$('select[name="item1"]').trigger('change');
	setTimeout(function(){$('select[name="item2"]').val(r[0].item2)}, 200);
}

/**
  * 启用用户
  */ 
function enableUser()
{
	confirmChangeStatus(1);
}

/**
  * 停用用户
  */ 
function disableUser()
{
	confirmChangeStatus(2);
}

/**
  * 改变员工状态
  */ 
function confirmChangeStatus(newStatus)
{
	var r = tableUser.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要' + (1 == newStatus ? '启用' : '停用') + '的账号！');
		return;
	}

	confirm('您确定要' + (1 == newStatus ? '启用' : '停用') + '该账号吗? ', function()
	{
		$.post
		(
			'${ROOT}/platform/user/status', {ids: r[0].id, newStatus: newStatus},
			function(rsp, textStatus, jqXHR)
			{
				if (rsp.success)
				{
					info('账号' + (1 == newStatus ? '启用' : '停用') + '成功！');
					search();
				}
				else
					error(rsp.msg, function(){});
			}
		);
	});
}
</script>
</html>