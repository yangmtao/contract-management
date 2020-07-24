function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/review/list' ,
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
                return nodeName=="合同归档" ? nodeName : nodeName+"待审核"
            }
        },
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId = rowData["contractId"];
                var contractName = rowData["contractName"];
                var contractCode = rowData["contractCode"];
                var nodeName = rowData["nodeName"]
                return nodeName=="合同归档" ? "<label>办理</label>" : "<a class='btn btn-primary' onclick='review(\""+contractId+"\",\""+contractName+"\",\""+contractCode+"\")' '>办理</a>"


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
        userReviewRecord:{

        },
        contract:{

        },
        paymentStages:{

        },
        contractSearch:{
            contractName:"",
            contractManagerName:""
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

        rateFormatter:function(row,column){
            return row.paymentRate+'%';
        },

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


        //同意或驳回
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