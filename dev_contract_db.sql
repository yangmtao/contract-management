/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.104
Source Server Version : 80017
Source Host           : 192.168.1.104:3306
Source Database       : dev_contract_db

Target Server Type    : MYSQL
Target Server Version : 80017
File Encoding         : 65001

Date: 2020-06-19 18:04:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `contract_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '合同id',
  `contract_name` varchar(50) NOT NULL COMMENT '合同名称',
  `contract_manager` bigint(20) NOT NULL COMMENT '合同经办人',
  `purchase_content` varchar(255) NOT NULL COMMENT '采购内容',
  `contract_amount` decimal(10,0) NOT NULL COMMENT '合同金额',
  `start_date` datetime NOT NULL COMMENT '开始时间',
  `end_date` datetime NOT NULL COMMENT '结束时间',
  `purchasing_dept_id` bigint(20) NOT NULL COMMENT '采购部门id',
  `demand_dept_id` bigint(20) NOT NULL COMMENT '需求部门',
  `contract_type` int(11) NOT NULL COMMENT '合同类型',
  `party_a_id` bigint(20) NOT NULL COMMENT '甲方',
  `party_b_id` bigint(20) NOT NULL COMMENT '乙方',
  `payment_type` int(11) NOT NULL COMMENT '支付类型',
  `contract_file` varchar(200) NOT NULL COMMENT '合同附件',
  `pay_status` int(11) NOT NULL COMMENT '支付状态',
  `contract_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同编码',
  `del_tag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0,未删除；1，逻辑删除',
  `create_date` datetime NOT NULL COMMENT '合同录入时间',
  PRIMARY KEY (`contract_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='合同信息表';

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES ('1', '对方嘎斯如果', '1', '啊大哥v发到付v', '50000', '2020-06-13 00:00:00', '2020-06-30 00:00:00', '10001', '10002', '3', '4', '555', '1', 'http://localhost:9001/localImage/18c2b9cb-04d4-4902-9193-e5c20c284554.jpg', '0', '1591943352414W', '0', '2020-06-17 14:53:40');
INSERT INTO `contract` VALUES ('2', '空调采购', '1', '采购空调五台', '60000', '2020-06-19 10:23:46', '2020-06-26 10:23:50', '10001', '10002', '3', '4', '555', '1', 'http://localhost:9001/localImage/18c2b9cb-04d4-4902-9193-e5c20c284554.jpg', '0', '214124434', '0', '2020-06-19 10:24:23');
INSERT INTO `contract` VALUES ('3', '盾构法代发', '2', '大厦打发法而发到付', '2000', '2020-06-20 00:00:00', '2020-06-30 00:00:00', '10003', '10001', '0', '5', '2', '0', 'http://localhost:9001/localImage/48046123-ccb1-476b-9f93-35ccaf7ab300.jpg', '0', '1592548802960', '0', '2020-06-19 14:40:03');
INSERT INTO `contract` VALUES ('4', '盾构法代发', '2', '撒大噶发嘎嘎', '2000', '2020-06-18 00:00:00', '2020-06-23 00:00:00', '10003', '10001', '0', '3', '2', '1', 'http://localhost:9001/localImage/4daf76d8-e2e7-401d-8cdc-bd9e8a7ed927.jpg', '0', '1592549644007', '0', '2020-06-19 14:54:04');
INSERT INTO `contract` VALUES ('6', 'dasgfadgfadf', '2', '啊大哥发噶好打发士大夫噶', '40000', '2020-06-25 00:00:00', '2020-06-30 00:00:00', '10003', '10001', '0', '4', '2', '0', 'http://localhost:9001/localImage/20a42805-f887-42db-bc65-5e597182279f.jpg', '0', '1592550028015', '0', '2020-06-19 15:00:28');

-- ----------------------------
-- Table structure for contract_change
-- ----------------------------
DROP TABLE IF EXISTS `contract_change`;
CREATE TABLE `contract_change` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `change_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变更名称',
  `change_reason` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变更原因',
  `contract_manager` bigint(20) DEFAULT NULL COMMENT '变更后的经办人，为空则未变更',
  `contract_id` bigint(20) NOT NULL COMMENT '合同id',
  `contract_amount` decimal(10,0) DEFAULT NULL COMMENT '变更后的合同金额，为空则未变更',
  `payment_date` datetime DEFAULT NULL COMMENT '变更后的支付时间，为空则未变更',
  `change_file` varchar(255) NOT NULL COMMENT '合同变更的协议原件图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同变更记录表';

-- ----------------------------
-- Records of contract_change
-- ----------------------------

-- ----------------------------
-- Table structure for contract_examine
-- ----------------------------
DROP TABLE IF EXISTS `contract_examine`;
CREATE TABLE `contract_examine` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同审查记录id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同id',
  `risk_level` enum('低','中','高') NOT NULL DEFAULT '中' COMMENT '合同审查风险等级；',
  `problem` varchar(255) NOT NULL COMMENT '审查问题',
  `status` tinyint(4) NOT NULL COMMENT '审查问题修正状态；0，待处理；1，处理中；2，已解决',
  `handle_way` varchar(255) DEFAULT NULL COMMENT '解决方案',
  `create_time` datetime NOT NULL COMMENT '提出时间',
  `handler` bigint(255) DEFAULT NULL COMMENT '处理人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_examine
-- ----------------------------

-- ----------------------------
-- Table structure for contract_payment_stage
-- ----------------------------
DROP TABLE IF EXISTS `contract_payment_stage`;
CREATE TABLE `contract_payment_stage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '合同付款阶段id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同id',
  `stage_name` varchar(50) NOT NULL,
  `uses` tinyint(4) NOT NULL COMMENT '付款用途',
  `payment_amount` decimal(10,0) NOT NULL COMMENT '付款金额',
  `payment_date` datetime NOT NULL COMMENT '付款时间',
  `payment_rate` decimal(10,0) NOT NULL COMMENT '付款比率',
  `payment_status` tinyint(4) DEFAULT '0' COMMENT '付款状态；0，未付；1，在付；2，已付',
  `paid_amount` decimal(10,0) DEFAULT '0' COMMENT '已支付金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_payment_stage
-- ----------------------------
INSERT INTO `contract_payment_stage` VALUES ('1', '6', '阶段一', '1', '40000', '2020-06-23 00:00:00', '100', '0', '0');

-- ----------------------------
-- Table structure for contract_review_record
-- ----------------------------
DROP TABLE IF EXISTS `contract_review_record`;
CREATE TABLE `contract_review_record` (
  `review_record_id` bigint(20) NOT NULL COMMENT '审查记录id',
  `risk_level` int(11) NOT NULL COMMENT '严重等级',
  `status` int(11) NOT NULL COMMENT '修正状态',
  `review_problem` varchar(255) NOT NULL COMMENT '审查问题',
  `solution_title` varchar(20) NOT NULL COMMENT '解决方案标题',
  `solution_detail` varchar(500) DEFAULT NULL COMMENT '解决方案详情',
  `contract_id` bigint(20) NOT NULL COMMENT '合同id',
  `put_time` datetime NOT NULL COMMENT '提出时间',
  `proposer` bigint(20) NOT NULL COMMENT '提出人',
  `solver` bigint(20) NOT NULL COMMENT '解决人',
  PRIMARY KEY (`review_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同审核记录表';

-- ----------------------------
-- Records of contract_review_record
-- ----------------------------

-- ----------------------------
-- Table structure for contract_risk
-- ----------------------------
DROP TABLE IF EXISTS `contract_risk`;
CREATE TABLE `contract_risk` (
  `id` bigint(20) NOT NULL,
  `contract_id` bigint(20) NOT NULL,
  `risk_type` varchar(255) DEFAULT NULL,
  `risk_name` varchar(255) DEFAULT NULL,
  `solution` varchar(1000) DEFAULT NULL,
  `del` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0正常，-1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contract_risk
-- ----------------------------
INSERT INTO `contract_risk` VALUES ('1', '1', '高', '没钱付', '找领导', '0');
INSERT INTO `contract_risk` VALUES ('2', '2', '低', '跑路', '辞职', '0');

-- ----------------------------
-- Table structure for contract_settlement
-- ----------------------------
DROP TABLE IF EXISTS `contract_settlement`;
CREATE TABLE `contract_settlement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `receive_bank` varchar(255) DEFAULT NULL COMMENT '收款银行',
  `receive_account` varchar(255) DEFAULT NULL COMMENT '收款账户',
  `receive_company` varchar(255) DEFAULT NULL COMMENT '收款单位',
  `receive_stage` varchar(255) DEFAULT NULL COMMENT '收款阶段',
  `receive_amount` int(11) DEFAULT NULL COMMENT '收款金额',
  `receive_time` datetime DEFAULT NULL COMMENT '收款时间',
  `contract_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='合同结算记录表';

-- ----------------------------
-- Records of contract_settlement
-- ----------------------------
INSERT INTO `contract_settlement` VALUES ('1', '农业', '124124', '重庆有线', '定金', '100000', '2020-06-18 16:07:58', '1');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `credit_code` varchar(50) NOT NULL COMMENT '统一社会信用代码',
  `lega_representative` varchar(20) NOT NULL COMMENT '法定代表人',
  `attribution` varchar(66) NOT NULL COMMENT '所属地',
  `registered_capital` varchar(50) NOT NULL COMMENT '注册资本',
  `operating_status` int(11) NOT NULL COMMENT '经营状态，0：开业，1：续业，2：在业，3：续存，4：在营',
  `supplier_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司类型',
  `business_scope` varchar(255) NOT NULL COMMENT '营业范围',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态，0：正常，-1：已删除',
  `supplier_name` varchar(255) NOT NULL COMMENT '供应商名字',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注',
  `qualifications` varchar(255) DEFAULT NULL COMMENT '资质',
  `black_list` int(11) DEFAULT '0' COMMENT '黑名单0不是，1是',
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商表';

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES ('2', '111', '雷军', '深圳', '10亿', '1', '科技', '信息服务', '0', '魅族', '表现好', null, '0');
INSERT INTO `supplier` VALUES ('3', '111', '雷军', '深圳', '10亿', '1', '科技', '信息服务', '0', '魅族', null, null, '1');
INSERT INTO `supplier` VALUES ('4', '112', '黄章', '北京', '10000000元', '1', '1', '科技', '-1', '小米', null, '甲', '1');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NOT NULL,
  `dept_name` varchar(25) DEFAULT NULL,
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '0,正常；1，删除',
  `sort_num` varchar(20) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10004 DEFAULT CHARSET=utf8 COMMENT='部门信息表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('10000', '0', '顶级部门', '0', '1');
INSERT INTO `sys_dept` VALUES ('10001', '10000', '研发部', '0', '2');
INSERT INTO `sys_dept` VALUES ('10002', '10000', '销售部', '0', '3');
INSERT INTO `sys_dept` VALUES ('10003', '10000', '采购部', '0', '3');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典值',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`,`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '性别', 'sex', '0', '女', '0', null, '0');
INSERT INTO `sys_dict` VALUES ('2', '性别', 'sex', '1', '男', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('3', '性别', 'sex', '2', '未知', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('4', '去', '去', 'ss', 's', '1', '1', '-1');
INSERT INTO `sys_dict` VALUES ('5', '经营状态', 'jyzt', '0', '在营', '1', '该企业正在营业', '0');
INSERT INTO `sys_dict` VALUES ('6', '经营状态', 'jyzt', '1', '注销', '2', '该企业已被注销', '0');
INSERT INTO `sys_dict` VALUES ('7', '经营状态', 'jyzt', '2', '吊销', '3', '企业已被吊销', '0');
INSERT INTO `sys_dict` VALUES ('8', '产品实例分类', 'instanceType', '10000', '深度报告', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('9', '产品实例分类', 'instanceType', '20000', '监控类', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('10', '产品实例分类', 'instanceType', '31000', '会员类', '2', '产品实例分类', '0');
INSERT INTO `sys_dict` VALUES ('12', '三方接口数据来源', 'apiSource', '1', '有数接口', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('13', '产品实例类型', 'instantType', '10001', '单次报告', '1', null, '-1');
INSERT INTO `sys_dict` VALUES ('14', '服务类型', 'serviceType', '1', '企业综合信用信息', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('15', '服务类型', 'serviceType', '2', '企业基本信息', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('16', '服务类型', 'serviceType', '3', '企业曾用名', '3', null, '0');
INSERT INTO `sys_dict` VALUES ('17', '服务类型', 'serviceType', '4', '企业最终持股人图谱', '4', null, '0');
INSERT INTO `sys_dict` VALUES ('18', '服务类型', 'serviceType', '5', '企业图谱', '5', null, '0');
INSERT INTO `sys_dict` VALUES ('19', '服务类型', 'serviceType', '6', '受益所有人', '6', null, '0');
INSERT INTO `sys_dict` VALUES ('20', '服务类型', 'serviceType', '7', '企业名称模糊查询', '7', null, '0');
INSERT INTO `sys_dict` VALUES ('21', '服务类型', 'serviceType', '8', '企业年报查询', '8', null, '0');
INSERT INTO `sys_dict` VALUES ('22', '服务类型', 'serviceType', '9', '企业法人认证三要素', '9', null, '0');
INSERT INTO `sys_dict` VALUES ('23', '服务类型', 'serviceType', '10', '企业关联人查询', '10', null, '0');
INSERT INTO `sys_dict` VALUES ('24', '服务类型', 'serviceType', '11', '企业整体涉诉情况', '11', null, '0');
INSERT INTO `sys_dict` VALUES ('25', '服务类型', 'serviceType', '12', '企业整体涉诉详情', '12', null, '0');
INSERT INTO `sys_dict` VALUES ('26', '服务类型', 'serviceType', '13', '企业舆情信息', '13', null, '0');
INSERT INTO `sys_dict` VALUES ('27', '服务类型', 'serviceType', '14', '企业风险排查', '14', null, '0');
INSERT INTO `sys_dict` VALUES ('28', '服务类型', 'serviceType', '15', '申请风险监控', '15', null, '0');
INSERT INTO `sys_dict` VALUES ('29', '服务类型', 'serviceType', '16', '暂停风险监控', '16', null, '0');
INSERT INTO `sys_dict` VALUES ('30', '服务类型', 'serviceType', '17', '作品著作权', '17', null, '0');
INSERT INTO `sys_dict` VALUES ('31', '服务类型', 'serviceType', '18', '软著信息', '18', null, '0');
INSERT INTO `sys_dict` VALUES ('32', '服务类型', 'serviceType', '19', '商标信息', '19', null, '0');
INSERT INTO `sys_dict` VALUES ('33', '服务类型', 'serviceType', '20', '专利信息', '20', null, '0');
INSERT INTO `sys_dict` VALUES ('34', '服务类型', 'serviceType', '21', '域名备案', '21', null, '0');
INSERT INTO `sys_dict` VALUES ('35', '客户所在企业类别', 'userEntType', '1', '房地产类', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('36', '客户所在企业类别', 'userEntType', '2', '工程施工类', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('37', '客户所在企业类别', 'userEntType', '3', '材料设备类', '3', null, '0');
INSERT INTO `sys_dict` VALUES ('39', '客户所在企业类别', 'userEntType', '4', '咨询服务类', '4', null, '0');
INSERT INTO `sys_dict` VALUES ('40', '客户所在企业类别', 'userEntType', '5', '设计类', '5', null, '0');
INSERT INTO `sys_dict` VALUES ('41', '客户所在企业类别', 'userEntType', '999', '其它', '999', null, '0');
INSERT INTO `sys_dict` VALUES ('42', '三方接口数据来源', 'apiSource', '2', '汇法网', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('43', '产品分类', 'productType', '10000', '信用决策报告', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('44', '产品分类', 'productType', '10001', '招投标报告', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('45', '产品实例分类', 'instanceType', '10001', '招投标报告', '2', null, '0');
INSERT INTO `sys_dict` VALUES ('46', '产品分类', 'productType', '20000', '风险监控', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('64', '服务类型', 'serviceType', '22', '企业股权穿透分析报告', '22', null, '0');
INSERT INTO `sys_dict` VALUES ('66', '服务类型', 'serviceType', '1001', '被执行人', '1001', '被执行人', '0');
INSERT INTO `sys_dict` VALUES ('67', '服务类型', 'serviceType', '1002', '失信被执行人', '1002', '失信被执行人', '0');
INSERT INTO `sys_dict` VALUES ('68', '服务类型', 'serviceType', '1004', '审判流程', '1004', '审判流程', '0');
INSERT INTO `sys_dict` VALUES ('69', '服务类型', 'serviceType', '1003', '裁判文书', '1003', '裁判文书', '0');
INSERT INTO `sys_dict` VALUES ('70', '服务类型', 'serviceType', '1005', '欠税信息', '1005', '欠税信息', '0');
INSERT INTO `sys_dict` VALUES ('71', '服务类型', 'serviceType', '1006', '非正常户', '1006', '非正常户', '0');
INSERT INTO `sys_dict` VALUES ('72', '服务类型', 'serviceType', '1007', '催收信息', '1007', '催收信息', '0');
INSERT INTO `sys_dict` VALUES ('73', '产品分类', 'productType', '10002', '汇法网报告', '10002', '汇法网报告', '0');
INSERT INTO `sys_dict` VALUES ('74', '产品实例分类', 'instanceType', '10002', '汇法网报告', '10002', '汇法网报告', '0');
INSERT INTO `sys_dict` VALUES ('75', '风险项类型', 'riskType', 'basicList', '工商信息', '1', '工商信息', '0');
INSERT INTO `sys_dict` VALUES ('76', '风险项类型', 'riskTpye', 'shareHolderList', '股东出资', '2', '股东出资', '0');
INSERT INTO `sys_dict` VALUES ('77', '风险项类型', 'riskType', 'personList', '高管任职', '3', '高管任职', '0');
INSERT INTO `sys_dict` VALUES ('78', '风险项类型', 'riskType', 'entinvItemList', '企业对外投资', '4', '企业对外投资', '0');
INSERT INTO `sys_dict` VALUES ('79', '风险项类型', 'riskType', 'frinvList', '法人对外投资', '5', '法人对外投资', '0');
INSERT INTO `sys_dict` VALUES ('80', '风险项类型', 'riskType', 'frPositionList', '法人任职信息', '6', '法人任职信息', '0');
INSERT INTO `sys_dict` VALUES ('81', '风险项类型', 'riskType', 'alterList', '工商变更', '7', '工商变更', '0');
INSERT INTO `sys_dict` VALUES ('82', '风险项类型', 'riskType', 'filiationList', '分支机构', '8', '分支机构', '0');
INSERT INTO `sys_dict` VALUES ('83', '风险项类型', 'riskType', 'caseInfoList', '行政处罚', '9', '行政处罚', '0');
INSERT INTO `sys_dict` VALUES ('84', '风险项类型', 'riskType', 'sharesFrostList', '股权冻结', '10', '股权冻结', '0');
INSERT INTO `sys_dict` VALUES ('85', '风险项类型', 'riskType', 'sharesImpawnList', '股权出质', '11', '股权出质', '0');
INSERT INTO `sys_dict` VALUES ('86', '风险项类型', 'riskType', 'morDetailList', '动产抵押', '12', '动产抵押', '0');
INSERT INTO `sys_dict` VALUES ('87', '风险项类型', 'riskType', 'exceptionList', '经营异常', '13', '经营异常', '0');
INSERT INTO `sys_dict` VALUES ('88', '风险项类型', 'riskType', 'illegalList', '严重违法失信', '14', '严重违法失信', '0');
INSERT INTO `sys_dict` VALUES ('89', '风险项类型', 'riskType', 'judicialRiskList', '司法信息', '15', '司法信息', '0');
INSERT INTO `sys_dict` VALUES ('90', '风险项类型', 'riskType', 'executiveList', '执行公告', '16', '执行公告', '0');
INSERT INTO `sys_dict` VALUES ('91', '风险项类型', 'riskType', 'judgementList', '裁判文书', '17', '裁判文书', '0');
INSERT INTO `sys_dict` VALUES ('92', '风险项类型', 'riskType', 'courtList', '法院公告', '18', '法院公告', '0');
INSERT INTO `sys_dict` VALUES ('93', '风险项类型', 'riskType', 'sessionList', '开庭公告', '19', '开庭公告', '0');
INSERT INTO `sys_dict` VALUES ('94', '风险项类型', 'riskType', 'punishBreakList', '失信公告', '20', '失信公告', '0');
INSERT INTO `sys_dict` VALUES ('95', '风险项类型', 'riskType', 'publicSentimentList', '舆情信息', '21', '舆情信息', '0');
INSERT INTO `sys_dict` VALUES ('96', '风险项类型', 'riskType', 'newsList', '新闻舆情', '22', '新闻舆情', '0');
INSERT INTO `sys_dict` VALUES ('97', '风险项类型', 'riskType', 'wechatList', '微信舆情', '23', '微信舆情', '0');
INSERT INTO `sys_dict` VALUES ('98', '行业指标', 'industryItem', '1', '资产负债率（%）', '1', '资产负债率（%）', '0');
INSERT INTO `sys_dict` VALUES ('99', '行业指标', 'industryItem', '2', '现金流动负债比率（%）', '2', '现金流动负债比率（%）', '0');
INSERT INTO `sys_dict` VALUES ('100', '行业指标', 'industryItem', '3', '速动比率（%）', '3', '速动比率（%）', '0');
INSERT INTO `sys_dict` VALUES ('101', '行业指标', 'industryItem', '4', '总资产周转率（次）', '4', '总资产周转率（次）', '0');
INSERT INTO `sys_dict` VALUES ('102', '行业指标', 'industryItem', '5', '应收账款周转率（次）', '5', '应收账款周转率（次）', '0');
INSERT INTO `sys_dict` VALUES ('103', '行业指标', 'industryItem', '6', '流动资产周转率（次）', '6', '流动资产周转率（次）', '0');
INSERT INTO `sys_dict` VALUES ('104', '行业指标', 'industryItem', '7', '净资产收益率（%）', '7', '净资产收益率（%）', '0');
INSERT INTO `sys_dict` VALUES ('105', '行业标准', 'industryItem', '8', 'EBITDA率（%）', '8', 'EBITDA率（%）', '0');
INSERT INTO `sys_dict` VALUES ('106', '行业指标', 'industryItem', '9', '成本费用利润率（%）', '9', '成本费用利润率（%）', '0');
INSERT INTO `sys_dict` VALUES ('107', '行业指标', 'industryItem', '10', '营业收入增长率（%）', '10', '营业收入增长率（%）', '0');
INSERT INTO `sys_dict` VALUES ('108', '行业指标', 'industryItem', '11', '资本保值增值率（%）', '11', '资本保值增值率（%）', '0');
INSERT INTO `sys_dict` VALUES ('109', '行业指标', 'industryItem', '12', '营业利润增长率（%）', '12', '营业利润增长率（%）', '0');
INSERT INTO `sys_dict` VALUES ('110', '行业分类', 'industryType', '999', '其它', '999', '其它', '0');
INSERT INTO `sys_dict` VALUES ('111', '行业分类', 'industryType', '1', '建筑行业', '1', '建筑行业', '0');
INSERT INTO `sys_dict` VALUES ('114', '产品实例分类', 'instanceType', '30000', 'SVIP套餐', '1', 'SVIP套餐', '0');
INSERT INTO `sys_dict` VALUES ('116', '三方数据接口来源', 'apiSource', '6', '中数', '6', '中数', '0');
INSERT INTO `sys_dict` VALUES ('117', '客户所在企业类别', 'userEntType', '6', '勘察类', '6', null, '0');
INSERT INTO `sys_dict` VALUES ('120', '风险项大类', 'parentRiskType', '1', '经营信息', '1', '经营信息', '0');
INSERT INTO `sys_dict` VALUES ('121', '风险项大类', 'parentRiskType', '2', '工商变更', '2', '工商变更', '0');
INSERT INTO `sys_dict` VALUES ('122', '风险项大类', 'parentRiskType', '3', '经营风险', '3', '经营风险', '0');
INSERT INTO `sys_dict` VALUES ('123', '风险项大类', 'parentRiskType', '4', '司法诉讼', '4', '司法诉讼', '0');
INSERT INTO `sys_dict` VALUES ('127', '产品实例分类', 'instanceType', '40000', '叠加包', '3', '叠加包', '0');
INSERT INTO `sys_dict` VALUES ('128', '服务类型', 'serviceType', '2001', '电子签章开户', '1', '电子签章开户', '0');
INSERT INTO `sys_dict` VALUES ('129', '服务类型', 'serviceType', '2002', '电子签章印章生成', '2', '电子签章印章生成', '0');
INSERT INTO `sys_dict` VALUES ('130', '服务类型', 'serviceType', '2003', '签名待签名数据', '3', '电子签章签名待签名数据', '0');
INSERT INTO `sys_dict` VALUES ('131', '三方接口数据来源', 'apiSource', '3', '大陆云盾', '3', '大陆云盾电子签', '0');
INSERT INTO `sys_dict` VALUES ('135', '服务类型', 'serviceType', '1021', '司法总接口', '1021', '司法总接口', '0');
INSERT INTO `sys_dict` VALUES ('136', '客户所在企业类别', 'userEntType', '7', '医疗类', '7', null, '0');
INSERT INTO `sys_dict` VALUES ('137', '客户所在企业类别', 'userEntType', '8', '高校类', '8', '高校类', '0');
INSERT INTO `sys_dict` VALUES ('138', '客户所在企业类别', 'userEntType', '9', '食品类', '9', '食品类', '0');
INSERT INTO `sys_dict` VALUES ('139', '客户所在企业类别', 'userEntType', '91', '军工类', '91', null, '0');
INSERT INTO `sys_dict` VALUES ('140', '产品实例分类', 'instanceType', '10003', '招投标信用报告(增值版)', '10003', null, '0');
INSERT INTO `sys_dict` VALUES ('141', '产品分类', 'productType', '10003', '招投标信用报告(增值版)', '10003', null, '0');
INSERT INTO `sys_dict` VALUES ('142', '三方接口数据来源', 'apiSource', '4', '国信', '4', '国信发票数据', '0');
INSERT INTO `sys_dict` VALUES ('143', '三方接口数据来源', 'apiSource', '5', '启信宝', '5', '启信宝', '0');
INSERT INTO `sys_dict` VALUES ('144', '服务类型', 'serviceType', '3001', '股权穿透', '3001', '股权穿透', '0');
INSERT INTO `sys_dict` VALUES ('145', '服务类型', 'serviceType', '4001', '行政许可', '4001', '行政许可', '0');
INSERT INTO `sys_dict` VALUES ('146', '服务类型', 'serviceType', '4002', '资质认证', '4002', '资质认证', '0');
INSERT INTO `sys_dict` VALUES ('148', '服务类型', 'serviceType', '2005', '同步保全', '2005', null, '0');
INSERT INTO `sys_dict` VALUES ('149', '服务类型', 'serviceType', '2006', '保全文件上传', '2006', null, '0');
INSERT INTO `sys_dict` VALUES ('150', '服务类型', 'serviceType', '3002', '族谱查询', '3002', '族谱查询', '0');
INSERT INTO `sys_dict` VALUES ('151', '产品分类', 'productType', '10004', '企业信用报告（金融版）', '10004', null, '0');
INSERT INTO `sys_dict` VALUES ('152', '产品实例分类', 'instanceType', '10004', '企业信用报告（金融版）', '10004', null, '0');
INSERT INTO `sys_dict` VALUES ('153', '产品实例分类', 'instanceType', '32000', '自定义套餐', '32000', '自定义套餐', '0');
INSERT INTO `sys_dict` VALUES ('154', '产品实例分类', 'instanceType', '32001', '自定义套餐一', '32001', null, '0');
INSERT INTO `sys_dict` VALUES ('155', '产品实例分类', 'instanceType', '32002', '自定义套餐二', '32002', null, '0');
INSERT INTO `sys_dict` VALUES ('156', '产品分类', 'productType', '19999', '报告预评分', '19999', null, '0');
INSERT INTO `sys_dict` VALUES ('157', '产品实例分类', 'instanceType', '19999', '报告预评分', '19999', null, '0');
INSERT INTO `sys_dict` VALUES ('158', '客户所在企业类别', 'userEntType', '10', '个体工商户类', '10', null, '0');
INSERT INTO `sys_dict` VALUES ('159', '使用平台', 'usePlatform', '1', '房企平台', '1', '房企平台', '0');
INSERT INTO `sys_dict` VALUES ('160', '使用平台', 'usePlatform', '2', '医疗平台', '2', '医疗平台', '0');
INSERT INTO `sys_dict` VALUES ('161', '使用平台', 'usePlatform', '3', '食品平台', '3', '食品平台', '0');
INSERT INTO `sys_dict` VALUES ('162', '使用平台', 'usePlatform', '4', '部队及高校平台', '4', '部队及高校平台', '0');
INSERT INTO `sys_dict` VALUES ('163', '使用平台', 'usePlatform', '5', '金融平台', '5', '金融平台', '0');
INSERT INTO `sys_dict` VALUES ('164', '服务类型', 'serviceType', '9997', '国信发票数据抽取', '9997', '国信发票数据抽取', '0');
INSERT INTO `sys_dict` VALUES ('165', '服务类型', 'serviceType', '2040', '司法总接口', '2040', '司法总接口', '-1');
INSERT INTO `sys_dict` VALUES ('167', '服务类型', 'serviceType', '2004', '保全握手', '2004', '保全握手', '0');
INSERT INTO `sys_dict` VALUES ('168', '性别', '性别', '1', '男', '1', null, '-1');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `menu_name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单URL',
  `menu_perms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `menu_type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `menu_icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单图标',
  `sort_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`),
  KEY `index_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=110214 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('100000', '0', '系统管理', null, null, '0', 'fa fa-cog', '999');
INSERT INTO `sys_menu` VALUES ('100100', '100000', '菜单管理', 'sys/menu.html', null, '1', 'fa fa-list', '10');
INSERT INTO `sys_menu` VALUES ('100101', '100100', '查看', null, 'sys:menu:list,sys:menu:info', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100102', '100100', '新增', null, 'sys:menu:save,sys:menu:select', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100103', '100100', '修改', null, 'sys:menu:update,sys:menu:select', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100104', '100100', '删除', null, 'sys:menu:delete', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100200', '100000', '用户管理', 'sys/user.html', null, '1', 'fa fa-list', '10');
INSERT INTO `sys_menu` VALUES ('100201', '100200', '新增', null, 'sys:user:save,sys:role:select', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100202', '100200', '查看', null, 'sys:user:list,sys:user:info', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100203', '100200', '修改', null, 'sys:user:update,sys:role:select', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100204', '100200', '删除', null, 'sys:user:delete', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100300', '100000', '部门管理', 'sys/dept.html', null, '1', 'fa fa-list', '10');
INSERT INTO `sys_menu` VALUES ('100301', '100300', '查看', null, 'sys:dept:list,sys:dept:info', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100302', '100300', '新增', null, 'sys:dept:save,sys:dept:select', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100303', '100300', '修改', null, 'sys:dept:update,sys:dept:select', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100304', '100300', '删除', null, 'sys:dept:delete', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100400', '100000', '角色管理', 'sys/role.html', null, '1', 'fa fa-list', '10');
INSERT INTO `sys_menu` VALUES ('100401', '100400', '查看', null, 'sys:role:list,sys:role:info', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100402', '100400', '新增', null, 'sys:role:save,sys:menu:perms', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100403', '100400', '修改', null, 'sys:role:update,sys:menu:perms', '2', null, null);
INSERT INTO `sys_menu` VALUES ('100404', '100400', '删除', null, 'sys:role:delete', '2', null, null);
INSERT INTO `sys_menu` VALUES ('110000', '0', '合同管理', null, null, '0', 'fa fa-file', '99');
INSERT INTO `sys_menu` VALUES ('110100', '110000', '合同录入', '/contract/add.html', 'contract:save', '1', 'fa fa-list', '10');
INSERT INTO `sys_menu` VALUES ('110200', '110000', '合同查询', '/contract/query.html', 'contract:select,contract:list', '1', 'fa fa-list', '10');
INSERT INTO `sys_menu` VALUES ('110201', '110000', '供应商管理', 'contract/supplier.html', 'contract:supplier:list', '1', 'fa fa-list', '4');
INSERT INTO `sys_menu` VALUES ('110202', '110000', '合同审查记录', '/contract/examine.html', 'contract:examine:list', '1', 'fa fa-list', '5');
INSERT INTO `sys_menu` VALUES ('110203', '110201', '新增', null, 'contract:supplier:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('110204', '110201', '详情', null, 'contract:supplier:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('110205', '110201', '修改', null, 'contract:supplier:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('110206', '110201', '删除', null, 'contract:supplier:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('110207', '110201', '查看黑名单', null, 'contract:supplier:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('110208', '110000', '合同审签管理', null, null, '0', 'fa fa-list', '0');
INSERT INTO `sys_menu` VALUES ('110209', '110208', '我的代办任务', 'contract/myMission.html', 'contract:myMission:list', '1', null, '0');
INSERT INTO `sys_menu` VALUES ('110210', '110000', '合同变更', '/contract/change.html', 'contract:change', '1', 'fa fa-list', '0');
INSERT INTO `sys_menu` VALUES ('110211', '110208', '历史记录', 'contract/userReviewRecord.html', 'contract:userReviewRecord:list', '1', null, '0');
INSERT INTO `sys_menu` VALUES ('110212', '110000', '合同结算管理', 'contract/settlement.html', 'contract:settlement:list', '1', 'fa fa-list', '0');
INSERT INTO `sys_menu` VALUES ('110213', '110000', '合同风险管理', 'contract/risk.html', 'contract:risk:list', '1', 'fa fa-list', '0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(10) NOT NULL,
  `remark` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `dept_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'admin', '系统管理员', '10000', '2020-06-01 17:15:21');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` int(11) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `dept_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('332', '1', '100000');
INSERT INTO `sys_role_menu` VALUES ('333', '1', '100100');
INSERT INTO `sys_role_menu` VALUES ('334', '1', '100101');
INSERT INTO `sys_role_menu` VALUES ('335', '1', '100102');
INSERT INTO `sys_role_menu` VALUES ('336', '1', '100103');
INSERT INTO `sys_role_menu` VALUES ('337', '1', '100104');
INSERT INTO `sys_role_menu` VALUES ('338', '1', '100200');
INSERT INTO `sys_role_menu` VALUES ('339', '1', '100201');
INSERT INTO `sys_role_menu` VALUES ('340', '1', '100202');
INSERT INTO `sys_role_menu` VALUES ('341', '1', '100203');
INSERT INTO `sys_role_menu` VALUES ('342', '1', '100204');
INSERT INTO `sys_role_menu` VALUES ('343', '1', '100300');
INSERT INTO `sys_role_menu` VALUES ('344', '1', '100301');
INSERT INTO `sys_role_menu` VALUES ('345', '1', '100302');
INSERT INTO `sys_role_menu` VALUES ('346', '1', '100303');
INSERT INTO `sys_role_menu` VALUES ('347', '1', '100304');
INSERT INTO `sys_role_menu` VALUES ('348', '1', '100400');
INSERT INTO `sys_role_menu` VALUES ('349', '1', '100401');
INSERT INTO `sys_role_menu` VALUES ('350', '1', '100402');
INSERT INTO `sys_role_menu` VALUES ('351', '1', '100403');
INSERT INTO `sys_role_menu` VALUES ('352', '1', '100404');
INSERT INTO `sys_role_menu` VALUES ('353', '1', '110000');
INSERT INTO `sys_role_menu` VALUES ('354', '1', '110100');
INSERT INTO `sys_role_menu` VALUES ('355', '1', '110200');
INSERT INTO `sys_role_menu` VALUES ('356', '1', '110201');
INSERT INTO `sys_role_menu` VALUES ('357', '1', '110203');
INSERT INTO `sys_role_menu` VALUES ('358', '1', '110204');
INSERT INTO `sys_role_menu` VALUES ('359', '1', '110205');
INSERT INTO `sys_role_menu` VALUES ('360', '1', '110206');
INSERT INTO `sys_role_menu` VALUES ('361', '1', '110207');
INSERT INTO `sys_role_menu` VALUES ('362', '1', '110202');
INSERT INTO `sys_role_menu` VALUES ('363', '1', '110208');
INSERT INTO `sys_role_menu` VALUES ('364', '1', '110209');
INSERT INTO `sys_role_menu` VALUES ('365', '1', '110211');
INSERT INTO `sys_role_menu` VALUES ('366', '1', '110210');
INSERT INTO `sys_role_menu` VALUES ('367', '1', '110212');
INSERT INTO `sys_role_menu` VALUES ('368', '1', '110213');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(25) NOT NULL,
  `user_pwd` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `real_name` varchar(15) DEFAULT NULL,
  `user_sex` tinyint(4) DEFAULT '0' COMMENT '0,未知；1，男；2，女',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `user_status` tinyint(4) DEFAULT NULL COMMENT '1，正常；2，停用；3，删除',
  `user_mobile` int(11) DEFAULT NULL COMMENT '电话号码',
  `pwd_salt` varchar(20) DEFAULT NULL COMMENT '加密盐',
  `user_email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'e1153123d7d180ceeb820d577ff119876678732a68eef4e6ffc0b1f06a01f91b', '2020-05-20 11:10:57', '2020-05-25 13:46:20', '管理员', '0', '10000', '1', null, 'YzcmCZNvbXocrsz9dm8e', null);
INSERT INTO `sys_user` VALUES ('2', 'ymt', 'c4eadbde11db4c1ddae74983adac2c9bc2e0c5647a9af5b8ed1d6dc65b011332', '2020-06-17 14:35:00', null, '杨明桃', '1', '10001', '1', null, '5MhqxcTriqoMsOvoWAFI', null);

-- ----------------------------
-- Table structure for sys_user_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_operation`;
CREATE TABLE `sys_user_operation` (
  `op_id` bigint(20) NOT NULL COMMENT '操作记录id',
  `user_id` int(11) NOT NULL,
  `user_ip` varchar(25) DEFAULT NULL,
  `user_name` varchar(25) DEFAULT NULL,
  `op_module` varchar(25) DEFAULT NULL,
  `op_content` varchar(25) DEFAULT NULL,
  `op_desc` varchar(50) DEFAULT NULL COMMENT '操作描述',
  `op_date` datetime NOT NULL,
  `op_source` int(11) DEFAULT NULL COMMENT '1，前台；2，后台',
  PRIMARY KEY (`op_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_operation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for user_review_record
-- ----------------------------
DROP TABLE IF EXISTS `user_review_record`;
CREATE TABLE `user_review_record` (
  `id` bigint(20) NOT NULL,
  `contarct_id` bigint(20) NOT NULL COMMENT '合同的id',
  `review_advise` varchar(1000) DEFAULT NULL COMMENT '审核意见',
  `review_result` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `reviewer` varchar(255) DEFAULT NULL COMMENT '审核的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_review_record
-- ----------------------------
INSERT INTO `user_review_record` VALUES ('1', '1', '没有', '通过', 'admin');

-- ----------------------------
-- Function structure for queryFinRootDeptInfo
-- ----------------------------
DROP FUNCTION IF EXISTS `queryFinRootDeptInfo`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `queryFinRootDeptInfo`(deptId INT) RETURNS varchar(255) CHARSET utf8
    READS SQL DATA
BEGIN
DECLARE tempPid BIGINT(20);
DECLARE tempName VARCHAR(255);
SELECT dept_name,parent_id INTO tempName,tempPid FROM sys_dept WHERE dept_id = deptId;
WHILE tempPid <> 0 DO
SELECT dept_name,parent_id INTO tempName,tempPid FROM sys_dept WHERE dept_id = tempPid;
END WHILE;
RETURN tempName;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for queryRootDepInfo
-- ----------------------------
DROP FUNCTION IF EXISTS `queryRootDepInfo`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `queryRootDepInfo`(deptId INT) RETURNS varchar(255) CHARSET utf8
    READS SQL DATA
BEGIN
DECLARE tempPid BIGINT(20);
DECLARE tempName VARCHAR(255);
SELECT name,parent_id INTO tempName,tempPid FROM sys_dept WHERE dept_id = deptId;
WHILE tempPid <> 0 DO
SELECT name,parent_id INTO tempName,tempPid FROM sys_dept WHERE dept_id = tempPid;
END WHILE;
RETURN tempName;
END
;;
DELIMITER ;
