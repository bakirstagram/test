
-- POSTGRES

CREATE TABLE IF NOT EXISTS book
(
    id          INT NOT NULL,
    title       VARCHAR(150) NOT NULL,
    author      VARCHAR(150) NOT NULL,
    description VARCHAR(150),
    constraint book_pk PRIMARY KEY (id)
);

INSERT INTO BOOK (id, title, author, description)
VALUES (1, 'Crime and Punishment', 'F. Dostoevsky', NULL);
INSERT INTO BOOK (id, title, author, description)
VALUES (2, 'Anna Karenina', 'L. Tolstoy', NULL);
INSERT INTO BOOK (id, title, author, description)
VALUES (3, 'The Brothers Karamazov', 'F. Dostoevsky', NULL);
INSERT INTO BOOK (id, title, author, description)
VALUES (4, 'War and Peace', 'L. Tolstoy', NULL);
INSERT INTO BOOK (id, title, author, description)
VALUES (5, 'Dead Souls', 'N. Gogol', NULL);
commit;