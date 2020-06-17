package com.bj.common.annotation;

import java.lang.annotation.*;

/**
 * 区域过滤
 * @Author: lmf
 * @Date: 2019/4/1 11:27
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AreaFilter {
    /**  表的别名  */
    String tableAlias() default "";

    /**  true：没有本区域数据权限，也能查询本区域数据 */
    boolean user() default true;

    /**  true：拥有下级区域权限 */
    boolean subArea() default false;

    /**  区域ID */
    String areaId() default "area_id";
    /**  用户ID */
    String userId() default "user_id";

}
