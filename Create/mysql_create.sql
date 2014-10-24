DROP DATABASE IF EXISTS vsdb_03;
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

ALTER TABLE Person ADD CONSTRAINT PK_Person PRIMARY KEY (vorname,nachname,aname);


CREATE TABLE Veranstaltung (
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
 verpflichtend BOOLEAN DEFAULT false, 
 kosten INT
);

ALTER TABLE Veranstaltung ADD CONSTRAINT PK_Veranstaltung PRIMARY KEY (vname,date);


CREATE TABLE Teilnehmer (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 aname VARCHAR(255) NOT NULL,
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL
);

ALTER TABLE Teilnehmer ADD CONSTRAINT PK_Teilnehmer PRIMARY KEY (vorname,nachname,aname,vname,date);


ALTER TABLE Person ADD CONSTRAINT FK_Person_0 FOREIGN KEY (aname) REFERENCES Abteilung (aname);


ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_0 FOREIGN KEY (vorname,nachname,aname) REFERENCES Person (vorname,nachname,aname);
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

 
INSERT INTO Teilnehmer VALUES ('Martin','Haidn','Managment','PPM Vortrag','2014-11-07');
INSERT INTO Teilnehmer VALUES ('Hannah','Siegel','HR','PPM Vortrag','2014-11-07');
INSERT INTO Teilnehmer VALUES ('Paul','Adeyemi','Sportabteilung','Party1','2015-01-04');
INSERT INTO Teilnehmer VALUES ('Wolfram','Soyka','Finance','Vortrag Wichtig','2014-12-13');
INSERT INTO Teilnehmer VALUES ('Jakob','Saxinger','Kueche','Vortrag zach','2014-11-18');
INSERT INTO Teilnehmer VALUES ('Philip','Schwarzkopf','Sales','Herbstwanderung','2014-11-11');
INSERT INTO Teilnehmer VALUES ('Elias','Frantar','Kindergarten','CCC','2015-10-31');
INSERT INTO Teilnehmer VALUES ('Gary','Ye','Putzfrauenabteilung','Weihnachtsfeier','2014-12-23');
INSERT INTO Teilnehmer VALUES ('Aly','Ahmed','Facility Management','Weihnachtsfeier','2014-12-23');
INSERT INTO Teilnehmer VALUES ('Martin','Haidn','Managment','Weihnachtsfeier','2014-12-23');
INSERT INTO Teilnehmer VALUES ('Dominik','Scholz','IT','Weihnachtsfeier','2014-12-23');
