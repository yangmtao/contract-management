function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/userReviewRecord/list' ,
    datatype: "json",
    colModel: [

        {label: 'Id', name: 'Id', index: 'id', width: 10, key: true, hidden: true},
        {label: '合同id', name: 'contractId', index: 'contract_id', width: 140, align: 'center', hidden: true},
        {label: '合同名称', name: 'contractName', index: 'contract_name', width: 140, align: 'center'},
        {label: '审核建议', name: 'reviewAdvise', index: 'review_advise', width: 100},
        {label: '审核结果', name: 'reviewResult', index: 'review_result', width: 140, align: 'center'},
        {label: '审核人', name: 'reviewer', index: 'reviewer', width: 100},
        {
            label: '开始时间', name: 'startDate', index: 'start_date', width: 80, hidden: true
        },
        {label: '结束时间', name: 'endDate', index: 'end_date', width: 180, align: 'center'},
        {label: '当前节点', name: 'nodeName', index: 'node_name', width: 140, align: 'center'},
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId  = rowData["contractId"];
                return "<a class='btn btn-primary' onclick='contractDetails(\""+contractId+"\")' '>合同详情</a>"


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

function contractDetails(id){
    $.ajax({
        type:"GET",
        data:{
            contractId:id
        },
        url:baseURL+"contract/detail",
        success:function(r){
            if(r.code===1){
                vm.contract=r.contract;
                vm.paymentStages=r.paymentStages;
                vm.showList=3;
            }else{
                alert("服务器异常，请联系系统管理员！");
            }
        }
    });

}

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: 1,
        title: null,
        opName: "",
        supplier:{
            blackList:1
        },
        reviewSearch:{
            contractName:"",
            reviewer:""
        },
        paymentStages:[]
        ,
        contract:{

        },

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

        rateFormatter:function(row,column){
            return row.paymentRate+'%';
        },

        //搜索
        queryReview: function () {
            this.reload();
        },
        //重置
        clearReview: function () {
            this.reviewSearch ={
                contractName:"",
                    reviewer:""
            }
            this.reload();
        },
        //新增
        addSupplier: function () {
            this.showList = 2;
            this.title = "新增";
            this.opName = "add";
            this.supplier = {
                blackList:1
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

        //跳转黑名单页面
        getBlackList:function(){
            this.$router.push({ path:'/blackList.html'  })
        },

        //跳转修改页面
        updateFinUser: function (event) {
            var supplierId = getSelectedRow();
            if (supplierId == null) {
                return;
            }
            this.showList = 2;
            this.title = "修改";
            this.opName = "update";
            this.getInfo(supplierId);
        },
        //新增或修改
        saveOrUpdate: function (event) {
            var url = this.supplier.supplierId == null ? "contract/supplier/save" : "contract/supplier/update";
            var _this = this;

            console.log("============")
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.supplier),
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
                "contractName":this.reviewSearch.contractName,
                "reviewer":this.reviewSearch.reviewer,
            };
            console.log(postData.contractName)
            $("#jqGrid").jqGrid('setGridParam', {
                page: 1, "postData": postData
            }).trigger("reloadGrid");
        },
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