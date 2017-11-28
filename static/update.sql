ALTER TABLE `zunyuandb`.`saler`
ADD COLUMN `salePwd` VARCHAR(64) NULL COMMENT '密码' AFTER `netNumber`,
ADD COLUMN `isInitPwd` VARCHAR(8) NULL AFTER `salepwd` default '0';
