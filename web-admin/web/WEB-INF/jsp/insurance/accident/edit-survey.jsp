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
<form id="formSurvey" class="form-horizontal" action="${ROOT}/accident/survey/${r.id}" method="post" enctype="multipart/form-data">
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
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>事故现场整体照片${var.index + 1}</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'zhengti', '${image}');" title='删除照片'><i class="fa fa-times"></i></a><span>
											</c:forEach>
										</div>
										<!-- 剩余可以上传图片 -->
										<div id='image-zhengti'>
											<c:forEach begin="${fn:length(r.zhengtiList) + 1}" end="4" step="1">
											<input type="file" name="zhengti" title="点击选择照片" class="files-4 form-control input-sm">
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
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>事故现场局部照片${var.index + 1}</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'jubu', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:forEach>
										</div>
										<!-- 剩余可以上传图片 -->
										<div id='image-jubu'>
											<c:forEach begin="${fn:length(r.jubuList) + 1}" end="20" step="1">
											<input type="file" name="jubu" title="点击选择照片" class="files-4 form-control input-sm">
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
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.xingshiList[0])}" title='查看图片'>行驶证照片</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'xingshi', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:if>
										</div>
										<!-- 未上传图片 -->
										<div id='image-xingshi'>
											<c:if test="${1 != fn:length(r.xingshiList)}">
											<input type="file" name="xingshi" title="点击选择照片" class="form-control input-sm">
											</c:if>
										</div>
									</td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">驾驶证照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.jiashiList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.jiashiList[0])}" title='查看图片'>行驶证照片</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'jiashi', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:if>
										</div>
										<!-- 未上传图片 -->
										<div id='image-jiashi'>
											<c:if test="${1 != fn:length(r.jiashiList)}">
											<input type="file" name="jiashi" title="点击选择照片" class="form-control input-sm">
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
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.jipaiList[0])}" title='查看图片'>机牌合影照片</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'jipai', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:if>
										</div>
										<!-- 未上传图片 -->
										<div id='image-jipai'>
											<c:if test="${1 != fn:length(r.jipaiList)}">
											<input type="file" name="jipai" title="点击选择照片" class="form-control input-sm">
											</c:if>
										</div>
									</td>
									<td><span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">农机机架号照片(1张)</span></td>
									<td>
										<!-- 已上传图片 -->
										<div>
											<c:if test="${1 == fn:length(r.jijiaList)}">
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.jijiaList[0])}" title='查看图片'>农机机架号照片</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'jijia', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:if>
										</div>
										<!-- 未上传图片 -->
										<div id='image-jijia'>
											<c:if test="${1 != fn:length(r.jijiaList)}">
											<input type="file" name="jijia" title="点击选择照片" class="form-control input-sm">
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
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(r.shangzheList[0])}" title='查看图片'>受伤者照片</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'shangzhe', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:if>
										</div>
										<!-- 未上传图片 -->
										<div id='image-shangzhe'>
											<c:if test="${1 != fn:length(r.shangzheList)}">
											<input type="file" name="shangzhe" title="点击选择照片" class="form-control input-sm">
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
											<span style='padding-right: 20px;'><a target='_blank' href="${forpFn:qiniuUrl(image)}" title='查看图片'>伤者部位照片${var.index + 1}</a>&nbsp;<a href="javascript:;" onclick="removeImage(this, 'buwei', '${image}');" title='删除照片'><i class="fa fa-times"></i></a></span>
											</c:forEach>
										</div>
										<!-- 剩余可以上传图片 -->
										<div id='image-buwei'>
											<c:forEach begin="${fn:length(r.buweiList) + 1}" end="5" step="1">
											<input type="file" name="buwei" title="点击选择照片" class="files-4 form-control input-sm">
											</c:forEach>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="50%" height="30" align="right"><input type="submit" class="btn btn-success" name="saveBtn" value="保存"></td>
								<td width="6%" height="30" align="right">&nbsp;</td>
								<td width="44%" height="30"><input type="button" class="btn btn-default" value="取消" onclick="goBack()"></td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
	</div>
</form>
</body>

<script type="text/javascript">
// 删除的图片
var deletedImages = [];

/**
 * jQuery初始化
 */
$(function ()
{
	$('#formSurvey').submit(saveSurvey);
});

/**
 * 删除图片
 */
function removeImage(thisA, fileInputName, imageId)
{
	// 记录删除的图片ID
	deletedImages.push(imageId);
	console.log(deletedImages);

	// 删除超链接
	$(thisA).parent('span').remove();
	// 底部区域添加文件上传框
	$('#image-' + fileInputName).append('<input type="file" name="' + fileInputName + '" title="点击选择照片" class="files-4 form-control input-sm">');
}

/**
 * 保存
 */
function saveSurvey()
{
	if (!$("#formSurvey").valid())
	{
		warn('保存失败：查勘信息填写不完整！');
		return false;
	}

	// 校验事故现场整体照片数量
	var hasOne = false;
	var images = $("input[name='zhengti']");
	console.log(images.length);
	if (4 == images.length)
	{
		images.each(function()
		{ 
			if ("" != $(this).val())
				hasOne = true;
		});

		if (!hasOne)
		{
			warn('请至少选择一张“事故现场整体照片”');
			return false;
		}
	}

	// 校验事故现场局部照片数量
	hasOne = false;
	images = $("input[name='jubu']");
	console.log(images.length);
	if (20 == images.length)
	{
		images.each(function()
		{ 
			if ("" != $(this).val())
				hasOne = true;
		});

		if (!hasOne)
		{
			warn('请至少选择一张“事故现场局部照片”');
			return false;
		}
	}

	// 记录删除的图片列表
	$('input[name="deletedImages"]').val(deletedImages.join(','));
	// alert($('input[name="deletedImages"]').val());
	// return false;

	$('#formSurvey').ajaxSubmit(
	{
		success: function(rsp)
		{
			if (rsp.success)
			{
				info('查勘信息修改成功！', function()
				{
					goBack();
				});
			}
			else
				error(rsp.msg);
		}
	});

	return false;
}

// 返回
function goBack()
{
	history.back();
}
</script>

</html>