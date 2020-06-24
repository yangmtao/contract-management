function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/list' ,
    datatype: "json",
    colModel: [

        {label: 'contractId', name: 'contractId', index: 'contract_id', width: 10, key: true, hidden: true},
        {label: '合同名称', name: 'contractName', index: 'contract_name', width: 140, align: 'center'},
        {label: '合同编号', name: 'contractCode', index: 'contract_code', width: 140,align: 'center',hidden: true},
        {label: '申请人', name: 'contractManagerName', index: 'contract_manager_name', width: 100,align: 'center'},
        {label: '申请部门', name: 'demandDeptName', index: 'demand_dept_name', width: 140, align: 'center'},
        {label: '申请时间', name: 'createDate', index: 'create_date', width: 160, align: 'center'},
        {
            label: '开始时间', name: 'startDate', index: 'start_date', width: 100, align: 'center'
        },
        {label: '结束时间', name: 'endDate', index: 'end_date', width: 100, align: 'center'},
        {label: '当前节点', name: 'nodeName', index: 'node_name', width: 140, align: 'center',
            formatter:function (cellValue, options, rowData) {
            var nodeName = rowData["nodeName"]
                return nodeName+"待审核"
            }
        },
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId = rowData["contractId"];
                var contractName = rowData["contractName"];
                var contractCode = rowData["contractCode"];
                return "<a class='btn btn-primary' onclick='review(\""+contractId+"\",\""+contractName+"\",\""+contractCode+"\")' '>办理</a>"


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

function review(contractId,contractName,contractCode){

    vm.getReview(contractId,contractName,contractCode)

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
        supplier:{
            blackList:1
        },
        finUser: {
            orgId: "",
            orgName: "",
            userFlag: 1
        },
        userReviewRecord:{

        },
        contract:{

        },
        contractSearch:{
            contractName:"",
            contractManagerName:""
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
        queryContract: function () {
            this.reload();
        },
        //重置
        clearContract: function () {
            this.contractSearch = {
                contractName:"",
                contractManagerName:""
            };
            this.reload();
        },
        //办理合同审核
        getReview:function(contractId,contractName,contractCode){
            this.showList = 2;
            this.title = "审核办理";
            this.contractName = contractName;
            this.contractCode = contractCode;
            this.contract = {
                contractId:contractId,
            };
            this.userReviewRecord = {
                contractId:contractId,
            }
            this.getInfo(contractId);


        },


        //新增或修改
        passOrReject: function (a) {
            var url = a == 1 ? "contract/userReviewRecord/save" : "contract/userReviewRecord/reject";
            var _this = this;

            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.userReviewRecord),
                success: function (r) {
                    if (r.code === 1) {
                        alert('操作成功', function (index) {
                            window.location.reload();
                        });
                    } else {
                        alert("失败");
                    }
                }
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
        getInfo: function (contractId) {
            var _this = this;
            $.get(baseURL + "contract/contractInfo/" + contractId, function (r) {
                _this.contract=r.contract
            });
        },

        //搜索函数
        reload: function (event) {
            this.showList = 1;
            // var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            var postData = {
                "contractName":this.contractSearch.contractName,
                "contractManagerName":this.contractSearch.contractManagerName,
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