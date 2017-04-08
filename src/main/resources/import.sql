-- Insert role
insert into role (name) values ('ROLE_USER');

-- Insert two users (passwords are 'password')
insert into user (username,enabled,password,role_id, email) values ('user',true,'$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u',1, 'ƒexampleemail@example.com');
insert into user (username,enabled,password,role_id, email) values ('user2',true,'password',1, 'exampleemail2@example.com');

-- Insert tasks
insert into task (complete,description, user_id) values (true,'Code Task entity', 1);
insert into task (complete,description, user_id) values (false,'Discuss users and roles', 1);
insert into task (complete,description, user_id) values (false,'Enable Spring Security', 2);
insert into task (complete,description, user_id) values (true,'Test application', 2);
