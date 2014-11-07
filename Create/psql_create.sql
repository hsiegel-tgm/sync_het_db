\c template1
DROP DATABASE vsdb_03;
CREATE DATABASE vsdb_03;
\c vsdb_03

CREATE TABLE Mitarbeiter (
 name VARCHAR(255) NOT NULL,
 abteilung VARCHAR(255)
);

ALTER TABLE Mitarbeiter ADD CONSTRAINT PK_Mitarbeiter PRIMARY KEY (name);


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
ALTER TABLE Besucher ADD CONSTRAINT FK_Besucher_0 FOREIGN KEY (name) REFERENCES Mitarbeiter (name);
ALTER TABLE Besucher ADD CONSTRAINT FK_Besucher_1 FOREIGN KEY (vname,date) REFERENCES Veranstaltung (vname,date);


CREATE TYPE action_type AS ENUM ('insert', 'update', 'delete');

CREATE TABLE Logged (
 id SERIAL,
 action action_type,
 tableName VARCHAR(255) NOT NULL,
 pks VARCHAR(255) NOT NULL,
 table_values VARCHAR(500),
 date_done TIMESTAMP
);
ALTER TABLE Logged ADD CONSTRAINT PK_Logged PRIMARY KEY (id);

INSERT INTO Logged VALUES (1,'insert','Mitarbeiter','pks','values',CURRENT_TIMESTAMP);

-- CREATE OR REPLACE FUNCTION function_name1(TEXT,TEXT) RETURNS VOID AS $$
-- 	INSERT INTO Logged VALUES (2,'insert','Mitarbeiter',$1 || $2,'values',CURRENT_TIMESTAMP);
--  $$LANGUAGE SQL;

-- CREATE TRIGGER triggertest AFTER INSERT
--    ON Mitarbeiter FOR EACH ROW
--    EXECUTE PROCEDURE function_name1 ( NEW.name, NEW.abteilung );


INSERT INTO Mitarbeiter VALUES ('Siegel','HR');
INSERT INTO Mitarbeiter VALUES ('Schrack','Analysten');
INSERT INTO Mitarbeiter VALUES ('Adeyemi','Sportabteilung');
INSERT INTO Mitarbeiter VALUES ('Soyka','Finance');
INSERT INTO Mitarbeiter VALUES ('Saxinger','Kueche');
INSERT INTO Mitarbeiter VALUES ('Schwarzkopf','Sales');
INSERT INTO Mitarbeiter VALUES ('Frantar','Kindergarten');
INSERT INTO Mitarbeiter VALUES ('Ye','Putzfrauenabteilung');
INSERT INTO Mitarbeiter VALUES ('Ahmed','Facility Management');
INSERT INTO Mitarbeiter VALUES ('Haidn','Managment');
INSERT INTO Mitarbeiter VALUES ('Scholz','IT');
INSERT INTO Mitarbeiter VALUES ('Franta','IT');	
	

INSERT INTO Veranstaltung VALUES ('Halloween party',TO_DATE('31.10.2014', 'DD.MM.YYYY'),false,10);
INSERT INTO Veranstaltung VALUES ('PPM Vortrag',TO_DATE('11.07.2014', 'DD.MM.YYYY'),true,0);
INSERT INTO Veranstaltung VALUES ('Herbstwanderung',TO_DATE('11.11.2014', 'DD.MM.YYYY'),false,5);
INSERT INTO Veranstaltung VALUES ('Party1',TO_DATE('04.01.2014', 'DD.MM.YYYY'),false,10);
INSERT INTO Veranstaltung VALUES ('Party2',TO_DATE('27.10.2014', 'DD.MM.YYYY'),false,10);
INSERT INTO Veranstaltung VALUES ('ABC Konferenz',TO_DATE('31.10.2014', 'DD.MM.YYYY'),true,50);
INSERT INTO Veranstaltung VALUES ('CCC',TO_DATE('31.10.2015', 'DD.MM.YYYY'),true,0);
INSERT INTO Veranstaltung VALUES ('Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),false,0);
INSERT INTO Veranstaltung VALUES ('Party3',TO_DATE('31.10.2014', 'DD.MM.YYYY'),false,25);
INSERT INTO Veranstaltung VALUES ('Vortrag Wichtig',TO_DATE('13.12.2014', 'DD.MM.YYYY'),true,0);
INSERT INTO Veranstaltung VALUES ('Vortrag zach',TO_DATE('18.11.2014', 'DD.MM.YYYY'),false,20);

INSERT INTO Besucher VALUES ('Haidn','PPM Vortrag',TO_DATE('11.07.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Siegel','PPM Vortrag',TO_DATE('11.07.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Adeyemi','Party1',TO_DATE('04.01.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Soyka','Vortrag Wichtig',TO_DATE('13.12.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Saxinger','Vortrag zach',TO_DATE('18.11.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Schwarzkopf','Herbstwanderung',TO_DATE('11.11.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Frantar','CCC',TO_DATE('31.10.2015', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Ye','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Ahmed','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Haidn','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'));
INSERT INTO Besucher VALUES ('Scholz','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'));


	
	
CREATE OR REPLACE FUNCTION auditlogfunc() RETURNS TRIGGER AS $example_mitarbeiter$
    BEGIN
        INSERT INTO Logged(id,action,tableName,pks,table_values,date_done) VALUES (2,'insert','mitarbeiter','{}','{"name":"'||NEW.name||'","abteilung":"'||NEW.abteilung||'"}',current_timestamp);
        RETURN NEW;
    END;
$example_mitarbeiter$ LANGUAGE plpgsql;

CREATE TRIGGER example_trigger AFTER INSERT ON Mitarbeiter
FOR EACH ROW EXECUTE PROCEDURE auditlogfunc();

INSERT INTO Mitarbeiter VALUES('test1','test2');

INSERT INTO Logged(action,tableName,pks,table_values,date_done) VALUES ('insert','Mitarbeiter','pks2'||'teeeeest','values2',CURRENT_TIMESTAMP);

