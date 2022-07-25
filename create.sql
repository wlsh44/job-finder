
    create table category (
       id bigint not null auto_increment,
        name varchar(20) not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table recruit (
       id bigint not null auto_increment,
        career varchar(10),
        corp varchar(20) not null,
        due_date TIMESTAMP,
        is_always_recruiting TINYINT not null,
        job_type varchar(10),
        location varchar(255),
        platform varchar(20) not null,
        tech_stack varchar(255),
        title varchar(255) not null,
        url varchar(255) not null,
        category_id bigint not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table recruit_tag (
       id bigint not null auto_increment,
        recruit_id bigint not null,
        tag_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table tag (
       id bigint not null auto_increment,
        name varchar(20),
        primary key (id)
    ) engine=InnoDB;

    create table user (
       id bigint not null auto_increment,
        created_at datetime,
        email varchar(100) not null,
        modified_at datetime,
        name varchar(50) not null,
        password varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    alter table category 
       add constraint UniqueUserCategory unique (user_id, name);

    alter table user 
       add constraint UK_email unique (email);

    alter table user 
       add constraint UK_password unique (password);

    alter table category 
       add constraint FK_categoryUser
       foreign key (user_id) 
       references user (id);

    alter table recruit 
       add constraint FK_recruitCategory
       foreign key (category_id) 
       references category (id);

    alter table recruit
        add constraint FK_recruitUser
            foreign key (user_id)
                references user (id);

    alter table recruit_tag 
       add constraint FK_tagRecruit
       foreign key (recruit_id) 
       references recruit (id);

    alter table recruit_tag 
       add constraint FK_recruitTag
       foreign key (tag_id) 
       references tag (id);

#   email: test@test.test, password: test
    INSERT INTO `user` (id, created_at, email, modified_at, name, password)
    VALUES ('1', '2022-07-22 17:27:57', 'test@test.test', '2022-07-22 17:27:57', 'test', '{bcrypt}$2a$10$C9ymci/vI5nxsy5v0.SAMODtwNc1b7gvCZf7tLdvoKBVWgvIPx4BG');

    INSERT INTO `category` (`id`, `name`, `user_id`) VALUES ('1', '테스트 카테고리1', '1');
    INSERT INTO `category` (`id`, `name`, `user_id`) VALUES ('2', '테스트 카테고리2', '1');

    INSERT INTO `recruit` (`id`, `career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('1', 'career', 'corp', '2023-10-17 00:00:00', '0', 'job_type1', 'location1', 'JOBKOREA', 'tech_stack1', 'title1', 'test_url1', '1', '1');
    INSERT INTO `recruit` (`id`, `career`, `corp`, `due_date`, `is_always_recruiting`, `job_type`, `location`, `platform`, `tech_stack`, `title`, `url`, `category_id`, `user_id`) VALUES ('2', 'career2', 'corp2', '2023-10-17 00:00:00', '1', 'job_type2', 'location2', 'JOBKOREA', 'tech_stack2', 'title2', 'test_url2', '1', '1');

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
