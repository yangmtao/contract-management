function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/settlement/list' ,
    datatype: "json",
    colModel: [

        {label: 'id', name: 'id', index: 'id', width: 10, key: true, hidden: true},
        {label: '合同id', name: 'contractId', index: 'contract_id', width: 140, align: 'center',hidden:true},
        {label: '合同编号', name: 'contractCode', index: 'contract_code', width: 140,align: 'center'},
        {label: '合同名称', name: 'contractName', index: 'contract_name', width: 100,align: 'center'},
        {label: '申请人', name: 'contractManagerName', index: 'contract_manager_name', width: 100,align: 'center'},
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

        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId = rowData["contractId"];
                var contractName = rowData["contractName"];
                var contractCode = rowData["contractCode"];
                var unPayAmount = rowData["unPayAmount"]
                return "<a class='btn btn-primary' onclick='settlement(\""+contractId+"\",\""+contractName+"\",\""+contractCode+"\",\""+unPayAmount+"\")' '>结算</a>"


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

function settlement(contractId,contractName,contractCode,unPayAmount){

vm.passSettlement(contractId,contractName,contractCode,unPayAmount)

}

Vue.use(VeeValidate);
VeeValidate.Validator.localize('zh_CN');

VeeValidate.Validator.extend('normalText',{
    getMessage:function(n){return n+"只能包含中文、字母、数字"},
    validate: function(value){return !!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value) }
});
VeeValidate.Validator.extend('myDecimal',{
    getMessage:function(n){return "只能是19位数字"},
    validate: function(value){return /^\d{19}$/.test(value) }
});
VeeValidate.Validator.extend('AmDecimal',{
    getMessage:function(n){return "不能为负"},
    validate: function(value){
        return /^[1-9]\d*$/.test(value)}
});
var vm = new Vue({
    el: '#rrapp',
    data: {
        //back:"default",
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
        unPayAmount:"",
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
            contractManagerName:""


        },
        supplierSearch:{
            supplierName:"",
            creditCode:""
        },
        operatingStatusList: [
            {code: 1, value: "营业"},
            {code: 2, value: "停业"}
        ],
        typeList: [
            {code: 1, value: "科技"},
            {code: 2, value: "教育"}
        ],
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
                contractManagerName:""
            };
            this.reload();
        },
        //结算
        passSettlement: function (contractId,contractName,contractCode,unPayAmount) {
            this.errors.clear()
            this.showList = 2;
            this.title = "结算";
            this.contractName = contractName;
            this.contractCode = contractCode;
            this.unPayAmount = unPayAmount
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
            var settlement = _this.settlement
            console.log(Object.getOwnPropertyNames(settlement).length)
            if (Object.getOwnPropertyNames(settlement).length<6){
                layer.msg("请输入完整信息")
                return;
            }
            for (item in settlement){
                if(_this.errors.has(item)||settlement[item]==null){

                    alert("请输入正确的信息！");
                    return;
                }
            }
            if(this.settlement.receiveAmount>this.unPayAmount){
                layer.msg("付款金额不能大于未付款金额")
            }
            else{
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
            }


        },


        //得到所选id的信息
        getInfo: function (supplierId) {
            var _this = this;
            $.get(baseURL + "contract/supplier/info/" + supplierId, function (r) {
                _this.supplier = r.supplier;

            });
        },

        //搜索函数
        reload: function (event) {
            this.errors.clear()
            this.showList = 1;
            // var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            var postData = {
                "contractName":this.contractSearch.contractName,
                "contractCode":this.contractSearch.contractCode,
                "supplierName":this.contractSearch.supplierName,
                "contractManagerName":this.contractSearch.contractManagerName
            };
            $("#jqGrid").jqGrid('setGridParam', {
                page: 1, "postData": postData
            }).trigger("reloadGrid");
        },

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