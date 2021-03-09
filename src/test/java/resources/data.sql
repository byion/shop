DROP TABLE IF EXISTS prices cascade;
DROP TABLE IF EXISTS products cascade;
DROP TABLE IF EXISTS exchange_rates cascade;

CREATE TABLE products
(
    id         uuid PRIMARY KEY,
    name       varchar(200),
    valid      boolean,
    created_at date default now()
);

CREATE TABLE prices
(
    id         uuid PRIMARY KEY,
    price      Decimal(19, 4),
    currency   varchar(5),
    created_at date default now(),
    product_id uuid
);

CREATE TABLE exchange_rates
(
    id         uuid PRIMARY KEY,
    rate       Decimal(19, 4),
    currency   varchar(5),
    created_at date default now()
);

insert into products(id, valid, name)
values ('b04e9ded-e857-4677-aee7-c61d327210a6', true, 'test1'),
       ('9ee6121d-22f2-40b2-80fb-87d068d15ca9', true, 'test2'),
       ('373400aa-8fc5-4269-a5b7-197578557580', true, 'test3');



insert into prices(id, price, currency, product_id, created_at)
values ('e78cb9a9-4b04-4a72-91cb-1e5d7cb04f0d', '120.3', 'USD', 'b04e9ded-e857-4677-aee7-c61d327210a6', '2020-08-08'),
       ('4a311278-ad5b-4eb0-852b-e8cc687785df', '5.3', 'USD', 'b04e9ded-e857-4677-aee7-c61d327210a6', '2020-03-08'),
       ('165901a6-05e2-4d1d-86d8-85babdb90132', '10.3', 'USD', 'b04e9ded-e857-4677-aee7-c61d327210a6', '2021-03-08'),
       ('f127f172-46c5-42b5-828e-4d4d8b46b8ae', '5.3', 'USD', '9ee6121d-22f2-40b2-80fb-87d068d15ca9', '2020-03-08'),
       ('706e81f9-2b5c-4ad4-bcc6-dd5c06b4529a', '10.3', 'USD', '373400aa-8fc5-4269-a5b7-197578557580', '2021-03-08');


insert into exchange_rates(id, rate, currency, created_at)
values ('1375b332-08aa-4455-bcfa-353043233e0e', '1.2', 'USD', '2020-08-08'),
       ('699b1dfd-25eb-4f28-8d81-e325c33eb982', '4.88', 'RON', '2020-08-08'),
       ('495c23ce-aa0d-414c-a1b7-5cefe6bec2d7', '33.38', 'UAH', '2020-08-08'),
       ('a7a30d54-1206-469a-bfba-e01243125868', '11.2', 'USD',  '2020-09-08'),
       ('2ab77b51-b610-4a66-a824-4ac37ff8c460', '14.88', 'RON',  '2020-09-08'),
       ('fff7fed6-57eb-4ae2-95ef-5464fcc9f27c', '133.38', 'UAH',  '2020-09-08');






