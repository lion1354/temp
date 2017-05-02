	<title>陕西省农业机械安全协会 - 管理平台</title>	
	<!-- Font Awesome -->
	<link rel="stylesheet" href="${ROOT}/js/font-awesome/font-awesome.min.css"/>

	<!-- jQuery & Bootstrap -->
	<script src="${ROOT}/js/jquery/jquery.min.js"></script>
	<script src="${ROOT}/js/jquery/jquery-migrate.min.js"></script>
	<link rel="stylesheet" href="${ROOT}/js/bootstrap/css/bootstrap.min.css"/>
	<script src="${ROOT}/js/bootstrap/bootstrap.min.js"></script>

	<!-- Dialog -->
	<script src="${ROOT}/js/jquery/dialog/jquery.iDialog.min.js" dialog-theme="default"></script>

	<!-- OpenTip -->
	<link rel="stylesheet" href="${ROOT}/js/jquery/opentip/opentip.css" />
	<script src="${ROOT}/js/jquery/opentip/opentip-jquery.min.js"></script>
	<script src="${ROOT}/js/jquery/opentip/opentip-jquery-excanvas.min.js"></script>

	<!-- Validate -->
	<script src="${ROOT}/js/jquery/validate/jquery.validate.min.js"></script>
	<script src="${ROOT}/js/jquery/validate/additional-methods.min.js"></script>
	<script src="${ROOT}/js/jquery/validate/localization/messages_zh.min.js"></script>

	<!-- 97 Date Picker -->
	<script src="${ROOT}/js/jquery/97-date-picker/WdatePicker.js"></script>

	<!-- jQuery Confirm -->
	<link rel="stylesheet" href="${ROOT}/js/bootstrap/jquery-confirm/jquery-confirm.min.css"/>
	<script src="${ROOT}/js/bootstrap/jquery-confirm/jquery-confirm.min.js"></script>

	<!-- Ajax Form -->
	<script src="${ROOT}/js/jquery/jquery.form.js"></script>

	<!-- Table -->
	<link rel="stylesheet" href="${ROOT}/js/bootstrap/table/bootstrap-table.min.css"/>
	<script src="${ROOT}/js/bootstrap/table/bootstrap-table.js"></script>
	<script src="${ROOT}/js/bootstrap/table/locale/bootstrap-table-zh-CN.min.js"></script>

	<script>
	// Global Const
	var $AppContext = '${ROOT}';
	var $PagingSize = <c:if test="${null != session_user}">${session_user.pageLimit} || </c:if>20;
	var $DBType = '<%=cn.forp.framework.core.FORP.DB_TYPE%>';
	// var $ShowGlolalLoadingMask = true;
	<%
		cn.forp.framework.platform.vo.User sessionUser = (cn.forp.framework.platform.vo.User) session.getAttribute("session_user");
		com.alibaba.fastjson.JSONObject dept = cn.forp.framework.core.util.Redis.getJSONObject(cn.forp.framework.core.FORP.CACHE_ROOT_DEPT + sessionUser.getDomainId(), null);
	%>
	var $RootDeptId = '<%=dept.getIntValue("id")%>';
	var $RootDeptName = '<%=dept.getString("name")%>';
	var $RootDeptNodeNo = '<%=dept.getString("nodeNo")%>';
	var $DomainId = '<%=sessionUser.getDomainId()%>';
	var $AttachmentEngine = '<%=cn.forp.framework.core.FORP.ATTACHMENT_ENGINE%>';
	var $AlibabaMediaNameSpace = '<%=null == cn.forp.framework.core.FORP.ALIBABA_MEDIA_CFG ? "" : cn.forp.framework.core.FORP.ALIBABA_MEDIA_CFG.getNamespace()%>';
	var $QiNiuDomainName = '<%=cn.forp.framework.core.util.QiNiu.getResourceURL("")%>';
</script>