CREATE TABLE Abteilung (
 aname VARCHAR(255) NOT NULL
);

ALTER TABLE Abteilung ADD CONSTRAINT PK_Abteilung PRIMARY KEY (aname);


CREATE TABLE Mitarbeiter (
 name VARCHAR(255) NOT NULL,
 abteilung CHAR(10)
);

ALTER TABLE Mitarbeiter ADD CONSTRAINT PK_Mitarbeiter PRIMARY KEY (name);


CREATE TABLE Person (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 aname VARCHAR(255) NOT NULL,
 addresse VARCHAR(255)
);

ALTER TABLE Person ADD CONSTRAINT PK_Person PRIMARY KEY (vorname,nachname,aname);


CREATE TABLE Veranstaltung (
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
 verpflichtend BOOLEAN DEFAULT false,
 kosten INT
);

ALTER TABLE Veranstaltung ADD CONSTRAINT PK_Veranstaltung PRIMARY KEY (vname,date);


CREATE TABLE Besucher (
 name VARCHAR(255) NOT NULL,
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL
);

ALTER TABLE Besucher ADD CONSTRAINT PK_Besucher PRIMARY KEY (name,vname,date);


CREATE TABLE Teilnehmer (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 aname VARCHAR(255) NOT NULL,
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL
);

ALTER TABLE Teilnehmer ADD CONSTRAINT PK_Teilnehmer PRIMARY KEY (vorname,nachname,aname,vname,date);


ALTER TABLE Person ADD CONSTRAINT FK_Person_0 FOREIGN KEY (aname) REFERENCES Abteilung (aname);


ALTER TABLE Besucher ADD CONSTRAINT FK_Besucher_0 FOREIGN KEY (name) REFERENCES Mitarbeiter (name);
ALTER TABLE Besucher ADD CONSTRAINT FK_Besucher_1 FOREIGN KEY (vname,date) REFERENCES Veranstaltung (vname,date);


ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_0 FOREIGN KEY (vorname,nachname,aname) REFERENCES Person (vorname,nachname,aname);
ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_1 FOREIGN KEY (vname,date) REFERENCES Veranstaltung (vname,date);


