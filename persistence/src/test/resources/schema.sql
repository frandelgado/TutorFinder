CREATE TABLE IF NOT EXISTS areas (
area_id IDENTITY PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
description VARCHAR(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects (
subject_id IDENTITY PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
description VARCHAR(512) NOT NULL,
area_id INT NOT NULL,
FOREIGN KEY(area_id) REFERENCES areas(area_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
user_id IDENTITY PRIMARY KEY,
username VARCHAR(128) UNIQUE NOT NULL,
password VARCHAR(64) NOT NULL,
email VARCHAR(512) UNIQUE NOT NULL,
name VARCHAR(128) NOT NULL,
lastname VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS professors (
user_id BIGINT NOT NULL,
description VARCHAR(512) NOT NULL,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS courses (
user_id BIGINT NOT NULL,
subject_id BIGINT NOT NULL,
description VARCHAR(512) NOT NULL,
price REAL NOT NULL,
rating REAL NOT NULL,
FOREIGN KEY(subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
FOREIGN KEY(user_id) REFERENCES professors(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id, subject_id)
);

Insert into areas (area_id, name, description) values (1, 'matematica', 'este area es dificil');
INSERT into users (user_id, username, password, email, name, lastname) values (2, 'juanchopanza', '12345', 'juan@hotmail.com', 'juan', 'lopez' );
INSERT into professors (user_id, description) values (2, 'Juan es un profesor dedicado');
INSERT into subjects (subject_id, name, description, area_id) values (1, 'Algebra', 'Complicado', 1);