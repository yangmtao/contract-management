$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/bjuseroperation/backList',
        datatype: "json",
        colModel: [
            {label: 'opId', name: 'opId', index: 'op_id', width: 50, key: true, hidden: true},
            {label: '用户ID', name: 'userId', index: 'user_id', width: 80, hidden: true},
            {label: '操作人', name: 'userName', index: 'user_name', width: 140, align: 'center'},
            {label: '操作模块', name: 'opModule', index: 'op_module', width: 140, align: 'center'},
            {label: '操作内容', name: 'opContent', index: 'op_content', width: 600, align: 'left'},
            {label: '操作描述', name: 'opDesc', index: 'op_desc', width: 280, align: 'left'},
            {label: '操作时间', name: 'opDate', index: 'op_date', width: 160, align: 'center'},
            {
                label: '使用平台', name: 'usePlatform', index: 'use_platform', width: 100, align: 'center',
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
            // $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vmFinBackLog = new Vue({
    el: '#rrapp',
    data: {
        showList: 1,
        title: null,
        bjUserOperation: {},
        backLogSearch: {
            userName: "",
            opModule: "",
            createDateStart: "",
            createDateEnd: ""
        }
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "后台操作日志",
            value: this
        });
    },
    methods: {
        queryBackLog: function () {
            this.reload();
        },
        clearBackLog: function () {
            this.backLogSearch = {
                userName: "",
                opModule: "",
                createDateStart: "",
                createDateEnd: ""
            };
            this.reload();
        },
        add: function () {
            this.showList = 2;
            this.title = "新增";
            this.bjUserOperation = {};
        },
        update: function (event) {
            var opId = getSelectedRow();
            if (opId == null) {
                return;
            }
            this.showList = 2;
            this.title = "修改";

            this.getInfo(opId);
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
            var _this = this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/bjuseroperation/delete",
                    contentType: "application/json",
                    data: JSON.stringify(opIds),
                    success: function (r) {
                        if (r.code == 1) {
                            alert('操作成功', function (index) {
                                _this.reload();
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
            this.showList = 1;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('clearGridData');  //清空表格
            var postData = {
                "userName": this.backLogSearch.userName,
                "opModule": this.backLogSearch.opModule,
                "createDateStart": this.backLogSearch.createDateStart,
                "createDateEnd": this.backLogSearch.createDateEnd
            };
            $("#jqGrid").jqGrid('setGridParam', {
                "datatype": "json",
                "page": 1,
                "postData": postData
            }).trigger('reloadGrid');
        },
        checkStartTime: function () {
            // console.log(1111111);
            if (this.backLogSearch.createDateStart) {
                // console.log("==s=="+this.warnStartDate);
                // console.log("==e=="+this.warnEndDate);
                if (this.backLogSearch.createDateEnd) {
                    var start = new Date((this.backLogSearch.createDateStart).replace(/-/g, '/'));
                    var end = new Date((this.backLogSearch.createDateEnd).replace(/-/g, '/'));
                    if (start.getTime() > end.getTime()) {
                        alert("起始时间不能大于截止时间！");
                        this.backLogSearch.createDateStart = '';
                        // console.log("========="+this.warnStartDate);
                        return;
                    }
                }

            }
        },
        checkEndTime: function () {
            // console.log(222);
            if (this.backLogSearch.createDateEnd) {
                // console.log("==s=="+this.warnStartDate);
                // console.log("==e=="+this.warnEndDate);
                if (this.backLogSearch.createDateStart) {
                    var start = new Date((this.backLogSearch.createDateStart).replace(/-/g, '/'));
                    var end = new Date((this.backLogSearch.createDateEnd).replace(/-/g, '/'));
                    if (end.getTime() < start.getTime()) {
                        alert("截止时间不能小于起始时间！");
                        this.backLogSearch.createDateEnd = '';
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