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
	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">
			<table>
				<tr>
					<td width="8%" class="left_txt3">农机类型</td>
					<td width="20%" class="left_txt3">
						<select id="queryMachineryType" name="queryMachineryType" class="form-control input-sm">
							<option value="-1" selected>所有</option>
							<option value="0">拖拉机</option>
							<option value="1">联合收割机</option>
							<option value="2">农业机械</option>
						</select>
					</td>
					<td width="8%" class="left_txt3">报案年份</td>
					<td width="12%" class="left_txt3"><select name='queryYear' id="queryYear" class='form-control input-sm'></select></td>
					<td width="8%" class="left_txt3">出险会员姓名</td>
					<td width="12%" class="left_txt3"><input type="text" id="queryMemberName" name="queryMemberName" class="form-control input-sm"></td>
					<td width="8%" class="left_txt3">报案人员电话</td>
					<td width="12%" class="left_txt3"><input type="text" id="queryCaseTel" name="queryCaseTel" class="form-control input-sm"></td>
				</tr>
				<tr>
					<td width="8%" class="left_txt3">所属区县</td>
					<td width="20%" colspan="3" class="left_txt3 nobr">
						<select name='diquSelected' class='form-control input-sm' onchange='refreshRegion();' style='width: 30%; float: left;'></select>
						<select name='xianSelected' class='form-control input-sm' style='width: 30%; float: left; margin-left: 8px;'></select>
					</td>
					<td width="8%" class="left_txt3">报案单状态</td>
					<td width="13%" class="left_txt3">
						<select id="queryApprovalStatus" name="queryApprovalStatus" class="form-control input-sm">
							<option value="-1" selected>所有</option>
							<option value="0">未审批</option>
							<option value="1">未通过</option>
							<option value="2">已通过</option>
							<option value="3">关闭</option>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan=8 height="30" align="center"><button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></td>
				</tr>
			</table>
		</div>
	</div>
</div>

<table id='tableReport' data-toggle="table" data-url="${ROOT}/insurance/accident/report-query/1" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="id" data-sort-order="desc">
	<thead>
	<tr>
		<th data-field="id" data-width='120' data-halign='left' data-align='left'>案件单号</th>
		<th data-field="caseName" data-width='120'>报案人姓名</th>
		<th data-field="caseTel" data-width='120' data-halign='left' data-align='left'>报案人电话</th>
		<th data-field="memberName" data-width='120' data-halign='left' data-align='left'>出险会员姓名</th>
		<th data-field="machineryType" data-width='120' data-formatter="machineryTypeFormatter">农机类型</th>
		<th data-field="carNum" data-width='120' data-halign='left' data-align='left'>号牌号码</th>
		<th data-field="reason" data-width='120' data-halign='left' data-align='left' data-formatter="reasonFormatter">出险原因</th>
		<th data-field="responsibility" data-width='120' data-formatter="responsibilityFormatter">责任类型</th>
		<th data-field="accident" data-width='120' data-formatter="accidentFormatter">案件情形</th>
		<th data-field="approvalStatus" data-width='120' data-formatter="approvalStatusFormatter">案件单状态</th>
		<th data-field="approvalRemark" data-width='120' data-formatter="approvalRemarkFormatter">审批原因</th>
		<th data-field="id" data-width='120' data-formatter="operatorFormatter" data-sortable="false">&nbsp;&nbsp;操&nbsp;作&nbsp;&nbsp;</th>
	</tr>
	</thead>
</table>

<div class="hidden">
	<form id='formRemark' class='form-horizontal' action='' method='post'>
		<input name='id' type='hidden' value='-1'>
		<table width='100%'>
			<tr >
				<td style="padding-left: 10px">审批原因：</td>
			</tr>
			<tr>
				<td style="padding-left: 10px"><textarea name='approvalRemark' class='form-control input-sm required' rows='3' maxlength='40'></textarea></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var tableReport = null;
var regions = null;
var provinces = [];
/**
 * jQuery初始化
 */
$(function()
{
	tableReport = $('#tableReport');

	$.post
	(
			'${ROOT}/insurance/get-region', function(rsp, textStatus, jqXHR)
			{
				regions = rsp.regions;
				// 省市

				$.each(rsp.provinces, function(index, r)
				{
					provinces.push([r.id, r.name]);
				});
				$('select[name="diquSelected"]').html(getSelectOptions(provinces, 0));
				$('select[name="diquSelected"]').trigger('change');
			}
	);
	var years = getQueryYearArray(10);
	$('select[name="queryYear"]').html(getSelectOptions(years, 0));
})

function getQueryYearArray(years)
{
	var ya = [], count = 0, y = 0;
	var thisYear = (new Date).getFullYear();
	ya.push([-1, '所有年份']);
	while (count < years)
	{
		y = thisYear - count;
		ya.push([y, y + ' 年']);

		count++;
	}
	return ya;
}

//动态刷新区县列表
function refreshRegion()
{
	var provinceId = $('select[name="diquSelected"]').val() + '';
	var selectOptions = [];

	$.each(regions, function(index, r)
	{
		if (provinceId == r.remark)
			selectOptions.push([r.id, r.name]);
	});

	$('select[name="xianSelected"]').html(getSelectOptions(selectOptions, 0));
}

/**
 * 查询列表数据
 */
function search()
{
	tableReport.bootstrapTable('refresh');
}

function setQueryParameters(params)
{
	params.queryMachineryType = $('select[name="queryMachineryType"]').val();
	params.queryYear = $('select[name="queryYear"]').val();
	params.queryMemberName = $('input[name="queryMemberName"]').val();
	params.queryCaseTel = $('input[name="queryCaseTel"]').val();
	params.diquSelected = $('select[name="diquSelected"]').val();
	params.xianSelected = $('select[name="xianSelected"]').val();
	params.queryApprovalStatus = $('select[name="queryApprovalStatus"]').val();

	return params;
}

/**
 * Formatter
 */
function machineryTypeFormatter(val, r)
{
	if (0 == val)
		return '拖拉机';
	else if (1 == val)
		return '收割机';
	else
		return '农业机械';
}
/**
 * Formatter
 */
function reasonFormatter(val, r)
{
	if (1 == val)
		return '操作失误';
	else if (2 == val)
		return '环境因素';
	else if (3 == val)
		return '第三方原因';
	else if (4 == val)
		return '机件失灵';
	else
		return '其它';
}

/**
 * Formatter
 */
function responsibilityFormatter(val, r)
{
	if (1 == val)
		return '全责';
	else if (2 == val)
		return '主责';
	else if (3 == val)
		return '同责';
	else if (4 == val)
		return '次责';
	else
		return '无责';
}

/**
 * Formatter
 */
function accidentFormatter(val, r)
{
	if (1 == val)
		return '翻机';
	else if (2 == val)
		return '坠落';
	else if (3 == val)
		return '碰撞';
	else if (4 == val)
		return '玻璃单独破碎';
	else if (5 == val)
		return '自燃';
	else
		return '其它';
}

/**
 * Formatter
 */
function approvalStatusFormatter(val, r)
{
	if (0 == val)
		return '未审批';
	else if (1 == val)
		return '未通过';
	else if (2 == val)
		return '已通过';
	else if (3 == val)
		return '关闭';
	else
		return '';
}

/**
 * Formatter
 */
function approvalRemarkFormatter(val, r)
{
	if (0 != r.approvalStatus || 2 != r.approvalStatus)
	{
		return val;
	}
	else
		return '';
}
/**
 * Formatter
 */
function operatorFormatter(val, r)
{
	var html = '';
	html += '<a href="${ROOT}/accident/show/' + val + '/' + r.status + '/false">查看</a>';
	if(r.approvalStatus == 0  || r.approvalStatus == 1)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="${ROOT}/accident/show/' + val + '/' + r.status + '/true">编辑</a>';
	}
	if(r.approvalStatus == 0)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="${ROOT}/accident/approve/' + val + '/' + r.status + '">审查通过</a>';
	}
	if(r.approvalStatus == 0)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="javascript:void(0)" onclick="editRemark(' + val + ',\'' + r.approvalRemark + '\', 1)">审查不通过</a>';
	}
	if(r.approvalStatus != 3)
	{
		if(html !== '')
		{
			html += '<br>';
		}
		html += '<a href="javascript:void(0)" onclick="editRemark(' + val + ',\'' + r.approvalRemark + '\', 3)">关闭</a>';
	}
	return html;
}

var dlg = null;
/**
 * 初始化Form对话框
 */
function initFormDialog(id, status)
{
	if (null == dlg)
	{
		dlg = iDialog(
		{
			content: $('#formRemark')[0], lock: true, effect: 'i-super-scale', width: 750, 
			btn: {ok: {val: '保存', type: 'green', click: saveRemark}, cancle: {val: '取消'}}
		});

		// 初始化校验规则
		$("#formRemark").validate();
		// Ajax Form提交
		$('#formRemark').submit(saveRemark);
	}
	if (1 == status)
	{
		dlg.$title.html('审查不通过');
		$("#formRemark")[0].reset();
		$('#formRemark').attr('action', '${ROOT}/accident/disapprove/' + id);
	} 
	else if (3 == status)
	{
		dlg.$title.html('关闭');
		$("#formRemark")[0].reset();
		$('#formRemark').attr('action', '${ROOT}/accident/drop/' + id);
	}
	$("#formRemark").clearValidate();

	dlg.show();
}

// 保存提交
function saveRemark()
{
	if (!$("#formRemark").valid())
	{
		warn('保存失败：表单信息填写不完整！');
		return false;
	}

	$('#formRemark').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				tableReport.bootstrapTable('refresh');
				info('保存成功！');
				dlg.hide();
			}
			else
				error(rsp.msg);
		}
	});

	// 取消默认submit方式 && 不关闭对话框
	return false;
}

function editRemark(id, approvalRemark, status)
{
	initFormDialog(id, status);
	$("[name='approvalRemark']").text(approvalRemark);
}
</script>
</html>