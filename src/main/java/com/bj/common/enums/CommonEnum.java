
package com.bj.common.enums;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2016-11-15
 */
public class CommonEnum {
    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 10000;
    /**
     * 数据权限过滤
     */
    public static final String SQL_FILTER = "sql_filter";

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 使用平台
     */
    public enum UsePlatform {
        /**
         * 1;房企平台
         */
        REALESTATE(1, "房企平台"),
        /**
         * 2：医疗平台
         */
        MEDICAL(2, "医疗平台"),
        /**
         * 3：食品平台
         */
        FOOD(3, "食品平台"),
        /**
         * 4：部队及高校平台
         */
        ARMYANDEDU(4, "部队及高校平台"),
        /**
         * 5：金融平台
         */
        FINANCE(5, "金融平台");
        private int value;
        private String text;

        UsePlatform(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * 根据使用平台的value值获取对应的名称值
     *
     * @param code
     * @return
     */
    public static String getUsePlatformNameByCode(int code) {
        for (UsePlatform usePlatform : UsePlatform.values()) {
            if (code == usePlatform.getValue()) {
                return usePlatform.getText();
            }
        }
        return "";
    }

    /**
     * 公共的数据状态，1：启用/正常/是/最新，2：停用/删除/否/不是最新，3：注销
     */
    public enum DataStatus {
        /**
         * 1：启用/正常/是/最新
         */
        ENABLE(1, "启用/正常/是/最新"),
        /**
         * 2：停用/删除/否/不是最新
         */
        DISABLE(2, "停用/删除/否/不是最新"),
        /**
         * 3：注销
         */
        CANCEL(3, "注销");

        private int value;
        private String text;

        DataStatus(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * 请求返回值
     */
    public enum ReturnCode {
        /**
         * 操作成功
         */
        SUCCESS(1, "操作成功"),
        /**
         * 操作失败
         */
        ERROR(0, "操作失败"),
        /**
         * 参数异常
         */
        PARAM(-1, "参数异常");

        private int value;
        private String text;

        ReturnCode(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * 客户账号标识,1:主账号,2:子账号
     */
    public enum UserFlag {
        /**
         * 主账号
         */
        MASTER(1, "主账号"),
        /**
         * 子账号
         */
        SUB(2, "子账号");

        private int value;
        private String text;

        UserFlag(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * 订单状态,1:待采集数据,2:订单已取消,3:报告生成中,4:报告生成失败,5:订单已完成,6:数据采集失败
     */
    public enum OrderStatus {
        /**
         * 1:待采集数据
         */
        WAIT(1, "待采集数据"),
        /**
         * 2:订单已取消
         */
        CANCEL(2, "订单已取消"),
        /**
         * 3:报告生成中
         */
        CREATE(3, "报告生成中"),
        /**
         * 4:报告生成失败
         */
        CREATE_FAIL(4, "报告生成失败"),
        /**
         * 5:订单已完成
         */
        FINISH(5, "订单已完成"),
        /**
         * 6:数据采集失败
         */
        COLLECTION_FAIL(6, "数据采集失败");

        private int value;
        private String text;

        OrderStatus(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

}
