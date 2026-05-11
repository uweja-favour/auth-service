-- V3__rename_userId_to_user_id.sql
-- Rename users.id -> users.user_id safely, compatible with MySQL

USE `auth_dev`;

-- Step 1: Drop all foreign keys referencing users.id
ALTER TABLE `fcm_devices` DROP FOREIGN KEY `fk_fcm_devices_user`;
ALTER TABLE `user_profiles` DROP FOREIGN KEY `fk_user_profiles_user`;
ALTER TABLE `user_subscription` DROP FOREIGN KEY `fk_user_subscription_user`;
ALTER TABLE `access_token` DROP FOREIGN KEY `fk_access_token_user`;
ALTER TABLE `notifications` DROP FOREIGN KEY `fk_notifications_user`;
ALTER TABLE `payment` DROP FOREIGN KEY `fk_payment_user`;
ALTER TABLE `refresh_tokens` DROP FOREIGN KEY `fk_refresh_tokens_user`;
ALTER TABLE `used_google_play_subscription_purchase_token` DROP FOREIGN KEY `fk_used_google_play_user`;

-- Step 2: Rename primary key column
ALTER TABLE `users` CHANGE COLUMN `id` `user_id` VARCHAR(255) NOT NULL;

-- Step 3: Recreate primary key
ALTER TABLE `users` DROP PRIMARY KEY;
ALTER TABLE `users` ADD PRIMARY KEY (`user_id`);

-- Step 4: Drop existing unique index on email if it exists, then recreate
DROP INDEX `uk_users_email` ON `users`;

CREATE UNIQUE INDEX `uk_users_email` ON `users`(`email`);

-- Step 5: Recreate foreign keys with updated column
ALTER TABLE `fcm_devices`
    ADD CONSTRAINT `fk_fcm_devices_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `user_profiles`
    ADD CONSTRAINT `fk_user_profiles_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `user_subscription`
    ADD CONSTRAINT `fk_user_subscription_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `access_token`
    ADD CONSTRAINT `fk_access_token_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `notifications`
    ADD CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `payment`
    ADD CONSTRAINT `fk_payment_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `refresh_tokens`
    ADD CONSTRAINT `fk_refresh_tokens_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;

ALTER TABLE `used_google_play_subscription_purchase_token`
    ADD CONSTRAINT `fk_used_google_play_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE;
