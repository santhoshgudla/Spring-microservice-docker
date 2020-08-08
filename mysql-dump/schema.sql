CREATE DATABASE IF NOT EXISTS blog;

USE blog;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `username` varchar(126) NOT NULL,
  `email` varchar(126) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '0',
  `is_verified` tinyint(1) DEFAULT '0',
  `is_delete` tinyint(1) DEFAULT '0',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role` enum('ADMIN','PROVIDER','USER') DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `user` (`uuid`, `username`, `email`, `password`, `first_name`, `last_name`, `is_active`, `is_verified`, `role`) VALUES ('aafc6888-8c12-4c6c-8b7f-a4a5f6b48859', 'admin', 'admin@gmail.com', '$2a$10$UlGPKbBbXCkxBn9FSBgzCOV/VDLMfPyeb5QAfQrXYhYmIxVwTmgbK', 'Admin', 'Admin', 1, 1, 'ADMIN');