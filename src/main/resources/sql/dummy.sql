#   email: test@test.test, password: test
    INSERT INTO `user` (id, created_at, email, modified_at, name, password)
    VALUES ('1', '2022-07-22 17:27:57', 'test@test.test', '2022-07-22 17:27:57', 'test', '{bcrypt}$2a$10$C9ymci/vI5nxsy5v0.SAMODtwNc1b7gvCZf7tLdvoKBVWgvIPx4BG');

INSERT INTO `category` (`id`, `name`, `user_id`) VALUES ('1', '테스트 카테고리1', '1');
INSERT INTO `category` (`id`, `name`, `user_id`) VALUES ('2', '테스트 카테고리2', '1');

INSERT INTO `recruit` (`id`, `career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('1', 'career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`id`, `career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('2', 'career2', 'corp2', '2023-10-17 00:00:00', '1', 'job_type2', 'location2', 'JOBKOREA', 'tech_stack2', 'title2', 'test_url2', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
INSERT INTO `recruit` (`career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');

INSERT INTO `recruit` (`id`, `career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('3', 'career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '2', '1');
INSERT INTO `recruit` (`id`, `career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('4', 'career2', 'corp2', '2023-10-17 00:00:00', '1', 'job_type2', 'location2', 'JOBKOREA', 'tech_stack2', 'title2', 'test_url2', '2', '1');

INSERT INTO `tag` (`id`, `name`) VALUES ('1', 'tag1');
INSERT INTO `tag` (`id`, `name`) VALUES ('2', 'tag2');

INSERT INTO `recruit_tag` (`recruit_id`, `tag_id`) VALUES ('1', '1');
INSERT INTO `recruit_tag` (`recruit_id`, `tag_id`) VALUES ('1', '2');

INSERT INTO `recruit_tag` (`recruit_id`, `tag_id`) VALUES ('2', '1');

INSERT INTO `recruit_tag` (`recruit_id`, `tag_id`) VALUES ('3', '1');
INSERT INTO `recruit_tag` (`recruit_id`, `tag_id`) VALUES ('3', '2');

INSERT INTO `recruit_tag` (`recruit_id`, `tag_id`) VALUES ('4', '1');
