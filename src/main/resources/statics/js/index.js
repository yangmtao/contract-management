//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props: {item: {list:[]}},
    template: [
        '<li :id="item.menuId">',
        '	<a v-if="item.menuType == 0" :href="\'#\'+item.menuUrl">',
        '		<i v-if="item.menuIcon != null" :class="item.menuIcon"></i>',
        '		<span>{{item.menuName}}</span>',
        '		<i class="fa fa-angle-left pull-right"></i>',
        '	</a>',
        '	<ul v-if="item.menuType == 0" class="treeview-menu">',
        '		<menu-item :item="item" v-for="item in item.list" :key="item.menuId"></menu-item>',
        '	</ul>',

        '	<a v-if="item.menuType == 1 && item.parentId == 0" :href="\'#\'+item.menuUrl">',
        '		<i v-if="item.menuIcon != null" :class="item.menuIcon"></i>',
        '		<span>{{item.menuName}}</span>',
        '	</a>',

        '	<a v-if="item.menuType == 1 && item.parentId != 0" :href="\'#\'+item.menuUrl" :url="item.menuUrl" onclick="vm.toRefresh();"><i v-if="item.menuIcon != null" :class="item.menuIcon"></i><i v-else style="margin-left:2em;"></i> {{item.menuName}}</a>',
        '</li>'
    ].join('')
});

//iframe自适应
$(window).on('resize', function () {
    var $content = $('.content');
    $content.height($(this).height() - 92);
    $content.find('iframe').each(function () {
        $(this).height($content.height());
    });
}).resize();

//注册菜单组件
Vue.component('menuItem', menuItem);
Vue.prototype.window = window;
var vm = new Vue({
    el: '#rrapp',
    data: {
        urlItems: [],
        user: {userName: ''},
        menuList: [],
        main: "main.html",
        password: '',
        newPassword: '',
        newPasswordSure: '',
        errMsg: '',
        navTitle: "首页",
        alarmList: [],
        t: 0,
        editableTabsValue: 'main.html',
        editableTabs: {'main.html': '首页'}
    },
    mounted: function () {
        this.user.userName = localStorage.getItem("account");
    },
    methods: {
        toRefresh: function () {
            var activeName = this.editableTabsValue;
            var labelName = this.editableTabs[activeName];
            vm.$set(vm.editableTabs, activeName, labelName);
            vm.editableTabsValue = activeName;
            if ($.trim(labelName) == '首页') {
                return;
            }
            //导航菜单展开
            $(".sidebar-menu li").removeClass("active");
            // console.log("activeName",activeName,"labelName",labelName);
            $("a[url='" + activeName + "']").parents("li").addClass("active");
            // console.log("activeName",activeName,"labelName",labelName);
            var url = top.location.href;
            top.location.href = url.substring(0, url.lastIndexOf("#")) + "#" + activeName;
            if (null != top.vm.urlItems && top.vm.urlItems.length > 0) {
                var obj = null;
                for (var i = 0; i < top.vm.urlItems.length; i++) {
                    var urlName = top.vm.urlItems[i].name;
                    // console.log("labelName=",labelName,"urlName=",urlName,$.trim(urlName) == $.trim(labelName),urlName==labelName);
                    if ($.trim(urlName) == $.trim(labelName)) {
                        // console.log(top.vm.urlItems[i].value);
                        obj = top.vm.urlItems[i].value;

                        if (parseInt(obj.showList) != 1) {
                            obj.showList = 1;
                        } else {
                            obj.showList = true;
                        }
                        obj.reload();
                    }
                }
            }
        },
        removeTab:function(targetName) {
            var tabs = Object.keys(this.editableTabs);
            var url = top.location.href;
            var activeName = this.editableTabsValue;
            // console.log(targetName,"===",activeName,"===",tabs);
            if (activeName === targetName) {
                // tabs.forEach((tab, index) => {
                //     if (tab === targetName) {
                //         let nextTab = tabs[index + 1] || tabs[index - 1];
                //         if (nextTab) {
                //             activeName = nextTab;
                //         }
                //     }
                // });
                if (tabs.length <= 2) {
                    top.location.href = url.substring(0, url.lastIndexOf("#"));
                } else {
                    for (var i = 0; i < tabs.length; i++) {
                        // console.log("tab==",tabs[i]);
                        if (tabs[i] == targetName) {
                            var nextTab = tabs[i + 1] || tabs[i - 1];
                            if (nextTab) {
                                activeName = nextTab;
                            }
                        }
                    }
                }

            }
            this.editableTabsValue = activeName;
            this.$delete(this.editableTabs, targetName);
        },
        startInterval:function() {
            var ul = $("#ul");
            // vm.t = setInterval(()=>{
            //     ul.animate({marginTop:'-40px'},2000,function () {
            //         ul.children().first().appendTo(ul);
            //     });
            //     ul.animate({marginTop:0},0);
            // },5000);
            vm.t = setInterval(function () {
                ul.animate({marginTop: '-40px'}, 2000, function () {
                    ul.children().first().appendTo(ul);
                });
                ul.animate({marginTop: 0}, 0);
            }, 5000);
        },
        clearInterval: function (t) {
            clearInterval(t);
        },
        getMenuList: function (event) {
            $.getJSON("sys/menu/nav?_" + $.now(), function (r) {
                if (r.status && r.status == 518) {
                    top.location.href = baseURL + "login.html";
                } else {
                    vm.menuList = r.menuList;
                }
            });
        },
        getUser: function () {
            $.getJSON("sys/user/info?_" + $.now(), function (r) {
                if (r.status && r.status == 518) {
                    top.location.href = baseURL + "login.html";
                } else {
                    vm.user = r.user;
                }
            });
        },
        updatePassword: function () {
            var _this = this;
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['550px', '360px'],
                shadeClose: false,
                content: jQuery("#passwordLayer"),
                btn: ['修改', '取消'],
                btn1: function (index) {
                    if (!_this.newPassword) {
                        _this.errMsg = "请输入新密码";
                        return false;
                    }
                    if (_this.newPassword != _this.newPasswordSure) {
                        _this.errMsg = "两次输入的密码不一致";
                        return false;
                    }
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type: "POST",
                        url: "sys/user/password",
                        data: data,
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 1) {
                                layer.close(index);
                                layer.alert('修改成功', function (index) {
                                    location.reload();
                                });
                            } else {
                                _this.errMsg = result.msg;
                            }
                        }
                    });
                }
            });
        },
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['http://cdn.renren.io/donate.jpg', 'no']
            });
        },
        quitSys: function () {
            window.location.href = "login.html";
        }

    },
    created: function () {
        this.getMenuList();
        this.getUser();
    },
    watch: {
        menuList:function() {
            //路由
            var router = new Router();
            routerList(router, this.menuList);
            router.start();
            this.$nextTick(toMenu);
        }
    }
});

function toMenu() {
    var url = window.location.hash;
    if (!url) {
        return;
    }
    var newTabName = url.replace('#', '');
    vm.$set(vm.editableTabs, newTabName, $("a[href='" + url + "']").text());
    vm.editableTabsValue = newTabName;
    //导航菜单展开
    $(".sidebar-menu li").removeClass("active");
    $("a[href='" + url + "']").parents("li").addClass("active");
}

function routerList(router, menuList) {
    for (var key in menuList) {
        var menu = menuList[key];
        if (menu.menuType == 0) {
            routerList(router, menu.list);
        } else if (menu.menuType == 1) {
            router.add('#' + menu.menuUrl, toMenu);
        }
    }
}
