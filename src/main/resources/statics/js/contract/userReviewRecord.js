function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/userReviewRecord/list' ,
    datatype: "json",
    colModel: [

        {label: 'Id', name: 'Id', index: 'id', width: 10, key: true, hidden: true},
        {label: '合同id', name: 'contractId', index: 'contract_id', width: 140, align: 'center', hidden: true},
        {label: '合同名称', name: 'contractName', index: 'contract_name', width: 140, align: 'center'},
        {label: '审核建议', name: 'reviewAdvise', index: 'review_advise', width: 100},
        {label: '审核结果', name: 'reviewResult', index: 'review_result', width: 140, align: 'center'},
        {label: '审核人', name: 'reviewer', index: 'reviewer', width: 100},
        {
            label: '开始时间', name: 'startDate', index: 'start_date', width: 80, hidden: true
        },
        {label: '结束时间', name: 'endDate', index: 'end_date', width: 180, align: 'center'},
        {label: '当前节点', name: 'nodeName', index: 'node_name', width: 140, align: 'center'},
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId  = rowData["contractId"];
                return "<a class='btn btn-primary' onclick='contractDetails(\""+contractId+"\")' '>合同详情</a>"


            }
        },
    ],
    viewrecords: true,
    height: 385,
    rowNum: 20,
    rowList: [20, 30, 40, 50],
    rownumbers: true,
    rownumWidth: 25,
    autowidth: true,
    shrinkToFit: false,
    multiselect: true,
    pager: "#jqGridPager",
    jsonReader: {
        root: "page.list",
        page: "page.currPage",
        total: "page.totalPage",
        records: "page.totalCount"
    },
    prmNames: {
        page: "page",
        rows: "limit",
        order: "order"
    },
    gridComplete: function () {
        //隐藏grid底部滚动条
        // $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
});}
$(function () {
    jqGrid();
});

function contractDetails(i,node){


}

var ztree;
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "orgId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var vmFinUser = new Vue({
    el: '#rrapp',
    data: {
        showList: 1,
        title: null,
        opName: "",
        supplier:{
            blackList:1
        },
        reviewSearch:{
            contractName:"",
            reviewer:""
        },
        finUser: {
            orgId: "",
            orgName: "",
            userFlag: 1
        },
        finUserSearch: {
            userName: "",
            realName: "",
            userStatus: "",
            orgId: "",
            orgName: "",
            createDateStart: "",
            createDateEnd: ""
        },
        supplierSearch:{
            supplierName:"",
            creditCode:""
        },
        userStatusList: [
            {code: 1, value: "启用"},
            {code: 2, value: "停用"},
            {code: 3, value: "注销"}
        ],
        operatingStatusList: [
            {code: 1, value: "营业"},
            {code: 2, value: "停业"}
        ],
        typeList: [
            {code: 1, value: "科技"},
            {code: 2, value: "教育"}
        ],
        oldPassword: "",
        newPassword: "",
        newPasswordSure: "",
        chooseUserId: ""
    },
    methods: {

        //搜索
        queryReview: function () {
            this.reload();
        },
        //重置
        clearReview: function () {
            this.reviewSearch ={
                contractName:"",
                    reviewer:""
            }
            this.reload();
        },
        //新增
        addSupplier: function () {
            this.showList = 2;
            this.title = "新增";
            this.opName = "add";
            this.supplier = {
                blackList:1
            };
        },

        //跳转详情页面
        supplierDetails:function(){
            var supplierId = getSelectedRow();
            if (supplierId == null) {
                return;
            }
            this.showList = 3;
            this.title = "详情";
            this.opName = "details";
            this.getInfo(supplierId);
            this.getContract(supplierId);
        },

        //跳转黑名单页面
        getBlackList:function(){
            this.$router.push({ path:'/blackList.html'  })
        },

        //跳转修改页面
        updateFinUser: function (event) {
            var supplierId = getSelectedRow();
            if (supplierId == null) {
                return;
            }
            this.showList = 2;
            this.title = "修改";
            this.opName = "update";
            this.getInfo(supplierId);
        },
        //新增或修改
        saveOrUpdate: function (event) {
            var url = this.supplier.supplierId == null ? "contract/supplier/save" : "contract/supplier/update";
            var _this = this;

            console.log("============")
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.supplier),
                success: function (r) {
                    if (r.code === 1) {
                        alert('操作成功', function (index) {
                            _this.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        //删除
        del: function (event) {
            var supplierIds = getSelectedRows();
            if (supplierIds == null) {
                return;
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "contract/supplier/delete",
                    contentType: "application/json",
                    data: JSON.stringify(supplierIds),
                    success: function (r) {
                        if (r.code == 1) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },

        handleFinUser: function (num) {
            var userIds = getSelectedRows();
            if (userIds == null) {
                return;
            }
            var ids = [];
            for (var i = 0; i < userIds.length; i++) {
                ids.push(userIds[i] + "--" + num);
            }
            var msg = "";
            if (num == 1) {
                msg = "确定启用所选客户的账户信息";
            } else if (num == 2) {
                msg = "确定停用所选客户的账户信息";
            } else if (num == 3) {
                msg = "确定注销所选客户的账户信息";
            }
            var _this = this;
            confirm(msg, function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "business/finuser/handleFinUser",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 1) {
                            alert(r.msg, function (index) {
                                _this.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },

        //得到所选id的信息
        getInfo: function (supplierId) {
            var _this = this;
            $.get(baseURL + "contract/supplier/info/" + supplierId, function (r) {
                _this.supplier = r.supplier;
                console.log(_this.supplier+"===================")
            });
        },

        //搜索函数
        reload: function (event) {
            this.showList = 1;
            // var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            var postData = {
                "contractName":this.reviewSearch.contractName,
                "reviewer":this.reviewSearch.reviewer,
            };
            console.log(postData.contractName)
            $("#jqGrid").jqGrid('setGridParam', {
                page: 1, "postData": postData
            }).trigger("reloadGrid");
        },
        finUserOrgTree: function (num) {
            var _this = this;
            this.getFinOrgInfoTreeData();
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择所属机构",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#finUserOrgInfoTreeLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    // console.log("node====", node, "==num==", num);
                    if (null != node) {
                        if (num == 1 || num == "1") {
                            _this.finUserSearch.orgId = node[0]["orgId"];
                            _this.finUserSearch.orgName = node[0]["orgName"];
                        } else if (num == 2 || num == "2") {
                            //选择上级菜单
                            _this.finUser.orgId = node[0]["orgId"];
                            _this.finUser.orgName = node[0]["orgName"];
                        }
                    }
                    layer.close(index);
                }
            });
        },
        checkStartDate: function () {
            if (this.finUserSearch.createDateStart) {
                if (this.finUserSearch.createDateEnd) {
                    var start = new Date((this.finUserSearch.createDateStart).replace(/-/g, '/'));
                    var end = new Date((this.finUserSearch.createDateEnd).replace(/-/g, '/'));
                    if (start.getTime() > end.getTime()) {
                        alert("起始时间不能大于截止时间！");
                        this.finUserSearch.createDateStart = '';
                        return;
                    }
                }

            }
        },
        checkEndDate: function () {
            if (this.finUserSearch.createDateEnd) {
                if (this.finUserSearch.createDateStart) {
                    var start = new Date((this.finUserSearch.createDateStart).replace(/-/g, '/'));
                    var end = new Date((this.finUserSearch.createDateEnd).replace(/-/g, '/'));
                    if (end.getTime() < start.getTime()) {
                        alert("截止时间不能小于起始时间！");
                        this.finUserSearch.createDateEnd = '';
                        return;
                    }
                }

            }
        },
        getFinOrgInfoTreeData: function () {
            var _this = this;
            //加载菜单树
            $.get(baseURL + "business/finorginfo/finOrgTreeList", function (r) {
                var arr = [];
                if (null != r["data"] && r["data"].length > 0) {
                    var total = r["data"].length;
                    for (var i = 0; i < total; i++) {
                        var obj = r["data"][i];
                        obj["name"] = r["data"][i]["orgName"];
                        arr.push(obj);
                    }
                }
                ztree = $.fn.zTree.init($("#finUserOrgInfoTree"), setting, arr);
                if (_this.finOrgInfo != null) {
                    var node = ztree.getNodeByParam("orgId", _this.finOrgInfo.parentId);
                    if (null != node) {
                        ztree.selectNode(node);
                        _this.finOrgInfo.parentName = node.orgName;
                    }
                }
            })
        }
    }
});

//加载页面
function read(){
    $("#jqGrid").jqGrid('setGridParam', {
        page: 1
    }).trigger("reloadGrid");
}

window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};