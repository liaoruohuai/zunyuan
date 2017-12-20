ALTER TABLE `zunyuandb`.`saler`
ADD COLUMN `salePwd` VARCHAR(64) NULL COMMENT '密码' AFTER `netNumber`,
ADD COLUMN `isInitPwd` VARCHAR(8) NULL AFTER `salepwd` default '0';


CREATE TABLE `zunyuandb`.`apply` (
  `apply_id` VARCHAR(32) NOT NULL,
  `name` VARCHAR(32) NULL COMMENT '姓名',
  `apply_type` VARCHAR(8) NULL COMMENT '0-自助 1-员工',
  `id_num` VARCHAR(32) NULL COMMENT '证件号',
  `mobile` VARCHAR(16) NULL COMMENT '手机',
  `id_date` VARCHAR(16) NULL COMMENT '证件有效期',
  `province` VARCHAR(16) NULL COMMENT '省',
  `city` VARCHAR(16) NULL COMMENT '市',
  `country` VARCHAR(16) NULL COMMENT '区',
  `address` VARCHAR(256) NULL COMMENT '地址',
  `vocation` VARCHAR(32) NULL COMMENT '职业\n',
  `apply_date` VARCHAR(16) NULL COMMENT '申请日期',
  `apply_time` VARCHAR(16) NULL COMMENT '申请时间',
  `sales_id` VARCHAR(45) NULL COMMENT '销售ID\n',
  `apply_status` VARCHAR(45) NULL COMMENT '申请进度\n',
  `last_update_time` DATETIME VARCHAR(32) NULL,
  PRIMARY KEY (`apply_id`));

  ALTER TABLE `zunyuandb`.`apply`
  CHANGE COLUMN `apply_id` `applyId` VARCHAR(32) NOT NULL ,
  CHANGE COLUMN `apply_type` `applyType` VARCHAR(8) NULL COMMENT '0-自助 1-员工' ,
  CHANGE COLUMN `id_num` `idNum` VARCHAR(32) NULL DEFAULT NULL COMMENT '证件号' ,
  CHANGE COLUMN `id_date` `idDate` VARCHAR(16) NULL DEFAULT NULL COMMENT '证件有效期' ,
  CHANGE COLUMN `apply_date` `applyDate` VARCHAR(16) NULL DEFAULT NULL COMMENT '申请日期' ,
  CHANGE COLUMN `apply_time` `applyTime` VARCHAR(16) NULL DEFAULT NULL COMMENT '申请时间' ,
  CHANGE COLUMN `apply_status` `applyStatus` VARCHAR(45) NULL DEFAULT NULL COMMENT '申请进度\n' ,
  CHANGE COLUMN `sales_id` `salesId` VARCHAR(45) NULL DEFAULT NULL COMMENT '销售ID\n' ,

  CHANGE COLUMN `last_update_time` `lastUpdateTime` VARCHAR(32) NULL DEFAULT NULL ;



ALTER TABLE `zunyuandb`.`apply`
ADD COLUMN `nation` VARCHAR(16) NULL COMMENT '国籍' AFTER `salesId`,
ADD COLUMN `birth` VARCHAR(64) NULL COMMENT '出生日期' AFTER `nation`;

CREATE TABLE `zunyuandb`.`smsLog` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `phoneNum` VARCHAR(16) NULL,
  `smsContent` VARCHAR(128) NULL,
  `smsDate` VARCHAR(16) NULL,
   `smsResult` VARCHAR(64) NULL,
  PRIMARY KEY (`id`))
COMMENT = '短信日志';


CREATE TABLE `member` (
  `memberId` varchar(10) NOT NULL COMMENT '会员id',
  `memberName` varchar(60) DEFAULT NULL COMMENT '会员姓名',
  `memberPhone` varchar(11) DEFAULT NULL COMMENT '会员手机号码',
  `memberPwd` varchar(64) DEFAULT NULL COMMENT '会员密码',
  `memberCertType` varchar(8) DEFAULT '0' COMMENT '会员证件类型',
  `memberCertNo` varchar(32) DEFAULT NULL COMMENT '会员证件号码',
  `memberLevel` varchar(32) DEFAULT NULL COMMENT '会员证件号码',
  `memberPoint` varchar(32) DEFAULT NULL COMMENT '会员积分',
  `isInitPwd` varchar(8) DEFAULT '0' COMMENT '初始密码标识',
  `lastLoginTime` varchar(32) DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`memberId`),
  UNIQUE KEY `memberPhone` (`memberPhone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员信息表';

ALTER TABLE `zunyuandb`.`member`
CHANGE COLUMN `memberId` `memberId` INT(11) NOT NULL AUTO_INCREMENT COMMENT '会员id' ;


ALTER TABLE `zunyuandb`.`apply`
ADD COLUMN `applyCard` VARCHAR(19) NULL COMMENT '申请卡号' AFTER `applyType`;


ALTER TABLE `zunyuandb`.`org`  DROP INDEX `supOrgNumber` ;



CREATE TABLE `zunyuandb`.`coupon` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `couponType` VARCHAR(8) NULL COMMENT '类型- 0 新户券',
 `couponInfo` VARCHAR(128) NULL COMMENT '卡券核销代码',
 `couponValidDate` VARCHAR(16) NULL COMMENT '卡券有效期\n',
 `couponDesp` VARCHAR(128) NULL COMMENT '卡券说明',
 `grantMember` VARCHAR(45) NULL COMMENT '赠予会员手机号\n',
 `grantTime` VARCHAR(16) NULL COMMENT '赠予时间',
 PRIMARY KEY (`id`))
COMMENT = '卡券表';

ALTER TABLE `zunyuandb`.`coupon`
ADD COLUMN `couponStatus` VARCHAR(16) NULL AFTER `grantTime`;