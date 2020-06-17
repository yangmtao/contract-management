$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/role/list',
        datatype: "json",
        colModel: [
            {label: '角色ID', name: 'roleId', index: "role_id", width: 80, key: true},
            {label: '角色名称', name: 'roleName', index: "role_name", width: 200, align: 'center'},
            {
                label: '所属部门', name: 'deptName', sortable: false, width: 330, align: 'center',
                formatter: function (a, b, c) {
                    if (c.companyName && c.companyName != a) {
                        return a + '(' + c.companyName + ')'
                    }
                    return a;
                }
            },
            {label: '备注', name: 'remark', width: 200, align: 'left'},
            {label: '创建时间', name: 'createTime', index: "create_time", width: 160, align: 'center'}
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

//菜单树
var menu_ztree;
var menu_setting = {
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
    },
    check: {
        enable: true,
        nocheckInherit: true
    }
};

//部门结构树
var dept_ztree;
var dept_setting = {
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

//数据树
var data_ztree;
var data_setting = {
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
    },
    check: {
        enable: true,
        nocheckInherit: true,
        chkboxType: {"Y": "", "N": ""}
    }
};

var vmFinRole = new Vue({
    el: '#rrapp',
    data: {
        q: {
            roleName: null
        },
        showList: true,
        title: null,
        role: {
            deptId: null,
            deptName: null
        }
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "角色管理",
            value: this
        });
    },
    methods: {
        queryRole: function () {
            this.reload();
        },
        clearRole: function () {
            this.q={
                roleName:null
            };
            this.reload();
        },
        add: function () {
            this.showList = false;
            this.title = "新增";
            this.role = {deptName: null, deptId: null};
            this.getMenuTree(null);

            this.getDept();

            this.getDataTree();
        },
        update: function () {
            var roleId = getSelectedRow();
            if (roleId == null) {
                return;
            }

            this.showList = false;
            this.title = "修改";
            this.getDataTree();
            this.getMenuTree(roleId);

            this.getDept();
        },
        del: function () {
            var roleIds = getSelectedRows();
            if (roleIds == null) {
                return;
            }
            var _this = this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/role/delete",
                    contentType: "application/json",
                    data: JSON.stringify(roleIds),
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
        getRole: function (roleId) {
            var _this = this;
            $.get(baseURL + "sys/role/info/" + roleId, function (r) {
                _this.role = r.role;

                //勾选角色所拥有的菜单
                var menuIds = _this.role.menuIdList;
                for (var i = 0; i < menuIds.length; i++) {
                    var node = menu_ztree.getNodeByParam("menuId", menuIds[i]);
                    menu_ztree.checkNode(node, true, false);
                }

                //勾选角色所拥有的部门数据权限
                var deptIds = _this.role.deptIdList;
                for (var i = 0; i < deptIds.length; i++) {
                    var node = data_ztree.getNodeByParam("deptId", deptIds[i]);
                    data_ztree.checkNode(node, true, false);
                }

                _this.getDept();
            });
        },
        saveOrUpdate: function () {
            //获取选择的菜单
            var nodes = menu_ztree.getCheckedNodes(true);
            var menuIdList = new Array();
            for (var i = 0; i < nodes.length; i++) {
                menuIdList.push(nodes[i].menuId);
            }
            this.role.menuIdList = menuIdList;

            //获取选择的数据
            var nodes = data_ztree.getCheckedNodes(true);
            var deptIdList = new Array();
            for (var i = 0; i < nodes.length; i++) {
                deptIdList.push(nodes[i].deptId);
            }
            this.role.deptIdList = deptIdList;
            var _this= this;
            var url = this.role.roleId == null ? "sys/role/save" : "sys/role/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(_this.role),
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
        getMenuTree: function (roleId) {
            //加载菜单树
            var _this =this;
            $.get(baseURL + "sys/menu/list", function (r) {
                menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
                //展开所有节点
                menu_ztree.expandAll(true);

                if (roleId != null) {
                    _this.getRole(roleId);
                }
            });
        },
        getDataTree: function (roleId) {
            //加载菜单树
            $.get(baseURL + "sys/dept/list", function (r) {
                data_ztree = $.fn.zTree.init($("#dataTree"), data_setting, r);
                //展开所有节点
                data_ztree.expandAll(true);
            });
        },
        getDept: function () {
            //加载部门树
            var _this = this;
            $.get(baseURL + "sys/dept/list", function (r) {
                dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r);
                var node = dept_ztree.getNodeByParam("deptId", _this.role.deptId);
                if (node != null) {
                    dept_ztree.selectNode(node);

                    _this.role.deptName = node.name;
                }
            })
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
                    var node = dept_ztree.getSelectedNodes();
                    //选择上级部门
                    _this.role.deptId = node[0].deptId;
                    _this.role.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            this.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'roleName': this.q.roleName},
                page: 1
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