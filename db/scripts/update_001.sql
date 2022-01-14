CREATE TABLE post (
                      id SERIAL PRIMARY KEY,
                      name TEXT
);

CREATE TABLE candidate (
    id serial primary key,
    name text
);

CREATE TABLE if not exists users (
    id serial primary key,
    name text,
    email text,
    password text
);