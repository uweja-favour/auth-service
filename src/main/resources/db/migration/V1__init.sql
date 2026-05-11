-- V1__init.sql
-- Initial schema generated from Exposed table definitions (MySQL)
-- DATETIME used for all date/time columns. No DATETIME defaults; application sets values except where noted (monthly reset).
-- Note: monthly_reset.id has a default and a seeded singleton row.

CREATE DATABASE IF NOT EXISTS `auth_dev` CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
USE `auth_dev`;

-- Users table (primary user PK)
CREATE TABLE IF NOT EXISTS `users` (
  `id` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  `is_banned` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- FCM devices
CREATE TABLE IF NOT EXISTS `fcm_devices` (
  `id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `token` VARCHAR(500) NOT NULL,
  `platform` VARCHAR(50) NOT NULL,
  `device_model` VARCHAR(255) NULL,
  `last_active_at` DATETIME NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fcm_devices_token` (`token`),
  CONSTRAINT `fk_fcm_devices_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- User profiles
CREATE TABLE IF NOT EXISTS `user_profiles` (
  `id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `avatar_s3_key` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_profiles_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- User subscription
CREATE TABLE IF NOT EXISTS `user_subscription` (
  `id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `subscription_plan` VARCHAR(100) NOT NULL,
  `platform` VARCHAR(100) NOT NULL,
  `start_at` DATETIME NOT NULL,
  `end_at` DATETIME NULL,
  `last_renewal` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_subscription_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Access token
CREATE TABLE IF NOT EXISTS `access_token` (
  `jti` VARCHAR(500) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `expiry_at` DATETIME NOT NULL,
  `revoked` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`jti`),
  CONSTRAINT `fk_access_token_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Monthly reset (singleton)
CREATE TABLE IF NOT EXISTS `monthly_reset` (
  `id` VARCHAR(100) NOT NULL DEFAULT 'monthly_reset_singleton',
  `last_reset` DATE NOT NULL DEFAULT '2023-01-01',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seed the monthly_reset singleton row (id default and initial last_reset)
INSERT INTO `monthly_reset` (`id`, `last_reset`)
SELECT 'monthly_reset_singleton', '2023-01-01'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `monthly_reset` WHERE `id` = 'monthly_reset_singleton');

-- Notifications
CREATE TABLE IF NOT EXISTS `notifications` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `message` VARCHAR(1024) NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  `timestamp` DATETIME NOT NULL,
  `is_read` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Payment
CREATE TABLE IF NOT EXISTS `payment` (
  `id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `amount` DOUBLE NOT NULL,
  `currency` VARCHAR(100) NOT NULL,
  `platform` VARCHAR(100) NOT NULL,
  `transaction_id` VARCHAR(255) NULL,
  `paid_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_payment_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Refresh tokens
CREATE TABLE IF NOT EXISTS `refresh_tokens` (
  `id` VARCHAR(36) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `jti` VARCHAR(255) NOT NULL,
  `token_hash` VARCHAR(64) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `last_used_at` DATETIME NULL,
  `device_ip` VARCHAR(45) NULL,
  `user_agent` VARCHAR(1024) NULL,
  `expiry_date` DATETIME NOT NULL,
  `revoked` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refresh_tokens_token_hash` (`token_hash`),
  CONSTRAINT `fk_refresh_tokens_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Used Google Play subscription purchase tokens
CREATE TABLE IF NOT EXISTS `used_google_play_subscription_purchase_token` (
  `id` VARCHAR(500) NOT NULL,
  `purchase_token` VARCHAR(500) NOT NULL,
  `subscription_id` VARCHAR(500) NOT NULL,
  `package_name` VARCHAR(500) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  `last_expiry_millis` BIGINT NOT NULL,
  `is_active` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_used_google_play_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
