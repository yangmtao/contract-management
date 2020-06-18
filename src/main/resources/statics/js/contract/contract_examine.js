$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'contract/examine/list',
        datatype: "json",
        colModel: [
            {label: '合同ID', name: 'contractId', index: "id", width: 20, key: true, hidden: true},
            {label: '合同编号', name: 'contractCode', width: 120, align: 'center',sortable:false},
            {label: '合同名称', name: 'contractName', width: 200, align: 'center',sortable:false},
           {label: '严重等级', name: 'purchaseContent', width: 100, align: 'center',sortable:false},
            {label: '审查问题', name: 'demandDeptId',width: 180, align: 'center',sortable:false},
            {label: '修正状态', name: 'partyBId', index:"publish_date", width: 100, align: 'center'},
            {label: '解决方案', name: 'payStatus', index: "create_date", width: 180, align: 'center',sorttype:'date'},
            {label: '提出人', name: 'contractAmount', width: 120, align: 'center',sortable:false,},
            {label: '提出时间', name: 'startDate', index: "create_date", width: 120, align: 'center',sorttype:'date'},
            {label: '处理人', name: 'demandDeptId',width: 120, align: 'center',sortable:false},
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

var vmContractExamine = new Vue({
    el: '#rrapp',
    data: {

        queryOption:{
            contractName:null,
            startDate:null,
            endDate:null,
            riskLevel:null,
            contractCode:null
        }
    },
    methods: {



        queryExamine: function(){
            console.log("正在查找");
        },
        reload: function () {
            window.location.reload();
        }


    }
});

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