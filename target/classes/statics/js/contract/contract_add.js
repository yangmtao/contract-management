/*$(function () {
    var dataList=[
        {   paymentStageId:'1',
            paymentStage:'阶段一',
            uses:'<select class="jqgridSelect" ><option></option></select>',
            paymentAmount:'<input class="jqgridInput">',
            paymentRate:'',
            paymentDate:'<input class="jqgridInput">'
        }];
    $("#jqGrid").jqGrid({
        datatype: "local",
        colModel: [
            {label: '付款阶段Id', name: 'paymentStageId',width: 80, key: true, hidden: true},
            {label: '付款阶段', name: 'paymentStage', width: 150,align: 'center'},
            {label: '用途', name: 'uses', width: 150, align: 'center'},
            {label: '付款金额', name: 'paymentAmount', width: 150, align: 'center'},
            {
                label: '付款比例', name: 'paymentRate', width: 115, align: 'center'
            },
            {
                label: '付款日期', name: 'paymentDate', sortable: false, width: 150, align: 'center'
            }
        ],
        viewrecords: true,
        height: 150,
        rowNum: 20,
        rowList: [50, 100, 500],
        autowidth: true,
        shrinkToFit: false,
        multiselect: false,
        pager: "#jqGridPager",
        styleUI: 'Bootstrap',
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        }
    });

    var localData = {page: 1, total: 0, records: dataList.length, rows: dataList};

    localData.total = (dataList.length % 5 == 0) ? (dataList.length / 5) : (Math.floor(dataList.length / 5) + 1);
    var reader = {
        root: function (obj) {
            return localData.rows;
        },
        page: function (obj) {
            return localData.page;
        },
        total: function (obj) {
            return localData.total;
        },
        records: function (obj) {
            return localData.records;
        }, repeatitems: false
    };
    $("#jqGrid").setGridParam({data: localData.rows, reader: reader}).trigger('reloadGrid');

});*/

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

VeeValidate.Validator.extend('normalText',{
    getMessage:function(n){return n+"只能包含中文、字母、数字"},
    validate: function(value){return !!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value) }
});

var vmContract = new Vue({
    el: '#rrapp',
    data: {

        dialogFormVisible:false,
        formLabelWidth: '120px',
        closeOnClickModal:false,
        remoteManagerLoading:false,
        remotePartyBLoading:false,
        remotePartyB:null,
        contractManager:null,
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
        paymentStage:{
            stageName: null,
            uses:null,
            paymentAmount:null,
            paymentRate:null,
            paymentDate: null
        },
        tableData: [],
        dept: {
            purchasingDeptName: null,
            demandDeptName: null
        },
        remoteManagers:[],
        remotePartyBs:[]
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
        showAddPaymentStage:function(){
          if(this.contract.contractAmount==null||this.contract.contractAmount=='') {
              alert("请先输入合同总金额");
          }else{
              this.dialogFormVisible=true;
          }
        },
        addPaymentStage:function(){
            var tempStage={};
            $.extend(tempStage,this.paymentStage);
            tempStage.paymentRate=((tempStage.paymentAmount/this.contract.contractAmount)*100).toFixed(2)+'%';
            this.tableData.push(tempStage);
          this.dialogFormVisible=false;
        },
        handleEdit:function(index, row) {
            console.log(index, row);
        },
        handleDelete:function(index, row) {
            console.log(index, row);
        },
        contractFileSuccess: function(r){
            var file={name:r.imageName,url:r.url};
            this.files.push(file);
          alert("上传成功");
        },
        cancleContractFile: function(){
            confirm("确定删除所选文件？",function(){
                return true;
            });
        },
        saveContract: function(){
            var _this = this;
            if(_this.files.length<1){
                alert("请上传合同附件");
                return;
            }
            var contract=_this.contract;
           contract.contractFile=_this.files[0].url;
            contract.paymentStage=JSON.stringify(this.tableData);
            contract.contractCode=new Date().getTime();
           var item;
           for (item in contract){
               if(contract[item]==null||contract[item]==''){
                   alert("请正确完整的录入合同信息！"+item);
                   return;
               }
           }
           var dataStr=JSON.stringify(contract);
           alert(dataStr);
            $.ajax({
                type: "POST",
                url: baseURL + "contract/save",
                contentType: "application/json",
                data: dataStr,
                success: function (r) {
                    if (r.code === 1) {
                        alert('操作成功');
                    } else {
                        alert(r.msg);
                    }
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
                        url:baseURL+"supplier/simpleInfo",
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
        reload: function () {
            window.location.reload();
        }


    }
});



var Dept = {
    id: "deptTable",
    table: null,
    layerIndex: -1
};




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