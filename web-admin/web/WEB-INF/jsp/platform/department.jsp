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
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
	<script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>
</head>

<body>
<div id='divToolbar' style='margin-bottom: 5px;'>
	<div class="btn-group">
		<button class='btn btn-primary' onclick='add();'><i class="fa fa-plus"></i>&nbsp;新建</button>
		<button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
	</div>

	<button class='btn btn-danger' onclick='removeIt();'><i class="fa fa-times"></i>&nbsp;删除</button>
</div>

<div id='divDeptTree' style="border: solid 1px lightgray;">
	<input id="selectTreeRow"  type="hidden" />
	<table id="treeDept"  class="table table-hover" style="margin-bottom: 0px;"></table>  
</div>

<div class="hidden">
	<form id='formDept' class='form-horizontal' action='' method='post'>
		<input name='parentNodeNo' type='hidden'>
		<table width='100%'>
			<tr>
				<td width='80' align='right'>部门名称：</td>
				<td><input name='name' class='form-control input-sm required' maxlength='50'></td>
			</tr>

			<tr>
				<td align='right'>部门说明：</td>
				<td><textarea name='remark' class='form-control input-sm' rows='3' maxlength='200'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var treeStore, treeGrid;
/**
 * Extjs初始化
 */
//Ext.onReady(function()
//{
//	Ext.tip.QuickTipManager.init();
//	// Model
//	Ext.define('Dept', {extend: 'Ext.data.Model', fields: ['id', 'nodeNo', 'name', 'createDate', 'createUserName', 'lastModifyDate', 'lastModifyUserName', 'remark']});
//	// Store
//	treeStore = Ext.create('Ext.data.TreeStore',
//	{
//		proxy: {type: 'ajax', url: '${ROOT}/platform/dept'}, model: 'Dept', nodeParam: 'extTreeNodeId', autoLoad: false,
//		listeners:
//		{
//			// 拼接id字段：id,nodeNo
//			load: function(store, records, successful, operation, node, eOpts)
//			{
//				Ext.each(records, function(r, index)
//				{
//					r.set('id', r.get('id') + ',' + r.get('nodeNo'));
//					r.set('icon', '${ROOT}/js/extjs/theme-classic/resources/images/tree/folder.gif');
//				});
//			}
//		}
//	}); 
//
//	treeGrid = Ext.create('Ext.tree.Panel',
//	{
//		renderTo: 'divDeptTree', layout: 'fit', height: $(window).height() - $('#divToolbar').outerHeight(true) - 10, useArrows: true, columnLines: true, rowLines: true, rootVisible: true, enableColumnResize: false, sortableColumns: false, border: false,
//		root: {name: $RootDeptName, id: $RootDeptId + ',' + $RootDeptNodeNo, expanded: true, icon: '${ROOT}/image/house.png'}, store: treeStore,
//		columns:
//		[
//			{text: '<b>部门名称</b>', xtype: 'treecolumn', dataIndex: 'name', width: 250, align: 'left'},
//			{text: '<b>创建日期</b>', dataIndex: 'createDate', width: 120, renderer: commDateColumnFormatter},
//			{text: '<b>创建人</b>', dataIndex: 'createUserName', width: 100},
//			{text: '<b>修改日期</b>', dataIndex: 'lastModifyDate', width: 120, renderer: commDateColumnFormatter},
//			{text: '<b>修改人</b>', dataIndex: 'lastModifyUserName', width: 100},
//			{text: '<b>备注</b>', dataIndex: 'remark', flex: 1, align: 'left'}
//		]
//	});
//
//	treeStore.load({node: treeGrid.getRootNode(), callback: function() {treeGrid.getRootNode().expand();}});
//});

//jqery 初始化方法
$(function()
{
	
	loadTree($RootDeptNodeNo);
});

function loadTree(nodeNo)
{
	//初始化 tr
	var heads = ["部门名称","创建日期","创建人","修改日期","修改人","备注"];
	//初始化父节点
	
	var initNode={ id: $RootDeptNodeNo ,leaf:false, pId: 0, name: "<i class='fa fa-home'>&nbsp;&nbsp;</i>"+$RootDeptName ,td:[]};
	var tNodes = [initNode];

	$.ajax({url: '${ROOT}/platform/dept' +'?extTreeNodeId=' + $RootDeptId+","+ $RootDeptNodeNo,
			success: function(rsp, status)
			{
				treeStore= new Map();
				treeStore.set($RootDeptNodeNo,initNode);
				$.each(rsp,function(index,value) 
				{ 
					
					var cell={ id: rsp[index].nodeNo,leaf:rsp[index].leaf, pId: rsp[index].parentNodeNo,rename:rsp[index].name,remark:rsp[index].remark,reId:rsp[index].id, name: "<i class='fa fa-folder'>&nbsp;&nbsp;</i>"+rsp[index].name,td:[rsp[index].createDate,rsp[index].createUserName,rsp[index].lastModifyDate,rsp[index].lastModifyUserName,rsp[index].remark]};
					
					
					treeStore.set(rsp[index].nodeNo,cell);
					tNodes.push(cell); 	
				});
				$.ForpTreeTable("treeDept", heads, tNodes);
				//treeGrid.expandNode($RootDeptNodeNo);
				$('#treeDept').treetable('reveal',nodeNo);
				// Highlight selected row
		        $("#treeDept tbody tr").mousedown(function () {
		              $("tr.selected").removeClass("selected");
		              $(this).addClass("selected");
		              $("#selectTreeRow").val($(this).attr("data-tt-id"));
		        });
				
				
				
			}
	});
	$("#selectTreeRow").val("");
	
}


var deptDlg = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id)
{
	if (null == deptDlg)
	{
		deptDlg = iDialog(
		{
			content: $('#formDept')[0], lock: true, effect: 'i-super-scale', width: 500,
			btn: {ok: {val: '保存', type: 'green', click: saveDept}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formDept").validate();
		// Ajax Form提交
		$('#formDept').submit(saveDept);
	}

	deptDlg.$title.html('<i class="fa fa-' + (-1 == id ? 'plus' : 'pencil') + '"></i>&nbsp;' + (-1 == id ? '新建' : '修改'));
	$("#formDept")[0].reset();
	$('#formDept').attr('action', '${ROOT}/platform/dept/' + id);

	deptDlg.show();
	$("#formDept").clearValidate();
}

// 保存提交
function saveDept()
{
	if (!$("#formDept").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formDept').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				info('部门“' + $('input[name="name"]').val() + '”保存成功！');

				// 刷新
				loadTree($("#selectTreeRow").val());
				
				deptDlg.hide();
				
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
	var r = $("#selectTreeRow").val();
	if (!r)
	{
		warn('请先选择您要新建的上级部门！', function(){});
		return;
	}
	$('input[name="parentNodeNo"]').val(r);
	initFormDialog(-1);
	// $('input[name="id"]').val(r.id.split(',')[0]);
	
}

/**
 * 修改
 */
function edit()
{
	var r = $("#selectTreeRow").val();
	if (!r)
	{
		warn('请先选择您要修改的部门！', function(){});
		return;
	}

	// 根节点不允许修改
	var parentId= $("#treeDept tbody tr.selected").attr("data-tt-parent-id");
	if (!parentId)
	{
		warn('根部门禁止修改！');
		return;
	}
	
	initFormDialog(treeStore.get(r).reId);
	$('input[name="parentNodeNo"]').val(r);
	
	$('input[name="name"]').val(treeStore.get(r).rename);
	$('textarea[name="remark"]').val(treeStore.get(r).remark);
	
//	fillFormData('formDept', r.raw);
	// fillFormData('formDept', Ext.create('Dept', r));
	// $('input[name="id"]').val(r.id.split(',')[0]);
}

/**
 * 删除
 */
function removeIt()
{
	
	
	var r = $("#selectTreeRow").val();
	if (!r)
	{
		warn('请先选择您要修改的部门！', function(){});
		return;
	}

	// 根节点不允许修改
	if (!treeStore.get(r).leaf)
	{
		warn('根节点不能删除！');
		return;
	}
	
	
	confirm('您确定要删除该部门吗？', function()
	{
		$.ajax(
		{
			url: '${ROOT}/platform/dept/' + treeStore.get(r).reId + '?nodeNo=' + treeStore.get(r).id + '&name=' + treeStore.get(r).rename , type: 'DELETE',
			success: function(rsp, status)
			{
				if (rsp.success)
				{
					info('部门“' + treeStore.get(r).rename  + '”删除成功！');
					loadTree(treeStore.get(r).pId);
					//deptDlg.hide();
				}
				else
					error(rsp.msg, function(){});
			}
		});
	});
}
</script>

</html>