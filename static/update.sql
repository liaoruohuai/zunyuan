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
