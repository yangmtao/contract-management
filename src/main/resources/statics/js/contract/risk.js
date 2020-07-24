function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/risk/list' ,
    datatype: "json",
    colModel: [

        {label: 'id', name: 'id', index: 'id', width: 10, key: true, hidden: true},
        {label: '合同id', name: 'contractId', index: 'contract_id', width: 140, align: 'center',hidden: true},
        {label: '合同编号', name: 'contractCode', index: 'contract_code', width: 140,align: 'center'},
        {label: '合同名称', name: 'contractName', index: 'contract_name', width: 140},
        {label: '风险类型', name: 'riskType', index: 'risk_type', width: 100, align: 'center'},
        {label: '风险名称', name: 'riskName', index: 'risk_name', width: 140, align: 'center'},
        {label: '解决方案', name: 'solution', index: 'solution', width: 140, align: 'center'},
        {label: '合同类型', name: 'contractType', index: 'contract_type', width: 100, align: 'center'},
        {label: '付款情况', name: 'payStatus', index: 'pay_status', width: 100,
            formatter: function (cellValue, options, rowData) {
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
        // {label: '合同未付款', name: 'receiveTime', index: 'receive_time', width: 100},

        {label: '乙方公司', name: 'supplierName', index: 'supplier_name', width: 100,align: 'center'},
        {label: '操作',  width: 100, sortable: false, align: 'center',
            formatter:function (cellValue, options, rowData) {
                var id  = rowData["id"];
                var del = rowData["del"]
                if (del==0){
                    return "<a class='btn btn-primary' onclick='over(\""+id+"\")' '>已解决</a>"
                } else{
                    return "<label>已解决</label>"
                }
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

function over(id){
    layer.confirm('确定解决了吗？', {
        btn: ['确定','还没有'] //按钮
    }, function(){
        $.ajax({
            type:"get",
            url:baseURL + "contract/risk/over?id="+id,
            success:function(re){
                if(re.code==1){
                    layer.alert("操作成功",{
                        icon:1
                    },function(index){
                        layer.close(index)
                        window.location.reload();
                    })
                }else {
                    layer.alert(re.message,{
                        icon:2
                    },function (index){
                        layer.close(index)
                    })
                }
            }


        })

    }, function(){
        layer.close();
    });



}


var vmFinUser = new Vue({
    el: '#rrapp',
    data: {
        showList: 1,
        title: null,
        opName: "",
        contractRisk:{
            contractName:""
        },
       riskSearch:{
           contractName:"",
           contractCode:"",
           supplierName:"",
           del:""

       },
        supplier:{

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
        queryRisk: function () {
            this.reload();
        },
        //重置
        clearRisk: function () {
            this.riskSearch = {
                contractName:"",
                contractCode:"",
                supplierName:"",
                del:""
            };
            this.reload();
        },

        //跳转详情页面
        riskDetails:function(){
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            this.showList = 3;
            this.title = "详情";
            this.opName = "details";
            this.getInfo(id);
        },


        //跳转修改页面
        updateRisk: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            this.showList = 2;
            this.title = "修改";
            this.opName = "update";
            this.getInfo(id);
        },
        //修改
        saveOrUpdate: function (event) {
            var url = "contract/risk/update";
            var _this = this;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.contractRisk),
                success: function (r) {
                    if (r.code === 1) {
                        alert('操作成功', function (index) {
                            // _this.reload();
                            window.location.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },

        //得到所选id的信息
        getInfo: function (id) {
            var _this = this;
            $.get(baseURL + "contract/risk/info/" + id, function (r) {
                _this.contractRisk = r.contractRisk;
            });
        },

        //搜索函数
        reload: function (event) {
            this.showList = 1;
            var postData = {
                "contractName":this.riskSearch.contractName,
                "contractCode":this.riskSearch.contractCode,
                "supplierName":this.riskSearch.supplierName,
                "del":this.riskSearch.del
            };
            console.log(postData.payStatus)
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