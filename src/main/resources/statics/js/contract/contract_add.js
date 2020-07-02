

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
            //contractFile:null,
            partyAId:null,
            partyBId:null,
            paymentType:null,
            //contractCode:null
        },
        paymentStage:{
            stageName: null,
            uses:null,
            paymentAmount:null,
            paymentRate:null,
            paymentDate: null
        },
        updateStageIndex:-1,
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
          if(this.contract.contractAmount==null||this.contract.startDate==null||this.contract.endDate==null) {
              alert("请先输入合同总金额,合同开始及结束时间");
          }else{
              this.dialogFormVisible=true;
          }
        },
        rateFormatter:function(row,column){
           return row.paymentRate+'%';
        },
        addPaymentStage:function(){
            var index=this.updateStageIndex;
            var tempStage={};
            $.extend(tempStage,this.paymentStage);
            tempStage.paymentRate=((tempStage.paymentAmount/this.contract.contractAmount)*100).toFixed(2);
            if(index>=0){
                this.tableData[index]=tempStage;
            }else{
                this.tableData.push(tempStage);
            }
            this.updateStageIndex=-1;
            this.dialogFormVisible=false;
        },
        handleEdit:function(index, row) {
            this.paymentStage=this.tableData[index];
            this.updateStageIndex=index;
            this.dialogFormVisible=true;
        },
        handleDelete:function(index, row) {
            this.tableData.splice(index,1);
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
            var item,contract=_this.contract;
            if(_this.files.length<1){
                alert("请上传合同附件");
                return;
            }else if(contract.endDate < contract.startDate){
                alert("合同结束时间不能小于开始时间");
                return;
            }else if(_this.tableData.length<1){
                alert("请输入付款阶段");
                return;
            }
            for (item in contract){
                if(_this.errors.has(item)||contract[item]==null){

                    alert("请正确完整的录入合同信息！"+item+","+contract[item]);
                    return;
                }
            }
            contract.paymentStage=JSON.stringify(this.tableData);

           contract.contractFile=_this.files[0].url;

            contract.contractCode=new Date().getTime();

           var dataStr=JSON.stringify(contract);
            $.ajax({
                type: "POST",
                url: baseURL + "contract/save",
                contentType: "application/json",
                data: dataStr,
                success: function (r) {
                    if (r.code === 1) {

                        alert('操作成功',function () {
                            _this.reload();
                        });
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