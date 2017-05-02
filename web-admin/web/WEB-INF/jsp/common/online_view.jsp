<%@include file="/header.jsp"%>

<html>

<head>
	<title>文档在线阅览</title>
	<cs:pageHeader security="false" />
	<script type="text/javascript" src="${ROOT}/third/flex-paper/flexpaper.js"></script>
	<script type="text/javascript" src="${ROOT}/third/flex-paper/flexpaper_handlers.js"></script>
	<!--script type="text/javascript" src="${ROOT}/js/LAB.min.js"></script-->
</head>

<body style="background: white; overflow-y: auto; overflow-x: auto;">
</body>

</html>

<script>
/**
  * 禁用右键菜单
  */
function disableContextMenu()
{
	document.oncontextmenu = function(){return false;}
}

Ext.onReady(function()
{
	// 文档参数
	var fileType = ${param.fileType};
	var fileName = '${param.fileName}';
	var filePath = '${param.filePath}';

	//----------------------------------------------------------------------------------------------
	//		Initialize
	//----------------------------------------------------------------------------------------------

	disableContextMenu();

	// 图片，直接显示
	if (1 == fileType)
	{
		Ext.getBody().set({scroll: 'auto'});
		Ext.getBody().createChild('<img src="${ROOT}/platform/thirdManager!downloadOnlineViewFile.do?fileType=' + fileType + '&filePath=' + filePath + '&flag=' + Math.random() + '"/>');
	}
	else
	{
		// 文档和视频，检查在线阅览swf和flv文件是否已准备好
		Ext.Ajax.request(
		{
			url: '${ROOT}/platform/thirdManager!checkOnlineViewFileStatus.do?fileType=' + fileType + '&fileName=' + fileName + '&filePath=' + filePath, failure: handleAjaxFailure, timeout: 1800 * 1000,		// 超时时长30分钟
			success: function(response, opts)
			{
				var json = Ext.decode(response.responseText);
				if (json.success)
				{
					var fileUrl = '${ROOT}' + filePath.substr(filePath.indexOf('/disk-file/'));
					// 文档
					if (2 == fileType)
					{
						//$LAB.script('${ROOT}/third/flex-paper/flexpaper.js').wait(function()
						//{
							Ext.getBody().createChild('<div id="onlineViewDiv" style="width: 100%; height: 100%;"></div><!--img src="${ROOT}/image/logo-flex-paper.png" style="position: absolute; top: 4px; left: 4px; height: 18px; width: 18px;" /-->');
							$('#onlineViewDiv').FlexPaperViewer({config:{SWFFile: escape(fileUrl + '.swf?flag=' + Math.random()), Scale: 1, PrintEnabled: false, PrintToolsVisible: false, localeChain: 'zh_CN', FullScreenVisible: false}});
						//});
					}
					else
						/*
						// 视频 TOOD 暂时不实现视频在线阅览
						if (3 == fileType)
						{
							Ext.getBody().createChild('<div id="flowPlayerDiv" style="display: block; width: 100%; height: 100%;"></div>');
							// pluginScript.src = '<%=request.getContextPath()%>/common/flow_player/flowplayer-3.2.6.min.js';
							$LAB.script('${ROOT}/third/flow_player/flowplayer-3.2.12.min.js').wait(function()
							{
								flowplayer('flowPlayerDiv','${ROOT}/third/flow_player/flowplayer-3.2.16.swf', fileUrl + '.flv?flag=' + Math.random());
							});
						}
						else
						*/
							warn('未知的在线文件阅览格式：' + fileType);
				}
				else
					warn(json.msg);
			}
		});
	}
});
</script>