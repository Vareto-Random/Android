CREATE TABLE items (_ID INTEGER PRIMARY KEY AUTOINCREMENT, listID INTEGER NOT NULL, itemName TEXT NOT NULL, itemQuantity INTEGER DEFAULT 1, itemDate TEXT NOT NULL, itemTime TEXT NOT NULL, itemDateTime TEXT NOT NULL, itemPurchased INTEGER, itemPrice REAL DEFAULT 0.00);
CREATE TABLE lists (_ID INTEGER PRIMARY KEY AUTOINCREMENT, listName TEXT NOT NULL, listDate TEXT NOT NULL, listTime TEXT NOT NULL, listDateTime TEXT NOT NULL);

INSERT INTO lists (listName, listDate, listTime, listDateTime) VALUES ('Supermercado', date('now'), time('now'), datetime('now'));
INSERT INTO lists (listName, listDate, listTime, listDateTime) VALUES ('Mercearia', date('now'), time('now'), datetime('now'));

INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Arroz', 1, date('now'), time('now'), datetime('now'), 0, 2.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Feijao', 3, date('now'), time('now'), datetime('now'), 0, 4.0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Batata', 2, date('now'), time('now'), datetime('now'), 0, 1.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Cafe', 2, date('now'), time('now'), datetime('now'), 0, 1.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Alcatra', 4, date('now'), time('now'), datetime('now'), 0, 9.99);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Linguica', 3, date('now'), time('now'), datetime('now'), 0, 4.99);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Nescau', 2, date('now'), time('now'), datetime('now'), 0, 3.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Macarrao', 3, date('now'), time('now'), datetime('now'), 0, 1.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (1, 'Massa de tomate', 5, date('now'), time('now'), datetime('now'), 0, 1.5);

INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Arroz', 1, date('now'), time('now'), datetime('now'), 0, 3.1);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Pao de quejo', 3, date('now'), time('now'), datetime('now'), 0, 2.0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Pizza', 3, date('now'), time('now'), datetime('now'), 0, 4.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Biscoito sal', 4, date('now'), time('now'), datetime('now'), 0, 0.9);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Biscoito recheado', 2, date('now'), time('now'), datetime('now'), 0, 1.2);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Chocolate', 3, date('now'), time('now'), datetime('now'), 0, 3.4);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Queijo fresco', 1, date('now'), time('now'), datetime('now'), 0, 6.5);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Queijo canastra', 1, date('now'), time('now'), datetime('now'), 0, 8.0);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Doce de leite', 1, date('now'), time('now'), datetime('now'), 0, 4.99);
INSERT INTO items (listID, itemName, itemQuantity, itemDate, itemTime, itemDateTime, itemPurchased, itemPrice) VALUES (2, 'Suco de abacaxi', 6, date('now'), time('now'), datetime('now'), 0, 1.5);

SELECT L._ID, L.listName, L.listDate, L.listTime, L.listDateTime, COUNT(I.listID) FROM (lists AS L LEFT OUTER JOIN items as I ON L._ID = I.listID) GROUP BY I.listID ORDER BY listDateTime DESC
SELECT I.listID, SUM(I.itemQuantity * I.itemPrice) FROM items AS I GROUP BY I.listID
SELECT COUNT(*) FROM items WHERE itemPurchased = 0 AND listID = 1