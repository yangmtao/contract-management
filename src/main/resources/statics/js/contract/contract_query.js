$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'contract/list',
        datatype: "json",
        colModel: [
            {label: '合同ID', name: 'contractId', index: "id", width: 40, key: true, hidden: true},
            {label: '合同编号', name: 'contractCode', width: 120, align: 'center',sortable:false},
            {label: '合同名称', name: 'contractName', width: 200, align: 'center',sortable:false},
           {label: '采购内容', name: 'purchaseContent', width: 200, align: 'center',sortable:false},
            {label: '需求部门', name: 'demandDeptId',width: 120, align: 'center',sortable:false},
            {label: '乙方单位', name: 'partyBId', index:"publish_date", width: 160, align: 'center'},
            {
                label: '合同类型', name: 'contractType', width: 120, align: 'center',sortable:false,
                formatter: function (value) {
                    return DICT.STATUS[value] || '';
                }
            },
            {label: '付款状态', name: 'payStatus', index: "create_date", width: 100, align: 'center',sorttype:'date'},
            {label: '合同金额', name: 'contractAmount', width: 120, align: 'center',sortable:false,},
            {label: '开始时间', name: 'startDate', index: "create_date", width: 120, align: 'center',sorttype:'date'},
            {label: '结束时间', name: 'endDate', index: "create_date", width: 120, align: 'center',sorttype:'date'},
            {label: '经办人', name: 'contractManager', index: "create_date", width: 100, align: 'center',sorttype:'date'},
            {label: '操作',  width: 110, align: 'center',sortable:false,
                formatter: function (value) {
                    return '<a class="btn btn-primary" href="/cmsContent/view/'+value+'" target="_blank">查看详情</a>';
                }}
        ],
        viewrecords: true,
        width: 1200,
        height: 385,
        rowNum: 20,
        rowList: [50, 100, 500],
        rownumbers: true,
        rownumWidth: 45,
        autowidth: true,
        sortName:'createDate',
        sortable:true,
        shrinkToFit: false,
        multiselect: true,
        pager: "#jqGridPager",
        styleUI: 'Bootstrap',
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
        }
    });
});
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;





var area_ztree;
var area_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: 0
        },
        key: {
            url: "nourl"
        }
    },
    check: {
        enable: true,
        nocheckInherit: true
    }

};
var setting1 = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: 0
        },
        key: {
            url: "nourl"
        }
    },
    callback: {
        onDblClick: function (event, treeId, node) {
            vmFinSysUser.user.areaId = node.id;
            vmFinSysUser.user.areaName = node.joinname;
            layer.closeAll();
        }
    }
};

var vmContract = new Vue({
    el: '#rrapp',
    data: {

        formLabelWidth: '120px',
        closeOnClickModal:false,
        remoteManagerLoading:false,
        remotePartyBLoading:false,
        contractManager:null,
        remotePartyB:null,
        title: null,
        files:[],
        contract:{
            contractName:null,
            startDate:null,
            endDate:null,
            contractManager:null,
            contractAmount:null,
            purchaseContent:null,
            purchasingDeptId:null,
            demandDeptId:null,
            contractType:null,
            contractFile:null,
            partyAId:null,
            partyBId:null,
            paymentType:null,
            contractCode:null
        },
        remoteManagers:[],
        remotePartyBs:[],
        tableData: [],
        dept: {
            purchasingDeptName: null,
            demandDeptName: null
        }
    },
    mounted: function () {
        //加载部门树
        $.get(baseURL + "sys/dept/select", function (r) {
            ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
        })
    },
    methods: {
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/select", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
            })
        },
        getRemotePartyB:function(){
            var _this=this;
            if(keyword!=''){
                _this.remotePartyBLoading=true;

                setTimeout(function(){
                    _this.remotePartyBLoading=false;
                    $.ajax({
                        type:"GET",
                        data:{
                            keyword:keyword
                        },
                        url:baseURL+"sys/user/userDept",
                        success:function(r){
                            if(r.code===1){
                                _this.remotePartyBs=r.managers;
                            }
                        }
                    });
                },200);


            }else{
                _this.remotePartyBs=[];
            }
        },
        deptTree: function (type) {
            var _this = this;
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    if(type=='purchase'){
                        _this.dept.purchasingDeptName = node[0].name;
                        _this.contract.purchasingDeptId=node[0].deptId+'';
                    }else{
                        _this.dept.demandDeptName=node[0].name;
                        _this.contract.demandDeptId=node[0].deptId+'';
                    }

                    layer.close(index);
                }
            });
        },
        remoteManager:function(keyword){
            var _this=this;
            if(keyword!=''){
                _this.remoteManagerLoading=true;

                setTimeout(function(){
                    _this.remoteManagerLoading=false;
                    $.ajax({
                        type:"GET",
                        data:{
                            keyword:keyword
                        },
                        url:baseURL+"sys/user/userDept",
                        success:function(r){
                            if(r.code===1){
                                _this.remoteManagers=r.managers;
                            }
                        }
                    });
                },200);


            }else{
                _this.remoteManagers=[];
            }

        },
        queryContract: function(){
            console.log("正在查找");
        },
        reload: function () {
            window.location.reload();
        }


    }
});

DICT = {
    STATUS: {0: '草稿', 1: '待审核', 2: '审核不通过', 3: '已发布'}
}
Vue.prototype.DICT = DICT;

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}
//重写alert
window.alert = function (msg, callback) {
    parent.layer.alert(msg, function (index) {
        parent.layer.close(index);
        if (typeof(callback) === "function") {
            callback("ok");
        }
    });
}
//重写confirm式样框
window.confirm = function (msg, callback, btn2) {
    parent.layer.confirm(msg, {btn: ['确定', '取消']},
        function (index) {//确定事件
            if (typeof(callback) === "function") {
                parent.layer.close(index);
                callback("ok");

            }
        }, function () {
            btn2 && btn2();

        });
}

window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};