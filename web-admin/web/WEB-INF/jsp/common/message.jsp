<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>西点软件 - SaaS平台开发框架</title>
	<link rel="stylesheet" href="${ROOT}/js/font-awesome/font-awesome.min.css"/>
	<link rel="stylesheet" href="${ROOT}/js/bootstrap/css/bootstrap.min.css"/>
	<link rel="stylesheet" href="${ROOT}/js/bootstrap/jquery-confirm/jquery-confirm.min.css"/>
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/jquery/jquery.min.js"></script>
	<script src="${ROOT}/js/jquery/jquery-migrate.min.js"></script>
	<script src="${ROOT}/js/bootstrap/bootstrap.min.js"></script>
	<script src="${ROOT}/js/bootstrap/jquery-confirm/jquery-confirm.min.js"></script>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<body>
</body>

</html>

<script>
/**
 * jQuery初始化
 */
$(function()
{
	<c:choose>
		<c:when test="${msg.type == 1}">
			$.alert(
			{
				title: '请注意',  confirmButton: '确定', content: '<span style="color: blue; font-size: 15px;">${msg.content}</span>', icon: 'fa fa-info-circle', backgroundDismiss: false,
				confirm: function()
				{
					<c:if test='${null != actionURL}'>top.location.href='${ROOT}${actionURL}';</c:if>
				}
			});
		</c:when>
		<c:when test="${msg.type == 2}">
			$.alert(
			{
				title: '请注意',  confirmButton: '确定', content: '<span style="color: orange; font-size: 15px;">${msg.content}</span>', icon: 'fa fa-exclamation-triangle', backgroundDismiss: false,
				confirm: function()
				{
					<c:if test='${null != actionURL}'>top.location.href='${ROOT}${actionURL}';</c:if>
				}
			});
		</c:when>
		<c:otherwise>
			$.alert(
			{
				title: '请注意',  confirmButton: '确定', content: '<span style="color: #ff0000; font-size: 15px;">${msg.content}</span>', icon: 'fa fa-times-circle', backgroundDismiss: false,
				confirm: function()
				{
					<c:if test='${null != actionURL}'>top.location.href='${ROOT}${actionURL}';</c:if>
				}
			});
		</c:otherwise>
	</c:choose>	
});
</script>