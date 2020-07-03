
//部门树设置
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

//表单校验，vee-validate
Vue.use(VeeValidate);
VeeValidate.Validator.localize('zh_CN');
//自定义校验规则
VeeValidate.Validator.extend('normalText',{
    getMessage:function(n){return n+"只能包含中文、字母、数字"},
    validate: function(value){return !!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value) }
});

//合同vue对象
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
            partyAId:null,
            partyBId:null,
            paymentType:null,
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
        this.getDept();
    },
    methods: {
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/select", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
            })
        },
        //显示添加付款阶段模态框
        showAddPaymentStage:function(){
          if(this.contract.contractAmount==null||this.contract.startDate==null||this.contract.endDate==null) {
              alert("请先输入合同总金额,合同开始及结束时间");
          }else{
              this.dialogFormVisible=true;
          }
        },
        //比率格式化，加上百分比符号
        rateFormatter:function(row,column){
           return row.paymentRate+'%';
        },
        //添加付款阶段
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
        //修改付款阶段
        handleEdit:function(index, row) {
            this.paymentStage=this.tableData[index];
            this.updateStageIndex=index;
            this.dialogFormVisible=true;
        },
        //删除付款阶段
        handleDelete:function(index, row) {
            this.tableData.splice(index,1);
            console.log(index, row);
        },
        //文件上传成功回调函数
        contractFileSuccess: function(r){
            var file={name:r.imageName,url:r.url};
            this.files.push(file);
          alert("上传成功");
        },
        //删除文件
        cancleContractFile: function(){
            confirm("确定删除所选文件？",function(){
                return true;
            });
        },
        //保存合同
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
        //根据关键词从后端获取相关用户信息
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
        //根据关键词从后端获取相关供应商信息
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
        //显示部门树选择弹框
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
        //刷新当前页面
        reload: function () {
            window.location.reload();
        }

    }
});
