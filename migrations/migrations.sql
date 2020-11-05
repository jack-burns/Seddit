-- Create database

DROP SCHEMA IF EXISTS seddit;
CREATE SCHEMA seddit;
USE seddit;


-- Users

CREATE TABLE users
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username char(255) NOT NULL,
    password char(255),
    firstname char(255),
    lastname char(255),
    email char(255)
);

INSERT INTO users (username, password, firstname, lastname, email)
VALUES ('a', 'a', 'John', 'Smith', 'a@a.com');

INSERT INTO users (username, password, firstname, lastname, email)
VALUES ('b', 'b', 'Mark', 'Cuban', 'b@b.com');

-- Posts

CREATE TABLE posts
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title char(255),
    content char(255),
    from_user_id int,
    create_timestamp Date,
    modified_timestamp Date,

    FOREIGN KEY (from_user_id) REFERENCES users (id)
);


-- Hastags linked to Posts

CREATE TABLE hashtags
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tag char(255),
    to_post_id int,
    FOREIGN KEY (to_post_id) REFERENCES posts (id)
);


-- Uploads lined to Posts

CREATE TABLE uploads
(
    id INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description CHAR(50),
    data LONGBLOB,
    filename CHAR(50),
    filesize CHAR(50),
    filetype CHAR(50),
    to_post_id int,
    FOREIGN KEY (to_post_id) REFERENCES posts (id)
);






