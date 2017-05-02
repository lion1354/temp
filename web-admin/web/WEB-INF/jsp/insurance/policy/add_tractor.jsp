<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="/common.jsp"%>

	<link rel="stylesheet" type="text/css" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css" >
	<script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${ROOT}/js/insurance/ui-lightness/jquery-ui-1.7.2.custom.css" />
    <link rel="stylesheet" type="text/css" href="${ROOT}/css/insurance/public.css" />
	<script src="${ROOT}/js/insurance/util.js"></script>
	<script src="${ROOT}/js/insurance/money.js"></script>
	<script src="${ROOT}/js/insurance/zy_Library.js"></script>
	<script src="${ROOT}/js/insurance/getHis.js"></script>
    <script src="${ROOT}/js/insurance/jquery-ui-1.7.2.custom.min.js"></script>
    <script src="${ROOT}/js/insurance/ui.datepicker-zh-CN.js"></script>
	<!-- Forp通用css和js补丁 -->
	<link rel="stylesheet" href="${ROOT}/css/common.css"/>
	<script src="${ROOT}/js/common.js"></script>
	<script src="${ROOT}/js/business.js"></script>
</head>

<body style='overflow: auto !important;'>
<form id="formTractor" class="form-horizontal" action="${ROOT}/insurance/tractor" method="post" onsubmit="return saveForm();">
	<input type="hidden" name="insuranceType" value="0">
    <div style='padding-bottom: 10px;'>
        <table width="100%">
            <tr>
                <td valign="top">
                    <table cellpadding="0" cellspacing="0" border="1" width="100%" bordercolor="#E6E6E6">
                        <thead></thead>
                        <tbody>
                        <!--头部开始-->
                        <c:if test="${editMode}">
                        <tr>
                            <td colspan="8"></td>
                            <td colspan="4" style="height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">NO:</span>
                                <span>
                                	<input type="text" id="insuranceTractorId" name="insuranceTractorId" value="${insuranceTractorId}" class="form-control input-sm required" readonly="true">
                                </span>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${!editMode}">
                        <tr>
                            <td style="width: 100%" colspan="12">
                                <span style="display: inline-block; margin-left: 10px;">上年度互保单号</span>
                                <span style="display: inline-block; margin-left: 10px;">
                                	<input type="text" id="ShangNianDuHuBaoDanHao" name="ShangNianDuHuBaoDanHao" class="form-control input-sm">
                                </span>
                                <span style="display: inline-block; margin-left: 10px;">
                                	<button type="button" class="btn btn-success btn-sm" onclick="getTljHis();"><i class="fa fa-search"></i>&nbsp;查询</button>
                                </span>
                            </td>
                        </tr>
                        </c:if>

						<input type="hidden" name="editMode" value="${editMode}">
                        <tr>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">机主姓名（单位）</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="owner" name="owner" class="form-control input-sm required">
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">身份证号（社会信用代码）</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="idCard" name="idCard" class="form-control input-sm required" maxlength="20">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">地址</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="address" name="address" class="form-control input-sm required" maxlength="40">
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">电话</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="telNum" name="telNum" class="form-control input-sm required" maxlength="20">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">号牌号码</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="carNum" name="carNum" class="form-control input-sm required" maxlength="15">
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">厂牌 型号</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="factoryNum" name="factoryNum" class="form-control input-sm required" maxlength="15">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">发动机号</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="engineNum" name="engineNum" class="form-control input-sm required" maxlength="15">
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: left; overflow: hidden; display: block; float: left;">机架号</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="jijiaNum" name="jijiaNum" class="form-control input-sm required" maxlength="15">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" style="width: 16%; height: 20px;">
                                <span style="margin-left: 10px; text-align: center; overflow: hidden; display: block; float: left;">新机购置价</span>
                            </td>
                            <td colspan="2" style="height: 20px;">
                            	<input type="text" id="broughtPrice" name="broughtPrice" class="form-control input-sm" maxlength="5" onblur="javascript:isNumeric(this.value)">
                            </td>
                            <td colspan="2" style="width: 16%; height: 20px;">
                                <span style="margin-left: 10px; text-align: center; overflow: hidden; display: block; float: left;">购置日期</span>
                            </td>
                            <td colspan="2" style="width: 16%; height: 20px;">
                                <input type="text" id="buyDate" name="buyDateStr" class="form-control input-sm">
                            </td>
                            <td colspan="2" style="width: 16%; height: 20px;">
                                <span style="margin-left: 10px; text-align: center; overflow: hidden; display: block; float: left;">初次登记日期</span>
                            </td>
                            <td colspan="2" style="width: 16%; height: 20px;">
                                <input type="text" id="regDate" name="regDateStr" class="form-control input-sm">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: center; overflow: hidden; display: block; float: left;">驾驶操作人姓名</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="secondDirverName" name="secondDirverName" class="form-control input-sm" maxlength="10">
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                                <span style="margin-left: 10px; text-align: center; overflow: hidden; display: block; float: left;">身份证号</span>
                            </td>
                            <td colspan="3" style="width: 25%; height: 20px;">
                            	<input type="text" id="secondDriverLience" name="secondDriverLience" class="form-control input-sm" maxlength="20">
                            </td>
                        </tr>
                        <!--内容部分-->
                        <tr>
                            <td rowspan="6" style="height: 20px; width: 5%; text-align: center;">
                                <span style="display: inline-block;">
                                    <input type="checkbox" id="huBaoZuHeZongBaoFei" name="huBaoZuHeZongBaoFei" value="300">
                                 </span>
                            </td>
                            <td colspan="2" rowspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">① 组合总保费</span>
                            </td>
                            <td rowspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">会员保费（元）：300</span>
                            </td>
                            <td colspan="2" rowspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">省财政补贴（元）：150</span>
                            </td>
                            <td colspan="6" style="height: 20px; width: 50%;">
                                <span style="display: inline-block; margin-left: 10px;">机身损失补偿限额：20000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" rowspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">驾驶人意外伤害</span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">身故、残疾补偿限额：50000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">医疗费用补偿限额：13000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" rowspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">市县财政补贴（元）:
                                    <input type="text" id="caizhengbutie" name="caizhengbutie" style="width:50px; height:20px;" size="10" onblur="javascript:isNumeric(this.value)">
                                </span>
                            </td>
                            <td rowspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">第三者责任</span>
                            </td>
                            <td colspan="2" rowspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">人身意外伤害</span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">身故、残疾补偿限额：100000元</span>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">医疗费用补偿限额：10000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="5" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">财产损失补偿限额：3000元</span>
                            </td>
                        </tr>
                        <!--2-->
                        <tr>
                            <td style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                	<input type="checkbox" id="jiShenSunShiBaoFei" name="jiShenSunShiBaoFei" value="50">
                                </span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">② 机身损失</span>
                            </td>
                            <td id="disBlabel" colspan="2" style="height: 20px;">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
	                            <tr><td><input type="radio" name="jishensunshi" id="tractorFrom_jishensunshi100" value="100" class="radio_c"><label for="tractorFrom_jishensunshi100">机身损失保费(元):100</label></td></tr>
	                            <tr><td><input type="radio" name="jishensunshi" id="tractorFrom_jishensunshi150" value="150" class="radio_c"><label for="tractorFrom_jishensunshi150">机身损失保费(元):150</label></td></tr>
	                            <tr><td><input type="radio" name="jishensunshi" id="tractorFrom_jishensunshi200" value="200" class="radio_c"><label for="tractorFrom_jishensunshi200">机身损失保费(元):200</label></td></tr>
	                            <tr><td><input type="radio" name="jishensunshi" id="tractorFrom_jishensunshi300" value="300" class="radio_c"><label for="tractorFrom_jishensunshi300">机身损失保费(元):300</label></td></tr>
	                            <tr><td><input type="radio" name="jishensunshi" id="tractorFrom_jishensunshi400" value="400" class="radio_c"><label for="tractorFrom_jishensunshi400">机身损失保费(元):400</label></td></tr>                           
                            </table>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;" class="bcxe">补偿限额：12000元</span>
                            </td>
                        </tr>
                        <!--3-->
                        <tr>
                            <td rowspan="2" style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                	<input type="checkbox" id="jiaShiRenYiWaiShangHaiBaoFei" name="jiaShiRenYiWaiShangHaiBaoFei" value="100">
                                </span>
                            </td>
                            <td rowspan="2" colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">③ 驾驶人意外伤害</span>
                            </td>
                            <td rowspan="2" colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）：100</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">身故、残疾补偿限额：20000元</span>
                            </td>

                        </tr>
                        <tr>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">医疗费用补偿限额：7000元</span>
                            </td>
                        </tr>
                        <!--4-->
                        <tr>
                            <td rowspan="3" style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                	<input type="checkbox" id="diSanZheZeRenBaoFei" name="diSanZheZeRenBaoFei" value="250">
                                </span>
                            </td>
                            <td rowspan="3" colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">④ 第三者责任</span>
                            </td>
                            <td rowspan="3" colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）：250</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">身故补偿限额：70000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">医疗补偿限额：8000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">财产损失补偿限额：2000元</span>
                            </td>
                        </tr>
                        <!--5-->
                        <tr>
                            <td style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                	<input type="checkbox" id="nongJiHuoTuoCheBaoFei" name="nongJiHuoTuoCheBaoFei" value="50">
                                </span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">⑤ 特约：农具或拖车</span>
                            </td>
                            <td colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）:50</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">补偿限额：6000元</span>
                            </td>
                        </tr>
                        <!--6-->
                        <tr>
                            <td style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                    <input type="checkbox" id="boLiDanDuPoSuiBaoFei" name="boLiDanDuPoSuiBaoFei" value="50">
                                </span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">⑥ 附加：玻璃单独破碎</span>
                            </td>
                            <td colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）:50</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">补偿限额：2000元</span>
                            </td>
                        </tr>
                        <!--7-->
                        <tr>
                            <td style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                    <input type="checkbox" id="jiJuTuoLuoSunShiBaoFei" name="jiJuTuoLuoSunShiBaoFei" value="200">
                                </span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">⑦ 附加：机具脱落损失</span>
                            </td>
                            <td colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）:200</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">补偿限额：7000元</span>
                            </td>
                        </tr>
                        <!--8-->
                        <tr>
                            <td rowspan="2" style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                    <input type="checkbox" id="yunZhuanYiWaiShangHaiBaoFei" name="yunZhuanYiWaiShangHaiBaoFei" value="200">
                                </span>
                            </td>
                            <td rowspan="2" colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">⑧ 附加：运转意外伤害</span>
                            </td>
                            <td rowspan="2" colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）：200</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">身故、残疾补偿限额：50000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">医疗费用补偿限额：10000元</span>
                            </td>
                        </tr>
                        <!--9-->
                        <tr>
                            <td rowspan="2" style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                	<input type="checkbox" id="weiXiuBaoYangZuoYeBaoFei" name="weiXiuBaoYangZuoYeBaoFei" value="50">
                                </span>
                            </td>
                            <td rowspan="2" colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">⑨ 特约：维修保养作业</span>
                            </td>
                            <td rowspan="2" colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）：50</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">身故、残疾补偿限额：10000元</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">医疗费用补偿限额：5000元</span>
                            </td>
                        </tr>
                        <!--10-->
                        <tr>
                            <td style="width: 5%; text-align: center; height: 20px;">
                                <span style="display: inline-block;">
                                    <input type="checkbox" id="buJiMianBuLvBaoFei" name="buJiMianBuLvBaoFei" value="100">
                                </span>
                            </td>
                            <td colspan="3" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">⑩ 附加：不计免补率</span>
                            </td>
                            <td colspan="2" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">保费（元）:100</span>
                            </td>
                            <td colspan="6" style="height: 20px;">
                                <span style="display: inline-block; margin-left: 10px;">会员每次事故承担责任免补额：0元/次</span>
                            </td>
                        </tr>
                        <!--尾部-->
                        <tr>
                            <td colspan="3" style="height: 20px; width: 10%;">
                                <span style="display: inline-block; margin-left: 10px;">积分抵扣（元）</span>
                            </td>
                            <td colspan="9" style="height: 20px; width: 15%;">
                                <span style="display: inline-block; text-align:left;">
                                	<input type="text" id="totalDikouMoney" name="totalDikouMoney" class="form-control input-sm" maxlength="15" onblur="javascript:isNumeric(this.value)">
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12">
                                <span style="display:block; float:left; margin-left: 10px; width: 10%;">保费合计大写金额：</span>
                                <span style="display:block; float:left; margin-left: 10px; width: 15%;" class="left_txt2" id="formRmb">人民币零元整</span>
                                <span style="display:block; float:left; margin-left: 10px; width: 10%;">小写：</span>
                                <span style="display:block; float:left; margin-left: 10px; width: 15%;" class="left_txt2" id="totalMoney">0</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12">
                                <span style="display: inline-block; margin-left: 10px; width: 10%;">互保期限：</span>
                                <span style="display: inline-block; margin-left: 10px; width: 15%;">
                                    <input type="text" id="sdate" name="insuranceStartStr" class="form-control input-sm required">
                                </span>
                                <span style="display: inline-block; margin-left: 10px; width: 5%;">零时起</span>
                                <span style="display: inline-block; margin-left: 10px; width: 15%;">
                                    <input type="text" id="edate" name="insuranceEndStr" class="form-control input-sm required">
                                </span>
                                <span style="display: inline-block; margin-left: 10px;">二十四时止</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <c:if test="${!cannotModify}">
                            <td width="50%" height="30" align="right">
                            	<input type="submit" class="btn btn-success" name="saveBtn" value="保存">
                            </td>
                            <td width="6%" height="30" align="right">&nbsp;</td>
                            <td width="44%" height="30">
                            	<input type="button" class="btn btn-default" value="取消" onclick="resetForm()">
                            </td>
                            </c:if>
                            <c:if test="${flag == 1}">
                            <div class="but_bottom">
                                <a href="javascript:;" class="charging" data="${tractor.insuranceTractorId}">
                                	<button type="button" class="btn btn-primary btn-sm">刷卡收费</button>
                                </a>
                                <c:if test="${isAdmin == 'Y'}">
                                <a href="javascript:;" class="charging_xianjin" data="${tractor.insuranceTractorId}">
                                    <button type="button" class="btn btn-info btn-sm">现金收费</button>
                                </a>
                                </c:if>
                            </div>
                            </c:if>
                            <c:if test="${flag == 2}">
                            <a href="javascript:;" class="print" data="${tractor.insuranceTractorId}">
                            	<button type="button" class="btn btn-primary btn-sm">打印</button>
                            </a>
                            </c:if>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>

<style type="text/css">
* {
    padding: 0px;
    margin: 0px;
    font-family: "宋体";
    font-size: 12px;
}

.print {
    display: block;
    text-align: center;
    margin-top: 10px;
}

.charging, .charging_xianjin {
    display: block;
    float: left;
    margin-top: 10px;
    margin: 0 10px;
}

.charging input[ type='button'] {
    width: 60px;
    height: 25px;
    background: #E2E2E2;
}

table {
    table-layout: fixed;
}

.but_bottom {
    text-align: center;
    margin: 10px auto;
    width: 165px;
}
</style>

<script>
/**
 * jQuery初始化
 */
$(function () {
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);

    $("#sdate").datepicker(
            {
                onSelect: function (dateText, inst) {
                    var date = new Date(dateText).getTime();
                    var year = parseInt(dateText.split("-")[0]);
                    var month = parseInt(dateText.split("-")[1]);
                    if (0 == year % 4 && (year % 100 != 0 || year % 400 == 0) && month < 3) {
                        date = date + 31536000000;//365
                    }
                    else {
                        date = date + (31536000000 - (31622400000 - 31536000000));//364
                    }
                    var newDate = new Date(date);
                    $("#edate").attr("readonly", "readonly");
                    $("#edate").attr("value", getSmpFormatDate(newDate));
                },
                dateFormat: 'yy-mm-dd',
                changeYear: true,
                changeMonth: true,
                readOnly: true,
                minDate: new Date(new Date().getTime() + (31622400000 - 31536000000))
            });
    $("#edate").datepicker(
            {
                dateFormat: 'yy-mm-dd',
                changeYear: true,
                changeMonth: true,
                readOnly: true,
                minDate: new Date(new Date().getTime() + (31622400000 - 31536000000))
            });
    $("#buyDate").datepicker(
            {
                dateFormat: 'yy-mm-dd',
                changeYear: true,
                changeMonth: true
            });
    $("#regDate").datepicker(
            {
                dateFormat: 'yy-mm-dd',
                changeYear: true,
                changeMonth: true
            });

    $("#huBaoZuHeZongBaoFei").click(function () {
        updateTotal();
    });
    $("#huBaoZuHeZongBaoFei").ready(function () {
        updateTotal();
    });
    $("#jiShenSunShiBaoFei").click(function () {
        if ($("#jiShenSunShiBaoFei")[0].checked) {
            $(".radio_c").eq(0).attr("checked", 'checked');
        }
        else {
            $(".radio_c").attr("checked", false);
        }
        updateTotal();
    });
    $("#jiShenSunShiBaoFei").ready(function () {
        updateTotal();
    });
    $("#jiaShiRenYiWaiShangHaiBaoFei").click(function () {
        updateTotal();
    });
    $("#jiaShiRenYiWaiShangHaiBaoFei").ready(function () {
        updateTotal();
    });
    $("#diSanZheZeRenBaoFei").click(function () {
        updateTotal();
    });
    $("#diSanZheZeRenBaoFei").ready(function () {
        updateTotal();
    });
    $("#nongJiHuoTuoCheBaoFei").click(function () {
        updateTotal();
    });
    $("#nongJiHuoTuoCheBaoFei").ready(function () {
        updateTotal();
    });

    $("#boLiDanDuPoSuiBaoFei").click(function () {
        updateTotal();
    });
    $("#boLiDanDuPoSuiBaoFei").ready(function () {
        updateTotal();
    });
    $("#jiJuTuoLuoSunShiBaoFei").click(function () {
        updateTotal();
    });
    $("#jiJuTuoLuoSunShiBaoFei").ready(function () {
        updateTotal();
    });
    $("#yunZhuanYiWaiShangHaiBaoFei").click(function () {
        updateTotal();
    });
    $("#yunZhuanYiWaiShangHaiBaoFei").ready(function () {
        updateTotal();
    });
    $("#weiXiuBaoYangZuoYeBaoFei").click(function () {
        updateTotal();
    });
    $("#weiXiuBaoYangZuoYeBaoFei").ready(function () {
        updateTotal();
    });
    $("#buJiMianBuLvBaoFei").click(function () {
        updateTotal();
    });
    $("#buJiMianBuLvBaoFei").ready(function () {
        updateTotal();
    });
    $("#totalDikouMoney").change(function () {
        updateTotal();
    });
    $("#totalDikouMoney").ready(function () {
        updateTotal();
    });
    $(".radio_c").eq(0).click(function () {
        $(".bcxe").text("补偿限额：12000元");
        updateTotal();
    });
    $(".radio_c").eq(1).click(function () {
        $(".bcxe").text("补偿限额：19000元");
        updateTotal();
    });
    $(".radio_c").eq(2).click(function () {
        $(".bcxe").text("补偿限额：26000元");
        updateTotal();
    });
    $(".radio_c").eq(3).click(function () {
        $(".bcxe").text("补偿限额：42000元");
        updateTotal();
    });
    $(".radio_c").eq(4).click(function () {
        $(".bcxe").text("补偿限额：58000元");
        updateTotal();
    });
    $("#owner").blur(function () {
        $("#secondDirverName").attr("value", $(this).val());
    });
    $("#idCard").blur(function () {
        $("#secondDriverLience").attr("value", $(this).val());
    });
	var editMode = '${editMode}';
	if('true' == editMode) {
		getTljHis(editMode);
	}
})

/**
 * 验证提交信息
 */
function validateForm() {
    if ($("#idCard").val().length == 18) {
        if (!checkIdCard("idCard")) {
            warn("身份证号为空或不合法");
            return false;
        }
    }
    else {
        warn("身份证号（社会信用代码）不合法");
        return false;
    }
    if (!checkMobilephone("telNum")) {
        warn("电话为空或不合法");
        return false;
    }
    if ($("#nongJiHuoTuoCheBaoFei").attr("checked") == true || $("#weiXiuBaoYangZuoYeBaoFei").attr("checked") == true) {
        if ($("#huBaoZuHeZongBaoFei").attr("checked") == false && $("#jiShenSunShiBaoFei").attr("checked") == false) {
            warn("请选择组合总保费或者机身损失");
            return false;
        }
        else if (!checkEmpty("sdate") || !checkEmpty("edate")) {
            warn("互助日期不能为空");
            return false;
        }
    }
    return true;
}

function isNumeric(value) {
    if (value == null || value == '' || value.length <= 0) {
        return true;
    }
    var patrn = /^(-|\+)?\d+(\.\d+)?$/;
    if (!patrn.test(value)) {
        warn(value + "必须为数字");
    }
}

function updateTotal() {
    var m1 = $("#huBaoZuHeZongBaoFei").val();
    var m2 = $(".radio_c:checked").val();
    var m3 = $("#jiaShiRenYiWaiShangHaiBaoFei").val();
    var m4 = $("#diSanZheZeRenBaoFei").val();
    var m5 = $("#nongJiHuoTuoCheBaoFei").val();
    var m6 = $("#boLiDanDuPoSuiBaoFei").val();
    var m7 = $("#jiJuTuoLuoSunShiBaoFei").val();
    var m8 = $("#yunZhuanYiWaiShangHaiBaoFei").val();
    var m9 = $("#weiXiuBaoYangZuoYeBaoFei").val();
    var m10 = $("#buJiMianBuLvBaoFei").val();
    var m11 = $("#totalDikouMoney").val();

    if (!$("#huBaoZuHeZongBaoFei")[0].checked) m1 = 0;
    if (!$("#jiShenSunShiBaoFei")[0].checked) m2 = 0;
    if (!$("#jiaShiRenYiWaiShangHaiBaoFei")[0].checked) m3 = 0;
    if (!$("#diSanZheZeRenBaoFei")[0].checked) m4 = 0;
    if (!$("#nongJiHuoTuoCheBaoFei")[0].checked) m5 = 0;
    if (!$("#boLiDanDuPoSuiBaoFei")[0].checked) m6 = 0;
    if (!$("#jiJuTuoLuoSunShiBaoFei")[0].checked) m7 = 0;
    if (!$("#yunZhuanYiWaiShangHaiBaoFei")[0].checked) m8 = 0;
    if (!$("#weiXiuBaoYangZuoYeBaoFei")[0].checked) m9 = 0;
    if (!$("#buJiMianBuLvBaoFei")[0].checked) m10 = 0;

    var m = Number(m1) + Number(m2) + Number(m3) + Number(m4) + Number(m5)
            + Number(m6) + Number(m7) + Number(m8) + Number(m9) + Number(m10) - Number(m11);
    $("#totalMoney").html(m);
    $("#formRmb").html(convertCurrency(m));
}

function getSmpFormatDate(date) {
    var pattern = "yyyy-MM-dd";
    return date.format(pattern);
}
Date.prototype.format = function (format) {
    var o =
    {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
// 保存提交
function saveForm() {
    if (!$("#formTractor").valid()) {
        warn("保存失败：表单信息填写不完整！");
        return false;
    }

    if (!validateForm()) {
        return false;
    }
    return true;
}

// 保单重置
function resetForm() {
    $("#formTractor")[0].reset();
    $("#secondDirverName").attr("value", "");
}
</script>

</html>