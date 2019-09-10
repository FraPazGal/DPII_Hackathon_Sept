start transaction;

create database `Acme-Booking`;

use `Acme-Booking`;

create user 'acme-user'@'%' 
	identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' 
	identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
	on `Acme-Booking`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Booking`.* to 'acme-manager'@'%';


-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Booking
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_spammer` bit(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_7ohwsa2usmvu0yxb44je2lge` (`user_account`),
  CONSTRAINT `FK_7ohwsa2usmvu0yxb44je2lge` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (19,0,'C/ Collapsing Star Roaring Cannon','boros@',NULL,'Dominator','Boros','+34666666666','https://vignette.wikia.nocookie.net/onepunchman/images/c/ce/Boros_color.jpg/revision/latest?cb=20151017212925','of the Universe',18);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `booking` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `booking_reason` longtext,
  `duration` int(11) DEFAULT NULL,
  `expected_attendance` int(11) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `requested_moment` datetime DEFAULT NULL,
  `reservation_date` datetime DEFAULT NULL,
  `reservation_price` double DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `customer` int(11) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_87ypsm0tmg3uh3w32plb2sm92` (`customer`),
  KEY `FK_4cr9x3sw4b4qg85e2peg74a7u` (`room`),
  CONSTRAINT `FK_4cr9x3sw4b4qg85e2peg74a7u` FOREIGN KEY (`room`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_87ypsm0tmg3uh3w32plb2sm92` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_services`
--

DROP TABLE IF EXISTS `booking_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `booking_services` (
  `booking` int(11) NOT NULL,
  `services` int(11) NOT NULL,
  KEY `FK_d9koxp02h2vifjqj9lgly9ade` (`services`),
  KEY `FK_22egjfy4k5i1hl3nht1m4gy1v` (`booking`),
  CONSTRAINT `FK_22egjfy4k5i1hl3nht1m4gy1v` FOREIGN KEY (`booking`) REFERENCES `booking` (`id`),
  CONSTRAINT `FK_d9koxp02h2vifjqj9lgly9ade` FOREIGN KEY (`services`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_services`
--

LOCK TABLES `booking_services` WRITE;
/*!40000 ALTER TABLE `booking_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `parent_category` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lhvwmxrdrjma5forcymeuolqn` (`parent_category`),
  CONSTRAINT `FK_lhvwmxrdrjma5forcymeuolqn` FOREIGN KEY (`parent_category`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (21,0,NULL),(22,0,21),(23,0,21),(24,0,21),(25,0,21),(26,0,22),(27,0,22),(28,0,23),(29,0,23),(30,0,23),(31,0,24),(32,0,24),(33,0,25),(34,0,24);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_child_categories`
--

DROP TABLE IF EXISTS `category_child_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_child_categories` (
  `category` int(11) NOT NULL,
  `child_categories` int(11) NOT NULL,
  UNIQUE KEY `UK_au0u5seen3d389asg00vkxpm` (`child_categories`),
  KEY `FK_8ocyptfkaphslih3earmvvk0q` (`category`),
  CONSTRAINT `FK_8ocyptfkaphslih3earmvvk0q` FOREIGN KEY (`category`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_au0u5seen3d389asg00vkxpm` FOREIGN KEY (`child_categories`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_child_categories`
--

LOCK TABLES `category_child_categories` WRITE;
/*!40000 ALTER TABLE `category_child_categories` DISABLE KEYS */;
INSERT INTO `category_child_categories` VALUES (21,22),(21,23),(21,24),(21,25),(22,26),(22,27),(23,28),(23,29),(23,30),(24,31),(24,32),(24,34),(25,33);
/*!40000 ALTER TABLE `category_child_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_rooms`
--

DROP TABLE IF EXISTS `category_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_rooms` (
  `category` int(11) NOT NULL,
  `rooms` int(11) NOT NULL,
  KEY `FK_c085yjt9eae443tk2m3j7ukqk` (`rooms`),
  KEY `FK_ithqdunruxmiq6tfes81i6h73` (`category`),
  CONSTRAINT `FK_ithqdunruxmiq6tfes81i6h73` FOREIGN KEY (`category`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_c085yjt9eae443tk2m3j7ukqk` FOREIGN KEY (`rooms`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_rooms`
--

LOCK TABLES `category_rooms` WRITE;
/*!40000 ALTER TABLE `category_rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_title`
--

DROP TABLE IF EXISTS `category_title`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_title` (
  `category` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `title_key` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`category`,`title_key`),
  CONSTRAINT `FK_geeokps9ryonllm6codorphrk` FOREIGN KEY (`category`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_title`
--

LOCK TABLES `category_title` WRITE;
/*!40000 ALTER TABLE `category_title` DISABLE KEYS */;
INSERT INTO `category_title` VALUES (21,'CATEGORY','English'),(21,'CATEGORIA','Español'),(22,'Recreation','English'),(22,'Ocio','Español'),(23,'Enterprise','English'),(23,'Empresa','Español'),(24,'Study','English'),(24,'Estudio','Español'),(25,'Culture','English'),(25,'Cultura','Español'),(26,'Party','English'),(26,'Fiesta','Español'),(27,'Cinema','English'),(27,'Cine','Español'),(28,'Conference','English'),(28,'Conferencia','Español'),(29,'Coworking','English'),(29,'Trabajo en equipo','Español'),(30,'Meeting','English'),(30,'Reuniones','Español'),(31,'Coworking','English'),(31,'Trabajo en equipo','Español'),(32,'Meeting','English'),(32,'Reuniones','Español'),(33,'Projection','English'),(33,'Proyección','Español'),(34,'Presentation','English'),(34,'Presentación','Español');
/*!40000 ALTER TABLE `category_title` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_spammer` bit(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  `cvv` int(11) DEFAULT NULL,
  `expiration_month` int(11) DEFAULT NULL,
  `expiration_year` int(11) DEFAULT NULL,
  `holder` varchar(255) DEFAULT NULL,
  `make` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mbvdes9ypo1yu76so76owiyqx` (`user_account`),
  CONSTRAINT `FK_mbvdes9ypo1yu76so76owiyqx` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder`
--

DROP TABLE IF EXISTS `finder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `capacity` int(11) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `key_word` varchar(255) DEFAULT NULL,
  `maximum_fee` double DEFAULT NULL,
  `search_moment` datetime DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `customer` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_i9i5l2bcvnc58jsb1fbufcowo` (`customer`),
  CONSTRAINT `FK_i9i5l2bcvnc58jsb1fbufcowo` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder`
--

LOCK TABLES `finder` WRITE;
/*!40000 ALTER TABLE `finder` DISABLE KEYS */;
/*!40000 ALTER TABLE `finder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder_results`
--

DROP TABLE IF EXISTS `finder_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder_results` (
  `finder` int(11) NOT NULL,
  `results` int(11) NOT NULL,
  KEY `FK_a376m4iqwc9bvh4fqwqbt7f0i` (`results`),
  KEY `FK_bo0nlx958mxty3aio68pb9bsq` (`finder`),
  CONSTRAINT `FK_bo0nlx958mxty3aio68pb9bsq` FOREIGN KEY (`finder`) REFERENCES `finder` (`id`),
  CONSTRAINT `FK_a376m4iqwc9bvh4fqwqbt7f0i` FOREIGN KEY (`results`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder_results`
--

LOCK TABLES `finder_results` WRITE;
/*!40000 ALTER TABLE `finder_results` DISABLE KEYS */;
/*!40000 ALTER TABLE `finder_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('domain_entity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` longtext,
  `is_spam` bit(1) NOT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `sent_moment` datetime DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `receiver` int(11) NOT NULL,
  `sender` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_box`
--

DROP TABLE IF EXISTS `message_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_box` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_predefined` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner` int(11) NOT NULL,
  `parent_message_boxes` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1r1litlkc3p93lnn6pt8wwllt` (`parent_message_boxes`),
  CONSTRAINT `FK_1r1litlkc3p93lnn6pt8wwllt` FOREIGN KEY (`parent_message_boxes`) REFERENCES `message_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_box`
--

LOCK TABLES `message_box` WRITE;
/*!40000 ALTER TABLE `message_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_box_messages`
--

DROP TABLE IF EXISTS `message_box_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_box_messages` (
  `message_box` int(11) NOT NULL,
  `messages` int(11) NOT NULL,
  KEY `FK_4ydibw5107qpqjwmw37t3cx5c` (`messages`),
  KEY `FK_i9fsy1u5e0dyn977c4uhdupur` (`message_box`),
  CONSTRAINT `FK_i9fsy1u5e0dyn977c4uhdupur` FOREIGN KEY (`message_box`) REFERENCES `message_box` (`id`),
  CONSTRAINT `FK_4ydibw5107qpqjwmw37t3cx5c` FOREIGN KEY (`messages`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_box_messages`
--

LOCK TABLES `message_box_messages` WRITE;
/*!40000 ALTER TABLE `message_box_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_box_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_message_boxes`
--

DROP TABLE IF EXISTS `message_message_boxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_message_boxes` (
  `message` int(11) NOT NULL,
  `message_boxes` int(11) NOT NULL,
  KEY `FK_9e3s75h2409iiuiugj5h00enq` (`message_boxes`),
  KEY `FK_pg69wrq7o02j8baa8d56f1x0q` (`message`),
  CONSTRAINT `FK_pg69wrq7o02j8baa8d56f1x0q` FOREIGN KEY (`message`) REFERENCES `message` (`id`),
  CONSTRAINT `FK_9e3s75h2409iiuiugj5h00enq` FOREIGN KEY (`message_boxes`) REFERENCES `message_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_message_boxes`
--

LOCK TABLES `message_message_boxes` WRITE;
/*!40000 ALTER TABLE `message_message_boxes` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_message_boxes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `owner`
--

DROP TABLE IF EXISTS `owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `owner` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_spammer` bit(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l09tkd2pdfn91tom3jb1qd91v` (`user_account`),
  CONSTRAINT `FK_l09tkd2pdfn91tom3jb1qd91v` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `owner`
--

LOCK TABLES `owner` WRITE;
/*!40000 ALTER TABLE `owner` DISABLE KEYS */;
/*!40000 ALTER TABLE `owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `booking_reason` longtext,
  `duration` int(11) DEFAULT NULL,
  `expected_attendance` int(11) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `requested_moment` datetime DEFAULT NULL,
  `reservation_date` datetime DEFAULT NULL,
  `reservation_price` double DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request_services`
--

DROP TABLE IF EXISTS `request_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_services` (
  `request` int(11) NOT NULL,
  `services` int(11) NOT NULL,
  KEY `FK_7y2qk8jh90selim800tvgra35` (`services`),
  KEY `FK_9yl2fjdlb98w5nncmxs0pqvmk` (`request`),
  CONSTRAINT `FK_9yl2fjdlb98w5nncmxs0pqvmk` FOREIGN KEY (`request`) REFERENCES `request` (`id`),
  CONSTRAINT `FK_7y2qk8jh90selim800tvgra35` FOREIGN KEY (`services`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request_services`
--

LOCK TABLES `request_services` WRITE;
/*!40000 ALTER TABLE `request_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `request_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `closing_hour` varchar(255) DEFAULT NULL,
  `description` longtext,
  `opening_hour` varchar(255) DEFAULT NULL,
  `photos` longtext,
  `price_per_hour` double DEFAULT NULL,
  `prove_of_ownership` varchar(255) DEFAULT NULL,
  `schedule_details` longtext,
  `status` varchar(255) DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `administrator` int(11) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_bs42hrd481h5h970bdwrlaka3` (`administrator`),
  KEY `FK_ey20koq65vwu6kl5clblc8lrs` (`owner`),
  CONSTRAINT `FK_ey20koq65vwu6kl5clblc8lrs` FOREIGN KEY (`owner`) REFERENCES `owner` (`id`),
  CONSTRAINT `FK_bs42hrd481h5h970bdwrlaka3` FOREIGN KEY (`administrator`) REFERENCES `administrator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_categories`
--

DROP TABLE IF EXISTS `room_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room_categories` (
  `room` int(11) NOT NULL,
  `categories` int(11) NOT NULL,
  KEY `FK_7ylum7e4kcoqox2vnxm5lf5eo` (`categories`),
  KEY `FK_c4roxjsu2epivptu4ujcijvma` (`room`),
  CONSTRAINT `FK_c4roxjsu2epivptu4ujcijvma` FOREIGN KEY (`room`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_7ylum7e4kcoqox2vnxm5lf5eo` FOREIGN KEY (`categories`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_categories`
--

LOCK TABLES `room_categories` WRITE;
/*!40000 ALTER TABLE `room_categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9nl1jl3qlv3otsd7or12gcobx` (`room`),
  CONSTRAINT `FK_9nl1jl3qlv3otsd7or12gcobx` FOREIGN KEY (`room`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration`
--

DROP TABLE IF EXISTS `system_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_configuration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `vattax` double DEFAULT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `makers` varchar(255) DEFAULT NULL,
  `max_results` int(11) DEFAULT NULL,
  `spam_words` varchar(255) DEFAULT NULL,
  `system_name` varchar(255) DEFAULT NULL,
  `time_results_cached` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration`
--

LOCK TABLES `system_configuration` WRITE;
/*!40000 ALTER TABLE `system_configuration` DISABLE KEYS */;
INSERT INTO `system_configuration` VALUES (20,0,0.21,'https://i.ibb.co/sqG7pgs/Acme-Booking.jpg','+034','VISA,MASTERCARD,DINNERS,AMEX',10,'sex,viagra,cialis,one million,you\'ve been selected,nigeria,sexo,un millón,un millon,has sido seleccionado','Acme-Booking',1);
/*!40000 ALTER TABLE `system_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration_breach_notification`
--

DROP TABLE IF EXISTS `system_configuration_breach_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_configuration_breach_notification` (
  `system_configuration` int(11) NOT NULL,
  `breach_notification` varchar(255) DEFAULT NULL,
  `breach_notification_key` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`system_configuration`,`breach_notification_key`),
  CONSTRAINT `FK_ayrxtmu4hqnpdg16dr66ftfun` FOREIGN KEY (`system_configuration`) REFERENCES `system_configuration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration_breach_notification`
--

LOCK TABLES `system_configuration_breach_notification` WRITE;
/*!40000 ALTER TABLE `system_configuration_breach_notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_configuration_breach_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration_welcome_message`
--

DROP TABLE IF EXISTS `system_configuration_welcome_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_configuration_welcome_message` (
  `system_configuration` int(11) NOT NULL,
  `welcome_message` varchar(255) DEFAULT NULL,
  `welcome_message_key` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`system_configuration`,`welcome_message_key`),
  CONSTRAINT `FK_afo2sg9l0wrwhjs6s7qjxwgjg` FOREIGN KEY (`system_configuration`) REFERENCES `system_configuration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration_welcome_message`
--

LOCK TABLES `system_configuration_welcome_message` WRITE;
/*!40000 ALTER TABLE `system_configuration_welcome_message` DISABLE KEYS */;
INSERT INTO `system_configuration_welcome_message` VALUES (20,'Welcome to Acme Booking! Welcome to a new era of room renting!','English'),(20,'¡Bienvenidos a Acme Booking! ¡Bienvenidos a una nueva era del alquiler de salas!','Español');
/*!40000 ALTER TABLE `system_configuration_welcome_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_banned` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_castjbvpeeus0r8lbpehiu0e4` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (18,0,'\0','21232f297a57a5a743894a0e4a801fc3','admin');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account_authorities`
--

DROP TABLE IF EXISTS `user_account_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account_authorities` (
  `user_account` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_pao8cwh93fpccb0bx6ilq6gsl` (`user_account`),
  CONSTRAINT `FK_pao8cwh93fpccb0bx6ilq6gsl` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account_authorities`
--

LOCK TABLES `user_account_authorities` WRITE;
/*!40000 ALTER TABLE `user_account_authorities` DISABLE KEYS */;
INSERT INTO `user_account_authorities` VALUES (18,'ADMIN');
/*!40000 ALTER TABLE `user_account_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-10  3:46:26

commit;