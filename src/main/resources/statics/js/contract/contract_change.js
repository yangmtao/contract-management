$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'contract/list',
        datatype: "json",
        colModel: [
            {label: '合同ID', name: 'contractId', index: "id", width: 40, key: true, hidden: true},
            {label: '合同编号', name: 'contractCode', width: 150, align: 'center',sortable:false},
            {label: '合同名称', name: 'contractName', width: 200, align: 'center',sortable:false},
            {label: '需求部门', name: 'demandDeptName',width: 120, align: 'center',sortable:false},
            {label: '采购内容', name: 'purchaseContent', width: 200, align: 'center',sortable:false},
            {label: '乙方单位', name: 'partyBName', index:"publish_date", width: 160, align: 'center'},
            {
                label: '合同类型', name: 'contractType', width: 120, align: 'center',sortable:false,
                formatter: function (value) {
                    return DICT.STATUS[value] || '';
                }
            },
            {label: '付款状态', name: 'payStatus', index: "create_date", width: 100, align: 'center',
                formatter: function (value) {
                    return DICT.PAYMENT_STATUS[value] || '';
                }
            },
            {label: '经办人', name: 'contractManagerName', index: "create_date", width: 100, align: 'center',sorttype:'date'},
            {label: '变更次数', name: 'changeTimes', index: "change_times", width: 100, align: 'center'},
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

Vue.use(VeeValidate);
VeeValidate.Validator.localize('zh_CN');

var vmContract = new Vue({
    el: '#rrapp',
    data: {

        formLabelWidth: '120px',
        closeOnClickModal:false,
        remoteManagerLoading:false,
        remotePartyBLoading:false,
        dialogFormVisible:false,
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
        contractChange:{
            changeName:null,
            changeReason:null,
            contractManager:null,
            contractAmount:null,
            paymentDate:null,
            contractId:null,
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
        showAddChange:function(){
            var id=getSelectedRow();
            if(id==null) {
                return;
            }else{
                this.contractChange.contractId=id;
                this.dialogFormVisible=true;
            }
        },
        cancleContractFile: function(){
            confirm("确定删除所选文件？",function(){
                return true;
            });
        },
        addContractChange:function(){

            if(this.files.length<1){
                alert("请先上传合同更改协议正文！");
                return;
            }
            var _this=this;
            var contract=_this.contractChange;
            for (item in contract){
                if(_this.errors.has(item)||contract[item]==null){
                    alert("请正确完整的录入合同信息！"+item+","+contract[item]);
                    return;
                }
            }
            contract.changeFile=this.files[0].url;;
            $.ajax({
                type: "POST",
                url: baseURL + "contract/change/add",
                contentType: "application/json",
                data: JSON.stringify(_this.contractChange),
                success: function (r) {
                    if (r.code === 1) {
                        alert('操作成功');
                        _this.reload();
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        contractFileSuccess: function(r){
            var file={name:r.imageName,url:r.url};
            this.files[0]=file;
            //this.files.push(file);
            alert("上传成功");
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
    STATUS: {0: '草稿', 1: '待审核', 2: '审核不通过', 3: '已发布'},
    PAYMENT_STATUS: {0: '未付', 1: '在付', 2: '已付'},
    CONTRACT_TYPE: {0: '买卖合同', 1: '供用电、水、气、热力合同', 2: '赠与合同', 3: '租赁合同',4:'承揽合同',5:'建设工程合同',6:'技术合同',7:'仓储合同',8:'委托合同',9:'房间合同'}
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