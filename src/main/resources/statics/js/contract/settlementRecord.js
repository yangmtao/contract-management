function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/settlement/list' ,
    datatype: "json",
    colModel: [

        {label: 'contractApplyRecordId', name: 'contractApplyRecordId', index: 'contract_apply_record_id', width: 10, key: true, hidden: true},
        {label: '合同名称', name: 'contractId', index: 'contract_id', width: 140, align: 'center'},
        {label: '收款银行', name: 'receiveBank', index: 'receive_bank', width: 100},
        {label: '收款账户', name: 'receiveAccount', index: 'receive_account', width: 140, align: 'center'},
        {label: '收款单位', name: 'receiveCompany', index: 'receive_company', width: 100},
        {label: '收款阶段', name: 'receiveStage', index: 'receive_stage', width: 100},
        {label: '收款金额', name: 'receiveAmount', index: 'receive_amount', width: 100},
        {label: '收款时间', name: 'receiveTime', index: 'receive_time', width: 100},

        {label: '结束时间', name: 'endDate', index: 'end_date', width: 180, align: 'center'},
        {label: '当前节点', name: 'contractNode', index: 'contratc_node', width: 140, align: 'center'},
        {label: '备注', name: 'remarks', index: 'remarks', width: 100,align: 'center'},
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var contractId = rowData["contractId"];

                return "<a class='btn btn-primary' onclick='settlement(\""+contractId+"\")' '>结算</a>"

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

function settlement(contractId){

   vm.addSupplier();

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
        querySupplier: function () {
            this.reload();
        },
        //重置
        clearSupplier: function () {
            this.supplierSearch = {
                supplierName:"",
                creditCode:""
            };
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
            };
            console.log(postData.supplierName)
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