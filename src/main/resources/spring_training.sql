-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: java_spring
-- ------------------------------------------------------
-- Server version	8.0.32-0ubuntu0.22.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config` (
                          `id` int NOT NULL,
                          `max_expired_password` int DEFAULT NULL,
                          `max_numbers_login_attempts` int DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,5,5);
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `user_role_id` int NOT NULL,
                        `user_email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                        `user_avatar` longtext COLLATE utf8mb4_general_ci,
                        `user_first_name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
                        `user_last_name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
                        `user_date_of_birth` date DEFAULT NULL,
                        `user_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `user_phone_number` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `user_init_password` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `user_password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `user_state` tinyint DEFAULT NULL,
                        `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `modify_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `user_number_login_attempts` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `user_last_time_changed_password` timestamp,
                        PRIMARY KEY (`id`),
                        KEY `user_role_id` (`user_role_id`),
                        CONSTRAINT `user_ibfk_1` FOREIGN KEY (`user_role_id`) REFERENCES `user_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,'a@gmai.com',NULL,'vu','vu',NULL,NULL,NULL,'12354aA!',NULL,1,'2023-04-10 03:09:35','2023-04-10 03:09:35','0');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `role_name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
                             `role_desc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `modify_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `role_icon` longtext COLLATE utf8mb4_general_ci,
                             `role_state` tinyint DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'Admin','Admin','2022-07-03 16:42:53','2022-07-03 16:42:53',NULL,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-10 11:03:34
