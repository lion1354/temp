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
<form id="formSettlement" class="form-horizontal" action="" method="post" enctype="multipart/form-data">
	<input type='hidden' name='deletedImages'/>
	<div style='padding-bottom: 10px;'>
		<table width="100%">
			<tr>
				<td valign="top">
					<table cellpadding="3" cellspacing="3" border="1" width="100%" bordercolor="#E6E6E6">
						<thead></thead>
						<tbody>
								<tr>
		                            <td width='20%'><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">查勘单号</span></td>
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
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">理赔款接收账户银行</span></td>
		                            <td><input type="text" name="paymentBank" class="form-control input-sm" value="${r.paymentBank}"></td>
								</tr>
								
								<tr>
		                            <td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">理赔款接收账户名</span></td>
		                            <td><input type="text" name="paymentName" class="form-control input-sm" value="${r.paymentName}"></td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">理赔款接收账户号</span></td>
		                            <td><input type="text" name="paymentAccount" class="form-control input-sm" value="${r.paymentAccount}">
									</td>
		                        </tr>

								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">本机损失修理票据(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.sunshiList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>本机损失修理票据${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
								
								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">维修清单(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.weixiuList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>维修清单${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
								
								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">责任认定书(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.zerenList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>责任认定书${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
								
								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">调解书或判决书(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.panjueList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>调解书或判决书${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
								
								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">诊断证明(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.zhenduanList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>诊断证明${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
								
								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">医疗发票或合作医疗及其他保险报销分割单(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.fapiaoList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>医疗发票或合作医疗及其他保险报销分割单${var.index + 1}</a>&nbsp;</span>
											</c:forEach>
										</div>
									</td>
								</tr>
								
								<tr>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">用药清单(1-5张)</span></td>
									<td colspan=3>
										<!-- 已上传图片 -->
										<div>
											<c:forEach items="${r.yongyaoList}" var="image" varStatus="var">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>用药清单${var.index + 1}</a>&nbsp;</span>
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