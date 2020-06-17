$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/dict/list',
        datatype: "json",
        colModel: [
            {label: '字典名称', name: 'name', index: 'name', width: 200, align: 'center'},
            {label: '字典类型', name: 'type', index: 'type', width: 180, align: 'center'},
            {label: '字典码', name: 'code', index: 'code', width: 180, align: 'center'},
            {label: '字典值', name: 'value', index: 'value', width: 200, align: 'center'},
            {label: '排序', name: 'orderNum', index: 'order_num', width: 80, align: 'center'},
            {label: '备注', name: 'remark', index: 'remark', width: 180, align: 'center'}
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

var vmDict = new Vue({
    el: '#rrapp',
    data: {
        q: {
            name: ""
        },
        showList: true,
        title: null,
        dict: {}
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "字典管理",
            value: this
        });
    },
    methods: {
        queryDict: function () {
            this.reload();
        },
        clearDict: function () {
            this.q = {
                name: ""
            }
            this.reload();
        },
        add: function () {
            this.showList = false;
            this.title = "新增";
            this.dict = {};
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            this.showList = false;
            this.title = "修改";

            this.getInfo(id);
        },
        saveOrUpdate: function (event) {
            var url = this.dict.id == null ? "sys/dict/save" : "sys/dict/update";
            var _this = this;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(_this.dict),
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
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            var _this = this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/dict/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
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
        getInfo: function (id) {
            var _this = this;
            $.get(baseURL + "sys/dict/info/" + id, function (r) {
                _this.dict = r.dict;
            });
        },
        reload: function (event) {
            this.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            console.log("page==dict==", page);
            $("#jqGrid").jqGrid('clearGridData');  //清空表格
            var postData = {
                "name": this.q.name
            };
            $("#jqGrid").jqGrid('setGridParam', {
                "page": 1,
                "postData": postData,
                "datatype": "json"
            }).trigger("reloadGrid");

        }
    }
});
window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};