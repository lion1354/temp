<%@include file="/header.jsp" %>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>收割机投保会员统计</title>
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
					<td rowspan="2" height="36" width="106">过滤条件</td>
					<td width="99">姓名</td>
					<td width="72"><input type="text" id="name" name="name" class="form-control input-sm"></td>
					<td width="72">所属地区</td>
					<td width="72"><select name='diquSelected' class='form-control input-sm' onchange='refreshRegion();' style='width: 30%; float: left;'></select></td>
					<td width="112"><select name='xianSelected' class='form-control input-sm' style='width: 30%; float: left; margin-left: 8px;'></select></td>
					<td width="107"></td>
					<td width="72"></td>
				</tr>
				<tr height="18">
					<td height="18">身份证号</td>
					<td><input type="text" id="idCard" name="idCard" class="form-control input-sm" maxlength="20"></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan=8 height="30" align="center">
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
					<td height="18" width="106">会员姓名</td>
					<td width="99">身份证号</td>
					<td width="72">所属地区</td>
					<td width="72">县</td>
					<td width="72">首保年份</td>
					<td width="112">连续投保（年）</td>
					<td width="107">总计保单（份）</td>
					<td width="72">出险次数</td>
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
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
