package com.bj.contract.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商表
 * 
 * @author contract
 * @email 1079626899@qq.com
 * @date 2020-06-08 10:41:06
 */
@Data
@TableName("supplier")
public class SupplierEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long supplierId;

	/**
	 * 供应商名称
	 */
	@TableField("supplier_name")
	private String supplierName;
	/**
	 * 统一社会信用代码
	 */
	private String creditCode;
	/**
	 * 法定代表人
	 */
	private String legaRepresentative;
	/**
	 * 所属地
	 */
	private String attribution;
	/**
	 * 注册资本
	 */
	private String registeredCapital;
	/**
	 * 经营状态，0：开业，1：续业，2：在业，3：续存，4：在营
	 */
	private Integer operatingStatus;
	/**
	 * 公司类型
	 */
	private String supplierType;
	/**
	 * 营业范围
	 */
	private String businessScope;
	/**
	 * 状态，0：黑名单，1：正常，2：已删除
	 */
	@TableLogic
	private Integer status;
	/**
	 * 资质
	 */
	private String qualifications;
	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 黑名单
	 */
	private Integer blackList;
}
