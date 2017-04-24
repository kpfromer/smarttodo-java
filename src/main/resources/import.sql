-- Insert role
INSERT INTO role (name) VALUES ('ROLE_USER');

-- Insert two users (passwords are 'password')
INSERT INTO user (username, enabled, password, role_id, email) VALUES ('user', TRUE, '$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u', 1, 'ƒexampleemail@example.com');
INSERT INTO user (username, enabled, password, role_id, email) VALUES ('user2', TRUE, 'password', 1, 'exampleemail2@example.com');

-- Insert tasks
INSERT INTO task (complete, description, user_id, completed, recurring) VALUES (TRUE, 'Code Task entity', 1, true, false);
INSERT INTO task (complete, description, user_id, completed, recurring) VALUES (FALSE, 'Discuss users and roles', 1, false, false);
INSERT INTO task (complete, description, user_id, completed, recurring) VALUES (FALSE, 'Enable Spring Security', 2, false, true);
INSERT INTO task (complete, description, user_id, completed, recurring) VALUES (TRUE, 'Test application', 2, true, false);
