create table users(
    id int primary key not null generated always as identity,
    login varchar(50),
    password varchar(25),
    role varchar(5)
)