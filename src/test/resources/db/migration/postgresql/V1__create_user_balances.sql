CREATE TABLE if not exists user_balances
(
    id BIGINT PRIMARY KEY,
    name TEXT,
    balance BIGINT,
    created TIMESTAMP,
    updated TIMESTAMP,
    version bigint
);