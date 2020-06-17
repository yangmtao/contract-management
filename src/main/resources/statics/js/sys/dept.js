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

var vmFinSysDept = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        dept: {
            parentName: null,
            parentId: 0,
            sortNum: 0
        }
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "部门管理",
            value: this
        });
    },
    methods: {
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/select", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
            })
        },
        add: function () {
            this.showList = false;
            this.title = "新增";
            this.dept = {parentName: null, parentId: 0, sortNum: 0};
            this.getDept();
        },
        update: function () {
            var deptId = getDeptId();
            if (deptId == null) {
                return;
            }
            var _this =this;
            $.get(baseURL + "sys/dept/info/" + deptId, function (r) {
                _this.showList = false;
                _this.title = "修改";
                _this.dept = r.dept;

                _this.getDept();
            });
        },
        del: function () {
            var deptId = getDeptId();
            if (deptId == null) {
                return;
            }
            var _this =this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/dept/delete",
                    data: "deptId=" + deptId,
                    success: function (r) {
                        if (r.code === 1) {
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
        saveOrUpdate: function (event) {
            var url = this.dept.deptId == null ? "sys/dept/save" : "sys/dept/update";
            var _this = this;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(_this.dept),
                success: function (r) {
                    if (r.code === 1) {
                        alert('操作成功', function () {
                            _this.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        deptTree: function () {
            var _this = this;
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    _this.dept.parentId = node[0].deptId;
                    _this.dept.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            this.showList = true;
            Dept.table.refresh();
        }
    }
});

var Dept = {
    id: "deptTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '部门ID', field: 'deptId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '部门名称', field: 'deptName', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级部门', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '排序号', field: 'sortNum', align: 'center', valign: 'middle', sortable: true, width: '100px'}]
    return columns;
};


function getDeptId() {
    var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;
    }
}


$(function () {
    $.get(baseURL + "sys/dept/info", function (r) {
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, baseURL + "sys/dept/list", colunms);
        table.setRootCodeValue(r.deptId);
        table.setExpandColumn(2);
        table.setIdField("deptId");
        table.setCodeField("deptId");
        table.setParentCodeField("parentId");
        table.setExpandAll(true);
        table.init();
        Dept.table = table;
    });
});
window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};