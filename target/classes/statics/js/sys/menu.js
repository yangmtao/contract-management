var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "menuId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;

var vmFinMenu = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        chooseMenuId: null,
        menu: {
            parentName: null,
            parentId: 0,
            menuType: 1,
            sortNum: 0
        }
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "菜单管理",
            value: this
        });
    },
    methods: {
        getMenu: function (menuId) {
            var _this = this;
            //加载菜单树
            $.get(baseURL + "sys/menu/select", function (r) {
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
                var node = ztree.getNodeByParam("menuId", _this.menu.parentId);
                ztree.selectNode(node);
                // console.log(node);
                _this.menu.parentName = node.name;
            })
        },
        add: function () {
            this.showList = false;
            this.title = "新增";
            this.menu = {parentName: null, parentId: 0, menuType: 1, sortNum: 0};
            this.getMenu();
        },
        update: function () {
            var menuId = getMenuId();
            if (menuId == null) {
                return;
            }
            var _this = this;
            $.get(baseURL + "sys/menu/info/" + menuId, function (r) {
                _this.showList = false;
                _this.title = "修改";
                _this.menu = r.menu;

                _this.getMenu();
            });
        },
        del: function () {
            var menuId = getMenuId();
            if (menuId == null) {
                return;
            }
            var _this = this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/menu/delete",
                    data: "menuId=" + menuId,
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
        saveOrUpdate: function () {
            if (this.validator()) {
                return;
            }

            var url = this.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
            var _this = this;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.menu),
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
        menuTree: function () {
            var _this = this;
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择菜单",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    _this.menu.parentId = node[0].menuId;
                    _this.menu.parentName = node[0].menuName;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            this.showList = true;
            Menu.table.refresh();
        },
        validator: function () {
            if (isBlank(this.menu.menuName)) {
                alert("菜单名称不能为空");
                return true;
            }

            //菜单
            if (this.menu.menuType === 1 && isBlank(this.menu.menuUrl)) {
                alert("菜单URL不能为空");
                return true;
            }
        }
    }
});


var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '菜单ID', field: 'menuId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '菜单名称', field: 'menuName', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级菜单', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {
            title: '图标',
            field: 'menuIcon',
            align: 'center',
            valign: 'middle',
            sortable: true,
            width: '80px',
            formatter: function (item, index) {
                return item.menuIcon == null ? '' : '<i class="' + item.menuIcon + ' fa-lg"></i>';
            }
        },
        {
            title: '类型',
            field: 'menuType',
            align: 'center',
            valign: 'middle',
            sortable: true,
            width: '100px',
            formatter: function (item, index) {
                if (item.menuType === 0) {
                    return '<span class="label label-primary">目录</span>';
                }
                if (item.menuType === 1) {
                    return '<span class="label label-success">菜单</span>';
                }
                if (item.menuType === 2) {
                    return '<span class="label label-warning">按钮</span>';
                }
            }
        },
        {title: '排序号', field: 'sortNum', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '菜单URL', field: 'menuUrl', align: 'center', valign: 'middle', sortable: true, width: '160px'},
        {title: '授权标识', field: 'menuPerms', align: 'center', valign: 'middle', sortable: true}]
    return columns;
};


function getMenuId() {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;
    }
}


$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, baseURL + "sys/menu/list", colunms);
    table.setExpandColumn(2);
    table.setIdField("menuId");
    table.setCodeField("menuId");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.init();
    Menu.table = table;
});
window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};