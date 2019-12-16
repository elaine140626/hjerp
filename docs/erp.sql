/*
Navicat MySQL Data Transfer
Date: 2019-11-28 18:48:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jsh_account
-- ----------------------------
DROP TABLE IF EXISTS `jsh_account`;
CREATE TABLE `jsh_account` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `SerialNo` varchar(50) DEFAULT NULL COMMENT '编号',
  `InitialAmount` decimal(24,6) DEFAULT NULL COMMENT '期初金额',
  `CurrentAmount` decimal(24,6) DEFAULT NULL COMMENT '当前余额',
  `Remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `IsDefault` bit(1) DEFAULT NULL COMMENT '是否默认',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='账户信息';

-- ----------------------------
-- Records of jsh_account
-- ----------------------------
INSERT INTO `jsh_account` VALUES ('21', '农行', '62284824156727988', '80000.000000', null, '', '\0', '63', '0');
INSERT INTO `jsh_account` VALUES ('22', '工行', '62220919281726655726', '50000.000000', null, '', '', '63', '0');

-- ----------------------------
-- Table structure for jsh_accounthead
-- ----------------------------
DROP TABLE IF EXISTS `jsh_accounthead`;
CREATE TABLE `jsh_accounthead` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Type` varchar(50) DEFAULT NULL COMMENT '类型(支出/收入/收款/付款/转账)',
  `OrganId` bigint(20) DEFAULT NULL COMMENT '单位Id(收款/付款单位)',
  `HandsPersonId` bigint(20) DEFAULT NULL COMMENT '经手人Id',
  `ChangeAmount` decimal(24,6) DEFAULT NULL COMMENT '变动金额(优惠/收款/付款/实付)',
  `TotalPrice` decimal(24,6) DEFAULT NULL COMMENT '合计金额',
  `AccountId` bigint(20) DEFAULT NULL COMMENT '账户(收款/付款)',
  `BillNo` varchar(50) DEFAULT NULL COMMENT '单据编号',
  `BillTime` datetime DEFAULT NULL COMMENT '单据日期',
  `Remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`),
  KEY `FK9F4C0D8DB610FC06` (`OrganId`),
  KEY `FK9F4C0D8DAAE50527` (`AccountId`),
  KEY `FK9F4C0D8DC4170B37` (`HandsPersonId`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COMMENT='财务主表';

-- ----------------------------
-- Records of jsh_accounthead
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_accountitem
-- ----------------------------
DROP TABLE IF EXISTS `jsh_accountitem`;
CREATE TABLE `jsh_accountitem` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `HeaderId` bigint(20) NOT NULL COMMENT '表头Id',
  `AccountId` bigint(20) DEFAULT NULL COMMENT '账户Id',
  `InOutItemId` bigint(20) DEFAULT NULL COMMENT '收支项目Id',
  `EachAmount` decimal(24,6) DEFAULT NULL COMMENT '单项金额',
  `Remark` varchar(100) DEFAULT NULL COMMENT '单据备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`),
  KEY `FK9F4CBAC0AAE50527` (`AccountId`),
  KEY `FK9F4CBAC0C5FE6007` (`HeaderId`),
  KEY `FK9F4CBAC0D203EDC5` (`InOutItemId`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COMMENT='财务子表';

-- ----------------------------
-- Records of jsh_accountitem
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_depot
-- ----------------------------
DROP TABLE IF EXISTS `jsh_depot`;
CREATE TABLE `jsh_depot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(50) DEFAULT NULL COMMENT '仓库地址',
  `warehousing` decimal(24,6) DEFAULT NULL COMMENT '仓储费',
  `truckage` decimal(24,6) DEFAULT NULL COMMENT '搬运费',
  `type` int(10) DEFAULT NULL COMMENT '类型',
  `sort` varchar(10) DEFAULT NULL COMMENT '排序',
  `remark` varchar(100) DEFAULT NULL COMMENT '描述',
  `principal` bigint(20) DEFAULT NULL COMMENT '负责人',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  `is_default` bit(1) DEFAULT NULL COMMENT '是否默认',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='仓库表';

-- ----------------------------
-- Records of jsh_depot
-- ----------------------------
INSERT INTO `jsh_depot` VALUES ('16', '达美1号仓', '北京通州', '200.000000', '15.000000', '0', '1', '', null, '63', '0', '');

-- ----------------------------
-- Table structure for jsh_depothead
-- ----------------------------
DROP TABLE IF EXISTS `jsh_depothead`;
CREATE TABLE `jsh_depothead` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Type` varchar(50) DEFAULT NULL COMMENT '类型(出库/入库)',
  `SubType` varchar(50) DEFAULT NULL COMMENT '出入库分类',
  `ProjectId` bigint(20) DEFAULT NULL COMMENT '项目Id',
  `DefaultNumber` varchar(50) DEFAULT NULL COMMENT '初始票据号',
  `Number` varchar(50) DEFAULT NULL COMMENT '票据号',
  `OperPersonName` varchar(50) DEFAULT NULL COMMENT '操作员名字',
  `CreateTime` datetime DEFAULT NULL COMMENT '创建时间',
  `OperTime` datetime DEFAULT NULL COMMENT '出入库时间',
  `OrganId` bigint(20) DEFAULT NULL COMMENT '供应商Id',
  `HandsPersonId` bigint(20) DEFAULT NULL COMMENT '采购/领料-经手人Id',
  `AccountId` bigint(20) DEFAULT NULL COMMENT '账户Id',
  `ChangeAmount` decimal(24,6) DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `AllocationProjectId` bigint(20) DEFAULT NULL COMMENT '调拨时，对方项目Id',
  `TotalPrice` decimal(24,6) DEFAULT NULL COMMENT '合计金额',
  `PayType` varchar(50) DEFAULT NULL COMMENT '付款类型(现金、记账等)',
  `Remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `Salesman` varchar(50) DEFAULT NULL COMMENT '业务员（可以多个）',
  `AccountIdList` varchar(50) DEFAULT NULL COMMENT '多账户ID列表',
  `AccountMoneyList` varchar(200) DEFAULT '' COMMENT '多账户金额列表',
  `Discount` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `DiscountMoney` decimal(24,6) DEFAULT NULL COMMENT '优惠金额',
  `DiscountLastMoney` decimal(24,6) DEFAULT NULL COMMENT '优惠后金额',
  `OtherMoney` decimal(24,6) DEFAULT NULL COMMENT '销售或采购费用合计',
  `OtherMoneyList` varchar(200) DEFAULT NULL COMMENT '销售或采购费用涉及项目Id数组（包括快递、招待等）',
  `OtherMoneyItem` varchar(200) DEFAULT NULL COMMENT '销售或采购费用涉及项目（包括快递、招待等）',
  `AccountDay` int(10) DEFAULT NULL COMMENT '结算天数',
  `Status` varchar(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核、2已转采购|销售',
  `LinkNumber` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`),
  KEY `FK2A80F214CA633ABA` (`AllocationProjectId`),
  KEY `FK2A80F214C4170B37` (`HandsPersonId`),
  KEY `FK2A80F214B610FC06` (`OrganId`),
  KEY `FK2A80F2142888F9A` (`ProjectId`),
  KEY `FK2A80F214AAE50527` (`AccountId`)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8 COMMENT='单据主表';

-- ----------------------------
-- Records of jsh_depothead
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_depotitem
-- ----------------------------
DROP TABLE IF EXISTS `jsh_depotitem`;
CREATE TABLE `jsh_depotitem` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `HeaderId` bigint(20) NOT NULL COMMENT '表头Id',
  `MaterialId` bigint(20) NOT NULL COMMENT '材料Id',
  `MUnit` varchar(20) DEFAULT NULL COMMENT '商品计量单位',
  `OperNumber` decimal(24,6) DEFAULT NULL COMMENT '数量',
  `BasicNumber` decimal(24,6) DEFAULT NULL COMMENT '基础数量，如kg、瓶',
  `UnitPrice` decimal(24,6) DEFAULT NULL COMMENT '单价',
  `TaxUnitPrice` decimal(24,6) DEFAULT NULL COMMENT '含税单价',
  `AllPrice` decimal(24,6) DEFAULT NULL COMMENT '金额',
  `Remark` varchar(200) DEFAULT NULL COMMENT '描述',
  `Img` varchar(50) DEFAULT NULL COMMENT '图片',
  `Incidentals` decimal(24,6) DEFAULT NULL COMMENT '运杂费',
  `DepotId` bigint(20) DEFAULT NULL COMMENT '仓库ID（库存是统计出来的）',
  `AnotherDepotId` bigint(20) DEFAULT NULL COMMENT '调拨时，对方仓库Id',
  `TaxRate` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `TaxMoney` decimal(24,6) DEFAULT NULL COMMENT '税额',
  `TaxLastMoney` decimal(24,6) DEFAULT NULL COMMENT '价税合计',
  `OtherField1` varchar(50) DEFAULT NULL COMMENT '自定义字段1-品名',
  `OtherField2` varchar(50) DEFAULT NULL COMMENT '自定义字段2-型号',
  `OtherField3` varchar(50) DEFAULT NULL COMMENT '自定义字段3-制造商',
  `OtherField4` varchar(50) DEFAULT NULL COMMENT '自定义字段4',
  `OtherField5` varchar(50) DEFAULT NULL COMMENT '自定义字段5',
  `MType` varchar(20) DEFAULT NULL COMMENT '商品类型',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`),
  KEY `FK2A819F475D61CCF7` (`MaterialId`),
  KEY `FK2A819F474BB6190E` (`HeaderId`),
  KEY `FK2A819F479485B3F5` (`DepotId`),
  KEY `FK2A819F47729F5392` (`AnotherDepotId`)
) ENGINE=InnoDB AUTO_INCREMENT=342 DEFAULT CHARSET=utf8 COMMENT='单据子表';

-- ----------------------------
-- Records of jsh_depotitem
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_functions
-- ----------------------------
DROP TABLE IF EXISTS `jsh_functions`;
CREATE TABLE `jsh_functions` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Number` varchar(50) DEFAULT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `PNumber` varchar(50) DEFAULT NULL COMMENT '上级编号',
  `URL` varchar(100) DEFAULT NULL COMMENT '链接',
  `State` bit(1) DEFAULT NULL COMMENT '收缩',
  `Sort` varchar(50) DEFAULT NULL COMMENT '排序',
  `Enabled` bit(1) DEFAULT NULL COMMENT '启用',
  `Type` varchar(50) DEFAULT NULL COMMENT '类型',
  `PushBtn` varchar(50) DEFAULT NULL COMMENT '功能按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8 COMMENT='功能模块表';

-- ----------------------------
-- Records of jsh_functions
-- ----------------------------
INSERT INTO `jsh_functions` VALUES ('1', '0001', '系统管理', '0', '', '', '0910', '', '电脑版', '', 'icon-settings', '0');
INSERT INTO `jsh_functions` VALUES ('13', '000102', '角色管理', '0001', '/pages/manage/role.html', '\0', '0130', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('14', '000103', '用户管理', '0001', '/pages/manage/user.html', '\0', '0140', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('15', '000104', '日志管理', '0001', '/pages/manage/log.html', '\0', '0160', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('16', '000105', '功能管理', '0001', '/pages/manage/functions.html', '\0', '0135', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('21', '0101', '商品管理', '0', '', '\0', '0620', '', '电脑版', null, 'icon-social-dropbox', '0');
INSERT INTO `jsh_functions` VALUES ('22', '010101', '商品类别', '0101', '/pages/materials/materialcategory.html', '\0', '0230', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('23', '010102', '商品信息', '0101', '/pages/materials/material.html', '\0', '0240', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('24', '0102', '基本资料', '0', '', '\0', '0750', '', '电脑版', null, 'icon-grid', '0');
INSERT INTO `jsh_functions` VALUES ('25', '01020101', '供应商信息', '0102', '/pages/manage/vendor.html', '\0', '0260', '', '电脑版', '1,2', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('26', '010202', '仓库信息', '0102', '/pages/manage/depot.html', '\0', '0270', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('31', '010206', '经手人管理', '0102', '/pages/materials/person.html', '\0', '0284', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('32', '0502', '采购管理', '0', '', '\0', '0330', '', '电脑版', '', 'icon-loop', '0');
INSERT INTO `jsh_functions` VALUES ('33', '050201', '采购入库', '0502', '/pages/materials/purchase_in_list.html', '\0', '0340', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('38', '0603', '销售管理', '0', '', '\0', '0390', '', '电脑版', '', 'icon-briefcase', '0');
INSERT INTO `jsh_functions` VALUES ('40', '080107', '调拨出库', '0801', '/pages/materials/allocation_out_list.html', '\0', '0807', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('41', '060303', '销售出库', '0603', '/pages/materials/sale_out_list.html', '\0', '0394', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('44', '0704', '财务管理', '0', '', '\0', '0450', '', '电脑版', '', 'icon-map', '0');
INSERT INTO `jsh_functions` VALUES ('59', '030101', '库存状况', '0301', '/pages/reports/in_out_stock_report.html', '\0', '0600', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('194', '010204', '收支项目', '0102', '/pages/manage/inOutItem.html', '\0', '0282', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('195', '010205', '结算账户', '0102', '/pages/manage/account.html', '\0', '0283', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('197', '070402', '收入单', '0704', '/pages/financial/item_in.html', '\0', '0465', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('198', '0301', '报表查询', '0', '', '\0', '0570', '', '电脑版', null, 'icon-pie-chart', '0');
INSERT INTO `jsh_functions` VALUES ('199', '050204', '采购退货', '0502', '/pages/materials/purchase_back_list.html', '\0', '0345', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('200', '060305', '销售退货', '0603', '/pages/materials/sale_back_list.html', '\0', '0396', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('201', '080103', '其它入库', '0801', '/pages/materials/other_in_list.html', '\0', '0803', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('202', '080105', '其它出库', '0801', '/pages/materials/other_out_list.html', '\0', '0805', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('203', '070403', '支出单', '0704', '/pages/financial/item_out.html', '\0', '0470', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('204', '070404', '收款单', '0704', '/pages/financial/money_in.html', '\0', '0475', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('205', '070405', '付款单', '0704', '/pages/financial/money_out.html', '\0', '0480', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('206', '070406', '转账单', '0704', '/pages/financial/giro.html', '\0', '0490', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('207', '030102', '结算账户', '0301', '/pages/reports/account_report.html', '\0', '0610', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('208', '030103', '进货统计', '0301', '/pages/reports/buy_in_report.html', '\0', '0620', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('209', '030104', '销售统计', '0301', '/pages/reports/sale_out_report.html', '\0', '0630', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('210', '040102', '零售出库', '0401', '/pages/materials/retail_out_list.html', '\0', '0405', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('211', '040104', '零售退货', '0401', '/pages/materials/retail_back_list.html', '\0', '0407', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('212', '070407', '收预付款', '0704', '/pages/financial/advance_in.html', '\0', '0495', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('217', '01020102', '客户信息', '0102', '/pages/manage/customer.html', '\0', '0262', '', '电脑版', '1,2', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('218', '01020103', '会员信息', '0102', '/pages/manage/member.html', '\0', '0263', '', '电脑版', '1,2', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('219', '000107', '资产管理', '0001', '/pages/asset/asset.html', '\0', '0170', '\0', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('220', '010103', '计量单位', '0101', '/pages/manage/unit.html', '\0', '0245', '', '电脑版', null, 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('225', '0401', '零售管理', '0', '', '\0', '0101', '', '电脑版', '', 'icon-present', '0');
INSERT INTO `jsh_functions` VALUES ('226', '030106', '入库明细', '0301', '/pages/reports/in_detail.html', '\0', '0640', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('227', '030107', '出库明细', '0301', '/pages/reports/out_detail.html', '\0', '0645', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('228', '030108', '入库汇总', '0301', '/pages/reports/in_material_count.html', '\0', '0650', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('229', '030109', '出库汇总', '0301', '/pages/reports/out_material_count.html', '\0', '0655', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('232', '080109', '组装单', '0801', '/pages/materials/assemble_list.html', '\0', '0809', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('233', '080111', '拆卸单', '0801', '/pages/materials/disassemble_list.html', '\0', '0811', '', '电脑版', '3,4,5', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('234', '000105', '系统配置', '0001', '/pages/manage/systemConfig.html', '\0', '0165', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('235', '030110', '客户对账', '0301', '/pages/reports/customer_account.html', '\0', '0660', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('236', '000106', '商品属性', '0001', '/pages/materials/materialProperty.html', '\0', '0168', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('237', '030111', '供应商对账', '0301', '/pages/reports/vendor_account.html', '\0', '0665', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('239', '0801', '仓库管理', '0', '', '\0', '0420', '', '电脑版', '', 'icon-layers', '0');
INSERT INTO `jsh_functions` VALUES ('240', '010104', '序列号', '0101', '/pages/manage/serialNumber.html', '\0', '0246', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('241', '050202', '采购订单', '0502', '/pages/materials/purchase_orders_list.html', '\0', '0335', '', '电脑版', '3', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('242', '060301', '销售订单', '0603', '/pages/materials/sale_orders_list.html', '\0', '0392', '', '电脑版', '3', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('243', '000108', '机构管理', '0001', '/pages/manage/organization.html', '', '0139', '', '电脑版', '', 'icon-notebook', '0');
INSERT INTO `jsh_functions` VALUES ('244', '030112', '库存预警', '0301', '/pages/reports/stock_warning_report.html', '\0', '0670', '', '电脑版', '', 'icon-notebook', '0');

-- ----------------------------
-- Table structure for jsh_inoutitem
-- ----------------------------
DROP TABLE IF EXISTS `jsh_inoutitem`;
CREATE TABLE `jsh_inoutitem` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `Type` varchar(20) DEFAULT NULL COMMENT '类型',
  `Remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='收支项目';

-- ----------------------------
-- Records of jsh_inoutitem
-- ----------------------------
INSERT INTO `jsh_inoutitem` VALUES ('22', '水费', '支出', '', '63', '0');
INSERT INTO `jsh_inoutitem` VALUES ('23', '家具', '收入', '', '63', '0');

-- ----------------------------
-- Table structure for jsh_log
-- ----------------------------
DROP TABLE IF EXISTS `jsh_log`;
CREATE TABLE `jsh_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userID` bigint(20) NOT NULL COMMENT '操作用户ID',
  `operation` varchar(500) DEFAULT NULL COMMENT '操作模块名称',
  `clientIP` varchar(50) DEFAULT NULL COMMENT '客户端IP',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '操作状态 0==成功，1==失败',
  `contentdetails` varchar(1000) DEFAULT NULL COMMENT '操作详情',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`),
  KEY `FKF2696AA13E226853` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Records of jsh_log
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_material
-- ----------------------------
DROP TABLE IF EXISTS `jsh_material`;
CREATE TABLE `jsh_material` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `CategoryId` bigint(20) DEFAULT NULL COMMENT '产品类型',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `Mfrs` varchar(50) DEFAULT NULL COMMENT '制造商',
  `Packing` decimal(24,6) DEFAULT NULL COMMENT '包装（KG/包）',
  `SafetyStock` decimal(24,6) DEFAULT NULL COMMENT '安全存量（KG）',
  `Model` varchar(50) DEFAULT NULL COMMENT '型号',
  `Standard` varchar(50) DEFAULT NULL COMMENT '规格',
  `Color` varchar(50) DEFAULT NULL COMMENT '颜色',
  `Unit` varchar(50) DEFAULT NULL COMMENT '单位-单个',
  `Remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `RetailPrice` decimal(24,6) DEFAULT NULL COMMENT '零售价',
  `LowPrice` decimal(24,6) DEFAULT NULL COMMENT '最低售价',
  `PresetPriceOne` decimal(24,6) DEFAULT NULL COMMENT '预设售价一',
  `PresetPriceTwo` decimal(24,6) DEFAULT NULL COMMENT '预设售价二',
  `UnitId` bigint(20) DEFAULT NULL COMMENT '计量单位Id',
  `FirstOutUnit` varchar(50) DEFAULT NULL COMMENT '首选出库单位',
  `FirstInUnit` varchar(50) DEFAULT NULL COMMENT '首选入库单位',
  `PriceStrategy` varchar(500) DEFAULT NULL COMMENT '价格策略',
  `Enabled` bit(1) DEFAULT NULL COMMENT '启用 0-禁用  1-启用',
  `OtherField1` varchar(50) DEFAULT NULL COMMENT '自定义1',
  `OtherField2` varchar(50) DEFAULT NULL COMMENT '自定义2',
  `OtherField3` varchar(50) DEFAULT NULL COMMENT '自定义3',
  `enableSerialNumber` varchar(1) DEFAULT '0' COMMENT '是否开启序列号，0否，1是',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  `BarCode` varchar(255) DEFAULT NULL COMMENT '条码',
  PRIMARY KEY (`Id`),
  KEY `FK675951272AB6672C` (`CategoryId`),
  KEY `UnitId` (`UnitId`)
) ENGINE=InnoDB AUTO_INCREMENT=611 DEFAULT CHARSET=utf8 COMMENT='产品表';

-- ----------------------------
-- Records of jsh_material
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_materialcategory
-- ----------------------------
DROP TABLE IF EXISTS `jsh_materialcategory`;
CREATE TABLE `jsh_materialcategory` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `CategoryLevel` smallint(6) DEFAULT NULL COMMENT '等级',
  `ParentId` bigint(20) DEFAULT NULL COMMENT '上级ID',
  `sort` varchar(10) DEFAULT NULL COMMENT '显示顺序',
  `status` varchar(1) DEFAULT '0' COMMENT '状态，0系统默认，1启用，2删除',
  `serial_no` varchar(100) DEFAULT NULL COMMENT '编号',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `updater` bigint(20) DEFAULT NULL COMMENT '更新人',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`Id`),
  KEY `FK3EE7F725237A77D8` (`ParentId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='产品类型表';

-- ----------------------------
-- Records of jsh_materialcategory
-- ----------------------------
INSERT INTO `jsh_materialcategory` VALUES ('23', '办公耗材', null, '-1', '001', '1', '001', '', '2019-11-28 17:54:49', '63', '2019-11-28 17:54:49', '63', '63');
INSERT INTO `jsh_materialcategory` VALUES ('24', '复印纸', null, '23', '', '1', '', '', '2019-11-28 17:55:13', '63', '2019-11-28 17:55:13', '63', '63');

-- ----------------------------
-- Table structure for jsh_materialproperty
-- ----------------------------
DROP TABLE IF EXISTS `jsh_materialproperty`;
CREATE TABLE `jsh_materialproperty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nativeName` varchar(50) DEFAULT NULL COMMENT '原始名称',
  `enabled` bit(1) DEFAULT NULL COMMENT '是否启用',
  `sort` varchar(10) DEFAULT NULL COMMENT '排序',
  `anotherName` varchar(50) DEFAULT NULL COMMENT '别名',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='产品扩展字段表';

-- ----------------------------
-- Records of jsh_materialproperty
-- ----------------------------
INSERT INTO `jsh_materialproperty` VALUES ('1', '规格', '', '02', '规格', '0');
INSERT INTO `jsh_materialproperty` VALUES ('2', '颜色', '', '01', '颜色', '0');
INSERT INTO `jsh_materialproperty` VALUES ('3', '制造商', '\0', '03', '制造商', '0');
INSERT INTO `jsh_materialproperty` VALUES ('4', '自定义1', '\0', '04', '自定义1', '0');
INSERT INTO `jsh_materialproperty` VALUES ('5', '自定义2', '\0', '05', '自定义2', '0');
INSERT INTO `jsh_materialproperty` VALUES ('6', '自定义3', '\0', '06', '自定义3', '0');

-- ----------------------------
-- Table structure for jsh_msg
-- ----------------------------
DROP TABLE IF EXISTS `jsh_msg`;
CREATE TABLE `jsh_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_title` varchar(100) DEFAULT NULL COMMENT '消息标题',
  `msg_content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `type` varchar(20) DEFAULT NULL COMMENT '消息类型',
  `status` varchar(1) DEFAULT NULL COMMENT '状态，1未读 2已读',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='消息表';

-- ----------------------------
-- Records of jsh_msg
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_organization
-- ----------------------------
DROP TABLE IF EXISTS `jsh_organization`;
CREATE TABLE `jsh_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_no` varchar(20) DEFAULT NULL COMMENT '机构编号',
  `org_full_name` varchar(500) DEFAULT NULL COMMENT '机构全称',
  `org_abr` varchar(20) DEFAULT NULL COMMENT '机构简称',
  `org_tpcd` varchar(9) DEFAULT NULL COMMENT '机构类型',
  `org_stcd` char(1) DEFAULT NULL COMMENT '机构状态,1未营业、2正常营业、3暂停营业、4终止营业、5已除名',
  `org_parent_no` varchar(20) DEFAULT NULL COMMENT '机构父节点编号',
  `sort` varchar(20) DEFAULT NULL COMMENT '机构显示顺序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `updater` bigint(20) DEFAULT NULL COMMENT '更新人',
  `org_create_time` datetime DEFAULT NULL COMMENT '机构创建时间',
  `org_stop_time` datetime DEFAULT NULL COMMENT '机构停运时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='机构表';

-- ----------------------------
-- Records of jsh_organization
-- ----------------------------
INSERT INTO `jsh_organization` VALUES ('12', '0001', '财务部', '财务', null, '2', '-1', '01', '', '2019-10-31 16:10:59', '63', '2019-10-31 16:10:59', '63', null, null, '63');
INSERT INTO `jsh_organization` VALUES ('13', '00001', '出纳', '出纳', null, '1', '0001', '001', '', '2019-10-31 16:11:40', '63', '2019-10-31 16:11:40', '63', null, null, '63');

-- ----------------------------
-- Table structure for jsh_orga_user_rel
-- ----------------------------
DROP TABLE IF EXISTS `jsh_orga_user_rel`;
CREATE TABLE `jsh_orga_user_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orga_id` bigint(20) NOT NULL COMMENT '机构id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_blng_orga_dspl_seq` varchar(20) DEFAULT NULL COMMENT '用户在所属机构中显示顺序',
  `delete_flag` char(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `updater` bigint(20) DEFAULT NULL COMMENT '更新人',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='机构用户关系表';

-- ----------------------------
-- Records of jsh_orga_user_rel
-- ----------------------------
INSERT INTO `jsh_orga_user_rel` VALUES ('1', '9', '64', '', '0', null, null, '2019-03-15 23:03:39', '63', null);
INSERT INTO `jsh_orga_user_rel` VALUES ('2', '3', '65', null, '0', null, null, null, null, null);
INSERT INTO `jsh_orga_user_rel` VALUES ('3', '3', '67', null, '0', null, null, null, null, null);
INSERT INTO `jsh_orga_user_rel` VALUES ('4', '4', '84', null, '0', null, null, null, null, null);
INSERT INTO `jsh_orga_user_rel` VALUES ('5', '5', '86', null, '0', null, null, null, null, null);
INSERT INTO `jsh_orga_user_rel` VALUES ('6', '3', '91', '', '0', '2019-03-12 21:55:28', '63', '2019-03-12 21:55:28', '63', null);
INSERT INTO `jsh_orga_user_rel` VALUES ('7', '9', '95', '', '0', '2019-03-15 23:03:22', '63', '2019-03-15 23:03:22', '63', null);
INSERT INTO `jsh_orga_user_rel` VALUES ('8', '9', '96', '', '0', '2019-03-17 23:32:08', '63', '2019-03-17 23:32:08', '63', null);
INSERT INTO `jsh_orga_user_rel` VALUES ('9', '10', '117', '', '0', '2019-03-31 21:53:03', '117', '2019-03-31 21:53:12', '117', '117');
INSERT INTO `jsh_orga_user_rel` VALUES ('10', '11', '133', '11', '0', '2019-10-25 21:49:36', '120', '2019-10-25 21:49:36', '120', null);
INSERT INTO `jsh_orga_user_rel` VALUES ('11', '13', '132', '', '0', '2019-10-31 16:20:14', '63', '2019-10-31 16:20:14', '63', '63');
INSERT INTO `jsh_orga_user_rel` VALUES ('12', '8', '134', '1', '0', '2019-11-04 17:14:30', '120', '2019-11-04 17:14:30', '120', null);
INSERT INTO `jsh_orga_user_rel` VALUES ('13', '13', '135', '', '0', '2019-11-19 17:43:07', '63', '2019-11-19 17:43:07', '63', '63');

-- ----------------------------
-- Table structure for jsh_person
-- ----------------------------
DROP TABLE IF EXISTS `jsh_person`;
CREATE TABLE `jsh_person` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Type` varchar(20) DEFAULT NULL COMMENT '类型',
  `Name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='经手人表';

-- ----------------------------
-- Records of jsh_person
-- ----------------------------
INSERT INTO `jsh_person` VALUES ('14', '业务员', '刘亚亚', '63', '0');

-- ----------------------------
-- Table structure for jsh_role
-- ----------------------------
DROP TABLE IF EXISTS `jsh_role`;
CREATE TABLE `jsh_role` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `value` varchar(200) DEFAULT NULL COMMENT '值',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of jsh_role
-- ----------------------------
INSERT INTO `jsh_role` VALUES ('17', '财务', null, null, null, '63', '0');
INSERT INTO `jsh_role` VALUES ('18', '业务员', null, null, null, '63', '0');

-- ----------------------------
-- Table structure for jsh_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `jsh_serial_number`;
CREATE TABLE `jsh_serial_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `material_Id` bigint(20) DEFAULT NULL COMMENT '产品表id',
  `serial_Number` varchar(64) DEFAULT NULL COMMENT '序列号',
  `is_Sell` varchar(1) DEFAULT '0' COMMENT '是否卖出，0未卖出，1卖出',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  `create_Time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_Time` datetime DEFAULT NULL COMMENT '更新时间',
  `updater` bigint(20) DEFAULT NULL COMMENT '更新人',
  `depothead_Id` bigint(20) DEFAULT NULL COMMENT '单据主表id，用于跟踪序列号流向',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8 COMMENT='序列号表';

-- ----------------------------
-- Records of jsh_serial_number
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_supplier
-- ----------------------------
DROP TABLE IF EXISTS `jsh_supplier`;
CREATE TABLE `jsh_supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier` varchar(255) NOT NULL COMMENT '供应商名称',
  `contacts` varchar(100) DEFAULT NULL COMMENT '联系人',
  `phonenum` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `description` varchar(500) DEFAULT NULL COMMENT '备注',
  `isystem` tinyint(4) DEFAULT NULL COMMENT '是否系统自带 0==系统 1==非系统',
  `type` varchar(20) DEFAULT NULL COMMENT '类型',
  `enabled` bit(1) DEFAULT NULL COMMENT '启用',
  `AdvanceIn` decimal(24,6) DEFAULT '0.000000' COMMENT '预收款',
  `BeginNeedGet` decimal(24,6) DEFAULT NULL COMMENT '期初应收',
  `BeginNeedPay` decimal(24,6) DEFAULT NULL COMMENT '期初应付',
  `AllNeedGet` decimal(24,6) DEFAULT NULL COMMENT '累计应收',
  `AllNeedPay` decimal(24,6) DEFAULT NULL COMMENT '累计应付',
  `fax` varchar(30) DEFAULT NULL COMMENT '传真',
  `telephone` varchar(30) DEFAULT NULL COMMENT '手机',
  `address` varchar(50) DEFAULT NULL COMMENT '地址',
  `taxNum` varchar(50) DEFAULT NULL COMMENT '纳税人识别号',
  `bankName` varchar(50) DEFAULT NULL COMMENT '开户行',
  `accountNumber` varchar(50) DEFAULT NULL COMMENT '账号',
  `taxRate` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8 COMMENT='供应商/客户信息表';

-- ----------------------------
-- Records of jsh_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_systemconfig
-- ----------------------------
DROP TABLE IF EXISTS `jsh_systemconfig`;
CREATE TABLE `jsh_systemconfig` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `company_id` varchar(20) DEFAULT NULL COMMENT '公司ID',
  `company_contacts` varchar(20) DEFAULT NULL COMMENT '公司联系人',
  `company_address` varchar(50) DEFAULT NULL COMMENT '公司地址',
  `company_tel` varchar(20) DEFAULT NULL COMMENT '公司电话',
  `company_fax` varchar(20) DEFAULT NULL COMMENT '公司传真',
  `company_post_code` varchar(20) DEFAULT NULL COMMENT '公司邮编',
  `depot_flag` varchar(1) DEFAULT '0' COMMENT '仓库启用标记，0未启用，1启用',
  `customer_flag` varchar(1) DEFAULT '0' COMMENT '客户启用标记，0未启用，1启用',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统参数';

-- ----------------------------
-- Records of jsh_systemconfig
-- ----------------------------
INSERT INTO `jsh_systemconfig` VALUES ('9', '公司名称', '00001', '张三', '', '', '', '', '1', '1', '63', '0');

-- ----------------------------
-- Table structure for jsh_tenant
-- ----------------------------
DROP TABLE IF EXISTS `jsh_tenant`;
CREATE TABLE `jsh_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `login_name` varchar(255) DEFAULT NULL COMMENT '登录名',
  `user_num_limit` int(11) DEFAULT NULL COMMENT '用户数量限制',
  `bills_num_limit` int(11) DEFAULT NULL COMMENT '单据数量限制',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8 COMMENT='租户';

-- ----------------------------
-- Records of jsh_tenant
-- ----------------------------
INSERT INTO `jsh_tenant` VALUES ('13', '63', 'admin', '20', '2000', null);

-- ----------------------------
-- Table structure for jsh_unit
-- ----------------------------
DROP TABLE IF EXISTS `jsh_unit`;
CREATE TABLE `jsh_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UName` varchar(50) DEFAULT NULL COMMENT '名称，支持多单位',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='多单位表';

-- ----------------------------
-- Records of jsh_unit
-- ----------------------------

-- ----------------------------
-- Table structure for jsh_user
-- ----------------------------
DROP TABLE IF EXISTS `jsh_user`;
CREATE TABLE `jsh_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) NOT NULL COMMENT '用户姓名--例如张三',
  `loginame` varchar(255) DEFAULT NULL COMMENT '登录用户名--可能为空',
  `password` varchar(50) DEFAULT NULL COMMENT '登陆密码',
  `position` varchar(200) DEFAULT NULL COMMENT '职位',
  `department` varchar(255) DEFAULT NULL COMMENT '所属部门',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `phonenum` varchar(100) DEFAULT NULL COMMENT '手机号码',
  `ismanager` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否为管理者 0==管理者 1==员工',
  `isystem` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否系统自带数据 ',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态，0：正常，1：删除，2封禁',
  `description` varchar(500) DEFAULT NULL COMMENT '用户描述信息',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of jsh_user
-- ----------------------------
INSERT INTO `jsh_user` VALUES ('63', '管理员', 'superadmin', 'e10adc3949ba59abbe56e057f20f883e', '', null, '', '', '1', '1', '0', '', null, '63');
INSERT INTO `jsh_user` VALUES ('135', '15889616931', '张宽宽', 'e10adc3949ba59abbe56e057f20f883e', '', null, '', '', '1', '0', '0', '', null, '63');

-- ----------------------------
-- Table structure for jsh_userbusiness
-- ----------------------------
DROP TABLE IF EXISTS `jsh_userbusiness`;
CREATE TABLE `jsh_userbusiness` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Type` varchar(50) DEFAULT NULL COMMENT '类别',
  `KeyId` varchar(50) DEFAULT NULL COMMENT '主ID',
  `Value` varchar(10000) DEFAULT NULL COMMENT '值',
  `BtnStr` varchar(2000) DEFAULT NULL COMMENT '按钮权限',
  `delete_Flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 COMMENT='用户/角色/模块关系表';

-- ----------------------------
-- Records of jsh_userbusiness
-- ----------------------------
INSERT INTO `jsh_userbusiness` VALUES ('5', 'RoleFunctions', '4', '[245][13][12][16][243][14][15][234][236][22][23][220][240][25][217][218][26][194][195][31][59][207][208][209][226][227][228][229][235][237][244][210][211][241][33][199][242][41][200][201][202][40][232][233][197][203][204][205][206][212][246]', '[{\"funId\":\"25\",\"btnStr\":\"1\"},{\"funId\":\"217\",\"btnStr\":\"1\"},{\"funId\":\"218\",\"btnStr\":\"1\"},{\"funId\":\"241\",\"btnStr\":\"3\"},{\"funId\":\"242\",\"btnStr\":\"3\"}]', '0');
INSERT INTO `jsh_userbusiness` VALUES ('6', 'RoleFunctions', '5', '[22][23][25][26][194][195][31][33][200][201][41][199][202]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('7', 'RoleFunctions', '6', '[22][23][220][240][25][217][218][26][194][195][31][59][207][208][209][226][227][228][229][235][237][210][211][241][33][199][242][41][200][201][202][40][232][233][197][203][204][205][206][212]', '[{\"funId\":\"33\",\"btnStr\":\"4\"}]', '0');
INSERT INTO `jsh_userbusiness` VALUES ('9', 'RoleFunctions', '7', '[168][13][12][16][14][15][189][18][19][132]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('10', 'RoleFunctions', '8', '[168][13][12][16][14][15][189][18][19][132][22][23][25][26][27][157][158][155][156][125][31][127][126][128][33][34][35][36][37][39][40][41][42][43][46][47][48][49][50][51][52][53][54][55][56][57][192][59][60][61][62][63][65][66][68][69][70][71][73][74][76][77][79][191][81][82][83][85][89][161][86][176][165][160][28][134][91][92][29][94][95][97][104][99][100][101][102][105][107][108][110][111][113][114][116][117][118][120][121][131][135][123][122][20][130][146][147][138][148][149][153][140][145][184][152][143][170][171][169][166][167][163][164][172][173][179][178][181][182][183][186][187]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('11', 'RoleFunctions', '9', '[168][13][12][16][14][15][189][18][19][132][22][23][25][26][27][157][158][155][156][125][31][127][126][128][33][34][35][36][37][39][40][41][42][43][46][47][48][49][50][51][52][53][54][55][56][57][192][59][60][61][62][63][65][66][68][69][70][71][73][74][76][77][79][191][81][82][83][85][89][161][86][176][165][160][28][134][91][92][29][94][95][97][104][99][100][101][102][105][107][108][110][111][113][114][116][117][118][120][121][131][135][123][122][20][130][146][147][138][148][149][153][140][145][184][152][143][170][171][169][166][167][163][164][172][173][179][178][181][182][183][186][187][188]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('12', 'UserRole', '1', '[5]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('13', 'UserRole', '2', '[6][7]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('14', 'UserDepot', '2', '[1][2][6][7]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('15', 'UserDepot', '1', '[1][2][5][6][7][10][12][14][15][17]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('16', 'UserRole', '63', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('18', 'UserDepot', '63', '[14][15][16][17][18][19]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('19', 'UserDepot', '5', '[6][45][46][50]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('20', 'UserRole', '5', '[5]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('21', 'UserRole', '64', '[13]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('22', 'UserDepot', '64', '[1]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('23', 'UserRole', '65', '[5]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('24', 'UserDepot', '65', '[1]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('25', 'UserCustomer', '64', '[5][2]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('26', 'UserCustomer', '65', '[6]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('27', 'UserCustomer', '63', '[86][84][75]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('28', 'UserDepot', '96', '[7]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('29', 'UserRole', '96', '[6]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('30', 'UserRole', '113', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('32', 'RoleFunctions', '10', '[245][13][243][14][15][234][22][23][220][240][25][217][218][26][194][195][31][59][207][208][209][226][227][228][229][235][237][244][210][211][241][33][199][242][41][200][201][202][40][232][233][197][203][204][205][206][212][246]', '[{\"funId\":\"25\",\"btnStr\":\"1\"},{\"funId\":\"217\",\"btnStr\":\"1\"},{\"funId\":\"218\",\"btnStr\":\"1\"},{\"funId\":\"241\",\"btnStr\":\"3\"},{\"funId\":\"242\",\"btnStr\":\"3\"}]', '0');
INSERT INTO `jsh_userbusiness` VALUES ('34', 'UserRole', '115', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('35', 'UserRole', '117', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('36', 'UserDepot', '117', '[8][9]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('37', 'UserCustomer', '117', '[52]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('38', 'UserRole', '120', '[4]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('39', 'UserDepot', '120', '[7][8][9][10][11][12][2][1][3]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('40', 'UserCustomer', '120', '[86][84][75]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('41', 'RoleFunctions', '12', '', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('48', 'RoleFunctions', '13', '[59][207][208][209][226][227][228][229][235][237][210][211][241][33][199][242][41][200]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('51', 'UserRole', '74', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('52', 'UserDepot', '121', '[13]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('54', 'UserDepot', '115', '[13]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('56', 'UserCustomer', '115', '[56]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('57', 'UserCustomer', '121', '[56]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('58', 'UserRole', '121', '[15]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('59', 'UserRole', '123', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('60', 'UserRole', '124', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('61', 'UserRole', '125', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('62', 'UserRole', '126', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('63', 'UserRole', '127', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('64', 'UserRole', '128', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('65', 'UserRole', '129', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('66', 'UserRole', '130', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('67', 'UserRole', '131', '[10]', null, '0');
INSERT INTO `jsh_userbusiness` VALUES ('68', 'RoleFunctions', '17', '[197][203][204][205][206][212][207][194]', '', '0');

-- ----------------------------
-- Table structure for tbl_sequence
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sequence`;
CREATE TABLE `tbl_sequence` (
  `seq_name` varchar(50) NOT NULL COMMENT '序列名称',
  `min_value` bigint(20) NOT NULL COMMENT '最小值',
  `max_value` bigint(20) NOT NULL COMMENT '最大值',
  `current_val` bigint(20) NOT NULL COMMENT '当前值',
  `increment_val` int(11) NOT NULL DEFAULT '1' COMMENT '增长步数',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sequence表';

-- ----------------------------
-- Records of tbl_sequence
-- ----------------------------
INSERT INTO `tbl_sequence` VALUES ('depot_number_seq', '1', '999999999999999999', '699', '1', '单据编号sequence');
