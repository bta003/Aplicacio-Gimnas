drop database if exists gimnas;
create database gimnas;
use gimnas;


CREATE TABLE `Clients` (
	`DNI` varchar(9) NOT NULL,
	`nom` varchar(30) NOT NULL,
	`cognom1` varchar(30) NOT NULL,
    `cognom2` varchar(30) NOT NULL,
    `sexe` char(1) NOT NULL,
    `comunicaciocomercial` varchar(2),
    `datanaixement` date NOT NULL,
    `email` varchar(60) NOT NULL,
    `domicili` varchar(60),
	`telefon` varchar(9) NOT NULL,
	`condiciofisica` varchar(255),
    `ccc` varchar(24) NOT NULL,
    `username` varchar(24),
    `passwd` varchar(32),
	PRIMARY KEY (`DNI`)
);

CREATE TABLE `Sala` (
	`id_sala` int NOT NULL AUTO_INCREMENT,
	`descripcio` varchar(255) NOT NULL,
	`aforament` int(2) NOT NULL,
	`DNI_monitor` varchar(30) NOT NULL,
	PRIMARY KEY (`id_sala`)
);

CREATE TABLE `Activitat` (
	`id_act` int NOT NULL AUTO_INCREMENT,
	`hora_inici` time(6) NOT NULL,
    `hora_final` time(6) NOT NULL,
    `nom` varchar(30) NOT NULL,
    `dia` int(1) NOT NULL,
    `id_sala` int NOT NULL,
    `imatge` varchar(50) NOT NULL,
	PRIMARY KEY (`id_act`)
);

CREATE TABLE `Activitat_lliure` (
    `id_act` INT NOT NULL UNIQUE,
    `descripcio` varchar(255) NOT NULL,
    PRIMARY KEY (`id_act`)
);

CREATE TABLE `Activitat_colectiva` (
    `id_act` INT NOT NULL UNIQUE,
    `descripcio` varchar(255) NOT NULL,
    PRIMARY KEY (`id_act`)
);

CREATE TABLE `Monitor` (
	`DNI_monitor` varchar(9) NOT NULL,
	`NSS` varchar(12) NOT NULL UNIQUE,
	`telefon` varchar(9) NOT NULL UNIQUE,
	`nom` varchar(30) NOT NULL,
	`cognom` varchar(30) NOT NULL,
	`email` varchar(60) NOT NULL UNIQUE,
	PRIMARY KEY (`DNI_monitor`)
);

CREATE TABLE `Realitzacio` (
    `idrealitzacio` int NOT NULL AUTO_INCREMENT,
	`data` DATE DEFAULT null,
	`hora` time(6) NOT NULL,
	`id_act` int NOT NULL,
	`id_sala` int NOT NULL,
	`DNI` varchar(9) NOT NULL,
    PRIMARY KEY (`idrealitzacio`)
);

CREATE TABLE `Altes` (
	`data_alta` DATE NOT NULL,
	`data_baixa` DATE,
	`DNI` varchar(9) NOT NULL,
    PRIMARY KEY (`DNI`)
);

CREATE TABLE `Curses` (
	`id_cursa` int NOT NULL AUTO_INCREMENT,
	`descripcio` varchar(255) NOT NULL,
    `distancia` int(255) NOT NULL,
	`durada` varchar(20) NOT NULL,
	PRIMARY KEY (`id_cursa`)
);

CREATE TABLE `Participa` (
	`data` DATE NOT NULL,
	`hora` time(6) NOT NULL,
	`id_cursa` int NOT NULL,
	`DNI` varchar(9) NOT NULL,
    PRIMARY KEY (`DNI`)
);


ALTER TABLE `Sala` ADD CONSTRAINT `Sala_fk0` FOREIGN KEY (`DNI_monitor`) REFERENCES `Monitor`(`DNI_monitor`);

ALTER TABLE `Realitzacio` ADD CONSTRAINT `Realitzacio_fk0` FOREIGN KEY (`id_act`) REFERENCES `Activitat`(`id_act`);

ALTER TABLE `Activitat_lliure` ADD CONSTRAINT `Activitat_lliure_fk0` FOREIGN KEY (`id_act`) REFERENCES `Activitat`(`id_act`);

ALTER TABLE `Activitat_colectiva` ADD CONSTRAINT `Activitat_colectiva_fk0` FOREIGN KEY (`id_act`) REFERENCES `Activitat`(`id_act`);

ALTER TABLE `Realitzacio` ADD CONSTRAINT `Realitzacio_fk1` FOREIGN KEY (`id_sala`) REFERENCES `Sala`(`id_sala`);

ALTER TABLE `Realitzacio` ADD CONSTRAINT `Realitzacio_fk2` FOREIGN KEY (`DNI`) REFERENCES `Clients`(`DNI`);

ALTER TABLE `Altes` ADD CONSTRAINT `Altes_fk0` FOREIGN KEY (`dni`) REFERENCES `Clients`(`DNI`);

ALTER TABLE `Participa` ADD CONSTRAINT `Participa_fk0` FOREIGN KEY (`id_cursa`) REFERENCES `Curses`(`id_cursa`);

ALTER TABLE `Participa` ADD CONSTRAINT `Participa_fk1` FOREIGN KEY (`DNI`) REFERENCES `Clients`(`DNI`);

ALTER TABLE `Activitat` ADD CONSTRAINT `Activitat_fk0` FOREIGN KEY (`id_sala`) REFERENCES `Sala`(`id_sala`);

SET SQL_SAFE_UPDATES = 0;

#PROCEDURE

DROP PROCEDURE IF EXISTS calcul_aforament;
DELIMITER //
CREATE PROCEDURE calcul_aforament ()
BEGIN
SELECT SUM(aforament)/(sum(aforament)+(SELECT count(*) FROM Realitzacio WHERE data is not null))*100 as "percentatge d'aforament"
FROM Sala;
END
//

# TRIGGER

DROP TRIGGER IF EXISTS restar_aforament;
DELIMITER $$
CREATE TRIGGER restar_aforament
AFTER INSERT ON Realitzacio
FOR EACH ROW
BEGIN
UPDATE Sala NATURAL JOIN Realitzacio, Activitat
SET Sala.aforament = Sala.aforament - 1
WHERE Realitzacio.id_act = Activitat.id_act
AND Activitat.id_sala=Sala.id_sala
AND Realitzacio.id_sala=Sala.id_sala;
END$$
DELIMITER ;


DROP TRIGGER IF EXISTS sumar_aforament;
DELIMITER $$
CREATE TRIGGER sumar_aforament
AFTER UPDATE ON Realitzacio
FOR EACH ROW
BEGIN
UPDATE Sala NATURAL JOIN Realitzacio, Activitat
SET Sala.aforament = Sala.aforament + 1
WHERE Realitzacio.id_act = Activitat.id_act
AND Activitat.id_sala=Sala.id_sala
AND Realitzacio.id_sala=Sala.id_sala;
END$$
DELIMITER ;


INSERT INTO Clients (DNI, nom, cognom1, cognom2, sexe, comunicaciocomercial, datanaixement, email, telefon, condiciofisica, ccc, username, passwd) VALUES 
('47137446G', 'Bruno', 'Tom??', 'Arias', 'M', 'SI', '2003-10-07', 'brunota.dam1@alumnescostafreda.cat', '656021302', null, "ES2412491614145851007544", 'BrunoTA', MD5('1234')),
('77383544K', 'Pau', 'Rubio', 'Silva', 'H', 'SI', '1990-02-15', 'paurs.dam1@alumnescostafreda.cat', '783250105', null, "ES6815257321906273010242", 'PauR', MD5('1234')),
('54126466Z', 'Ignasi', 'Cabrera', 'Fernandez', 'M', 'NO', '1999-11-15','igansicf.dam1@alumnescostafreda.cat', '613019912', null, "ES5600368145265920955294", 'IgnasiC', MD5('1234')),
('51833470A', 'Myriam', 'Mari', 'Lopez', 'H', 'NO', '1996-06-05','myriamml.dam1@alumnescostafreda.cat', '613965253', null, "ES3631901491813119440760", 'MyriamM', MD5('1234')),
('67289921V', 'Ayoub', 'Rosales', 'Sanches', 'M', 'SI', '2003-08-21','ayoubrs.dam1@alumnescostafreda.cat', '653934787', null, "ES0420405698814239157974", 'AyoubR', MD5('1234')),
('00046319C', 'Sergio', 'Caceres', 'Rodriguez', 'M', 'SI', '2001-2-15','sergiocr.dam1@alumnescostafreda.cat', '632892252', null, "ES6330726229373463442372", 'SergioC', MD5('1234'));

INSERT INTO Monitor(DNI_monitor, NSS, telefon, nom, cognom, email) VALUES
('08578598N', '408472140491', '623792716', 'Rafael', 'Luz', 'rafaell@gmail.com'),
('70134652Q', '302866612208', '667881156', 'Jos??', 'Antonio', 'josea@gmail.com'),
('90758723X', '315152690913', '777672774', 'Andrea', 'Requena', 'andrear@gmail.com'),
('17504793E', '350060969692', '692445785', 'Laura', 'Macias', 'lauram@gmail.com');

INSERT INTO Sala(descripcio, aforament, DNI_monitor) VALUES
('Sala de piscina', 40, '08578598N'),
('Sala de maquines de exercicis', 40, '70134652Q'),
('Sala de zumba', 40, '90758723X'),
('Sala de maquines de exercicis Num 2', 40, '17504793E');


INSERT INTO Activitat (hora_inici, hora_final, nom, dia, id_sala, imatge) VALUES 
('12:30:00', '13:30:00', 'piscina', 1, 1, '../img/piscina.png'),
('14:00:00', '15:00:00', 'fitness', 1, 2, '../img/fitness.png'),
('12:30:00', '13:30:00', 'padel', 1, 4, '../img/padel.png'),
('14:00:00', '15:00:00', 'bici', 2, 4, '../img/bici.png'),
('12:30:00', '13:30:00', 'cinta de correr', 2, 2, '../img/cinta.png'),
('14:00:00', '15:00:00', 'cycling', 2, 2, '../img/cycling.png'),
('12:30:00', '13:30:00', 'body pump', 3, 2, '../img/bodypump.png'),
('14:00:00', '15:00:00', 'pilates', 3, 3, '../img/pilates.png'),
('12:30:00', '13:30:00', 'nataci??', 3, 1, '../img/natacio.png'),
('14:00:00', '15:00:00', 'aquagym', 4, 1, '../img/aquagym.png'),
('12:30:00', '13:30:00', 'running', 4, 3, '../img/running.png'),
('14:00:00', '15:00:00', 'zumba', 4, 3, '../img/zumba.png'),
('12:30:00', '13:30:00', 'boxa', 5, 2, '../img/boxa.png'),
('14:00:00', '15:00:00', 'ioga', 5, 3, '../img/ioga.png');


INSERT INTO Activitat_lliure(id_act, descripcio) VALUES
(1, 'Piscina'),
(2, 'Fitness'),
(3, 'Partits de padel'),
(4, 'Bici'),
(5, 'Cinta de correr'),
(13, 'Boxa');


INSERT INTO Activitat_colectiva(id_act, descripcio) VALUES
(6, 'Cycling'),
(7, 'Body pump'),
(8, 'Pilates'),
(9, 'Nataci?? en piscina descoberta'),
(10, 'Aquagym'),
(11, 'Running'),
(12, 'Zumba'),
(14, 'Ioga');

INSERT INTO Altes(data_alta, data_baixa, DNI) VALUES
('2022-02-24', null, '47137446G'),
('2022-01-24', '2022-02-24', '77383544K'),
('2022-01-20', null, '54126466Z'),
('2022-01-10', '2022-02-20', '51833470A'),
('2022-02-04', null, '67289921V'),
('2022-01-15', null, '00046319C');


INSERT INTO Curses (descripcio, distancia, durada) VALUES 
('Cursa solidaria a peu al voltant de Tarrega', 12, '4 hores'),
('Cursa amb bici per Cervera', 15, '4,5 hores'),
('Cursa de nataci?? a Lleida', 10, '5 hores'),
('Cursa de triatl?? a Barcelona', 12, '4,5 hores');


INSERT INTO Participa (data, hora, id_cursa, DNI) VALUES
('2022-02-17', '12:00:00', 1, '47137446G'),
('2022-02-17', '12:00:00', 1, '54126466Z'),
('2022-02-17', '12:00:00', 1, '51833470A');


INSERT INTO Realitzacio (data, hora, id_act, id_sala, dni) VALUES
('2022-02-28', '13:30:00', '1', 1, '47137446G'),
(null, '13:30:00', '1', 1, '47137446G'),
('2022-02-21', '12:00:00', '3', 4, '77383544K'),
('2022-02-21', '10:15:00', '5', 2, '67289921V'),
('2022-02-21', '10:30:00', '5', 2, '54126466Z'),
('2022-02-14', '11:30:00', '5', 2, '77383544K'),
('2022-02-28', '13:30:00', '1', 1, '00046319C');

