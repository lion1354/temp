<%@include file="/header.jsp" %>

<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%@include file="/common.jsp" %>
    <link rel="stylesheet" href="${ROOT}/js/jquery/treeTable/jquery.treetable.css">
    <script src="${ROOT}/js/jquery/treeTable/jquery.treetable.js"></script>
		<!-- Forp通用css和js补丁 -->
    <link rel="stylesheet" href="${ROOT}/css/common.css"/>
    <script src="${ROOT}/js/common.js"></script>
    <script src="${ROOT}/js/business.js"></script>
</head>

<body>
<div id='divToolbar' style='margin-bottom: 5px;'>
    <button class='btn btn-primary' onclick='edit();'><i class="fa fa-pencil"></i>&nbsp;修改</button>
</div>

<input id="selectTreeRow" type="hidden"/>
<div id='divRegion' style="border: solid 1px lightgray; overflow: auto;">
	<table id="treeDept" class="table table-hover"></table>
</div>

<div class="hidden">
	<form id='formDept' class='form-horizontal' action='' method='post'>
		<table width='100%'>
			<tr>
				<td width='140' align='right'>地区名称：</td>
				<td><input name='name' class='form-control input-sm' disabled></td>
			</tr>

			<tr>
				<td align='right'>积分抵扣金额比例：</td>
				<td><input name='bonus' class='form-control input-sm digits' maxlength='6'></td>
			</tr>
		</table>
	</form>
</div>
</body>

<script>
var treeStore;

//jqery 初始化方法
$(function ()
{
	// 设置右侧列表高度
	$('#divRegion').height($('body')[0].scrollHeight - 50);
	loadTree($RootDeptNodeNo);
});

    function loadTree(nodeNo) {
        //初始化 tr
        var heads = ["地区", "积分抵扣金额比例"];
        //初始化父节点

        var initNode = {
            id: $RootDeptNodeNo,
            leaf: false,
            pId: 0,
            name: "<i class='fa fa-home'>&nbsp;&nbsp;</i>陕西省",
            td: []
        };
        var tNodes = [initNode];

        $.ajax({
            url: '${ROOT}/bonus/get-region',
            success: function (rsp, status) {
                treeStore = new Map();
                treeStore.set($RootDeptNodeNo, initNode);
                $.each(rsp, function (index, value) {
                    var cell = {
                        id: rsp[index].nodeNo,
                        leaf: rsp[index].leaf,
                        pId: rsp[index].parentNodeNo,
                        rename: rsp[index].name,
                        bonus: rsp[index].bonus,
                        reId: rsp[index].id,
                        name: "<i class='fa fa-folder'>&nbsp;&nbsp;</i>" + rsp[index].name,
                        td: [rsp[index].bonus]
                    };

                    treeStore.set(rsp[index].nodeNo, cell);
                    tNodes.push(cell);
                });
                $.ForpTreeTable("treeDept", heads, tNodes);
                $('#treeDept').treetable('reveal', nodeNo);
                // Highlight selected row
                $("#treeDept tbody tr").mousedown(function () {
                    $("tr.selected").removeClass("selected");
                    $(this).addClass("selected");
                    $("#selectTreeRow").val($(this).attr("data-tt-id"));
                });


            }
        });
        $("#selectTreeRow").val("");

    }


    var deptDlg = null;
    /**
     * 初始化Form对话框
     */
    function initFormDialog(id) {
        if (null == deptDlg) {
            deptDlg = iDialog(
                    {
                        content: $('#formDept')[0], lock: true, effect: 'i-super-scale', width: 500,
                        btn: {ok: {val: '保存', type: 'green', click: saveDept}, cancle: {val: '取消'}}
                    });

            // 初始化校验规则
            $("#formDept").validate();
            // Ajax Form提交
            $('#formDept').submit(saveDept);
        }

        deptDlg.$title.html('<i class="fa fa-pencil"></i>&nbsp;修改');
        $("#formDept")[0].reset();
        $('#formDept').attr('action', '${ROOT}/bonus/bonus/' + id);

        deptDlg.show();
        $("#formDept").clearValidate();
    }

    // 保存提交
    function saveDept() {
        if (!$("#formDept").valid()) {
            warn('保存失败：表单信息填写不完整！');
            return false;
        }

        $('#formDept').ajaxSubmit(
                {
                    success: function (rsp) {
                        if (rsp.success) {
                            info('地区“' + $('input[name="name"]').val() + '”保存成功！');

                            // 刷新
                            loadTree($("#selectTreeRow").val());

                            deptDlg.hide();

                        }
                        else
                            error(rsp.msg);
                    }
                });

        // 取消默认submit方式 && 不关闭对话框
        return false;
    }


    /**
     * 修改
     */
    function edit() {
        var r = $("#selectTreeRow").val();
        if (!r) {
            warn('请先选择您要修改的区县！', function () {
            });
            return;
        }
        // 根节点不允许修改
        var parentId = $("#treeDept tbody tr.selected").attr("data-tt-parent-id");
        if (!parentId) {
            warn('省市禁止修改！');
            return;
        }
        
        var isLeaf = treeStore.get(r).leaf;
        if(isLeaf) 
        {
        	warn('省市禁止修改！');
            return;
        }

        initFormDialog(treeStore.get(r).reId);
        $('input[name="name"]').val(treeStore.get(r).rename);
        $('input[name="bonus"]').val(treeStore.get(r).bonus);

    }
</script>

</html>