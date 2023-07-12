CREATE TABLE IF NOT EXISTS api_route
(
    id         bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    path       varchar(100) NOT NULL,
    method     varchar(10),
    uri        varchar(100) NOT NULL
);

INSERT INTO api_route(path, method, uri)
VALUES ('/get', 'GET', 'https://httpbin.org/get')
ON DUPLICATE KEY UPDATE path = VALUES(path);