<%@include file="/header.jsp"%>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>
	<link rel="stylesheet" href="${ROOT}/css/home.css">
	<script type="text/javascript" src='${ROOT}/js/jquery/jquery.cookie.js'></script>
	<!-- zTree -->
	<link rel="stylesheet" href="${ROOT}/js/jquery/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${ROOT}/js/jquery/zTree/jquery.ztree.core.js"></script>
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>

	<style>
	/* 顶部菜单的整体背景色 */
	.navbar {background-color: #23262a; padding: 0; margin: 0;}
	/* LOGO 部分背景色和字体颜色*/
	.navbar .navbar-brand {color: #fff;}

	/* 普通菜单项的颜色和背景  */
	.navbar .navbar-nav>li>a {color: #fff; background-color: #23262a;} 

	/* 当前选中菜单项的字体颜色和背景  */
	.navbar .navbar-nav>.active>a, 
	.navbar .navbar-nav>.active>a:hover, 
	.navbar .navbar-nav>.active>a:focus {color: #fff;}

	.navbar .navbar-header {line-height: 50px; color: #fff; font-size: 18px; padding-left: 0px; padding-right: 15px; border-right: solid 1px #808080;}

	.navbar .navbar-nav .dropdown-menu {background-color: #23262a;}
	.navbar .navbar-nav .dropdown-menu a {color: #fff; padding: 8px 0; padding-left: 10px;}
	.navbar .navbar-nav .dropdown-menu a:hover {color: #23262a; background-color: white;}
	.nav .open>a, .nav .open>a:focus, .nav .open>a:hover {background-color: transparent;}

	/* 菜单分割线 */
	.dropdown-menu .divider {margin: 0px; background-color: #808080}

	/* 告警动画 TODO没 没有效果 */
	@-moz-keyframes ringing
	{0%{-moz-transform:rotate(-15deg)}2%{-moz-transform:rotate(15deg)}12%,4%{-moz-transform:rotate(-18deg)}14%,6%{-moz-transform:rotate(18deg)}8%{-moz-transform:rotate(-22deg)}10%{-moz-transform:rotate(22deg)}16%{-moz-transform:rotate(-12deg)}18%{-moz-transform:rotate(12deg)}20%{-moz-transform:rotate(0)}}
	@-webkit-keyframes ringing
	{0%{-webkit-transform:rotate(-15deg)}2%{-webkit-transform:rotate(15deg)}12%,4%{-webkit-transform:rotate(-18deg)}14%,6%{-webkit-transform:rotate(18deg)}8%{-webkit-transform:rotate(-22deg)}10%{-webkit-transform:rotate(22deg)}16%{-webkit-transform:rotate(-12deg)}18%{-webkit-transform:rotate(12deg)}20%{-webkit-transform:rotate(0)}}
	@-ms-keyframes ringing
	{0%{-ms-transform:rotate(-15deg)}2%{-ms-transform:rotate(15deg)}12%,4%{-ms-transform:rotate(-18deg)}14%,6%{-ms-transform:rotate(18deg)}8%{-ms-transform:rotate(-22deg)}10%{-ms-transform:rotate(22deg)}16%{-ms-transform:rotate(-12deg)}18%{-ms-transform:rotate(12deg)}20%{-ms-transform:rotate(0)}}
	@keyframes ringing
	{0%{transform:rotate(-15deg)}2%{transform:rotate(15deg)}12%,4%{transform:rotate(-18deg)}14%,6%{transform:rotate(18deg)}8%{transform:rotate(-22deg)}10%{transform:rotate(22deg)}16%{transform:rotate(-12deg)}18%{transform:rotate(12deg)}20%{transform:rotate(0)}}

	.animated-bell
	{
		display: inline-block;
    -moz-animation: ringing 2s 5 ease 1s;
    -webkit-animation: ringing 2s 5 ease 1s;
    -o-animation: ringing 2s 5 ease 1s;
    -ms-animation: ringing 2s 5 ease 1s;
    animation: ringing 2s 5 ease 1s;
    -moz-transform-origin: 50% 0;
    -webkit-transform-origin: 50% 0;
    -o-transform-origin: 50% 0;
    -ms-transform-origin: 50% 0;
    transform-origin: 50% 0;
	}

	/* 主菜单 */
	.ztree * {font-size: 14px;}
	.ztree li ul{ margin: 0; padding: 0}
	.ztree li {line-height: 30px;}
	.ztree li a {width: 100%; height: 30px;}
	.ztree li a:hover {text-decoration: none; background-color: #eaeff4;}
	.ztree li a.curSelectedNode {background-color: #ffefbb; border:1px solid #0080ff; height: 30px}

	.ztree li span.button {background-image: url("image/ztree_outlook.png"); *background-image:url("image/ztree_outlook.gif")}
	/*.ztree ul.level0 {padding-left: 15px;}
	.ztree ul.level1 {padding-left: 15px;}
	.ztree ul.level2 {padding-left: 15px;}
	.ztree ul.level3 {padding-left: 15px;}
	.ztree ul.level4 {padding-left: 15px;}
	.ztree ul.level5 {padding-left: 15px;}
	.ztree ul.level6 {padding-left: 15px;}
	.ztree ul.level7 {padding-left: 15px;}*/
	.ztree li span.button.noline_open {background-position: 0 0;}
	.ztree li span.button.noline_close {background-position: -18px 0;}
	.ztree li span.button.noline_open.level0 {background-position: 0 -18px;}
	.ztree li span.button.noline_close.level0 {background-position: -18px -18px;}
	</style>
</head>

<body style='margin: 0px; background: #e5e5e5;'>
<!-- banner -->
<nav class="navbar" role="navigation" style="height: 52px; border-radius: 0px;">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header" style="margin-left: -10px; border: 0px;">西点软件 - SaaSFramework</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="padding:0; margin:0;">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown" style="border-left: solid 1px #808080;"><a href="#" style="color: white; font-size: 19px;"><i class="fa fa-bell-o animated-bell" style="width: 50px; text-align: center;"><span class="badge" style='margin-top: -20px; margin-left: -5px; background: red;'>4</span></i></a></li>

				<li class="dropdown" style="border-left: solid 1px #808080; border-right: solid 1px #808080;">
					<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" style="font-size: 19px;"><i class='fa fa-th' style='width: 50px; text-align: center;'></i>&nbsp;<span class="caret" style='padding-top: 10px; margin-left: -15px;'></span></a>
					<ul class="dropdown-menu">
						<li><a href="javascript: switchLayout('default');">经典布局（默认）</a></li>
						<li><a href="#">左侧导航布局&nbsp;<i class="fa fa-check"></i></a></li>
					</ul>
				</li>

				<!-- 个人参数 -->
				<li class="dropdown">
					<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" style="height: 60px; margin-top: -10px;"><img id='imgUserAvatar' class='img-circle' src="disk-file/user-avatar/default.png" width=40 height=40 style='border: solid 2px;' />&nbsp;${session_user.userName}&nbsp;<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="javascript: changeProfile();"><i class='fa fa-cog'></i>&nbsp;个人参数</a></li>
						<li role="presentation" class="divider"></li>
						<li><a href="javascript: logoutSystem();"><i class='fa fa-power-off'></i>&nbsp;退出系统</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>

<!-- menu -->
<div style='position: absolute; top: 52px; bottom: 0px; left: 0; padding: 0; width: 250px; background: white;'>
	<div class="panel panel-default" style='border-radius: 0px; border: 0px;'>
		<div class="panel-heading" style='padding-left: 5px;'><i class="fa fa-th-list fa-lg" aria-hidden="true"></i>&nbsp;系统菜单<i class="fa fa-arrow-left fa-lg" aria-hidden="true" style='position: absolute; right: 7px; top: 12px; cursor: pointer;' title=' 最小化 '></i></div>
		<div class="panel-body" style='height: 100%; padding: 0px;'>
			<ul id="ulMenu" class="ztree"></ul>
		</div>
	</div>
</div>

<!-- 工作区 -->
<div style='position: absolute; left: 257px; top: 52px; bottom: 0px; right: 0px;'>
	<iframe id='ifrmWorkspace' name='ifrmWorkspace' width='100%' height='100%' frameborder='0' src='${ROOT}/welcome' style="background-color: #fff; padding: 5px;"></iframe>
</div>

<div class='interruption' style="display: none"><div class='loading'><img src='${ROOT}/image/loading.gif' /><span>请稍候...</span></div></div>

<div class="hidden">
	<form id="formProfile" class="form-horizontal" action="${ROOT}/platform/profile" method="post">
		<input name="headImg" type="hidden" value="default.png">
		<table width="100%">
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td align="right" width="90">手机号码：</td>
							<td><input name="mobilePhone" class="form-control input-sm" pattern='1\d{10}' data-msg-pattern='无效的手机号码！'></td>
						</tr>

						<tr>
							<td align="right">电子邮件：</td>
							<td><input name="email" class="form-control input-sm email" maxlength="30"></td>
						</tr>

						<tr>
							<td align="right">分页记录数：</td>
							<td><select name="pageLimit" class="form-control input-sm"></select></td>
						</tr>

						<tr>
							<td align="right">原密码：</td>
							<td><input name="origPassword" type="password" class="form-control input-sm"></td>
						</tr>

						<tr>
							<td align="right">新密码：</td>
							<td><input id="newPassword" name="newPassword" type="password" class="form-control input-sm" minlength=6></td>
						</tr>

						<tr>
							<td align="right">确认新密码：</td>
							<td><input name="confirmNewPassword" type="password" class="form-control input-sm" equalto="#newPassword" data-msg-equalto="您2次输入的新密码不一致！" minlength=6></td>
						</tr>
					</table>
				</td>
				<td width="160" align="right"><img id="userHeadImg" src="" style="cursor: pointer; border: solid 1px #c0c0c0; margin: 5 0 0 5; width: 150px; height: 170px;" onclick="changeHeadImage();"></td>
			</tr>
		</table>
	</form>
</div>

<script>
var ifrmWorkspace = null;
var myProfile = {headImg: '${session_user.headImg}', mobilePhone: '${session_user.mobilePhone}', email: '${session_user.email}', pageLimit: ${session_user.pageLimit}};

/**
 * jQuery初始化
 */
$(function()
{
	ifrmWorkspace = $("#ifrmWorkspace");
	initBannerMenu();
	initMainMenu();

	// 刷新头像
	$('#imgUserAvatar').prop('src', getUserAvatarUrl('${session_user.headImg}', ${session_user.domainId}));
})

/**
 * 初始化顶部菜单
 */
function initBannerMenu()
{
	// 屏蔽无效的下拉菜单标识
	$.each($('ul.dropdown-menu'), function(index, ddm)
	{
		if (0 == ddm.children.length)
		{
			// 删除下拉图标
			$(ddm).prev().html($(ddm).prev().html().replace('<b class="caret"></b>', ''));
			// 删除下拉菜单项
			$(ddm).remove();
		}
	});

	// 菜单大类切换鼠标移动切换
	$('ul.nav li.dropdown').hover
	(
		function() {$(this).find('.dropdown-menu').stop(true, true).delay(10).fadeIn(200);},
		function() {$(this).find('.dropdown-menu').stop(true, true).delay(10).fadeOut(200);}
	);

	// 菜单事件
	var menuNav = $("#menuNav");
	$("#menu").delegate("a","click",function()
	{
		var curThis = $(this);
		if (curThis.attr('url').length > 0)
		{
			// 隐藏菜单
			curThis.closest(".dropdown-menu").hide();
			ifrmWorkspace.attr("src", curThis.attr("url"));
		}
	});
}

/**
 * 初始化系统菜单
 */
function initMainMenu()
{
	var setting =
	{
		view: {showLine: false, showIcon: false, selectedMulti: false, dblClickExpand: true, nameIsHTML: true, showTitle: false, addDiyDom: addDiyDom},
		data: {simpleData: {enable: true}},
		callback:
		{
			onClick: function(event, treeId, treeNode, clickFlag)
			{
				if (treeNode._url.length > 0)
					ifrmWorkspace.attr("src", treeNode._url);
			}
		}
	};

	// 层级缩进，整行选中效果
	function addDiyDom(treeId, treeNode)
	{
		var spaceWidth = 15;
		var switchObj = $("#" + treeNode.tId + "_switch"),
		icoObj = $("#" + treeNode.tId + "_ico");
		switchObj.remove();
		icoObj.before(switchObj);

		if (treeNode.level > 0)
		{
			var spaceStr = "<span style='display: inline-block; width:" + (spaceWidth * treeNode.level)+ "px;'></span>";
			switchObj.before(spaceStr);
		}
	}

	var menus = [];
<%
	Long menuId = null;
	String menuUrl = null;
	java.lang.String lastFirstMenuNodeNo = "";
	com.alibaba.fastjson.JSONObject menu = null;
	redis.clients.jedis.JedisPool pool = (redis.clients.jedis.JedisPool) cn.forp.framework.core.FORP.SPRING_CONTEXT.getBean("defaultPool");

	com.alibaba.fastjson.JSONArray menuIds = cn.forp.framework.core.util.Redis.getJSONArray(cn.forp.framework.core.FORP.CACHE_USER_PERMISSION + sessionUser.getId(), pool);
	for (int i = 0; i < menuIds.size(); i++)
	{
		menuId = menuIds.getLongValue(i);
		menu = cn.forp.framework.core.util.Redis.getJSONObject(cn.forp.framework.core.FORP.CACHE_MENU + menuId, pool);

		if (menu.containsKey("url"))
			menuUrl = cn.forp.framework.core.FORP.WEB_APP_CONTEXT + "/platform/menu/" + menu.getString("id") + "/" + sessionUser.getId();
		else
			menuUrl = "";
%>
	menus.push({id: '<%=menu.getString("nodeNo")%>', pId: '<%=menu.getString("parentNodeNo")%>', name: "<%=menu.getString("icon")%>&nbsp;<%=menu.getString("name")%>", open: <%=0 == i ? "true" : "false"%>, _url: '<%=menuUrl%>'});
<%
	}
%>

	var treeMenu = $("#ulMenu");
	$.fn.zTree.init(treeMenu, setting, menus);
}

/**
 * 切换工作区
 */
function switchLayout(layout)
{
	$.cookie('layout', layout, {expires: 365});
	window.location.href = 'home';
}

var profileDlg;
/**
 * 修改个人参数
 */
function changeProfile(id)
{
	if (null == profileDlg)
	{
		profileDlg = iDialog(
		{	
			content: $('#formProfile')[0], effect: 'i-super-scale', width: 500, lock: true,
			btn:
			{
				ok:
				{
					val: '保存', type: 'green', click: function(btn)
					{
						if (!$("#formProfile").valid())
						{
							warn('保存失败：表单信息填写不完整！');
							return false;
						}

						// 校验修改密码逻辑
						var passwds = 0;
						if ($('input[name="origPassword"]').val().length > 0)
							passwds ++;
	
						if ($('input[name="newPassword"]').val().length > 0)
							passwds ++;
	
						if ($('input[name="confirmNewPassword"]').val().length > 0)
							passwds ++;
	
						if (passwds > 0 && 3 != passwds)
						{
							warn('请输入“原密码”、“新密码”和“确认新密码”3项信息才能修改密码。请检查您的输入！');
							return false;
						}

						$('#formProfile').ajaxSubmit(
						{
							success: function(rsp)
							{
								if (rsp.success)
								{
									profileDlg.hide();
									info('个人参数信息保存成功！', function(){window.location.reload();});
								}
								else
									error(rsp.msg);
							}
						});

						// 不关闭对话框
						return false;
					}
				},
				cancle: {val: '取消'}
			}
		});

		$('select[name="pageLimit"]').html(getSelectOptions([[20, '20条每页'], [30, '30条每页'], [40, '40条每页'], [50, '50条每页'], [100, '10条每页']], 20));
		profileDlg.$title.html('<i class="fa fa-pencil"></i>&nbsp修改');

		// 初始化校验规则
		$("#formProfile").validate();
	}

	$('#formProfile')[0].reset();
	fillFormData('formProfile', myProfile);
	$('#userHeadImg').prop('src', getUserAvatarUrl(myProfile.headImg, ${session_user.domainId}));
	profileDlg.show();
	$("#formProfile").clearValidate();
}

/// 更换新头像
function changeHeadImage()
{
	uploadNewHeadImage({}, function(newFileName)
	{
		$('#userHeadImg').prop('src', '${ROOT}/third/avatar/temp/' + newFileName);
		$('input[name="headImg"]').val(newFileName);
	});
}

/**
 * 退出系统
 */
function logoutSystem()
{
	confirm('您确定现在就退出系统吗？', function()
	{
		window.location = '${ROOT}/platform/logout/${session_user.id}/default';
	});
}
</script>
</body>

</html>