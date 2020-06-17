$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/bjuseroperation/frontList',
        datatype: "json",
        // colModel: [
        // { label: 'opId', name: 'opId', index: 'op_id', width: 50, key: true,hidden:true },
        // { label: '用户ID', name: 'userId', index: 'user_id', width: 80,hidden:true },
        // { label: '操作人', name: 'userName', index: 'user_name', width: 140,align:'center' },
        // { label: '操作模块', name: 'opModule', index: 'op_module', width: 140 ,align:'center'},
        // { label: '操作内容', name: 'opContent', index: 'op_content', width: 600,align:'left' },
        // { label: '操作描述', name: 'opDesc', index: 'op_desc', width: 280,align:'left' },
        // { label: '操作时间', name: 'opDate', index: 'op_date', width: 160,align:'center' },
        // { label: '使用平台', name: 'usePlatform', index: 'use_platform', width: 100,align:'center',
        // 	formatter:function(cellValue){
        // 		var val="";
        // 		if(cellValue=="1" || cellValue==1){
        // 			val="房企";
        // 		}else if(cellValue=="2" || cellValue==2){
        // 			val="医疗";
        // 		}else if(cellValue=="3" || cellValue==3){
        // 			val="食品";
        // 		}
        // 		return val;
        // 	}
        // }
        // ],
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 10, key: true, hidden: true},
            {label: '用户ID', name: 'userId', index: 'userId', width: 10, hidden: true},
            {label: '操作人', name: 'userName', index: 'userName', width: 140, align: 'center', sortable: false},
            {label: '企业名称', name: 'entName', index: 'entName', width: 330, align: 'center', sortable: false},
            {label: '统一社会信用代码', name: 'entId', index: 'entId', width: 160, align: 'center', sortable: false},
            {label: '请求IP', name: 'requestIp', index: 'requestIp', width: 120, align: 'center', sortable: false},
            {
                label: '操作模块',
                name: 'opModule',
                index: 'opModule',
                width: 140,
                align: 'center',
                hidden: true,
                sortable: false
            },
            {label: '操作内容', name: 'operationDesc', index: 'operationDesc', width: 600, align: 'left', sortable: false},
            {label: '操作耗时(ms)', name: 'opTime', index: 'opTime', width: 160, align: 'center', sortable: false},
            {label: '操作时间', name: 'createDate', index: 'createDate', width: 160, align: 'center', sortable: false},
            {
                label: '操作分类', name: 'opType', index: 'opType', width: 80, align: 'center', sortable: false,
                formatter: function (cellValue) {
                    var val = "";
                    if (cellValue == "1" || cellValue == 1) {
                        val = "用户操作";
                    } else if (cellValue == "2" || cellValue == 2) {
                        val = "系统请求";
                    }
                    return val;
                }
            },
            {label: '方法描述', name: 'methodDesc', index: 'methodDesc', width: 280, align: 'left', sortable: false},
            {label: '请求数据', name: 'requestData', index: 'requestData', width: 380, align: 'left', sortable: false},
            {
                label: '使用平台', name: 'platform', index: 'platform', width: 100, align: 'center', sortable: false,
                formatter: function (cellValue) {
                    var val = "";
                    if (cellValue == "1" || cellValue == 1) {
                        val = "房企";
                    } else if (cellValue == "2" || cellValue == 2) {
                        val = "医疗";
                    } else if (cellValue == "3" || cellValue == 3) {
                        val = "食品";
                    }
                    return val;
                }
            }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 20,
        rowList: [20, 30, 40, 50],
        rownumbers: true,
        rownumWidth: 45,
        autowidth: true,
        shrinkToFit: false,
        multiselect: false,
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
            // $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vmFinFrontLog = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        bjUserOperation: {},
        frontLogSearch: {
            userName: "",
            entName: "",
            entId: "",
            opModule: "",
            opType: 1,
            createDateStart: "",
            createDateEnd: ""
        },
        opTypeList: [
            {"code": 1, "value": "用户操作"},
            {"code": 2, "value": "系统请求"}
        ]
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "前台操作日志",
            value: this
        });
    },
    methods: {
        queryFrontLog: function () {
            this.reload();
        },
        clearFrontLog: function () {
            this.frontLogSearch = {
                userName: "",
                entName: "",
                entId: "",
                opModule: "",
                opType: 1,
                createDateStart: "",
                createDateEnd: ""
            };
            this.reload();
        },
        add: function () {
            this.showList = false;
            this.title = "新增";
            this.bjUserOperation = {};
        },
        update: function (event) {
            var opId = getSelectedRow();
            if (opId == null) {
                return;
            }
            this.showList = false;
            this.title = "修改";

            this.getInfo(opId)
        },
        saveOrUpdate: function (event) {
            var url = this.bjUserOperation.opId == null ? "sys/bjuseroperation/save" : "sys/bjuseroperation/update";
            var _this = this;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.bjUserOperation),
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
        del: function (event) {
            var opIds = getSelectedRows();
            if (opIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/bjuseroperation/delete",
                    contentType: "application/json",
                    data: JSON.stringify(opIds),
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
        getInfo: function (opId) {
            var _this = this;
            $.get(baseURL + "sys/bjuseroperation/info/" + opId, function (r) {
                _this.bjUserOperation = r.bjUserOperation;
            });
        },
        reload: function (event) {
            this.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('clearGridData');  //清空表格
            var postData = {
                "userName": this.frontLogSearch.userName,
                "entName": this.frontLogSearch.entName,
                "entId": this.frontLogSearch.entId,
                "opModule": this.frontLogSearch.opModule,
                "opType": this.frontLogSearch.opType,
                "createDateStart": this.frontLogSearch.createDateStart,
                "createDateEnd": this.frontLogSearch.createDateEnd
            };
            $("#jqGrid").jqGrid('setGridParam', {
                "datatype": "json",
                "page": 1,
                "postData": postData
            }).trigger('reloadGrid');
        },
        checkStartTime: function () {
            // console.log(1111111);
            if (this.frontLogSearch.createDateStart) {
                // console.log("==s=="+this.warnStartDate);
                // console.log("==e=="+this.warnEndDate);
                if (this.frontLogSearch.createDateEnd) {
                    var start = new Date((this.frontLogSearch.createDateStart).replace(/-/g, '/'));
                    var end = new Date((this.frontLogSearch.createDateEnd).replace(/-/g, '/'));
                    if (start.getTime() > end.getTime()) {
                        alert("起始时间不能大于截止时间！");
                        this.frontLogSearch.createDateStart = '';
                        // console.log("========="+this.warnStartDate);
                        return;
                    }
                }

            }
        },
        checkEndTime: function () {
            // console.log(222);
            if (this.frontLogSearch.createDateEnd) {
                // console.log("==s=="+this.warnStartDate);
                // console.log("==e=="+this.warnEndDate);
                if (this.frontLogSearch.createDateStart) {
                    var start = new Date((this.frontLogSearch.createDateStart).replace(/-/g, '/'));
                    var end = new Date((this.frontLogSearch.createDateEnd).replace(/-/g, '/'));
                    if (end.getTime() < start.getTime()) {
                        alert("截止时间不能小于起始时间！");
                        this.frontLogSearch.createDateEnd = '';
                        // console.log("========="+this.warnEndDate);
                        return;
                    }
                }

            }
        }
    }
});
window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};