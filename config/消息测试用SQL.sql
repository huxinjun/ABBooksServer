/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.7.17-log : Database - accountbook
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`accountbook` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `accountbook`;

/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `book_id` varchar(64) DEFAULT NULL COMMENT '所属账本,0为默认全部账目的账本',
  `type` varchar(8) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `description` text,
  `imgs` text,
  `date_timestamp` timestamp NULL DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL,
  `addr_name` varchar(64) DEFAULT NULL,
  `addr` varchar(64) DEFAULT NULL,
  `addr_lat` double DEFAULT NULL,
  `addr_lon` double DEFAULT NULL,
  `paid_in` double DEFAULT NULL,
  `is_private` tinyint(1) DEFAULT NULL COMMENT '是否是个人账单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `account` */

insert  into `account`(`id`,`user_id`,`book_id`,`type`,`name`,`description`,`imgs`,`date_timestamp`,`create_timestamp`,`addr_name`,`addr`,`addr_lat`,`addr_lon`,`paid_in`,`is_private`) values ('+Aid_0vFkYJJBiQ5ufpIEg==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','zf','租房','a\r\nb','','2017-12-26 00:00:00','2017-12-26 19:46:53',NULL,NULL,0,0,1,1),('2vTuBBDRaojpr6mM84k_tQ==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','zf','租房','','','2017-12-26 00:00:00','2017-12-26 20:15:40',NULL,NULL,0,0,2,0),('3auNBcI66JjlYoTYkl_3XQ==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','sh','生活','','','2017-12-15 00:00:00','2017-12-15 20:52:24',NULL,NULL,0,0,68.5,0),('3GXc1c96f94SWEqc3FEIsg==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','sh','生活','','','2017-12-15 00:00:00','2017-12-15 20:44:18',NULL,NULL,0,0,68.5,0),('6UBk5uNl23UNMpmILCn0bw==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','zf','aaaaaaaaaa','今天心情不错\r\n西祠低首付\r\n你的合围请回复\r\ndsadewq cdwew\r\nfewfwefe\r\nfre\r\nrfgewgfrewgrew\r\nrefrewq\r\ndsadsadsa','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/ZzKUYM_PKAi1ld+S1Qws3g==XzBB','2017-12-26 00:00:00','2017-12-26 20:01:49',NULL,NULL,0,0,1,1),('7hFmSoyxreXi38LwWuJQcA==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','zf','租房','a\r\nb\r\nc','','2017-12-26 00:00:00','2017-12-26 19:44:06',NULL,NULL,0,0,1,1),('b6oPKWtfHwM4yP6SRhb8BQ==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','sr','收入','','','2017-12-19 00:00:00','2017-12-19 17:26:29',NULL,NULL,0,0,222,1),('IidpeK7cD167rCo6ELkarQ==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','jt','交通','','','2017-12-27 00:00:00','2017-12-27 16:50:09',NULL,NULL,0,0,1,1),('LFkP6DoOfi0mRLqP8gcm7A==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','zf','租房','','','2017-12-26 00:00:00','2017-12-26 20:07:02',NULL,NULL,0,0,258,0),('MHT_DNNaEbQzvgvo60Icow==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','zf','租房','','','2017-12-26 00:00:00','2017-12-26 20:30:31',NULL,NULL,0,0,123,0),('x2awRnFvQAf7tzLGUyxfHw==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','','sh','生活','','','2017-12-15 00:00:00','2017-12-15 20:54:52',NULL,NULL,0,0,68.5,0);

/*Table structure for table `account_member` */

DROP TABLE IF EXISTS `account_member`;

CREATE TABLE `account_member` (
  `id` varchar(64) NOT NULL,
  `account_id` varchar(64) DEFAULT NULL,
  `is_group` tinyint(1) NOT NULL COMMENT '是否是成员组,默认为0',
  `member_id` varchar(64) NOT NULL,
  `member_name` varchar(20) DEFAULT NULL,
  `member_icon` varchar(128) DEFAULT NULL,
  `rule_type` tinyint(2) DEFAULT NULL COMMENT '-1:没有特殊规则 0:基于总支出的百分比的值1:缴费为一个固定值',
  `rule_num` double DEFAULT NULL COMMENT '特殊规则的数额',
  `money_for_self` double DEFAULT NULL COMMENT '自费',
  `paid_in` double NOT NULL COMMENT '实付款',
  `should_pay` double DEFAULT NULL COMMENT '应付款',
  `parent_member_id` varchar(64) DEFAULT NULL COMMENT '如果此成员是组中的,那么会有这个属性标识父成员的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `account_member` */

insert  into `account_member`(`id`,`account_id`,`is_group`,`member_id`,`member_name`,`member_icon`,`rule_type`,`rule_num`,`money_for_self`,`paid_in`,`should_pay`,`parent_member_id`) values ('02VoZs9mJ1nkMGINtK+QnQ==','x2awRnFvQAf7tzLGUyxfHw==',1,'zTvfnSZr_iy71JeHca7SLQ==','次卧','zTvfnSZr_iy71JeHca7SLQ==/GCuElZhDoIFgA0P6ZWI99A==XzBB',0,0,0,68.5,17.13,NULL),('47B2SmQZLO2y3lpOfr3WQg==','LFkP6DoOfi0mRLqP8gcm7A==',0,'oCBrx0FreB-L8pIQM5_RYDGoWOKQ','游客','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/bgxj85FZiEvGpJvAO+ybgA==XzBB',0,0,0,0,129,NULL),('554DCPs9ZrcToyKA1BVvZw==','3auNBcI66JjlYoTYkl_3XQ==',1,'7jjn17xgz48fR+_9BZRqpg==','隔断','7jjn17xgz48fR+_9BZRqpg==/9+Wv3ONwi4mA9gjnxc2nYw==XzBB',0,0,0,0,17.13,NULL),('6L7Nm36GfAbycDzDCVFUWw==','3GXc1c96f94SWEqc3FEIsg==',1,'7jjn17xgz48fR+_9BZRqpg==','隔断','7jjn17xgz48fR+_9BZRqpg==/9+Wv3ONwi4mA9gjnxc2nYw==XzBB',0,0,0,0,17.13,NULL),('8r_zz+RJeB06ZPLQB2TKUw==','2vTuBBDRaojpr6mM84k_tQ==',0,'oCBrx0ExpR4J4DM1qM342xQ4HYWQ','xzbenben','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/qzuzImEx08gYwyC3VcARYA==XzBB',0,0,0,2,1,NULL),('8stU4WWs_EnskR_2+g_Ouw==','x2awRnFvQAf7tzLGUyxfHw==',1,'0FUPM94WNJU0ZB268nCAOA==','书房','0FUPM94WNJU0ZB268nCAOA==/2aGNgzTkOFwanWcd_CJ0fQ==XzBB',0,0,0,0,17.13,NULL),('995j2jNKSHno2eGjHU412A==','3auNBcI66JjlYoTYkl_3XQ==',1,'zTvfnSZr_iy71JeHca7SLQ==','次卧','zTvfnSZr_iy71JeHca7SLQ==/GCuElZhDoIFgA0P6ZWI99A==XzBB',0,0,0,68.5,17.13,NULL),('A3I39PEQpvPZrWnf3A2Zdg==','x2awRnFvQAf7tzLGUyxfHw==',0,'oCBrx0ExpR4J4DM1qM342xQ4HYWQ','xzbenben','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/qzuzImEx08gYwyC3VcARYA==XzBB',0,0,0,0,8.56,'MvZOjzjjBzgBzwyn3ImUcA=='),('A7j+g_evU_bXfab7qzRa6w==','x2awRnFvQAf7tzLGUyxfHw==',1,'MvZOjzjjBzgBzwyn3ImUcA==','主卧','MvZOjzjjBzgBzwyn3ImUcA==/_Xs7ymCztMb02p4giOOHXQ==XzBB',0,0,0,0,17.13,NULL),('AX6V15ALNWJZseB6XGTn6A==','x2awRnFvQAf7tzLGUyxfHw==',0,'oCBrx0FreB-L8pIQM5_RYDGoWOKQ','新军','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/CxFtR0zITLxnOhz+Rm4Kpw==XzBB',0,0,0,17.13,8.56,'MvZOjzjjBzgBzwyn3ImUcA=='),('c_tp1lwoJhN9VJs7RIYl8w==','MHT_DNNaEbQzvgvo60Icow==',1,'0FUPM94WNJU0ZB268nCAOA==','书房','0FUPM94WNJU0ZB268nCAOA==/ER+G2IFQ+wHWkXeD1T3o4A==XzBB',0,0,0,0,41,NULL),('d6dxm9a8KHaBIXGBhii2XA==','MHT_DNNaEbQzvgvo60Icow==',1,'t2+Ani3iELDH8g8LUFymaQ==','123','t2+Ani3iELDH8g8LUFymaQ==/r5EXW7D0pWdvQUBXdfdj1w==XzBB',0,0,0,123,41,NULL),('dsA2fL1EPzN1qk2fQp4HUQ==','MHT_DNNaEbQzvgvo60Icow==',0,'oCBrx0FreB-L8pIQM5_RYDGoWOKQ','游客','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/bgxj85FZiEvGpJvAO+ybgA==XzBB',0,0,0,20,20.5,'MvZOjzjjBzgBzwyn3ImUcA=='),('F2zhnvYrDfjeZAl8HYywAA==','3auNBcI66JjlYoTYkl_3XQ==',1,'0FUPM94WNJU0ZB268nCAOA==','书房','0FUPM94WNJU0ZB268nCAOA==/2aGNgzTkOFwanWcd_CJ0fQ==XzBB',0,0,0,0,17.13,NULL),('iI3bb7HKcpJaIz3NkLkwSg==','jCttYcLpCXMij+DdRcUX5A==',0,'oCBrx0ExpR4J4DM1qM342xQ4HYWQ','xzbenben','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/qzuzImEx08gYwyC3VcARYA==XzBB',0,0,0,1,0,NULL),('iZyosAusDIFpzgxkKac9Mw==','jCttYcLpCXMij+DdRcUX5A==',0,'oCBrx0FreB-L8pIQM5_RYDGoWOKQ','游客','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/bgxj85FZiEvGpJvAO+ybgA==XzBB',0,0,0,0,0,NULL),('J9C97tvespma6ynXHExS9Q==','3GXc1c96f94SWEqc3FEIsg==',1,'0FUPM94WNJU0ZB268nCAOA==','书房','0FUPM94WNJU0ZB268nCAOA==/2aGNgzTkOFwanWcd_CJ0fQ==XzBB',0,0,0,0,17.13,NULL),('krTQN1cOL9lWwkmvGeHQcQ==','3GXc1c96f94SWEqc3FEIsg==',1,'zTvfnSZr_iy71JeHca7SLQ==','次卧','zTvfnSZr_iy71JeHca7SLQ==/GCuElZhDoIFgA0P6ZWI99A==XzBB',0,0,0,68.5,17.13,NULL),('mDascohgjQXpVVuEDxfOFg==','LFkP6DoOfi0mRLqP8gcm7A==',0,'oCBrx0ExpR4J4DM1qM342xQ4HYWQ','xzbenben','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/qzuzImEx08gYwyC3VcARYA==XzBB',0,0,0,258,129,NULL),('MSr_F31GdzmHIfDawDTSjQ==','MHT_DNNaEbQzvgvo60Icow==',0,'oCBrx0ExpR4J4DM1qM342xQ4HYWQ','xzbenben','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/qzuzImEx08gYwyC3VcARYA==XzBB',0,0,0,21,20.5,'MvZOjzjjBzgBzwyn3ImUcA=='),('NhrldAUf5GAHiSlB6gKrTg==','3GXc1c96f94SWEqc3FEIsg==',1,'MvZOjzjjBzgBzwyn3ImUcA==','主卧','MvZOjzjjBzgBzwyn3ImUcA==/_Xs7ymCztMb02p4giOOHXQ==XzBB',0,0,0,0,17.13,NULL),('PM3i9U8pZ0yizDSP_f9LJA==','x2awRnFvQAf7tzLGUyxfHw==',1,'7jjn17xgz48fR+_9BZRqpg==','隔断','7jjn17xgz48fR+_9BZRqpg==/9+Wv3ONwi4mA9gjnxc2nYw==XzBB',0,0,0,0,17.13,NULL),('ri6cG7NJ8G_eIbdMKfZZhQ==','2vTuBBDRaojpr6mM84k_tQ==',0,'oCBrx0FreB-L8pIQM5_RYDGoWOKQ','游客','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/bgxj85FZiEvGpJvAO+ybgA==XzBB',0,0,0,0,1,NULL),('vsldTiz0TBFynfo+br87LA==','3auNBcI66JjlYoTYkl_3XQ==',1,'MvZOjzjjBzgBzwyn3ImUcA==','主卧','MvZOjzjjBzgBzwyn3ImUcA==/_Xs7ymCztMb02p4giOOHXQ==XzBB',0,0,0,0,17.13,NULL),('xg1t_R_DzCPAJ2WUMnqgYQ==','MHT_DNNaEbQzvgvo60Icow==',1,'MvZOjzjjBzgBzwyn3ImUcA==','主卧','MvZOjzjjBzgBzwyn3ImUcA==/_Xs7ymCztMb02p4giOOHXQ==XzBB',0,0,0,0,41,NULL);

/*Table structure for table `account_pay` */

DROP TABLE IF EXISTS `account_pay`;

CREATE TABLE `account_pay` (
  `id` varchar(64) NOT NULL,
  `account_id` varchar(64) NOT NULL,
  `paid_id` varchar(64) NOT NULL,
  `receipt_id` varchar(64) NOT NULL,
  `money` double DEFAULT '0',
  `wait_paid_money` double DEFAULT '0' COMMENT '待付金额',
  `paid_status` tinyint(1) DEFAULT NULL COMMENT '0:无需完善 1:需要手动完善组内账单 2:已经完善账单 3:自动抵消',
  `receipt_status` tinyint(1) DEFAULT NULL COMMENT '0:无需完善 1:需要手动完善组内账单 2:已经完善账单 3:自动抵消',
  `offset_count` int(11) DEFAULT NULL COMMENT '抵消记录的个数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `account_pay` */

insert  into `account_pay`(`id`,`account_id`,`paid_id`,`receipt_id`,`money`,`wait_paid_money`,`paid_status`,`receipt_status`,`offset_count`) values ('50j26_HN0swGcDlh_94HqQ==','3GXc1c96f94SWEqc3FEIsg==','7jjn17xgz48fR+_9BZRqpg==','zTvfnSZr_iy71JeHca7SLQ==',17.13,17.13,0,0,0),('6y4+JzTIhtKBGc_YqHs3Xw==','MHT_DNNaEbQzvgvo60Icow==','0FUPM94WNJU0ZB268nCAOA==','t2+Ani3iELDH8g8LUFymaQ==',41,41,0,0,0),('dbhP9Zl+Xa9AxZRbSnTWSw==','x2awRnFvQAf7tzLGUyxfHw==','0FUPM94WNJU0ZB268nCAOA==','zTvfnSZr_iy71JeHca7SLQ==',17.11,17.11,0,0,0),('dT+QDTx4jo8939duX3O9AA==','3GXc1c96f94SWEqc3FEIsg==','MvZOjzjjBzgBzwyn3ImUcA==','zTvfnSZr_iy71JeHca7SLQ==',17.13,17.13,1,0,0),('Fy0NzcDPeIwGKTaLJ0Pfpw==','LFkP6DoOfi0mRLqP8gcm7A==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',129,120.44,0,0,1),('GTDPZZOMj86IOCozxcYJqg==','MHT_DNNaEbQzvgvo60Icow==','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','t2+Ani3iELDH8g8LUFymaQ==',21,0,0,0,0),('hFH2C4vNHjjxYJAjjAmDCw==','MHT_DNNaEbQzvgvo60Icow==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','t2+Ani3iELDH8g8LUFymaQ==',20,0,0,0,0),('Hq_xSUD2mrXYHHnpDL0lGg==','3auNBcI66JjlYoTYkl_3XQ==','MvZOjzjjBzgBzwyn3ImUcA==','zTvfnSZr_iy71JeHca7SLQ==',17.13,17.13,1,0,0),('IdyYX+lEKZdQb_ZrvwOCuQ==','x2awRnFvQAf7tzLGUyxfHw==','7jjn17xgz48fR+_9BZRqpg==','zTvfnSZr_iy71JeHca7SLQ==',17.13,17.13,0,0,0),('JWPhnd3VlEPkrlSyAlVh0g==','MHT_DNNaEbQzvgvo60Icow==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',0.5,0.5,0,0,0),('MFTbIzuA7asnAXt1ciRDOA==','x2awRnFvQAf7tzLGUyxfHw==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','zTvfnSZr_iy71JeHca7SLQ==',17.13,0,0,0,0),('taQyRc1T42LuGwvkP7Jauw==','3GXc1c96f94SWEqc3FEIsg==','0FUPM94WNJU0ZB268nCAOA==','zTvfnSZr_iy71JeHca7SLQ==',17.11,17.11,0,0,0),('toOaN6V6_VeG5P9yjC9fHw==','x2awRnFvQAf7tzLGUyxfHw==','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',8.56,0,0,0,1),('Ynt9UgXLIbQ4Pjfzgq6SNg==','2vTuBBDRaojpr6mM84k_tQ==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',1,1,0,0,0),('zgR3mZC+TYzBwX+MVACCpA==','3auNBcI66JjlYoTYkl_3XQ==','0FUPM94WNJU0ZB268nCAOA==','zTvfnSZr_iy71JeHca7SLQ==',17.11,17.11,0,0,0),('_bbRIwjqOnzl0_mxMj7OMg==','3auNBcI66JjlYoTYkl_3XQ==','7jjn17xgz48fR+_9BZRqpg==','zTvfnSZr_iy71JeHca7SLQ==',17.13,17.13,0,0,0);

/*Table structure for table `account_pay_offset` */

DROP TABLE IF EXISTS `account_pay_offset`;

CREATE TABLE `account_pay_offset` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `target_pay_id` varchar(64) DEFAULT NULL COMMENT '抵扣账单',
  `origin_pay_id` varchar(64) DEFAULT NULL COMMENT '被抵扣账单,原始的',
  `money` double DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `account_pay_offset` */

insert  into `account_pay_offset`(`id`,`target_pay_id`,`origin_pay_id`,`money`) values (1,'toOaN6V6_VeG5P9yjC9fHw==','Fy0NzcDPeIwGKTaLJ0Pfpw==',8.56);

/*Table structure for table `book` */

DROP TABLE IF EXISTS `book`;

CREATE TABLE `book` (
  `id` varchar(64) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `icon` varchar(128) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `book` */

/*Table structure for table `form` */

DROP TABLE IF EXISTS `form`;

CREATE TABLE `form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) DEFAULT NULL,
  `form_id` varchar(40) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4;

/*Data for the table `form` */

/*Table structure for table `friend` */

DROP TABLE IF EXISTS `friend`;

CREATE TABLE `friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `invite_id` varchar(64) NOT NULL,
  `accept_id` varchar(64) NOT NULL,
  `time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

/*Data for the table `friend` */

insert  into `friend`(`id`,`invite_id`,`accept_id`,`time`) values (18,'oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1515041270642);

/*Table structure for table `group_` */

DROP TABLE IF EXISTS `group_`;

CREATE TABLE `group_` (
  `id` varchar(64) NOT NULL,
  `name` varchar(20) NOT NULL,
  `admin_id` varchar(64) NOT NULL,
  `time` bigint(20) NOT NULL,
  `icon` varchar(128) DEFAULT NULL,
  `qr` varchar(64) DEFAULT NULL,
  `category` varchar(30) DEFAULT NULL COMMENT '简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `group_` */

insert  into `group_`(`id`,`name`,`admin_id`,`time`,`icon`,`qr`,`category`) values ('0FUPM94WNJU0ZB268nCAOA==','书房','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1507543961526,'0FUPM94WNJU0ZB268nCAOA==/ER+G2IFQ+wHWkXeD1T3o4A==XzBB',NULL,'国际城4单元2201'),('7jjn17xgz48fR+_9BZRqpg==','隔断','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1507543974428,'7jjn17xgz48fR+_9BZRqpg==/T9ZBQQTfJ+ynyRB6ed2swg==XzBB',NULL,'国际城4单元2201'),('Bhcz_7EB_YETX5aGZhGvDg==','哈哈','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1512751520728,'Bhcz_7EB_YETX5aGZhGvDg==/p7xzYOBC3AF3lSRYksvoOQ==XzBB',NULL,'分'),('IqSdYHxc587BdLUlTvSoJQ==','2','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1509446172152,'IqSdYHxc587BdLUlTvSoJQ==/5vMVtlfYTqmbXneIG83+4w==XzBB','IqSdYHxc587BdLUlTvSoJQ==/oTjm4xbgK9EQ8LSc1OBZ0g==XzBB',''),('MvZOjzjjBzgBzwyn3ImUcA==','主卧','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1507547872767,'MvZOjzjjBzgBzwyn3ImUcA==/_Xs7ymCztMb02p4giOOHXQ==XzBB','MvZOjzjjBzgBzwyn3ImUcA==/ui28SXRtVJN1eLIV0yrQPQ==XzBB','国际城4单元2202'),('t2+Ani3iELDH8g8LUFymaQ==','123','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1509445820879,'t2+Ani3iELDH8g8LUFymaQ==/r5EXW7D0pWdvQUBXdfdj1w==XzBB','t2+Ani3iELDH8g8LUFymaQ==/sAUK_TY3LZhb8mqvk2TYNQ==XzBB',''),('T85TAXQ1RuMig9LjMC84qw==','⊙∀⊙！','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1509447169836,'T85TAXQ1RuMig9LjMC84qw==/YSobZxEip_tddsMOcKC5Jg==XzBB','T85TAXQ1RuMig9LjMC84qw==/sPByZnJryg8oSGj7n+6wgA==XzBB','哈哈?'),('VqejC8mla4wDpquE8H0n5g==','q','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1509446096518,'VqejC8mla4wDpquE8H0n5g==/DoPFH3fW96mqjq9VTdteKg==XzBB','VqejC8mla4wDpquE8H0n5g==/v++CPX7wYUKqWRsKutBOOQ==XzBB',''),('zTvfnSZr_iy71JeHca7SLQ==','次卧','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1507543930783,'zTvfnSZr_iy71JeHca7SLQ==/GCuElZhDoIFgA0P6ZWI99A==XzBB',NULL,'国际城4单元2202');

/*Table structure for table `group_user` */

DROP TABLE IF EXISTS `group_user`;

CREATE TABLE `group_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;

/*Data for the table `group_user` */

insert  into `group_user`(`id`,`group_id`,`user_id`) values (24,'MvZOjzjjBzgBzwyn3ImUcA==','oCBrx0ExpR4J4DM1qM342xQ4HYWQ'),(25,'MvZOjzjjBzgBzwyn3ImUcA==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ'),(27,'7jjn17xgz48fR+_9BZRqpg==','oCBrx0FreB-L8pIQM5_RYDGoWOKQ');

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` varchar(64) NOT NULL,
  `from_id` varchar(64) DEFAULT NULL,
  `to_id` varchar(64) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '0:普通消息 1:好友邀请',
  `content` text,
  `time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `message` */

insert  into `message`(`id`,`from_id`,`to_id`,`type`,`content`,`time`) values ('186','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[Create]:3GXc1c96f94SWEqc3FEIsg==','2017-12-15 20:44:18'),('187','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[Create]:3auNBcI66JjlYoTYkl_3XQ==','2017-12-15 20:52:24'),('188','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[Create]:x2awRnFvQAf7tzLGUyxfHw==','2017-12-15 20:54:52'),('189','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[CreateInner]:x2awRnFvQAf7tzLGUyxfHw==:MvZOjzjjBzgBzwyn3ImUcA==','2017-12-15 20:55:47'),('190','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[Create]:LFkP6DoOfi0mRLqP8gcm7A==','2017-12-26 20:07:03'),('191','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[Create]:2vTuBBDRaojpr6mM84k_tQ==','2017-12-26 20:15:40'),('192','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[Create]:MHT_DNNaEbQzvgvo60Icow==','2017-12-26 20:30:31'),('193','oCBrx0FreB-L8pIQM5_RYDGoWOKQ','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',3,'[CreateInner]:MHT_DNNaEbQzvgvo60Icow==:MvZOjzjjBzgBzwyn3ImUcA==','2017-12-26 20:38:24'),('219','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:33'),('220','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:33'),('221','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:33'),('222','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:33'),('223','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:35'),('224','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:35'),('225','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:35'),('226','oCBrx0ExpR4J4DM1qM342xQ4HYWQ','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1,NULL,'2018-01-04 14:37:35');

/*Table structure for table `message_state` */

DROP TABLE IF EXISTS `message_state`;

CREATE TABLE `message_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msg_id` varchar(64) DEFAULT NULL,
  `pid` varchar(64) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `message_state` */

insert  into `message_state`(`id`,`msg_id`,`pid`,`state`) values (1,'186','oCBrx0FreB-L8pIQM5_RYDGoWOKQ',1),(2,'186','oCBrx0ExpR4J4DM1qM342xQ4HYWQ',1);

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` varchar(50) NOT NULL,
  `token` varchar(50) NOT NULL,
  `expire_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `token` */

insert  into `token`(`id`,`token`,`expire_time`) values ('oCBrx0ExpR4J4DM1qM342xQ4HYWQ','2hNCFpFaI3Oo0chxRIHmmg==',-1),('oCBrx0FreB-L8pIQM5_RYDGoWOKQ','8TTkKrpb69pO0oXXmlNnNw==',-1);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(64) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `icon` varchar(128) DEFAULT NULL,
  `avatarUrl` varchar(256) DEFAULT NULL,
  `qr` varchar(64) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `language` varchar(10) DEFAULT NULL,
  `notif_open` tinyint(1) DEFAULT '1' COMMENT '微信模板提醒',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user` */

insert  into `user`(`id`,`nickname`,`icon`,`avatarUrl`,`qr`,`gender`,`country`,`province`,`city`,`language`,`notif_open`) values ('oCBrx0ExpR4J4DM1qM342xQ4HYWQ','xzbenben','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/qzuzImEx08gYwyC3VcARYA==XzBB','https://wx.qlogo.cn/mmopen/vi_32/FcDVBfSKEialwnpwep7H6XcCkVNDSVs5MOx3yzGuxwIhfNYB169sWCic5zPB5ptRyrqeM0w7VPWyotdRhP9cBI9A/0','oCBrx0ExpR4J4DM1qM342xQ4HYWQ/CP3D_CX79kfrwwj2ghulrA==XzBB',NULL,NULL,NULL,NULL,'zh_CN',0),('oCBrx0FreB-L8pIQM5_RYDGoWOKQ','游客','oCBrx0FreB-L8pIQM5_RYDGoWOKQ/bgxj85FZiEvGpJvAO+ybgA==XzBB','https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1513847354&di=5e58923ad6a9f7d8cc7a9c76c062d206&src=http://tupian.enterdesk.com/2014/lxy/2014/12/03/39/7.jpg',NULL,NULL,NULL,NULL,NULL,NULL,1);

/*Table structure for table `temp_me_account` */

DROP TABLE IF EXISTS `temp_me_account`;

/*!50001 DROP VIEW IF EXISTS `temp_me_account` */;
/*!50001 DROP TABLE IF EXISTS `temp_me_account` */;

/*!50001 CREATE TABLE  `temp_me_account`(
 `account_id` varchar(64) 
)*/;

/*View structure for view temp_me_account */

/*!50001 DROP TABLE IF EXISTS `temp_me_account` */;
/*!50001 DROP VIEW IF EXISTS `temp_me_account` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `temp_me_account` AS select distinct `account_member`.`account_id` AS `account_id` from `account_member` where ((`account_member`.`member_id` = 'oCBrx0FreB-L8pIQM5_RYDGoWOKQ') or `account_member`.`member_id` in (select `group_user`.`group_id` from `group_user` where (`group_user`.`user_id` = 'oCBrx0FreB-L8pIQM5_RYDGoWOKQ') union select `group_`.`id` from `group_` where (`group_`.`admin_id` = 'oCBrx0FreB-L8pIQM5_RYDGoWOKQ'))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
