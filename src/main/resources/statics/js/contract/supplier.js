function jqGrid(){$("#jqGrid").jqGrid({
    url: baseURL + 'contract/supplier/list',
    datatype: "json",
    colModel: [

        {label: 'supplierId', name: 'supplierId', index: 'supplier_id', width: 10, key: true, hidden: true},
        {label: '公司名称', name: 'supplierName', index: 'supplier_name', width: 140, align: 'center'},
        {label: '统一社会信用代码', name: 'creditCode', index: 'credit_code', width: 140, align: 'center'},
        {label: '法定代表人', name: 'legaRepresentative', index: 'lega_representative', width: 100, align:'center'},
        {label: '资质', name: 'qualifications', index: 'qualifications', width: 100},
        {label: '所属地', name: 'attribution', index: 'attribution', width: 100},
        {label: '注册资本', name: 'registeredCapital', index: 'registered_capital', width: 140, align: 'center'},
        {label: '经营状态,1:营业,2:停业,3:跑路', name: 'operatingStatus', index: 'operating_status', width: 10, hidden: true},
        {
            label: '经营状态',
            name: 'operatingStatus',
            index: 'operating_status',
            width: 80,
            sortable: false,
            align: 'center',
            formatter: function (cellValue, options, rowData) {
                var val = "";
                var operatingStatus = rowData["operatingStatus"];
                if (operatingStatus == "1") {
                    val = "营业";
                } else if (operatingStatus == "2") {
                    val = "停业";
                }
                return val;
            }
        },
        {
            label: '公司类型,1:主账号,2:子账号', name: 'supplierType', index: 'supplier_type', width: 80, hidden: true
        },
        {
            label: '公司类型', name: 'supplierType', index: 'supplier_type', width: 80, sortable: false, align: 'center',
            formatter: function (cellValue, options, rowData) {
                var val = "";
                var type = rowData["supplierType"];
                if (type == "2") {
                    val = "教育";
                } else if (type == "1") {
                    val = "科技";
                }
                return val;
            }
        },
        {label: '经营范围', name: 'businessScope', index: 'business_scope', width: 180, align: 'center'},
        {label: '黑名单，1：2：', name: 'status', index: 'status', width: 10, hidden: true},
        {
            label: '黑名单', name: 'blackList', index: 'blackList', width: 140, sortable: false, align: 'center',
            formatter: function (cellValue, options, rowData) {
                var val = "";
                var blackList = rowData["blackList"];
                var sid = rowData["supplierId"]
                var bid = rowData["blackList"]
                if (blackList == "0") {
                    val = "移入黑名单";
                    return "<a class=\"btn btn-danger plain\" onclick=\"move(" + sid + "," + bid + ")\" >"+val+"</a>";

                } else if (blackList == "1") {
                    val = "移出黑名单";
                    return "<a class=\"btn btn-warning plain\" onclick=\"move(" + sid + "," + bid + ")\" >"+val+"</a>";
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
//移动黑名单
function move(id,bid) {
    layer.msg(id)
    if (id == null) {
        return;
    }
    if(bid=="0"){
        var ready= '确定加入黑名单?请给出理由。'
        bid='1'
    }else if(bid=="1"){
        var ready= '确定移出黑名单？请给出理由。'
        bid='0'
    }
    layer.prompt({title: ready, formType: 2}, function(text, index){
        layer.close(index);
        $.ajax({
            type:"get",
            url:"../contract/supplier/move?supplierId="+id+"&blackList="+bid+"&remarks="+text,
            success:function(re){
                if(re.code==1){
                    layer.alert("操作成功",{
                        icon:1
                    },function(index){
                        layer.close(index)
                        read()
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
    });
    read()
}

Vue.use(VeeValidate);
VeeValidate.Validator.localize('zh_CN');

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: 1,
        title: null,
        opName: "",
        supplier:{
            orgId: "",
        },
        paymentStages:[]
        ,
        contract:{

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
        rateFormatter:function(row,column){
            return row.paymentRate+'%';
        },
        show:function(){
            var supplierId = getSelectedRow();
            if (supplierId == null) {
                return;
            }
            this.showList = 3;
            this.title = "详情";
            this.opName = "details";
            this.getInfo(supplierId);
        },

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
                supplierName: ""
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
           // jQuery("#jqGridContract").jqGrid("clearGridData")
            $("#jqGridContract").jqGrid('setGridParam',{  // 重新加载数据
                postData: {'partyBId':supplierId},   //  newdata 是符合格式要求的需要重新加载的数据
                page:1
            }).trigger("reloadGrid");
            this.getInfo(supplierId);
        },

        //跳转黑名单页面
        getBlackList:function(){
            this.$router.push({ path:'/blackList.html'  })
        },

        //跳转修改页面
        updateSupplier: function (event) {
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
        //删除
        del: function (event) {
            var supplierIds = getSelectedRows();
            if (supplierIds == null) {
                return;
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "contract/supplier/delete",
                    contentType: "application/json",
                    data: JSON.stringify(supplierIds),
                    success: function (r) {
                        if (r.code == 1) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
//得到所选id的信息
        getInfo: function (supplierId) {
            var _this = this;
            $.get(baseURL + "contract/supplier/info/" + supplierId, function (r) {
                _this.supplier = r.supplier;
                console.log(_this.supplier+"===================")
                _this.getContract(supplierId);
            });
            //this.getContract(supplierId);
        },

        //获取相关合同
        getContract:function(supplierId){
            $("#jqGridContract").jqGrid({
                url: baseURL + 'contract/list',
                datatype: "json",
                colModel: [

                    {label: '合同id', name: 'contractId', index: 'contract_id', width: 140, align: 'center',hidden:true},
                    {label: '供应商id', name: 'partyBId', index: 'party_b_id', width: 140, align: 'center',hidden:true},
                    {label: '合同编号', name: 'contractCode', index: 'contract_code', width: 140,align: 'center'},
                    {label: '合同名称', name: 'contractName', index: 'contract_name', width: 100},
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
                    },{label: '操作',  width: 100, sortable: false, align: 'center',
                        formatter:function (cellValue, options, rowData) {
                            var contractId = rowData["contractId"];
                            return "<a class='btn btn-primary' onclick='contractDetails(\""+contractId+"\")' '>详情</a>"
                        }
                    },
                ],
                viewrecords: true,
                height: 150,
                rowNum: 20,
                rowList: [20, 30, 40, 50],
                rownumbers: true,
                rownumWidth: 25,
                autowidth: true,
                shrinkToFit: false,
                multiselect: true,
                pager: "#contractPager",
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
            });
        },

        //搜索函数
        reload: function (event) {
            this.errors.clear()
            this.showList = 1;
            // var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            var postData = {
                "supplierName":this.supplierSearch.supplierName,
                "creditCode":this.supplierSearch.creditCode,
            };
            console.log(postData.supplierName)
            $("#jqGrid").jqGrid('setGridParam', {
                page: 1, "postData": postData
            }).trigger("reloadGrid");
        },
    }
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
                vm.showList=4;
            }else{
                alert("服务器异常，请联系系统管理员！");
            }
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