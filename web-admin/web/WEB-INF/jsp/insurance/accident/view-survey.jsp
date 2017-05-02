<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/insurance/util.js"></script>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<style>
input[type=file] {color: lightgray;}
.files-4 {width: 24%; float: left; margin-right: 5px;}
form table td {padding: 5px;}
</style>

<body style='overflow: auto !important;'>
<form id="formSurvey" class="form-horizontal" action="" method="post" enctype="multipart/form-data">
	<input type='hidden' name='deletedImages'/>
	<div style='padding-bottom: 10px;'>
		<table width="100%">
			<tr>
				<td valign="top">
					<table cellpadding="3" cellspacing="3" border="1" width="100%" bordercolor="#E6E6E6">
						<thead></thead>
						<tbody>
								<tr>
									<td width='20%'><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">报案单号</span></td>
									<td width='30%'><input type="text" name="accidentId" class="form-control input-sm required" readonly value="${r.accidentId}"></td>
									<td width='20%'></td>
									<td width='30%'></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">联系电话</span></td>
									<td><input type="text" name="caseTel" class="form-control input-sm required digits" maxlength="11" readonly value="${r.caseTel}"></td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">车牌号码</span></td>
									<td><input type="text" name="carNum" class="form-control input-sm required" readonly value="${r.carNum}"></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">农机类型</span></td>
									<td>
										<select name="machineryTypeSelect" class="form-control input-sm" disabled>
											<option value="0" <c:if test="${r.machineryType == 0}">selected</c:if>>拖拉机</option>
											<option value="1" <c:if test="${r.machineryType == 1}">selected</c:if>>收割机</option>
											<option value="2" <c:if test="${r.machineryType == 2}">selected</c:if>>农业机械</option>
										</select>
									</td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">事故现场整体照片(1-4张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.zhengtiList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>事故现场整体照片${var.index + 1}</a>&nbsp;<span>
											</c:forEach>
										</div>
									</td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">事故现场局部照片(1-20张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.jubuList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>事故现场局部照片${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">行驶证照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.xingshiList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.xingshiList[0])}" title='查看图片'>行驶证照片</a>&nbsp;</span>
											</c:if>
										</div>
									</td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">驾驶证照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.jiashiList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.jiashiList[0])}" title='查看图片'>行驶证照片</a>&nbsp;</span>
											</c:if>
										</div>
									</td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">机牌合影照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.jipaiList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.jipaiList[0])}" title='查看图片'>机牌合影照片</a>&nbsp;</span>
											</c:if>
										</div>
									</td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">农机机架号照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.jijiaList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.jijiaList[0])}" title='查看图片'>农机机架号照片</a>&nbsp;</span>
											</c:if>
										</div>
									</td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">受伤者照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.shangzheList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.shangzheList[0])}" title='查看图片'>受伤者照片</a>&nbsp;</span>
											</c:if>
										</div>
									</td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">伤者部位照片(5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.buweiList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>伤者部位照片${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" align="center">
									<input type="button" class="btn btn-default" value="取消" onclick="goBack()">
								</td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
	</div>
</form>
</body>

<script type="text/javascript">

/**
 * jQuery初始化
 */
$(function ()
{
});



// 返回
function goBack()
{
	history.back();
}
</script>

</html>