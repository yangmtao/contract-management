//初始化加载合同信息列表
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'contract/list',
        datatype: "json",
        colModel: [
            {label: '合同ID', name: 'contractId', index: "id", width: 40, key: true, hidden: true},
            {label: '合同编号', name: 'contractCode', width: 120, align: 'center',sortable:false},
            {label: '合同名称', name: 'contractName', width: 200, align: 'center',sortable:false},
            {label: '采购内容', name: 'purchaseContent', width: 200, align: 'center',sortable:false},
            {label: '需求部门', name: 'demandDeptName',width: 120, align: 'center',sortable:false},
            {label: '乙方单位', name: 'partyBName', index:"publish_date", width: 160, align: 'center'},
            {
                label: '合同类型', name: 'contractType', width: 120, align: 'center',sortable:false,
                formatter: function (value) {
                    return DICT.CONTRACT_TYPE[value] || '';
                }
            },
            {label: '付款状态', name: 'payStatus', index: "create_date", width: 100, align: 'center',
                formatter: function (value) {
                    return DICT.PAYMENT_STATUS[value] || '';
                }},
            {label: '合同金额', name: 'contractAmount', width: 120, align: 'center',sortable:false,},
            {label: '开始时间', name: 'startDate', index: "create_date", width: 120, align: 'center',sorttype:'date'},
            {label: '结束时间', name: 'endDate', index: "create_date", width: 120, align: 'center',sorttype:'date'},
            {label: '经办人', name: 'contractManagerName', index: "create_date", width: 100, align: 'center'},
            {label: '操作',  name: 'contractId', width: 110, align: 'center',sortable:false,
                formatter: function (value) {
                    return '<button class="btn btn-primary" onclick=showContractInfo('+value+')>查看详情</button>';
                }}
        ],
        viewrecords: true,
        width: 1200,
        height: 520,
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

//部门树设置
var userName;
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


var vmContract = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        formLabelWidth: '120px',
        closeOnClickModal:false,
        remoteManagerLoading:false,
        remotePartyBLoading:false,
        contractManager:null,
        remotePartyB:null,
        readonly:true,
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
            payStatus:null,
            startNumber:null,
            endNumber:null,
            paymentRange:null,
            contractCode:null,
            contractFile:null
        },
        paymentStages:[],
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
        //根据关键字查询相关乙方单位
        getRemotePartyB:function(keyword){
            var _this=this;
            if(keyword!=''){
                _this.remotePartyBLoading=true;
                console.log("正在查找相关单位")
                setTimeout(function(){
                    _this.remotePartyBLoading=false;
                    $.ajax({
                        type:"GET",
                        data:{
                            keyword:keyword
                        },
                        url:baseURL+"contract/supplier/simpleInfo",
                        success:function(r){
                            if(r.code===1){
                                _this.remotePartyBs=r.partyBs;
                            }
                        }
                    });
                },200);


            }else{
                _this.remotePartyBs=[];
            }
        },
        rateFormatter:function(row,column){
            return row.paymentRate+'%';
        },
        //显示部门树
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
        //根据关键字查询经办人（用户）
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
        //合同查询
        queryContract: function(){
            console.log("正在查找");
            var _this=this;
            if(_this.contract.startNumber > _this.contract.endNumber){
                alert("合同金额错误，范围起始值必须小于等于终止值");
                return;
            }
            _this.contract.paymentRange=_this.contract.startNumber+","+_this.contract.endNumber;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'contract':JSON.stringify(_this.contract)},
                page: page
            }).trigger("reloadGrid");
        },
        //导出为excel
        excelExport:function(){
            console.log("正在导出为excel");
            var ids = [];
            ids=getSelectedRows();
            var idStr='';
            if (ids == null) {
                return;
            }else{
                for(var i=0;i<ids.length;i++){
                    if((i+1)!=ids.length){
                        idStr+=(ids[i]+',');
                    }else{
                        idStr+=ids[i];
                    }

                }

            }

            window.location.href="/contract/export/excel?ids="+idStr;
        },

        reload: function () {
            window.location.reload();
        }


    }
});

//在当前页面显示合同详情
function showContractInfo(id) {
    $.ajax({
        type:"GET",
        data:{
            contractId:id
        },
        url:baseURL+"contract/detail",
        success:function(r){
            if(r.code===1){
                vmContract.contract=r.contract;
                vmContract.paymentStages=r.paymentStages;
                vmContract.showList=false;
            }else{
                alert("服务器异常，请联系系统管理员！");
            }
        }
    });

}


