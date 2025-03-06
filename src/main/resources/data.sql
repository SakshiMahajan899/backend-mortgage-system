-- data.sql
CREATE TABLE IF NOT EXISTS interest_rate
(
    id
    UUID
    PRIMARY
    KEY,
    maturity_period
    INT,
    interest_rate
    FLOAT,
    last_update
    TIMESTAMP
);

INSERT INTO interest_rate (id, maturity_period, interest_rate, last_update)
VALUES ('d1e2f3a4-5678-4cde-abcd-12345678abcd', 10, 5.0, '2025-03-03 12:00:00'),
       ('e3f4a5b6-7890-5cde-cdef-23456789bcde', 20, 6.0, '2025-03-03 12:00:00');
