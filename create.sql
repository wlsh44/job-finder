
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
        user_id bigint not null,
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
       add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);

    alter table user 
       add constraint UK_kiqfjabx9puw3p1eg7kily8kg unique (password);

    alter table category 
       add constraint FKpfk8djhv5natgshmxiav6xkpu 
       foreign key (user_id) 
       references user (id);

    alter table recruit 
       add constraint FKa9e0dndfl31gli9na0vh3b20c 
       foreign key (category_id) 
       references category (id);

    alter table recruit_tag 
       add constraint FKelwb3nmj9ngwt4p959nb6478n 
       foreign key (recruit_id) 
       references recruit (id);

    alter table recruit_tag 
       add constraint FK70a55wyf8xfl8nyehxicoe8vu 
       foreign key (tag_id) 
       references tag (id);

    alter table tag 
       add constraint FKld85w5kr7ky5w4wda3nrdo0p8 
       foreign key (user_id) 
       references user (id);
