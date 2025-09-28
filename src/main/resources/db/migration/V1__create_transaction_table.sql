CREATE TABLE transaction
(
    transaction_id UUID PRIMARY KEY,
    order_id       VARCHAR(255)   NOT NULL,
    user_id        VARCHAR(255)   NOT NULL,
    amount         NUMERIC(15, 2) NOT NULL,
    status         VARCHAR(50)    NOT NULL,
    payment_mode   VARCHAR(50)    NOT NULL,
    initiated_at   TIMESTAMP      NOT NULL,
    completed_at   TIMESTAMP
);
