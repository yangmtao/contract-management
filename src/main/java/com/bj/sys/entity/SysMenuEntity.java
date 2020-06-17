
package com.bj.sys.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhph
 */
@ApiModel("菜单管理")
@TableName("sys_menu")
@Data
public class SysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	@TableId(type = IdType.AUTO)
	private Long menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private Long parentId;
	
	/**
	 * 父菜单名称
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 菜单URL
	 */
	private String menuUrl;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String menuPerms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer menuType;

	/**
	 * 菜单图标
	 */
	private String menuIcon;

	/**
	 * 排序
	 */
	private Integer sortNum;

	/**
	 * ztree属性 菜单名称
	 */
	@TableField(exist = false)
	private String name;
	
	/**
	 * ztree属性 是否展开
	 */
	@TableField(exist=false)
	private Boolean open;

	@TableField(exist=false)
	private List<?> list;

}
