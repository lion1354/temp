<%@include file="/header.jsp" %>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>案件统计单</title>
	<%@include file="/common.jsp" %>

	<link rel="stylesheet" type="text/css" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css">
	<script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>

	<link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/public.css"/>
	<link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/displaytag.css"/>
	<link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/alternative.css"/>

	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>
<body style='overflow: auto !important;'>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table>
				<tr height="18">
					<td rowspan="2" height="36" width="72">过滤条件</td>
					<td width="72">案件编号</td>
					<td width="72"><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td width="72">农机类型</td>
					<td width="72"><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td width="72"></td>
					<td width="72"></td>
					<td width="72">出险原因</td>
					<td width="72"><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td width="72">责任划分</td>
					<td width="72"><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td width="72">案件状态</td>
					<td width="72"><input type="text" id="name" name="name" class="form-control input-sm"></td>
				</tr>
				<tr height="18">
					<td height="18">会员身份证号</td>
					<td><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td>所属地区  </td>
					<td><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td>县</td>
					<td><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td>事故类型</td>
					<td><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td>起始报案时间</td>
					<td><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td>结束报案时间</td>
					<td><input type="text" id="name" name="name" class="form-control input-sm"></td>
				</tr>
				<tr height="18">
					<td colspan=13>
						<button type="submit" class="btn btn-success btn-sm" onclick='search();'><i class="fa fa-search"></i>&nbsp;查询</button>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="1">
				<tr height="18">
					<td height="18">案件编号</td>
					<td>出险会员</td>
					<td>身份证号</td>
					<td>农机类型</td>
					<td>所属地区</td>
					<td>县</td>
					<td>保单年份</td>
					<td>出险原因</td>
					<td>事故类型</td>
					<td>责任划分</td>
					<td>报案时间</td>
					<td>案件状态</td>
					<td>赔付金额</td>
				</tr>
				<tr height="18">
					<td height="18">　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
				</tr>
				<tr height="18">
					<td height="18">　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
					<td>　</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
