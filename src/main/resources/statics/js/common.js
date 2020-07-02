//jqGrid的配置信息
if ($.jgrid && $.jgrid.defaults) {
    $.jgrid.defaults.width = 1000;
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap';
}


//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
T.p = url;

//全局配置
//全局的ajax访问，处理ajax清求时session超时
$.ajaxSetup({
    dataType: "json",
    cache: false,
    // contentType:"application/x-www-form-urlencoded;charset=utf-8",
    complete: function (XMLHttpRequest, textStatus) {
        // console.log("====here===",XMLHttpRequest.responseText,textStatus);
        //通过XMLHttpRequest取得响应头，sessionStatus，
        var sessionStatus = XMLHttpRequest.getResponseHeader("sessionStatus");
        // console.log("sessionStatus====",sessionStatus,"success"!=textStatus);
        // return;
        /*if (textStatus != "success") {
            top.location.href = baseURL + "index.html";
        }*/
        if (sessionStatus == "timeout") {
            //如果超时就处理 ，指定要跳转的页面(比如登陆页)
            top.location.href = baseURL + "index.html";
        }
    }
});
var removeByValue = function (arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val) {
            arr.splice(i, 1);
            break;
        }
    }
}

//重写alert
window.alert = function (msg, callback) {
    parent.layer.alert(msg, function (index) {
        parent.layer.close(index);
        if (typeof(callback) === "function") {
            callback("ok");
        }
    });
}

//重写confirm式样框
window.confirm = function (msg, callback, btn2) {
    parent.layer.confirm(msg, {btn: ['确定', '取消']},
        function (index) {//确定事件
            if (typeof(callback) === "function") {
                parent.layer.close(index);
                callback("ok");

            }
        }, function () {
            btn2 && btn2();

        });
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}

DICT = {
    SEX: {0: '未知', 1: '男', 2: '女'},
    PAYMENT_TYPE:{0:'现金',1:'银行转账',2:'支票',3:'支付宝',4:'微信'},
    CONTRACT_TYPE: {1: '通用物资', 2: '医用物资', 3: '工程',4:'服务',5:'其他'}
}
Vue.prototype.DICT = DICT;


function isInt(str) {
    var reg = /^(-|\+)?\d+$/;
    return reg.test(str);
}

/**
 * 使用平台
 * @returns {*[]}
 */
function getUsePlatform() {
    return [
        {code: 1, value: "房企平台"},
        {code: 2, value: "医疗平台"},
        {code: 3, value: "食品平台"},
        {code: 4, value: "部队及高校平台"},
        {code: 5, value: "金融平台"}
    ]
}


/**
 * 三方接口数据来源
 * @returns {*[]}
 */
function getApiSource() {
    return [
        {code: 0, value: "本地缓存"},
        {code: 1, value: "有数接口"},
        {code: 2, value: "汇法网"},
        {code: 3, value: "大陆云盾"},
        {code: 4, value: "国信"},
        {code: 5, value: "启信宝"},
        {code: 6, value: "中数"},
        {code: 999, value: "其它"}
    ]
}

/**
 *  报告订单状态
 * @returns {*[]}
 */
function getFinReportOrderStatus() {
    return [
        {code: 1, value: "待采集数据"},
        {code: 2, value: "订单已取消"},
        {code: 3, value: "报告生成中"},
        {code: 4, value: "报告生成失败"},
        {code: 5, value: "订单已完成"},
        {code: 6, value: "数据采集失败"}
    ]
}

/**
 * 信用报告申请类别
 */
function getReportType() {
    return [
        {code: 10000, value: "信用决策报告"},
        {code: 10001, value: "招投标信用报告(标准版)"},
        {code: 10002, value: "汇法网报告"},
        {code: 10003, value: "招投标信用报告(增值版)"},
        {code: 10004, value: "企业信用报告(金融版)"}
    ]
}

/**
 * 信用报告的报告类别
 */
function getUserBiddingType() {
    return [
        {code: 1, value: "招投标信用报告(标准版)"},
        {code: 2, value: "招投标信用报告(增值版)"},
        {code: 3, value: "企业信用报告(金融版)"}
    ]
}

/**
 * 用户所在企业的类型
 * @returns {*[]}
 */
function getUserEntType() {
    return [
        {code: 1, value: "房地产类"},
        {code: 2, value: "工程施工类"},
        {code: 3, value: "材料设备类"},
        {code: 4, value: "咨询服务类"},
        {code: 5, value: "设计类"},
        {code: 6, value: "勘察类"},
        {code: 7, value: "医疗类"},
        {code: 8, value: "高校类"},
        {code: 9, value: "食品类"},
        {code: 91, value: "军工类"},
        {code: 999, value: "其它"}
    ]
}

function showImg(url) {
    $(".mask-layer-imgbox").append("<p><img src=\"\" alt=\"\" ></p>");
    $(".mask-layer-imgbox img").prop("src", url); //给弹出框的Img赋值

    //图片居中显示
    var box_width = $(".auto-img-center").width(); //图片盒子宽度
    var box_height = $(".auto-img-center").height() < 500 ? 500 : $(".auto-img-center").height();//图片高度高度
    var initial_width = $(".auto-img-center img").width();//初始图片宽度
    var initial_height = $(".auto-img-center img").height();//初始图片高度
    // console.log("box_width",box_width,"box_height",box_height,"initial_width",initial_width,"initial_height",initial_height);
    if (initial_width > initial_height) {
        $(".auto-img-center img").css("width", box_width);
        var last_imgHeight = $(".auto-img-center img").height();
        $(".auto-img-center img").css("margin-top", -(last_imgHeight - box_height) / 2);
    } else {
        $(".auto-img-center img").css("height", box_height);
        var last_imgWidth = $(".auto-img-center img").width();
        $(".auto-img-center img").css("margin-left", -(last_imgWidth - box_width) / 2);
    }

    //图片拖拽
    var $div_img = $(".mask-layer-imgbox p");
    //绑定鼠标左键按住事件
    $div_img.bind("mousedown", function (event) {
        event.preventDefault && event.preventDefault(); //去掉图片拖动响应
        //获取需要拖动节点的坐标
        var offset_x = $(this)[0].offsetLeft;//x坐标
        var offset_y = $(this)[0].offsetTop;//y坐标
        //获取当前鼠标的坐标
        var mouse_x = event.pageX;
        var mouse_y = event.pageY;
        //绑定拖动事件
        //由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素
        $(".mask-layer-imgbox").bind("mousemove", function (ev) {
            // 计算鼠标移动了的位置
            var _x = ev.pageX - mouse_x;
            var _y = ev.pageY - mouse_y;
            //设置移动后的元素坐标
            var now_x = (offset_x + _x) + "px";
            var now_y = (offset_y + _y) + "px";
            //改变目标元素的位置
            $div_img.css({
                marginTop: now_y,
                marginLeft: now_x
            });
        });
    });
    //当鼠标左键松开，接触事件绑定
    $(".mask-layer-imgbox").bind("mouseup", function () {
        $(this).unbind("mousemove");
    });
}

/**
 * 判断当前cookie是否过期
 */
function checkLogin() {
    var user = null != sessionStorage.kfs && localStorage.kfs != "" ? JSON.parse(localStorage.kfs) : {};
    console.log("user", user);
    if (!user.username) {
        window.alert("登录已超时，请重新登录！", function () {
            //本地路径
            // top.location.href=baseURL+"login.html";
            //服务器路径
            top.location.href = "https://www.zhongzhuxin.com/admin/login.html";
        });
    }
}