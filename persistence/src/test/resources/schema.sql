CREATE TABLE IF NOT EXISTS subjects (
subject_id IDENTITY PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
description VARCHAR(512) NOT NULL,
area_id INT,
);