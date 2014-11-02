DROP DATABASE vsdb_03;
CREATE DATABASE vsdb_03;
USE vsdb_03;

CREATE TABLE Abteilung (
 aname VARCHAR(255) NOT NULL
);

ALTER TABLE Abteilung ADD CONSTRAINT PK_Abteilung PRIMARY KEY (aname);


CREATE TABLE Person (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 aname VARCHAR(255) NOT NULL,
 addresse VARCHAR(255) DEFAULT NULL
);

ALTER TABLE Person ADD CONSTRAINT PK_Person PRIMARY KEY (vorname,nachname);


CREATE TABLE Veranstaltung (
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
 verpflichtend BOOLEAN,
 kosten INT
);

ALTER TABLE Veranstaltung ADD CONSTRAINT PK_Veranstaltung PRIMARY KEY (vname,date);


CREATE TABLE Teilnehmer (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL
);

ALTER TABLE Teilnehmer ADD CONSTRAINT PK_Teilnehmer PRIMARY KEY (vorname,nachname,vname,date);


ALTER TABLE Person ADD CONSTRAINT FK_Person_0 FOREIGN KEY (aname) REFERENCES Abteilung (aname);


ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_0 FOREIGN KEY (vorname,nachname) REFERENCES Person (vorname,nachname);
ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_1 FOREIGN KEY (vname,date) REFERENCES Veranstaltung (vname,date);

INSERT INTO Abteilung VALUES ('HR');
INSERT INTO Abteilung VALUES ('IT');
INSERT INTO Abteilung VALUES ('Facility Management');
INSERT INTO Abteilung VALUES ('Kueche');
INSERT INTO Abteilung VALUES ('Finance');
INSERT INTO Abteilung VALUES ('Managment');
INSERT INTO Abteilung VALUES ('Putzfrauenabteilung');
INSERT INTO Abteilung VALUES ('Kindergarten');
INSERT INTO Abteilung VALUES ('Sales');
INSERT INTO Abteilung VALUES ('Analysten');
INSERT INTO Abteilung VALUES ('Sportabteilung');


INSERT INTO Person VALUES ('Hannah','Siegel','HR','Max Kahrer gasse');
INSERT INTO Person VALUES ('Nikolaus','Schrack','Analysten','Max Ernst gasse');
INSERT INTO Person VALUES ('Paul','Adeyemi','Sportabteilung','Max Posch gasse');
INSERT INTO Person VALUES ('Wolfram','Soyka','Finance','Max Lehrer gasse');
INSERT INTO Person VALUES ('Jakob','Saxinger','Kueche','Max Soundso gasse');
INSERT INTO Person VALUES ('Philip','Schwarzkopf','Sales','Jaegergasse');
INSERT INTO Person VALUES ('Elias','Frantar','Kindergarten','Heiligenstadt gasse');
INSERT INTO Person VALUES ('Gary','Ye','Putzfrauenabteilung','Einfache gasse');
INSERT INTO Person VALUES ('Aly','Ahmed','Facility Management','Doppelte gasse');
INSERT INTO Person VALUES ('Martin','Haidn','Managment','Gruene gasse');
INSERT INTO Person VALUES ('Dominik','Scholz','IT','Schwarze gasse');

 INSERT INTO Veranstaltung VALUES ('Halloween party','2014-10-31',0,10);
 INSERT INTO Veranstaltung VALUES ('PPM Vortrag','2014-11-07',1,0);
 INSERT INTO Veranstaltung VALUES ('Herbstwanderung','2014-11-11',0,5);
 INSERT INTO Veranstaltung VALUES ('Party1','2015-01-04',0,10);
 INSERT INTO Veranstaltung VALUES ('Party2','2014-10-27',0,10);
 INSERT INTO Veranstaltung VALUES ('ABC Konferenz','2014-10-31',1,50);
 INSERT INTO Veranstaltung VALUES ('CCC','2015-10-31',1,0);
 INSERT INTO Veranstaltung VALUES ('Weihnachtsfeier','2014-12-23',0,0);
 INSERT INTO Veranstaltung VALUES ('Party3','2014-10-31',0,25);
 INSERT INTO Veranstaltung VALUES ('Vortrag Wichtig','2014-12-13',1,0);
 INSERT INTO Veranstaltung VALUES ('Vortrag zach','2014-11-18',0,20);

INSERT INTO Teilnehmer VALUES ('Martin','Haidn','PPM Vortrag','2014-11-07');
INSERT INTO Teilnehmer VALUES ('Hannah','Siegel','PPM Vortrag','2014-11-07');
INSERT INTO Teilnehmer VALUES ('Paul','Adeyemi','Party1','2015-01-04');
INSERT INTO Teilnehmer VALUES ('Wolfram','Soyka','Vortrag Wichtig','2014-12-13');
INSERT INTO Teilnehmer VALUES ('Jakob','Saxinger','Vortrag zach','2014-11-18');
INSERT INTO Teilnehmer VALUES ('Philip','Schwarzkopf','Herbstwanderung','2014-11-11');
INSERT INTO Teilnehmer VALUES ('Elias','Frantar','CCC','2015-10-31');
INSERT INTO Teilnehmer VALUES ('Gary','Ye','Weihnachtsfeier','2014-12-23');
INSERT INTO Teilnehmer VALUES ('Aly','Ahmed','Weihnachtsfeier','2014-12-23');
INSERT INTO Teilnehmer VALUES ('Martin','Haidn','Weihnachtsfeier','2014-12-23');
INSERT INTO Teilnehmer VALUES ('Dominik','Scholz','Weihnachtsfeier','2014-12-23');



CREATE TABLE Logged (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 action ENUM('insert', 'update', 'delete') NOT NULL,
 tableName VARCHAR(255) NOT NULL,
 pks VARCHAR(255) NOT NULL,
 table_values VARCHAR(500),
 date_done TIMESTAMP
);

-- insert teilnehmer
delimiter //
CREATE TRIGGER insertteilnehmer AFTER INSERT ON Teilnehmer FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('insert','Teilnehmer','{}',CONCAT('{"vorname":"',NEW.vorname,'","nachname":"',NEW.nachname,'","vname":"',NEW.vname,'","date":"',NEW.date,'"}'),NOW());
END;//
delimiter ;

-- update teilnehmer
delimiter //
CREATE TRIGGER updateteilnehmer AFTER UPDATE ON Teilnehmer FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('update','Teilnehmer',CONCAT('{"vorname":"',OLD.vorname,'","nachname":"',OLD.nachname,'","vname":"',OLD.vname,'","date":"',OLD.date,'"}'),CONCAT('{"vorname":"',NEW.vorname,'","nachname":"',NEW.nachname,'","vname":"',NEW.vname,'","date":"',NEW.date,'"}'),NOW());
END;//
delimiter ;

-- delete teilnehmer
delimiter //
CREATE TRIGGER deleteteilnehmer AFTER DELETE ON Teilnehmer FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('delete','Teilnehmer',CONCAT('{"vorname":"',OLD.vorname,'","nachname":"',OLD.nachname,'","vname":"',OLD.vname,'","date":"',OLD.date,'"}'),'{}',NOW());
END;//
delimiter ;


-- insert Veranstaltung
delimiter //
CREATE TRIGGER insertveranstaltung AFTER INSERT ON Veranstaltung FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('insert','Veranstaltung','{}',CONCAT('{"vname":"',NEW.vname,'","date":"',NEW.date,'","verpflichtend":',NEW.verpflichtend,',"kosten":',NEW.kosten,'}'),NOW());
END;//
delimiter ;

-- update Veranstaltung
delimiter //
CREATE TRIGGER updateveranstaltung AFTER UPDATE ON Veranstaltung FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('update','Veranstaltung',CONCAT('{"vname":"',OLD.vname,'","date":"',OLD.date,'","verpflichtend":',OLD.verpflichtend,',"kosten":',OLD.kosten,'}'),CONCAT('{"vname":"',NEW.vname,'","date":"',NEW.date,'","verpflichtend":',NEW.verpflichtend,',"kosten":',NEW.kosten,'}'),NOW());
END;//
delimiter ;

-- delete Veranstaltung
delimiter //
CREATE TRIGGER deleteveranstaltung AFTER DELETE ON Veranstaltung FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('delete','Veranstaltung',CONCAT('{"vname":"',OLD.vname,'","date":"',OLD.date,'"}'),'{}',NOW());
END;//
delimiter ;



-- insert Person
delimiter //
CREATE TRIGGER insertperson AFTER INSERT ON Person FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('insert','Person','{}',CONCAT('{"vorname":"',NEW.vorname,'","nachname":"',NEW.nachname,'","aname":"',NEW.aname,'","addresse":"',NEW.addresse,'"}'),NOW());
END;//
delimiter ;

-- update Veranstaltung
delimiter //
CREATE TRIGGER updateperson AFTER UPDATE ON Person FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('update','Person',CONCAT('{"vorname":"',OLD.vorname,'","nachname":"',OLD.nachname,'"}'),CONCAT('{"vorname":"',NEW.vorname,'","nachname":"',NEW.nachname,'","aname":"',NEW.aname,'","addresse":"',NEW.addresse,'"}'),NOW());
END;//
delimiter ;

-- delete Veranstaltung
delimiter //
CREATE TRIGGER deleteperson AFTER DELETE ON Person FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('delete','Person',CONCAT('{"vorname":"',OLD.vorname,'","nachname":"',OLD.nachname,'"}'),'{}',NOW());
END;//
delimiter ;



-- insert Abteilung
delimiter //
CREATE TRIGGER insertabteilung AFTER INSERT ON Abteilung FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('insert','Abteilung','{}',CONCAT('{"aname":"',NEW.aname,'"}'),NOW());
END;//
delimiter ;

-- update Veranstaltung
delimiter //
CREATE TRIGGER updateabteilung AFTER UPDATE ON Abteilung FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('update','Abteilung',CONCAT('{"aname":"',OLD.aname,'"}'),CONCAT('{"aname":"',OLD.aname,'"}'),NOW());
END;//
delimiter ;

-- delete Veranstaltung
delimiter //
CREATE TRIGGER deleteabteilung AFTER DELETE ON Abteilung FOR EACH ROW
BEGIN
	INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('delete','Abteilung',CONCAT('{"aname":"',OLD.aname,'"}'),'{}',NOW());
END;//
delimiter ;


INSERT INTO Teilnehmer VALUES ('Hannah','Siegel','Weihnachtsfeier','2014-12-23');
UPDATE Teilnehmer SET  vorname='Wolfram', nachname='Soyka' WHERE  vorname='Hannah' AND nachname='Siegel' AND vname='Weihnachtsfeier' AND date='2014-12-23';
DELETE FROM Teilnehmer WHERE vorname='Gary' AND nachname='Ye' AND vname='Weihnachtsfeier';

INSERT INTO Veranstaltung VALUES ('Party20','2014-12-26',1,5);
DELETE FROM Veranstaltung WHERE vname='Party3' AND date='2014-10-31';
UPDATE Veranstaltung SET  vname='Party200' WHERE  vname='Party20' AND date='2014-12-26';

INSERT INTO Person VALUES ('Max','Mustermann','HR','adresse Max Mustermann');
UPDATE Person SET  addresse='neue addresse' WHERE vorname='Nikolaus' AND nachname='Schrack';
DELETE FROM Person WHERE vorname='Nikolaus' AND nachname='Schrack';

INSERT INTO Abteilung VALUES ('Ganz Neue Abteilung');
UPDATE Abteilung SET  aname='Neue Abteilung' WHERE aname='Ganz Neue Abteilung';
DELETE FROM Abteilung WHERE aname='Neue Abteilung';

SELECT action, tableName from Logged;
