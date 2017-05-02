<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>
	
	<link rel="stylesheet" type="text/css" href="${ROOT}/js/insurance/ui-lightness/jquery-ui-1.7.2.custom.css" />
    <link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/public.css" />
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
</head>
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td valign="top" bgcolor="#F7F8F9">
				<table width="100%" height="138" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="13" valign="top">&nbsp;</td>
					</tr>
					<tr>
						<td valign="top">
							<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td height="20">
										<table width="100%" height="1" border="0" cellpadding="0" cellspacing="0">
											<tr><td></td></tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%" height="55" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td width="10%" height="55" valign="middle"><img src="${ROOT}/image/user-info.gif" width="54" height="55"></td>
												<td width="90%" valign="top"><span class="left_txt2">保存成功</span><br></td>
											</tr>
										</table>
									</td>
								</tr>

							</table>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>

								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><input type="button" class="btn btn-default" value="确定" onclick="url();"></td>
		</tr>
	</table>

</body>

<script>
	function url()
	{
		window.location.href = '${ROOT}/insurance/back/${view}';
	}
</script>
</html>
