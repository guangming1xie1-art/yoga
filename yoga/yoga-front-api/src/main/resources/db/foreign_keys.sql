-- ============================================================
-- 添加外键约束
-- ============================================================

-- 为bookings表添加外键约束
ALTER TABLE `bookings`
ADD CONSTRAINT `fk_bookings_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_bookings_session`
FOREIGN KEY (`session_id`) REFERENCES `course_sessions`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_bookings_order`
FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE SET NULL;

-- 为orders表添加外键约束
ALTER TABLE `orders`
ADD CONSTRAINT `fk_orders_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_orders_session`
FOREIGN KEY (`session_id`) REFERENCES `course_sessions`(`id`) ON DELETE CASCADE;

-- 为reviews表添加外键约束
ALTER TABLE `reviews`
ADD CONSTRAINT `fk_reviews_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_reviews_session`
FOREIGN KEY (`session_id`) REFERENCES `course_sessions`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_reviews_coach`
FOREIGN KEY (`coach_id`) REFERENCES `coaches`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_reviews_booking`
FOREIGN KEY (`booking_id`) REFERENCES `bookings`(`id`) ON DELETE CASCADE;

-- 为checkins表添加外键约束
ALTER TABLE `checkins`
ADD CONSTRAINT `fk_checkins_booking`
FOREIGN KEY (`booking_id`) REFERENCES `bookings`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_checkins_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_checkins_session`
FOREIGN KEY (`session_id`) REFERENCES `course_sessions`(`id`) ON DELETE CASCADE;

-- 为user_roles表添加外键约束
ALTER TABLE `user_roles`
ADD CONSTRAINT `fk_user_roles_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_user_roles_role`
FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_user_roles_venue`
FOREIGN KEY (`venue_id`) REFERENCES `venues`(`id`) ON DELETE CASCADE;

-- 为coaches表添加外键约束
ALTER TABLE `coaches`
ADD CONSTRAINT `fk_coaches_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_coaches_venue`
FOREIGN KEY (`venue_id`) REFERENCES `venues`(`id`) ON DELETE CASCADE;

-- 为course_templates表添加外键约束
ALTER TABLE `course_templates`
ADD CONSTRAINT `fk_templates_venue`
FOREIGN KEY (`venue_id`) REFERENCES `venues`(`id`) ON DELETE CASCADE;

-- 为course_schedules表添加外键约束
ALTER TABLE `course_schedules`
ADD CONSTRAINT `fk_schedules_template`
FOREIGN KEY (`template_id`) REFERENCES `course_templates`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_schedules_coach`
FOREIGN KEY (`coach_id`) REFERENCES `coaches`(`id`) ON DELETE CASCADE;

-- 为course_sessions表添加外键约束
ALTER TABLE `course_sessions`
ADD CONSTRAINT `fk_sessions_schedule`
FOREIGN KEY (`schedule_id`) REFERENCES `course_schedules`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_sessions_template`
FOREIGN KEY (`template_id`) REFERENCES `course_templates`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_sessions_coach`
FOREIGN KEY (`coach_id`) REFERENCES `coaches`(`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_sessions_venue`
FOREIGN KEY (`venue_id`) REFERENCES `venues`(`id`) ON DELETE CASCADE;

-- 为notifications表添加外键约束
ALTER TABLE `notifications`
ADD CONSTRAINT `fk_notifications_user`
FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE;

-- 为operation_logs表添加外键约束
ALTER TABLE `operation_logs`
ADD CONSTRAINT `fk_logs_operator`
FOREIGN KEY (`operator_id`) REFERENCES `users`(`id`) ON DELETE SET NULL;
