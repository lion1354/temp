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
	<div class="form-inline" role="form">
		<div class="form-group" style='margin-left: -10px;'>操作日期：<input name='fromDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' value='${forpFn:getFirstDateOfThisMonth()}' onChange='search();'>~<input name='toDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' value='${forpFn:getTodayDate()}' onChange='search();'></div>
		<div class="form-group">&nbsp;模糊搜索：<input name='content' class="form-control input-sm" style='width: 400px;' placeholder='操作，操作描述' title='操作，操作描述'>&nbsp;<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableLog' data-toggle="table" data-url="${ROOT}/platform/log" data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="createDate" data-sort-order="desc">
	<thead>
		<tr>
			<th data-checkbox="true" data-visible="false" data-sortable='false'></th>
			<th data-field="createDate" data-width='170'>操作日期</th>
			<th data-field="userName" data-formatter="userNameFormatter" data-width='200'>员工姓名/系统账号</th>
			<th data-field="content" data-width='200'>操作</th>
			<th data-field="parameters" data-formatter="commRemarkColumnFormatter" data-sortable='false' data-halign='left' data-align='left'>操作描述</th>
		</tr>
	</thead>
</table>
</body>

<script>
var tableLog = null;
/**
 * jQuery初始化
 */
$(function()
{
	tableLog = $('#tableLog');
});

/**
 * 查询列表数据
 */
function search()
{
	 tableLog.bootstrapTable('refresh');
}

/**
 * 组合Table查询条件
 */
function setQueryParameters(params)
{
	params.fromDate = $('input[name="fromDate"]').val();
	params.toDate = $('input[name="toDate"]').val();
	params.content = $('input[name="content"]').val();

	return params;
}

/**
 * Formatter - 员工姓名/系统账号
 */
function userNameFormatter(val, r)
{
	return val + '/' + r.loginName;
}
</script>

</html>