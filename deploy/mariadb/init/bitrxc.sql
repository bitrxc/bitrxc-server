-- MariaDB dump 10.19  Distrib 10.5.9-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bitrxc
-- ------------------------------------------------------
-- Server version	10.5.9-MariaDB-1:10.5.9+maria~focal

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(16) NOT NULL,
  `password` char(64) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','admin','123456','123456@qq.com');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_role`
--

DROP TABLE IF EXISTS `admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role`
--

LOCK TABLES `admin_role` WRITE;
/*!40000 ALTER TABLE `admin_role` DISABLE KEYS */;
INSERT INTO `admin_role` VALUES (1,1,1);
/*!40000 ALTER TABLE `admin_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deal`
--

DROP TABLE IF EXISTS `deal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deal` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `room` smallint(5) unsigned NOT NULL,
  `launch_time` int(10) DEFAULT NULL,
  `launcher` varchar(255) NOT NULL,
  `status` enum('new','receive','executing','reject','cancel','finish','missed','illegal','signed') DEFAULT NULL,
  `user_note` tinytext CHARACTER SET utf8 DEFAULT NULL,
  `check_note` tinytext DEFAULT NULL,
  `conductor` varchar(255) DEFAULT NULL,
  `launch_date` datetime DEFAULT NULL,
  `check_date` datetime DEFAULT NULL,
  `exec_date` date DEFAULT NULL,
  `begin` tinyint(3) unsigned DEFAULT NULL,
  `end` tinyint(3) unsigned DEFAULT NULL,
  `attendance` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deal`
--

LOCK TABLES `deal` WRITE;
/*!40000 ALTER TABLE `deal` DISABLE KEYS */;
/*!40000 ALTER TABLE `deal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gallery`
--

DROP TABLE IF EXISTS `gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gallery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prefered_image` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gallery`
--

LOCK TABLES `gallery` WRITE;
/*!40000 ALTER TABLE `gallery` DISABLE KEYS */;
/*!40000 ALTER TABLE `gallery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `images` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `image_hash` char(64) NOT NULL,
  `desc` tinytext CHARACTER SET utf8 DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'','manager');
INSERT INTO `permission` VALUES (2,'','appointCheck');
INSERT INTO `permission` VALUES (3,'','room');
INSERT INTO `permission` VALUES (4,'','appoint');
INSERT INTO `permission` VALUES (5,'','basicView');
INSERT INTO `permission` VALUES (6,'','user');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'root');
INSERT INTO `role` VALUES (2,'appointChecker');
INSERT INTO `role` VALUES (3,'superAppointmentLauncher');
INSERT INTO `role` VALUES (4,'userChecker');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(11) NOT NULL,
  `permission_id` bigint(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1,1);
INSERT INTO `role_permission` VALUES (2,1,2);
INSERT INTO `role_permission` VALUES (3,2,2);
INSERT INTO `role_permission` VALUES (4,1,3);
INSERT INTO `role_permission` VALUES (5,1,4);
INSERT INTO `role_permission` VALUES (6,3,4);
INSERT INTO `role_permission` VALUES (7,1,5);
INSERT INTO `role_permission` VALUES (8,2,5);
INSERT INTO `role_permission` VALUES (9,3,5);
INSERT INTO `role_permission` VALUES (10,4,5);
INSERT INTO `role_permission` VALUES (11,1,6);
INSERT INTO `role_permission` VALUES (12,4,6);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` text CHARACTER SET utf8 DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'导学室','{ \"面积\": \"30\", \"容纳人数\": \"25\", \"设备情况\": \"投影、桌椅\", \"承载功能\": \"导学活动、学业指导、读书沙龙、社团活动、创意研讨、党团活动\" }','https://static.bitrxc.com/image/room/1379053355070001152.jpeg'),(2,'动感驿站','{         \"面积\": \"65\",         \"容纳人数\": \"30\",         \"设备情况\": \"健身器材、垫子板凳\",         \"承载功能\": \"器乐演奏、歌舞排练\"       }','https://static.bitrxc.com/image/room/1379056585350975488.jpeg'),(3,'心理放松室','    {       \"面积\": \"40\",       \"容纳人数\": \"25\",       \"设备情况\": \"桌椅\",       \"承载功能\": \"导学活动、学业指导、读书沙龙、社团活动、创意研讨、心理辅导、党团活动（若要使用桌游棋牌等物资，请联系桌游社指导）\"     }',NULL),(4,'1024·研讨室','    {       \"面积\": \"15\",       \"容纳人数\": \"8\",       \"设备情况\": \"桌椅\",       \"承载功能\": \"导学活动、学业指导、创意研讨、小型报告\"     }','https://static.bitrxc.com/image/room/1379065434132844544.jpeg'),(5,'RGB·研讨室','    {       \"面积\": \"15\",       \"容纳人数\": \"8\",       \"设备情况\": \"桌椅\",       \"承载功能\": \"导学活动、学业指导、创意研讨、小型报告\"     }','https://static.bitrxc.com/image/room/1379066384977367040.jpeg'),(6,'Facetime室','{       \"面积\": \"45\",       \"容纳人数\": \"30\",       \"设备情况\": \"多媒体、坐垫、阶梯长椅\",       \"承载功能\": \"导学活动、读书沙龙、社团活动、创意研讨、小型报告、心理辅导、党团活动\"     }','https://static.bitrxc.com/image/room/1379066612468027392.jpeg'),(7,'自控会议室','    {       \"面积\": 40,       \"容纳人数\": 12,       \"设备情况\": \"投影、桌椅\",       \"承载功能\": \"导学活动、学业指导、读书沙龙、社团活动、创意研讨、党团活动\"     }','https://static.bitrxc.com/image/room/1379066827241558016.jpeg'),(8,'党建办公室','    {       \"面积\": 40,       \"容纳人数\": 12,       \"设备情况\": \"投影\",       \"承载功能\": \"党团活动（仅供共学会预约）\"     }','https://static.bitrxc.com/image/room/1379067114467495936.jpeg'),(9,'鸿远报告厅','    {       \"面积\": 70,       \"容纳人数\": 40,       \"设备情况\": \"多媒体\",       \"承载功能\": \"导学活动、学业指导、读书沙龙、社团活动、创意研讨、党团活动\"     }','https://static.bitrxc.com/image/room/1379067441912614912.jpeg');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `begin` time DEFAULT NULL,
  `end` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,'08:00:00','10:00:00'),(2,'10:00:00','12:00:00'),(3,'13:00:00','15:00:00'),(4,'15:00:00','18:00:00'),(5,'18:00:00','21:00:00'),(6,'21:00:00','22:00:00');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wxid` char(28) DEFAULT NULL,
  `phone` varchar(14) DEFAULT NULL,
  `organize` varchar(8) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(8) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `school_id` char(10) DEFAULT NULL,
  `checked` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting`
--

DROP TABLE IF EXISTS `meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meeting` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint(20) DEFAULT NULL,
  `name` varchar(8) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `launcher_id` int(10) unsigned NOT NULL,
  `begin` time DEFAULT NULL,
  `end` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `attendence_record`
--

DROP TABLE IF EXISTS `attendence_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attendence_record` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `meeting_id` int(10) unsigned DEFAULT NULL,
  `user_note` varchar(255) DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `begin` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-13 13:44:38
