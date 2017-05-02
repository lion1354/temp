<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>
	<link rel="stylesheet" href="${ROOT}/js/extjs/theme-crisp/resources/theme-crisp-all.css">
	<link  rel="stylesheet" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css" >
	<script src="${ROOT}/js/extjs/ext-all.js"></script>
	<script src="${ROOT}/js/extjs/theme-crisp/theme-crisp.js"></script>
	<script src="${ROOT}/js/extjs/locale/locale-zh_CN.js"></script>
	<script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script> 
</head>

<style>
.no-icon {display: none;}
</style>

<body>
<div style='width: 60%; float: left;'>
	<div id="table-toolbar">
		<div class="btn-group">
			<button class='btn btn-primary' onclick='add();'><i class="fa fa-plus"></i>&nbsp;新建</button>
			<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
		</div>

		<button class='btn btn-danger' onclick='removeIt();'><i class="fa fa-times"></i>&nbsp;删除</button>
	</div>

	<table id='tableRole' data-toggle="table" data-url="${ROOT}/platform/role" data-pagination='false' data-side-pagination='client' data-toolbar="#table-toolbar">
		<thead>
			<tr>
				<th data-radio="true" data-visible="true" data-sortable='false'></th>
				<th data-field="name" data-width='130'>角色名称</th>
				<th data-field="createDate" data-width='100' data-formatter="commDateColumnFormatter">创建日期</th>
				<th data-field="createUserName" data-width='90'>创建人</th>
				<th data-field="lastModifyDate" data-width='100' data-formatter="commDateColumnFormatter">修改日期</th>
				<th data-field="lastModifyUserName" data-width='90'>修改人</th>
				<th data-field="remark" data-halign='left' data-align='left' data-sortable='false' data-formatter='commRemarkColumnFormatter'>角色说明</th>
			</tr>
		</thead>
	</table>
</div>

<div style='width: 40%; float: left; padding-left: 10px;'>
	<button class='btn btn-primary' onclick='modifyRolePermission();' style='margin-bottom: 6px;'><i class="fa fa-pencil"></i>&nbsp;修改角色权限</button>
	<div id='divRolePermission' style='border: solid 1px #e1e6eb; overflow: auto;'>	
	  <table id="treeMenu"  class="table table-hover"></table>
	</div>
</div>

<div class="hidden">
	<form id='formRole' class='form-horizontal' action='' method='post'>
		<table width='100%'>
			<tr>
				<td width='80' align='right'>角色名称：</td>
				<td><input name='name' class='form-control input-sm required' maxlength=50></td>
			</tr>

			<tr>
				<td align='right'>角色说明：</td>
				<td><textarea name='remark' class='form-control input-sm' rows='3' maxlength=200></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var treeRolePermission, treeRolePermissionModify, storeRolePermission;
var tableRole, thisRole;

/**
 * Extjs初始化
 */
Ext.onReady(function()
{
	Ext.tip.QuickTipManager.init();
	// Model - RolePermission
	Ext.define('RolePermission', {extend: 'Ext.data.Model', fields: ['id', 'text', 'icon', 'expanded', 'leaf', 'children', 'privileges']});
	// Store - RolePermission
	storeRolePermission = Ext.create('Ext.data.TreeStore', {proxy: {type: 'ajax', url: '${ROOT}/platform/role/permission'}, model: 'RolePermission', nodeParam: 'roleId', autoLoad: false, listeners: {load: prepareTreeNodeDatas}});
});

/**
 * jQuery初始化
 */
$(function()
{
	tableRole = $('#tableRole');
	// Table - 行选中事件：刷新右侧角色权限
	tableRole.on('check.bs.table', loadRolePermission);
	// Table - Load事件：选中第一行记录
	tableRole.on('load-success.bs.table', function(){tableRole.bootstrapTable('check', 0);});

	// 设置右侧列表高度
	$('#divRolePermission').height($('.fixed-table-container').css('height'));
});

//----------------------------------------------------------------------------------------------
//		角色CRUD
//----------------------------------------------------------------------------------------------

var dlgRole = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == dlgRole)
	{
		dlgRole = iDialog(
		{
			content: $('#formRole')[0], lock: true, effect: 'i-super-scale', width: 500, 
			btn: {ok: {val: '保存', type: 'green', click: saveRole}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formRole").validate();
		// Ajax Form提交
		$('#formRole').submit(saveRole);
	}

	dlgRole.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formRole")[0].reset();
	$('#formRole').attr('action', '${ROOT}/platform/role/' + id);

	dlgRole.show();
	$("#formRole").clearValidate();
}

// 保存提交
function saveRole()
{
	if (!$("#formRole").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formRole').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableRole.bootstrapTable('refresh');
				info('角色“' + $('#formRole input[name="name"]').val() + '”保存成功！');
				dlgRole.hide();
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
	var r = tableRole.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的角色！');
		return;
	}

	initFormDialog(r[0].id);
	// 回填Form
	fillFormData('formRole', r[0]);
}

/**
 * 删除
 */
function removeIt()
{
	var r = tableRole.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要删除的角色！');
		return;
	}

	confirm('您确定要删除该角色吗？', function()
	{
		$.ajax(
		{
			url: '${ROOT}/platform/role/' + r[0].id + '?name=' + r[0].name, type: 'DELETE',
			success: function(rsp, status)
			{
				if (rsp.success)
				{
					info('角色“' + r[0].name + '”删除成功！');
					tableRole.bootstrapTable('refresh');
				}
				else
					warn('删除失败：该角色正在使用中，不能删除！', function(){});
			}
		});
	});
}

//----------------------------------------------------------------------------------------------
//		角色权限
//----------------------------------------------------------------------------------------------

/**
 * Tree节点数据预处理：拼接图片路径
*/
function prepareTreeNodeDatas(store, records, successful, operation, node, eOpts)
{
	Ext.each(records, function(r, index)
	{
		r.set('text', r.get('icon') + '&nbsp;' + r.get('text'));
		r.set('iconCls', 'no-icon');

		if (r.childNodes && r.childNodes.length > 0)
			prepareTreeNodeChildrenDatas(r.childNodes);
	});

	store.commitChanges();
}

// Tree节点数据预处理：拼接图片路径
function prepareTreeNodeChildrenDatas(sons)
{
	Ext.each(sons, function(r, index)
	{
		r.set('text', r.get('icon') + '&nbsp;' + r.get('text'));
		r.set('iconCls', 'no-icon');

		if (r.childNodes && r.childNodes.length > 0)
			prepareTreeNodeChildrenDatas(r.childNodes);
	});
}

/**
 * 加载指定角色的权限
 */
function loadRolePermission()
{
	var r = tableRole.bootstrapTable('getSelections');
	if(r[0]==null){
	setTimeout(function(){loadRolePermission()}, 100);
	return;
	}
	//初始化 tr
	var heads = ["模块名称","模块权限"];
	//初始化父节点
	var tNodes = [{ id: -1, pId: 0, name: "<i class='fa fa-home'>&nbsp;&nbsp;</i>"+r[0].name ,td:[]}];
	$.ajax({url: '${ROOT}/platform/role/jqTreePermission' +'?roleId=' + r[0].id,
			success: function(rsp, status)
			{
				$.each(rsp,function(index,value) 
				{ 
					tNodes.push(rsp[index]); 	
				});
				$.ForpTreeTable("treeMenu", heads, tNodes);

				$('#treeMenu').treetable('expandAll');
			}
	});
}

var rolePermissionDlg = null;
/**
 * 修改角色权限
 */
function modifyRolePermission()
{
	var r = tableRole.bootstrapTable('getSelections');
	thisRole=r[0];
	if (!r || 1 != r.length)
	{
		warn('请先选择您要修改的角色！');
		return;
	}

	if (null == rolePermissionDlg)
	{
		// 对话框
		rolePermissionDlg = iDialog(
		{
			content: '<div id="divRolePermissionModify" style="height: 500px; border: solid 1px lightgray; margin-left: 5px; margin-top: 5px;"></div>', lock: true, effect: 'i-super-scale', width: 600,
			btn:
			{
				ok:
				{
					val: '保存', type: 'green', click: function(btn)
					{
						// 有效性检查
						var checkedNodes = treeRolePermissionModify.getView().getChecked();
						var ids = [];
						Ext.Array.each(checkedNodes, function(node)
						{
							ids.push(node.get('id').split(',')[0]);
						});

						// 模块细粒度权限列表
						var menuPrivileges = [], rmps = $(':checkbox:checked');
						$.each(rmps, function(index, r)
						{
							menuPrivileges.push(r.value);
						});

						if (ids.length < 1)
						{
							warn('请分配该角色的系统权限！');
							return false;
						}

						$.post
						(
							'${ROOT}/platform/role/permission/' + thisRole.id, {name: thisRole.name, menus: ids.join(','), menuPrivileges: menuPrivileges.join(';')},
							function(rsp, textStatus, jqXHR)
							{
								if (rsp.success)
								{
									info('角色权限修改成功！');
									loadRolePermission();
									rolePermissionDlg.hide();
								}
								else
									warn('角色权限修改失败：' + json.msg, function(){});
							}
						);

						// 不关闭对话框
						return false;
					}
				},
				cancle: {val: '取消'}
			}
		});

		// 权限树
		treeRolePermissionModify = Ext.create('Ext.tree.Panel',
		{
			renderTo: 'divRolePermissionModify', layout: 'fit', height: 498, useArrows: true, columnLines: true, rowLines: true, rootVisible: true, enableColumnResize: false, sortableColumns: false, border: false,
			root: {text: r[0].name, id: '-1', icon: '${ROOT}/image/house.png', expanded: false}, store: Ext.create('Ext.data.TreeStore', {proxy: {type: 'ajax', url: '${ROOT}/platform/role/permission/modify'}, nodeParam: 'roleId', autoLoad: false, listeners: {load: prepareTreeNodeDatas}}),
			columns:
			[
				{text: '<b>模块名称</b>', xtype: 'treecolumn', dataIndex: 'text', width: 280, align: 'left'},
				{text: '<b>模块权限</b>', dataIndex: 'privileges', flex: 1, align: 'left', renderer: privilegeRenderer},
			]
		});

		// 权限树 - 复选框选择事件
		treeRolePermissionModify.on('checkchange', function(node, checked)
		{
			Ext.getBody().mask('&nbsp;&nbsp;系统正在检查权限，请稍候......&nbsp;&nbsp;', 'ajax-loading');

			// 同步所有子节点的选中状态
			checkAllChildNode(node, checked);

			// 选中所有父节点（选中时）
			if (checked)
			{
				var pNode = node.parentNode;
				while (null != pNode && '-1' != pNode.get('id'))
				{
					pNode.set('checked', checked);
					pNode = pNode.parentNode;
				}
			}

			Ext.getBody().unmask(true);
		});
	}

	// 加载角色权限
	treeRolePermissionModify.getStore().proxy.extraParams = {roleId: r[0].id};
	treeRolePermissionModify.getStore().load(
	{
		callback: function()
		{
			treeRolePermissionModify.getRootNode().expand();
			// treeRolePermissionModify.getView().refresh();
		}
	});

	// 显示对话框
	rolePermissionDlg.$title.html('<i class="fa fa-pencil"/>&nbsp;修改角色权限');
	rolePermissionDlg.show();
}

// 同步所有子节点的选中状态
function checkAllChildNode(treeNode, checked)
{
	treeNode.eachChild(function(childNode)
	{
		childNode.set('checked', checked);
		// 递归所有子节点
		checkAllChildNode(childNode, checked);
	});
}

/**
 * Grid - 只读模块细粒度权限列Renderer
 */
function privilegeReadOnlyRenderer(value, cellmeta, record, rowIndex, columnIndex, store)
{
	if (!value || 0 == value.length)
		return '';

	var pa = [];
	Ext.each(value, function(p, index)
	{
		pa.push('<span ' + (p.value == p.standardValue ? '' : 'style="color: #c0c0c0; text-decoration: line-through"') + '>' + p.name + '</span>');
	});

	return pa.join('，');
}

/**
 * Grid - 模块细粒度权限列Renderer
 */
function privilegeRenderer(value, cellmeta, record, rowIndex, columnIndex, store)
{
	if (!value || 0 == value.length)
		return '';

	var pa = [];
	var mId = record.data['id'].split(',')[0];
	Ext.each(value, function(p, index)
	{
		pa.push('<input type="checkbox" id="rmp-' + mId + '-' + p.id + '" value="' + mId + ',' + p.id + ',' + p.standardValue + '" ' + (p.value == p.standardValue ? 'checked' : '') + '>' +
						'<label for="rmp-' + mId+ '-' + p.id + '">' + p.name + '</label>');
	});

	return pa.join('&nbsp;');
}
</script>

</html>