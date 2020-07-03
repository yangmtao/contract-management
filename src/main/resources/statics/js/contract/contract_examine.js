$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'contract/examine/list',
        datatype: "json",
        colModel: [
            {label: '合同ID', name: 'id', index: "id", width: 20, key: true, hidden: true},
            {label: '合同编号', name: 'contractCode', width: 120, align: 'center',sortable:false},
            {label: '合同名称', name: 'contractName', width: 200, align: 'center',sortable:false},
           {label: '严重等级', name: 'riskLevel', width: 100, align: 'center',sortable:false},
            {label: '审查问题', name: 'problem',width: 180, align: 'center',sortable:false},
            {label: '修正状态', name: 'status',  width: 100, align: 'center'},
            {label: '解决方案', name: 'handleWay',  width: 180, align: 'center',sorttype:'date'},
            {label: '提出人', name: 'handlerName', width: 120, align: 'center',sortable:false,},
            {label: '提出时间', name: 'createTime', index: "create_time", width: 120, align: 'center',sorttype:'date'},
            {label: '经办人', name: 'handlerName',width: 120, align: 'center',sortable:false},
            {label: '操作',  name: 'id',width: 110, align: 'center',sortable:false,
                formatter: function (value) {
                    return '<button class="btn btn-success" onclick="showUpdate('+value+')" >修 正</button>';
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

DICT = {
    STATUS: {0: '待处理', 1: '处理中', 2: '已解决'},
    RISK_LEVEL: {0: '低', 1: '中', 2: '高'},
}
Vue.prototype.DICT = DICT;

var vmContractExamine = new Vue({
    el: '#rrapp',
    data: {
        formLabelWidth: '120px',
        dialogFormVisible:false,
        queryOption:{
            contractName:null,
            startDate:null,
            endDate:null,
            riskLevel:null,
            contractCode:null
        },
        contractExamine:{}
    },
    methods: {
        //根据id获取合同审查记录信息
        getContractExamine:function(id){
            var _this = this;
            $.get(baseURL + "contract/examine/info/" + id, function (r) {
                _this.contractExamine = r.contractExamine;
            });
        },

        updateContractExamine:function(){
            var _this=this;
            $.ajax({
                type: "POST",
                url: baseURL + "contract/examine/update",
                contentType: "application/json",
                data: JSON.stringify(this.contractExamine),
                success: function (r) {
                    if (r.code == 1) {
                        alert('操作成功', function () {
                            _this.dialogFormVisible=false;
                            _this.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        //删除审查记录
        deleteExamine:function(){
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            var _this = this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "contract/examine/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 1) {
                            alert('操作成功', function () {
                               _this.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        //查询审查记录
        queryExamine: function(){
            console.log("正在查找");
            var _this=this;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'risk_level': _this.queryOption.riskLevel,
                           'contract_code':_this.queryOption.contractCode,
                           'contract_name':_this.queryOption.contractName,
                           'end_date':_this.queryOption.endDate,
                           'start_date':_this.queryOption.startDate
                },
                page: 1
            }).trigger("reloadGrid");
        },

        reload: function () {
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }


    }
});
//显示合同修改模态框
function  showUpdate(id) {

    if (id == null) {
        return;
    }
    this.vmContractExamine.dialogFormVisible = true;

    this.vmContractExamine.getContractExamine(id);

}
