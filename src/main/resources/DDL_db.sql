create schema if not exists maonamassa;
use maonamassa;
create user if not exists 'admin'@'localhost' identified by 'admin';
grant select, insert, delete, update, create, alter, drop, index, references on maonamassa.* to admin@'localhost';