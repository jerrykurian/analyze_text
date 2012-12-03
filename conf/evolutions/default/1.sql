# --- !Ups

CREATE TABLE `sms_lingos` (
  `id` int(11) NOT NULL auto_increment,
  `sms_word` varchar(255) default NULL,
  `english_text` varchar(255) default NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `action_failures` (
  `id` int(11) NOT NULL auto_increment,
  `source_number` varchar(255) default NULL,
  `text` varchar(255) default NULL,
  `retried` int(1) default NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `branches` (
  `id` int(11) NOT NULL auto_increment,
  `address_lane1` varchar(255) default NULL,
  `address_lane2` varchar(255) default NULL,
  `city` varchar(255) default NULL,
  `state` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `created_by` varchar(255) default NULL,
  `business_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `contact_number` text ,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `businesses` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `contact_person` varchar(255) default NULL,
  `contact_number` varchar(255) default NULL,
  `address_lane1` varchar(255) default NULL,
  `address_lane2` varchar(255) default NULL,
  `city` varchar(255) default NULL,
  `state` varchar(255) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `login_id` varchar(255) default NULL,
  `password` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `categories` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `created_by` varchar(255) default NULL,
  `business_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `category_keywords` (
  `id` int(11) NOT NULL auto_increment,
  `keyword` varchar(255) default NULL,
  `created_by` varchar(255) default NULL,
  `category_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `custom_messages` (
  `id` int(11) NOT NULL auto_increment,
  `message` varchar(255) default NULL,
  `created_by` varchar(255) default NULL,
  `sentiment_id` int(11) default NULL,
  `branch_id` int(11) default NULL,
  `business_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `fbusers` (
  `id` int(11) NOT NULL auto_increment,
  `provider` varchar(255) default NULL,
  `uid` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `oauth_token` varchar(255) default NULL,
  `oauth_expires_at` datetime default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `feedback_categories` (
  `feedback_id` int(11) default NULL,
  `category_id` int(11) default NULL,
  `branch_id` int(11) default NULL
);

CREATE TABLE `feedback_topics` (
  `feedback_id` int(11) default NULL,
  `trendingtopic_id` int(11) default NULL,
  `branch_id` int(11) default NULL
);

CREATE TABLE `feedbacks` (
  `id` int(11) NOT NULL auto_increment,
  `message` varchar(255) default NULL,
  `corrected_text` varchar(255) default NULL,
  `user_id` int(11) default NULL,
  `sentiment_id` int(11) default NULL,
  `branch_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `sentiment_score` int(11) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `incoming_message_logs` (
  `id` int(11) NOT NULL auto_increment,
  `message` varchar(255) default NULL,
  `mobile_number` varchar(255) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `keywords` (
  `id` int(11) NOT NULL auto_increment,
  `keyword` varchar(255) default NULL,
  `mobile_number` varchar(255) default NULL,
  `business_id` int(11) default NULL,
  `branch_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `mcoupons` (
  `id` int(11) NOT NULL auto_increment,
  `coupon_id` varchar(255) default NULL,
  `coupon_code` varchar(255) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `status` int(11) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `offers` (
  `id` int(11) NOT NULL auto_increment,
  `message` varchar(255) default NULL,
  `start_date` date default NULL,
  `end_date` date default NULL,
  `created_by` varchar(255) default NULL,
  `sentiment_id` int(11) default NULL,
  `branch_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `outgoing_message_logs` (
  `id` int(11) NOT NULL auto_increment,
  `message` varchar(255) default NULL,
  `destination_mobile_number` varchar(255) default NULL,
  `message_status` varchar(255) default NULL,
  `user_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `sentiment_overrides` (
  `id` int(11) NOT NULL auto_increment,
  `text` varchar(255) default NULL,
  `sentiment_id` int(11) default NULL,
  `created_at` datetime default NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `sentiments` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `value` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `trending_topics` (
  `id` int(11) NOT NULL auto_increment,
  `text` varchar(255) default NULL,
  `sentiment_id` int(11) default NULL,
  `branch_id` int(11) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `sentiment_score` int(11) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `mobile_number` varchar(255) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`)
);

# --- !Downs

DROP TABLE IF EXISTS `aiaioo_failures`;

DROP TABLE IF EXISTS `branches`;

DROP TABLE IF EXISTS `businesses`;

DROP TABLE IF EXISTS `categories`;

DROP TABLE IF EXISTS `category_keywords`;

DROP TABLE IF EXISTS `custom_messages`;

DROP TABLE IF EXISTS `fbusers`;

DROP TABLE IF EXISTS `feedback_categories`;

DROP TABLE IF EXISTS `feedback_topics`;

DROP TABLE IF EXISTS `feedbacks`;

DROP TABLE IF EXISTS `incoming_message_logs`;

DROP TABLE IF EXISTS `keywords`;

DROP TABLE IF EXISTS `mcoupons`;

DROP TABLE IF EXISTS `offers`;

DROP TABLE IF EXISTS `outgoing_message_logs`;

DROP TABLE IF EXISTS `sentiment_overrides`;

DROP TABLE IF EXISTS `sentiments`;

DROP TABLE IF EXISTS `sms_lingos`;

DROP TABLE IF EXISTS `trending_topics`;

DROP TABLE IF EXISTS `users`;
