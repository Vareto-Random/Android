CREATE TABLE items (_ID INTEGER PRIMARY KEY AUTOINCREMENT, listID INTEGER NOT NULL, itemName TEXT NOT NULL, itemQuantity INTEGER DEFAULT 1, itemDate TEXT NOT NULL, itemTime TEXT NOT NULL, itemDateTime TEXT NOT NULL, itemPurchased INTEGER, itemPrice NUMERIC(10,2) DEFAULT 0);
CREATE TABLE lists (_ID INTEGER PRIMARY KEY AUTOINCREMENT, listName TEXT NOT NULL, listDate TEXT NOT NULL, listTime TEXT NOT NULL, listDateTime TEXT NOT NULL);

INSERT INTO lists (listName, listDate, listTime, listDateTime) VALUES ('Supermercado', date('now'), time('now'), datetime('now'));
INSERT INTO lists (listName, listDate, listTime, listDateTime) VALUES ('Mercearia', date('now'), time('now'), datetime('now'));

INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Arroz', 1, date('now'), time('now'), datetime('now'), 0, 3.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Feijao', 1, date('now'), time('now'), datetime('now'), 0, 5.89);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Batata', 1, date('now'), time('now'), datetime('now'), 0, 1.8);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Cafe', 1, date('now'), time('now'), datetime('now'), 0, 3.15);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Alcatra', 1, date('now'), time('now'), datetime('now'), 0, 12.457);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Linguica', 1, date('now'), time('now'), datetime('now'), 0, 5.345);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Nescau', 1, date('now'), time('now'), datetime('now'), 0, 3.99);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Macarrao', 1, date('now'), time('now'), datetime('now'), 0, 2);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Massa de tomate', 1, date('now'), time('now'), datetime('now'), 0, 3.5);

INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Pao de quejo', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Pizza', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Biscoito sal', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Biscoito recheado', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Chocolate', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Queijo fresco', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Queijo canastra', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Doce de leite', 1, date('now'), time('now'), datetime('now'), 0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased) VALUES (2, 'Suco de abacaxi', 1, date('now'), time('now'), datetime('now'), 0);

SELECT L._ID, L.listName, L.listDate, L.listTime, L.listDateTime, COUNT(I.listID) FROM (lists AS L LEFT OUTER JOIN items as I ON L._ID = I.listID) GROUP BY I.listID ORDER BY listDateTime DESC