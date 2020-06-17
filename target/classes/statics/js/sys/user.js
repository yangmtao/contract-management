$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [
            {label: '用户ID', name: 'userId', index: "user_id", width: 80, key: true, hidden: true},
            {label: '用户名', name: 'userName', width: 200, align: 'center'},
            {label: '真实姓名', name: 'realName', width: 200, align: 'center'},
            {
                label: '性别', name: 'userSex', width: 75, align: 'center',
                formatter: function (value) {
                    return DICT.SEX[value] || '';
                }
            },
            {
                label: '所属部门', name: 'deptName', sortable: false, width: 330, align: 'left',
                formatter: function (a, b, c) {
                    if (c.companyName && c.companyName != a) {
                        return a + '(' + c.companyName + ')'
                    }
                    return a;
                }
            },
            {label: '创建时间', name: 'createTime', index: "create_time", width: 160, align: 'center'}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 20,
        rowList: [50, 100, 500],
        rownumbers: true,
        rownumWidth: 45,
        autowidth: true,
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
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            // $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
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


var area_ztree;
var area_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: 0
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
var setting1 = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: 0
        },
        key: {
            url: "nourl"
        }
    },
    callback: {
        onDblClick: function (event, treeId, node) {
            vmFinSysUser.user.areaId = node.id;
            vmFinSysUser.user.areaName = node.joinname;
            layer.closeAll();
        }
    }
};
var ztree1;
var vmFinSysUser = new Vue({
    el: '#rrapp',
    data: {
        q: {
            userName: null
        },
        showList: true,
        title: null,
        roleList: {},
        opName: "",
        oldPassword: "",
        newPassword: "",
        newPasswordSure: "",
        chooseUserId: "",
        user: {
            status: 1,
            deptId: null,
            deptName: null,
            areaId: '',
            areaName: '',
            costPrice: 1,
            roleIdList: []
        }
    },
    mounted: function () {
        top.vm.urlItems.push({
            name: "用户管理",
            value: this
        });
    },
    methods: {
        queryUser: function () {
            this.reload();
        },
        clearUser: function () {
            this.q = {
                userName: null
            }
            this.reload();
        },
        addUser: function () {
            this.showList = false;
            this.title = "新增系统用户";
            this.roleList = {};
            this.opName = "add";
            this.user = {
                deptName: null,
                deptId: null,
                userStatus: 1,
                roleIdList: [],
                areaId: '',
                areaName: '',
                costPrice: 1
            };

            //获取角色信息
            this.getRoleList();

            this.getDept();
        },
        getDept: function () {
            var _this = this;
            //加载部门树
            $.get(baseURL + "sys/dept/list", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", _this.user.deptId);
                if (node != null) {
                    ztree.selectNode(node);

                    _this.user.deptName = node.name;
                }
            })
        },
        updateUser: function () {
            var userId = getSelectedRow();
            if (userId == null) {
                return;
            }
            this.opName = "update";
            this.showList = false;
            this.title = "修改系统用户";

            this.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        updateUserPwd: function () {
            var userId = getSelectedRow();
            if (userId == null) {
                return;
            }
            this.chooseUserId = userId;
            this.getUser(userId);
            var _this = this;
            var pwdIndex = layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['540px', '500px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#sysUserPwdLayer"),
                btn: ['确定', '取消'],
                btnAlign: 'c',
                btn1: function (index) {
                    if (!_this.newPassword) {
                        alert("请输入新密码");
                        return;
                    }
                    if (_this.newPassword != _this.newPasswordSure) {
                        alert("两次输入的密码不一致");
                        return;
                    }
                    var data = "userId=" + _this.chooseUserId + "&password=" + _this.oldPassword + "&newPassword=" + _this.newPassword;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sys/user/modifyUserPwd",
                        data: data,
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 1) {
                                layer.alert('修改成功', function (index) {
                                    _this.reload();
                                    layer.close(pwdIndex);
                                    layer.close(index);
                                });

                            } else {
                                alert(result.msg);
                            }
                        }
                    });
                },
                btn2: function (index, layero) {
                    //按钮【按钮二】的回调
                    layer.close(index);
                    //return false 开启该代码可禁止点击该按钮关闭
                },
                cancel: function () {
                    //右上角关闭回调
                    layer.close(pwdIndex);
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
        },
        delUser: function () {
            var userIds = getSelectedRows();
            if (userIds == null) {
                return;
            }
            var _this = this;
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
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
        saveOrUpdate: function () {
            var _this = this;
            var url = this.user.userId == null ? "sys/user/save" : "sys/user/update";
            // let treeObj = $.fn.zTree.getZTreeObj("menuTree");
            // let nodes = treeObj.getCheckedNodes(true)||[];
            // if(nodes.length < 1){
            //     this.$message.error("区域权限不能为空");
            //     return;
            // }
            // this.user.areaIds = nodes.map(function (v) {
            //     return v.id;
            // });
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(this.user),
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
        getUser: function (userId) {
            var _this = this;
            $.get(baseURL + "sys/user/info/" + userId, function (r) {
                _this.user = r.user;
                // console.log("r.user===",r.user);
                _this.user.deptName = r.user.deptName != null && r.user.deptName != "" ? r.user.deptName : null;
                _this.user.deptId = r.user.deptId != null ? r.user.deptId : null;
                _this.user.status = r.user.status != null ? r.user.status : 1;
                _this.user.roleIdList = r.user.roleIdList != null ? r.user.roleIdList : [];
                _this.user.areaId = r.user.areaId != null ? r.user.areaId : '';
                _this.user.areaName = r.user.areaName != null && r.user.areaName != "" ? r.user.areaName : '';
                _this.user.costPrice = r.user.costPrice != null ? r.user.costPrice : 1;
                // _this.user = {deptName: null, deptId: null, status: 1, roleIdList: [],areaId:'', areaName:'',costPrice:1,...r.user};
                _this.user.password = null;
                _this.user.sex += '';
                _this.getDept();
            });
        },
        getRoleList: function () {
            var _this = this;
            $.get(baseURL + "sys/role/select", function (r) {
                _this.roleList = r.list;
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
                    _this.user.deptId = node[0].deptId;
                    _this.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            this.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'userName': this.q.userName},
                page: 1
            }).trigger("reloadGrid");
        },
        getAreaTree: function (roleId) {
            var _this = this;
            $.get(baseURL + "sys/sysarea/queryAreaByParentId?parentId=", function (r) {
                area_ztree = $.fn.zTree.init($("#menuTree"), area_setting, r.areaEntities);
                area_ztree.expandAll(true);
                ztree1 = $.fn.zTree.init($("#areaTree"), setting1, r.areaEntities);
                ztree1.expandAll(true);
                if (_this.user.areaId) {
                    var o = r.areaEntities.find(function (v) {
                        return v.id == _this.user.areaId;
                    });
                    if (o) {
                        _this.user.areaName = o.joinname;
                    } else {
                        _this.user.areaId = '';
                    }
                }
                //加载默认选中
                $.get(baseURL + "sys/sysarea/queryUserAreaIds", {userId: _this.user.userId}, function (d) {
                    var areaIds = d.userAreaIds;
                    for (var i = 0; i < areaIds.length; i++) {
                        var node = area_ztree.getNodeByParam("id", areaIds[i]);
                        if (node != null) {
                            area_ztree.checkNode(node, true)
                        }
                    }
                });
            });
        },
        areaTree: function () {
            var _this = this;
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择区域",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#areaLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree1.getSelectedNodes();
                    //选择上级部门
                    _this.user.areaId = node[0].id;
                    _this.user.areaName = node[0].joinname;

                    layer.close(index);
                }
            });
        }
    }
});
window.onload = window.onresize = function () {
    var target = document.querySelector(".ui-jqgrid-bdiv");
    if (target) {
        target.style.height = (document.documentElement.clientHeight - document.querySelector('.ui-jqgrid').offsetTop - 82) + 'px';
    }
};