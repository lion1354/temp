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
		<button class='btn btn-primary' onclick='backup();'><i class="fa fa-copy"></i>&nbsp;备份数据库</button>
		<button class='btn btn-primary' onclick='download();'><i class="fa fa-download"></i>&nbsp;下载备份文件</button>
	</div>

	<div class="btn-group"><button class='btn btn-danger' onclick='removeIt();'><i class="fa fa-times"></i>&nbsp;删除备份文件</button></div>&nbsp;&nbsp;

	<div class="form-inline" role="form" style='float: right;'>
		<div class="form-group">备份日期：<input name='fromDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' value='${forpFn:getFirstDateOfThisMonth()}' onChange='search();'>~<input name='toDate' type='text' class="form-control input-sm" onClick="WdatePicker()" style='width: 110px;' value='${forpFn:getTodayDate()}' onChange='search();'></div>
		<div class="form-group">备份结果：<select name="result" class="form-control input-sm" onChange='search();'><option value="-1" selected>所有</option><option value="0">成功</option><option value="1">失败</option></select>&nbsp;<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button></div>
	</div>
</div>

<table id='tableLog' data-toggle="table" data-url='${ROOT}/platform/database/log' data-toolbar="#table-toolbar" data-query-params="setQueryParameters" data-sort-name="createDate" data-sort-order="desc">
	<thead>
		<tr>
			<th data-radio="true" data-visible="true" data-sortable='false'></th>
			<th data-field="createDate" data-width='170'>备份时间</th>
			<th data-field="result" data-width='120' data-formatter="resultFormatter">备份结果</th>
			<th data-field="fileName" data-width='200'>文件名称</th>
			<th data-field="fileSize" data-width='150' data-formatter="fileSizeFormatter">文件大小</th>
			<th data-field="beginTime" data-sortable='false' data-formatter="beginTimeFormatter" data-halign='left' data-align='left'>备份开始 ~ 结束时间</th>
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
	// 列表行点击换色
	// tableLog.on('click-row.bs.table', function (e, row, element){$(element).toggleClass('default');});
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
	params.result = $('select[name="result"]').val();

	return params;
}

/**
 * 备份数据库
 */
function backup()
{
	confirm('您确定现在就备份数据库吗?', function()
	{
		$.post
		(
			'${ROOT}/platform/database/backup', function(rsp, textStatus, jqXHR)
			{
				if (rsp.success)
				{
					search();
					info('数据库备份操作成功！');
				}
				else
					warn(rsp.msg);
			}
		);
	});
}

/**
 * 下载数据库文件
 */
function download()
{
	var r = tableLog.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要下载的数据库备份文件！');
		return;
	}

	if (0 != r[0].result)
	{
		warn('本次数据库备份失败，无法下载数据库备份文件！');
		return;
	}

	window.open("${ROOT}/platform/database/log/" + r[0].createDate + '/' + r[0].fileName.replace('.', '-'), "_blank",
							 "width=900, height=400, status=no, toolbar=no, menubar=no,location=no, top=" + (screen.height - 400) / 2 + ", left=" + (screen.width - 900) / 2);
}

/**
 * 删除数据库文件
 */
function removeIt()
{
	var r = tableLog.bootstrapTable('getSelections');
	if (!r || 1 != r.length)
	{
		warn('请先选择您要删除的数据库备份文件！');
		return;
	}

	confirm('您确定要永久删除该数据库备份日志以及备份文件吗?', function()
	{
		$.ajax
		({
			url: '${ROOT}/platform/database/log/' + r[0].id, type: 'DELETE',
			success: function(rsp, status)
			{
				if (rsp.success)
				{
					info('数据库备份日志以及备份文件删除成功！');
					search();
				}
				else
					warn(rsp.msg);
			}
		});
	});
}

/**
 * Formatter - 备份结果
 */
function resultFormatter(val, r)
{
	return '<span style="color: ' + (0 == val ? 'green' : 'red') + '">' + (0 == val ? '成功' : '失败') + '</span';
}

/**
 * Formatter - 文件大小
 */
function fileSizeFormatter(val, r)
{
	return getNiceFileSize(val);
}

/**
 * Formatter - 备份开始 ~ 结束时间
 */
function beginTimeFormatter(val, r)
{
	return val + ' 至 ' + r.endTime;
}
</script>

</html>