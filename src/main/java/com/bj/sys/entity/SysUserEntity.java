package com.bj.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhph
 * @date 2020-04-21 14:26:20
 */
@ApiModel("系统用户")
@TableName("sys_user")
@Data
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("userId")
	@TableId(type=IdType.AUTO)
	private Long userId;

	@ApiModelProperty("客户账号")
	@NotBlank(message="客户账号不能为空")
	private String userName;

	@ApiModelProperty("密码")
	private String userPwd;

	@ApiModelProperty("盐值")
	private String pwdSalt;

	@ApiModelProperty("邮箱")
	private String userEmail;

	@ApiModelProperty("手机号")
	private String userMobile;

	@ApiModelProperty("状态  0：禁用   1：正常")
	private Integer userStatus;

	@ApiModelProperty("所属部门")
	@NotNull(message="所属部门不能为空")
	private Long deptId;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("性别:0:保密,1:男,2:女")
	private Integer userSex;

	@ApiModelProperty("客户姓名")
	@NotBlank(message="客户姓名不能为空")
	private String realName;

	/**
	 * 部门名称
	 */
	@TableField(exist=false)
	private String deptName;

	@TableField(exist=false)
	@ApiModelProperty("公司名称")
	private String companyName;

	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * 用户角色
	 */
	@TableField(exist=false)
	private String roleNames;


	/**
	 * 用户区域ids
	 */
	@TableField(exist=false)
	private List<String> areaIds;
}
