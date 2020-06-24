function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/settlement/list' ,
    datatype: "json",
    colModel: [

        {label: 'id', name: 'id', index: 'id', width: 10, key: true, hidden: true},
        {label: '合同id', name: 'contractId', index: 'contract_id', width: 140, align: 'center',hidden:true},
        {label: '合同编号', name: 'contractCode', index: 'contract_code', width: 140,align: 'center'},
        {label: '合同名称', name: 'contractName', index: 'contract_name', width: 100},
        {label: '采购部门', name: 'purchasingDeptName', index: 'dept1_name', width: 140, align: 'center'},
        {label: '需求部门', name: 'demandDeptName', index: 'demand_dept_name', width: 100},
        {label: '乙方单位', name: 'supplierName', index: 'supplier_name', width: 100},
        {label: '合同类型', name: 'contractType', index: 'contract_type', width: 100},
        {label: '付款情况', name: 'payStatus', index: 'pay_status', width: 100,
        formatter:function (cellValue, options, rowData) {
            var val = "";
            var payStatus = rowData["payStatus"];
            if (payStatus == "0") {
                val = "已支付";
            } else if (payStatus == "1") {
                val = "正在支付";
            }else if(payStatus="2") {
                val = "未支付"
            }
            return val;
        }
        },
        {label: '合同金额', name: 'contractAmount', index: 'contract_amount', width: 100},
        {label: '合同未付款', name: 'unPayAmount', index: 'un_pay_amount', width: 100,
            formatter:function (cellValue, options, rowData) {
                var val = "";
                var unPayAmount = rowData["unPayAmount"];
                if (unPayAmount == null) {
                    val = "已支付完成";
                    return val;
                }else {
                    return unPayAmount;
                }
            }},

        {label: '备注', name: 'remarks', index: 'remarks', width: 100,align: 'center'},
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId = rowData["contractId"];
                var contractName = rowData["contractName"];
                var contractCode = rowData["contractCode"];
                return "<a class='btn btn-primary' onclick='settlement(\""+contractId+"\",\""+contractName+"\",\""+contractCode+"\")' '>结算</a>"


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

function settlement(contractId,contractName,contractCode){

vm.passSettlement(contractId,contractName,contractCode)

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
var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: 1,
        title: null,
        opName: "",
        contractCode:"",
        contractName:"",
        finUser: {
            orgId: "",
            orgName: "",
            userFlag: 1
        },
        contract:{
            contractName:null,
            startDate:null,
            endDate:null,
            contractManager:null,
            contractAmount:null,
            purchaseContent:null,
            demandDeptId:null,
            contractType:null,
            contractFile:null,
            partyAId:null,
            partyBId:null,
            paymentType:null,
            contractCode:null
        },
        settlement:{

        },
        contractSearch:{
            contractName:null,
            contractCode:null,
            supplierName:"",
            contractManager:""


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
        querySett: function () {
            this.reload();
        },
        //重置
        clearSett: function () {
            this.contractSearch = {
                contractName:"",
                contractCode:"",
                supplierName:"",
                contractManager:""
            };
            this.reload();
        },
        //结算
        passSettlement: function (contractId,contractName,contractCode) {
            this.showList = 2;
            this.title = "结算";
            this.contractName = contractName;
            this.contractCode = contractCode;
            this.opName = "settlement";
            this.settlement = {
                contractId:contractId,
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


        //新增或修改
        saveOrUpdate: function (event) {
            var url = "contract/settlement/save" ;
            var _this = this;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.settlement),
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
                "contractName":this.contractSearch.contractName,
                "contractCode":this.contractSearch.contractCode,
                "supplierName":this.contractSearch.supplierName,
                "contractManager":this.contractSearch.contractManager
            };
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