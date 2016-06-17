/*
Navicat MySQL Data Transfer

Source Server         : 192.168.17.230
Source Server Version : 50525
Source Host           : 192.168.17.230:3306
Source Database       : pay_center

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2016-02-19 14:39:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `alipay_trade_flow_2015`
-- ----------------------------
DROP TABLE IF EXISTS `alipay_trade_flow_2015`;
CREATE TABLE `alipay_trade_flow_2015` (
  `trade_flow_no` varchar(32) NOT NULL COMMENT '支付中心支付流水号  主键(日期+序列号)',
  `pay_trade_no` varchar(50) DEFAULT '' COMMENT '第三方(支付宝,微信，环迅)等支付平台产生的支付流水号',
  `order_id` varchar(32) NOT NULL COMMENT '业务系统订单号',
  `business_key` varchar(50) NOT NULL COMMENT '业务系统标识',
  `total_price` decimal(15,3) NOT NULL DEFAULT '0.000' COMMENT '实际总金额',
  `trade_status` int(5) NOT NULL DEFAULT '1' COMMENT '支付状态（1=初始化，2=交易完成，3=交易关闭,4=等待卖家收款（买家付款后，如果卖家账号被冻结））',
  `payment_type` int(5) DEFAULT '1' COMMENT '支付类型（1=商品购买，4=捐赠，47=电子卡券）',
  `payer_trade_account` varchar(100) DEFAULT NULL COMMENT '付款方支付平台交易账号（如支付宝账号）',
  `seller_trade_account` varchar(100) NOT NULL COMMENT '收款方支付平台交易账号（如支付宝账号）',
  `extra_param` varchar(200) DEFAULT '''''' COMMENT '业务系统需要的回传参数',
  `payer_busniess_account` varchar(100) NOT NULL DEFAULT '' COMMENT '付款方业务系统所在的账号,没有为空字符串',
  `is_notice_client` int(2) NOT NULL DEFAULT '1' COMMENT '是否已经通知了客户端并得到客户端相应1=未通知，2=已得到客户端相应',
  `back_url` varchar(100) NOT NULL COMMENT '业务系统回调的URL',
  `description` varchar(100) DEFAULT '' COMMENT '备注(业务系统传递过来，非必填)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`trade_flow_no`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `business_key` (`business_key`),
  KEY `trade_status` (`trade_status`),
  KEY `pay_trade_no` (`pay_trade_no`) USING BTREE,
  KEY `is_notice_client` (`is_notice_client`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of alipay_trade_flow_2015
-- ----------------------------

-- ----------------------------
-- Table structure for `alipay_trade_flow_2016`
-- ----------------------------
DROP TABLE IF EXISTS `alipay_trade_flow_2016`;
CREATE TABLE `alipay_trade_flow_2016` (
  `trade_flow_no` varchar(32) NOT NULL COMMENT '支付中心支付流水号  主键(日期+序列号)',
  `pay_trade_no` varchar(50) DEFAULT '' COMMENT '第三方(支付宝,微信，环迅)等支付平台产生的支付流水号',
  `order_id` varchar(32) NOT NULL COMMENT '业务系统订单号',
  `business_key` varchar(50) NOT NULL COMMENT '业务系统标识',
  `total_price` decimal(15,3) NOT NULL DEFAULT '0.000' COMMENT '实际总金额',
  `trade_status` int(5) NOT NULL DEFAULT '1' COMMENT '支付状态（1=初始化，2=交易完成，3=交易关闭,4=等待卖家收款（买家付款后，如果卖家账号被冻结）,）',
  `payment_type` int(5) DEFAULT '1' COMMENT '支付类型（1=商品购买，4=捐赠，47=电子卡券）',
  `payer_trade_account` varchar(100) DEFAULT NULL COMMENT '付款方支付平台交易账号（如支付宝账号）',
  `seller_trade_account` varchar(100) NOT NULL COMMENT '收款方支付平台交易账号（如支付宝账号）',
  `extra_param` varchar(200) DEFAULT '''''' COMMENT '业务系统需要的回传参数',
  `payer_busniess_account` varchar(100) NOT NULL DEFAULT '' COMMENT '付款方业务系统所在的账号,没有为空字符串',
  `is_notice_client` int(2) NOT NULL DEFAULT '1' COMMENT '是否已经通知了客户端并得到客户端相应1=未通知，2=已得到客户端相应',
  `back_url` varchar(100) NOT NULL COMMENT '业务系统回调的URL',
  `description` varchar(100) DEFAULT '' COMMENT '备注(业务系统传递过来，非必填)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`trade_flow_no`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `business_key` (`business_key`),
  KEY `trade_status` (`trade_status`),
  KEY `pay_trade_no` (`pay_trade_no`) USING BTREE,
  KEY `is_notice_client` (`is_notice_client`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of alipay_trade_flow_2016

-- ----------------------------
-- Table structure for `alipay_trade_notice_log_2015`
-- ----------------------------
DROP TABLE IF EXISTS `alipay_trade_notice_log_2015`;
CREATE TABLE `alipay_trade_notice_log_2015` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_flow_no` varchar(32) NOT NULL DEFAULT '' COMMENT '支付中心交易流水',
  `pay_trade_no` varchar(60) NOT NULL COMMENT '银行网关交易唯一支付流水号',
  `trade_status` int(5) NOT NULL COMMENT '支付状态（1=初始化，2=交易完成，3=交易关闭,4=等待卖家收款（买家付款后，如果卖家账号被冻结）,）',
  `total_price` decimal(15,3) NOT NULL,
  `payer_trade_account` varchar(120) DEFAULT NULL COMMENT '付款方支付平台交易账号（如支付宝账号）',
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '业务系统交易订单号,业务系统唯一标识',
  `seller_trade_account` varchar(120) DEFAULT NULL COMMENT '收款方支付平台交易账号（如支付宝账号）',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `trade_flow_no` (`trade_flow_no`),
  KEY `pay_trade_no` (`pay_trade_no`),
  KEY `trade_status` (`trade_status`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付宝异步回调通知日志';

-- ----------------------------
-- Records of alipay_trade_notice_log_2015
-- ----------------------------

-- ----------------------------
-- Table structure for `alipay_trade_notice_log_2016`
-- ----------------------------
DROP TABLE IF EXISTS `alipay_trade_notice_log_2016`;
CREATE TABLE `alipay_trade_notice_log_2016` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_flow_no` varchar(32) NOT NULL DEFAULT '' COMMENT '支付中心交易流水',
  `pay_trade_no` varchar(60) NOT NULL COMMENT '银行网关交易唯一支付流水号',
  `trade_status` int(5) NOT NULL COMMENT '支付状态（1=初始化，2=交易完成，3=交易关闭,4=等待卖家收款（买家付款后，如果卖家账号被冻结））',
  `total_price` decimal(15,3) NOT NULL,
  `payer_trade_account` varchar(120) DEFAULT NULL COMMENT '付款方支付平台交易账号（如支付宝账号）',
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '业务系统交易订单号,业务系统唯一标识',
  `seller_trade_account` varchar(120) DEFAULT NULL COMMENT '收款方支付平台交易账号（如支付宝账号）',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `trade_flow_no` (`trade_flow_no`),
  KEY `pay_trade_no` (`pay_trade_no`),
  KEY `trade_status` (`trade_status`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付宝异步回调通知日志';

-- ----------------------------
-- Records of alipay_trade_notice_log_2016
-- ----------------------------

-- ----------------------------
-- Table structure for `tbl_white_list`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_white_list`;
CREATE TABLE `tbl_white_list` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `businese_name` varchar(64) NOT NULL COMMENT '业务系统名称',
  `rsa_public_key` varchar(100) NOT NULL COMMENT '业务系统rsa公钥key文件路径',
  `white_ip` varchar(150) NOT NULL COMMENT '业务系统IP，多个以逗号分隔',
  `rsa_private_key` varchar(100) NOT NULL COMMENT '业务系统RSA私钥key文件路径',
  `busniess_key` varchar(60) NOT NULL COMMENT '业务系统标识，唯一',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商户白名单';

-- ----------------------------
-- Records of tbl_white_list
-- ----------------------------
INSERT INTO `tbl_white_list` VALUES ('1', 'Ak平台', 'D:\\\\koko_pub.txt', '192.168.17.48,127.0.0.1,', '', 'koko');

-- ----------------------------
-- Table structure for `wechat_trade_flow_2015`
-- ----------------------------
DROP TABLE IF EXISTS `wechat_trade_flow_2015`;
CREATE TABLE `wechat_trade_flow_2015` (
  `trade_flow_no` varchar(40) NOT NULL,
  `order_id` varchar(40) NOT NULL,
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '微信支付订单号，由微信生成',
  `body` varchar(100) NOT NULL DEFAULT '""',
  `total_fee` bigint(16) NOT NULL COMMENT '支付金额，分为单位整型',
  `product_id` varchar(40) NOT NULL,
  `extra_param` varchar(150) DEFAULT NULL,
  `payer_busniess_account` varchar(100) DEFAULT NULL,
  `seller_trade_account` varchar(100) DEFAULT NULL,
  `back_url` varchar(200) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `business_key` varchar(50) NOT NULL,
  `trade_type` varchar(20) NOT NULL,
  `trade_status` int(4) NOT NULL DEFAULT '1' COMMENT '支付状态（1=初始化，2=交易完成,3=交易失败,4=已关闭）',
  `wechat_err_code_des` varchar(128) DEFAULT NULL,
  `wechat_err_code` varchar(32) DEFAULT NULL,
  `wechat_result_code` varchar(16) DEFAULT NULL,
  `wechat_return_msg` varchar(128) DEFAULT NULL,
  `code_url` varchar(70) DEFAULT NULL,
  `wechat_return_code` varchar(16) DEFAULT NULL,
  `is_notice_client` int(2) NOT NULL DEFAULT '1' COMMENT '是否已经通知了客户端并得到客户端相应1=未通知，2=已得到客户端相应',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`trade_flow_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_trade_flow_2015
-- ----------------------------

-- ----------------------------
-- Table structure for `wechat_trade_flow_2016`
-- ----------------------------
DROP TABLE IF EXISTS `wechat_trade_flow_2016`;
CREATE TABLE `wechat_trade_flow_2016` (
  `trade_flow_no` varchar(40) NOT NULL,
  `order_id` varchar(40) NOT NULL,
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '微信支付订单号，由微信生成',
  `body` varchar(100) NOT NULL DEFAULT '""',
  `total_fee` bigint(16) NOT NULL COMMENT '支付金额，分为单位整型',
  `product_id` varchar(40) NOT NULL,
  `extra_param` varchar(150) DEFAULT NULL,
  `payer_busniess_account` varchar(100) DEFAULT NULL,
  `seller_trade_account` varchar(100) DEFAULT NULL,
  `back_url` varchar(200) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `business_key` varchar(50) NOT NULL,
  `trade_type` varchar(20) NOT NULL,
  `trade_status` int(4) NOT NULL COMMENT '支付状态（1=初始化，2=交易完成,3=交易失败,4=已关闭）',
  `wechat_err_code_des` varchar(128) DEFAULT NULL,
  `wechat_err_code` varchar(32) DEFAULT NULL,
  `wechat_result_code` varchar(16) DEFAULT NULL,
  `wechat_return_msg` varchar(128) DEFAULT NULL,
  `code_url` varchar(70) DEFAULT NULL,
  `wechat_return_code` varchar(16) DEFAULT NULL,
  `is_notice_client` int(2) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`trade_flow_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_trade_flow_2016
-- ----------------------------

-- ----------------------------
-- Table structure for `wechat_trade_notice_log_2015`
-- ----------------------------
DROP TABLE IF EXISTS `wechat_trade_notice_log_2015`;
CREATE TABLE `wechat_trade_notice_log_2015` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_flow_no` varchar(40) NOT NULL COMMENT '支付中心交易流水号',
  `order_id` varchar(40) NOT NULL COMMENT '业务系统订单号',
  `transaction_id` varchar(40) NOT NULL COMMENT '微信支付交易订单号，微信生成',
  `total_fee` bigint(16) DEFAULT NULL COMMENT '交易金额，分为单位',
  `trade_type` varchar(20) DEFAULT NULL COMMENT '交易类型 JSAPI、NATIVE、APP',
  `trade_status` int(3) DEFAULT NULL COMMENT '交易状态，1=失败，2=成功',
  `wechat_err_code_des` varchar(128) DEFAULT NULL COMMENT '错误代码描述',
  `wechat_err_code` varchar(18) DEFAULT NULL COMMENT '错误代码',
  `wechat_result_code` varchar(18) DEFAULT NULL COMMENT '业务结果',
  `wechat_return_msg` varchar(128) DEFAULT NULL COMMENT '返回信息',
  `wechat_return_code` varchar(18) DEFAULT NULL COMMENT '返回状态码',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_trade_notice_log_2015
-- ----------------------------

-- ----------------------------
-- Table structure for `wechat_trade_notice_log_2016`
-- ----------------------------
DROP TABLE IF EXISTS `wechat_trade_notice_log_2016`;
CREATE TABLE `wechat_trade_notice_log_2016` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_flow_no` varchar(40) NOT NULL COMMENT '支付中心交易流水号',
  `order_id` varchar(40) NOT NULL COMMENT '业务系统订单号',
  `transaction_id` varchar(40) NOT NULL COMMENT '微信支付交易订单号，微信生成',
  `total_fee` bigint(16) DEFAULT NULL COMMENT '交易金额，分为单位',
  `trade_type` varchar(20) DEFAULT NULL COMMENT '交易类型 JSAPI、NATIVE、APP',
  `trade_status` int(3) DEFAULT NULL COMMENT '交易状态，1=失败，2=成功',
  `wechat_err_code_des` varchar(128) DEFAULT NULL COMMENT '错误代码描述',
  `wechat_err_code` varchar(18) DEFAULT NULL COMMENT '错误代码',
  `wechat_result_code` varchar(18) DEFAULT NULL COMMENT '业务结果',
  `wechat_return_msg` varchar(128) DEFAULT NULL COMMENT '返回信息',
  `wechat_return_code` varchar(18) DEFAULT NULL COMMENT '返回状态码',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_trade_notice_log_2016
-- ----------------------------

-- ----------------------------
-- Procedure structure for `proc_apliy_notice_trade_flow`
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_apliy_notice_trade_flow`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_apliy_notice_trade_flow`(
        IN buyerEmail VARCHAR(100),IN tradeno VARCHAR(60),
        IN sellerEmail VARCHAR(100),IN totalFee DECIMAL(10,3),
        IN tradeFlowNo VARCHAR(40),IN tradeStatus INT,
        IN orderId VARCHAR(60),IN yearstr VARCHAR(10),IN isNoticed INT)
BEGIN
  DECLARE insertsql VARCHAR(1024) DEFAULT '';
  DECLARE updatesql VARCHAR(1024) DEFAULT '';
  SET @buyerEmail=buyerEmail;
  SET @tradeno=tradeno;
  SET @sellerEmail=sellerEmail;
  SET @totalFee=totalFee;
  SET @tradeFlowNo=tradeFlowNo;
  SET @tradeStatus=tradeStatus;
  SET @orderId=orderId;
  SET @yearstr=yearstr;
  SET @isNoticed=isNoticed;
  /**记录支付宝回调日志**/
  SET insertsql=CONCAT('INSERT INTO alipay_trade_notice_log_',yearstr,'(trade_flow_no,pay_trade_no,trade_status,total_price,payer_trade_account,seller_trade_account,order_id,create_time) VALUES(?,?,?,?,?,?,?,now())');
  SET @sqlInsert = insertsql;
  PREPARE sqlA FROM @sqlInsert;
  EXECUTE sqlA USING @tradeFlowNo,@tradeno,@tradeStatus,@totalFee,@buyerEmail,@sellerEmail,@orderId;
  DEALLOCATE PREPARE sqlA;
  /**更新支付宝交易流水记录**/
  SET updatesql=CONCAT('update alipay_trade_flow_',yearstr,' set pay_trade_no=?,trade_status=?,is_notice_client=?,update_time=now() where trade_flow_no=?');
  SET @sqlupdate = updatesql;  
  PREPARE sqlB FROM @sqlupdate;
  EXECUTE sqlB USING @tradeno,@tradeStatus,@isNoticed,@tradeFlowNo;
  DEALLOCATE PREPARE sqlB;
  
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `proc_wechat_notice_trade_flow`
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_wechat_notice_trade_flow`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_wechat_notice_trade_flow`(
        IN out_trade_no VARCHAR(40),
	IN order_id VARCHAR(40),
        IN transaction_id VARCHAR(40),
	IN total_fee Long,
        IN trade_type VARCHAR(20),
	IN trade_status INT,
        IN err_code_des VARCHAR(128),
	IN err_code VARCHAR(18),
	IN result_code VARCHAR(18),
	IN return_msg VARCHAR(128),
	IN return_code VARCHAR(18),
	IN yearstr VARCHAR(10),
	IN is_notice_client INT
	)
BEGIN
  DECLARE insertsql VARCHAR(1024) DEFAULT '';
  DECLARE updatesql VARCHAR(1024) DEFAULT '';
  SET @out_trade_no=out_trade_no;
  SET @order_id=order_id;
  SET @transaction_id=transaction_id;
  SET @total_fee=total_fee;
  SET @trade_type=trade_type;
  SET @trade_status=trade_status;
  SET @err_code_des=err_code_des;
  SET @err_code=err_code;
  SET @result_code=result_code;
  SET @return_msg=return_msg;
  SET @return_code=return_code;
  SET @is_notice_client=is_notice_client;
  /**记录微信回调日志**/
  SET insertsql=CONCAT('INSERT INTO wechat_trade_notice_log_',yearstr,'(trade_flow_no,order_id,transaction_id,total_fee,trade_type,trade_status,wechat_err_code_des,wechat_err_code,wechat_result_code,wechat_return_msg,wechat_return_code,create_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,now())');
  SET @sqlInsert = insertsql;
  PREPARE sqlA FROM @sqlInsert;
  EXECUTE sqlA USING @out_trade_no,@order_id,@transaction_id,@total_fee,@trade_type,@trade_status,@err_code_des,@err_code,@result_code,@return_msg,@return_code;
  DEALLOCATE PREPARE sqlA;
  /**更新微信交易流水记录**/
  SET updatesql=CONCAT('update wechat_trade_flow_',yearstr,' set transaction_id=?,trade_status=?,wechat_err_code_des=?,wechat_err_code=?,wechat_result_code=?,wechat_return_msg=?,wechat_return_code=?,is_notice_client=?,update_time=now() where trade_flow_no=?');
  SET @sqlupdate = updatesql;  
  PREPARE sqlB FROM @sqlupdate;
  EXECUTE sqlB USING @transaction_id,@trade_status,@err_code_des,@err_code,@result_code,@return_msg,@return_code,@is_notice_client,@out_trade_no;
  DEALLOCATE PREPARE sqlB;
  
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `CURRVAL`
-- ----------------------------
DROP FUNCTION IF EXISTS `CURRVAL`;
DELIMITER ;;
CREATE DEFINER=`wilson`@`%` FUNCTION `CURRVAL`(seq_name VARCHAR(50)) RETURNS bigint(16)
    DETERMINISTIC
BEGIN  
         DECLARE v_value BIGINT;  
         SET v_value = 0;  
         SELECT current_value INTO v_value  
                   FROM setting_sequence  
                   WHERE NAME = seq_name;  
         RETURN v_value;  
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `NEXTVAL`
-- ----------------------------
DROP FUNCTION IF EXISTS `NEXTVAL`;
DELIMITER ;;
CREATE DEFINER=`wilson`@`%` FUNCTION `NEXTVAL`(seq_name VARCHAR(50)) RETURNS bigint(16)
    DETERMINISTIC
BEGIN  
         UPDATE setting_sequence  
                   SET current_value = CONVERT(concat(DATE_FORMAT(NOW(),'%Y%m%d'),substring(current_value,9)),UNSIGNED) + increment  
                   WHERE name = seq_name;  
         RETURN currval(seq_name);  
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `SETVAL`
-- ----------------------------
DROP FUNCTION IF EXISTS `SETVAL`;
DELIMITER ;;
CREATE DEFINER=`wilson`@`%` FUNCTION `SETVAL`(seq_name VARCHAR(50), v_value bigint) RETURNS int(11)
    DETERMINISTIC
BEGIN  
         UPDATE setting_sequence  
                   SET current_value = v_value  
                   WHERE name = seq_name;  
         RETURN currval(seq_name);  
END
;;
DELIMITER ;
