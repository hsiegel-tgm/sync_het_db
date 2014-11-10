\c template1
DROP DATABASE vsdb_03;
CREATE DATABASE vsdb_03;
\c vsdb_03

CREATE TYPE sync_type AS ENUM ('current' , 'old' , 'new' , 'syncing','deleting');


CREATE TABLE Mitarbeiter (
 name VARCHAR(255) NOT NULL,
 abteilung VARCHAR(255),
 sync sync_type DEFAULT 'new' NOT NULL
);

ALTER TABLE Mitarbeiter ADD CONSTRAINT PK_Mitarbeiter PRIMARY KEY (name,sync);


CREATE TABLE Veranstaltung (
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
 verpflichtend BOOLEAN DEFAULT false,
 kosten INT,
 sync sync_type DEFAULT 'new' NOT NULL
);

ALTER TABLE Veranstaltung ADD CONSTRAINT PK_Veranstaltung PRIMARY KEY (vname,date,sync);


CREATE TABLE Besucher (
 name VARCHAR(255) NOT NULL,
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
 sync sync_type DEFAULT 'new' NOT NULL
);

ALTER TABLE Besucher ADD CONSTRAINT PK_Besucher PRIMARY KEY (name,vname,date,sync);
ALTER TABLE Besucher ADD CONSTRAINT FK_Besucher_0 FOREIGN KEY (name) REFERENCES Mitarbeiter (name);
ALTER TABLE Besucher ADD CONSTRAINT FK_Besucher_1 FOREIGN KEY (vname,date) REFERENCES Veranstaltung (vname,date);


CREATE TYPE action_type AS ENUM ('insert', 'update', 'delete');

CREATE TABLE Logged (
 id SERIAL,
 action action_type,
 tableName VARCHAR(255) NOT NULL,
 old_values VARCHAR(255) NOT NULL,
 new_values VARCHAR(500),
 date_done TIMESTAMP
);
ALTER TABLE Logged ADD CONSTRAINT PK_Logged PRIMARY KEY (id);


-- CREATE OR REPLACE FUNCTION function_name1(TEXT,TEXT) RETURNS VOID AS $$
-- 	INSERT INTO Logged VALUES (2,'insert','Mitarbeiter',$1 || $2,'values',CURRENT_TIMESTAMP);
--  $$LANGUAGE SQL;

-- CREATE TRIGGER triggertest AFTER INSERT
--    ON Mitarbeiter FOR EACH ROW
--    EXECUTE PROCEDURE function_name1 ( NEW.name, NEW.abteilung );

INSERT INTO Mitarbeiter VALUES ('Hannah Siegel','HR',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Nikolaus Schrack','Analysten',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Paul Adeyemi','Sportabteilung',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Wolfram Soyka','Finance',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Jakob Saxinger','Kueche',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Philip Schwarzkopf','Sales',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Elias Frantar','Kindergarten',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Gary Ye','Putzfrauenabteilung',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Aly Ahmed','Facility Management',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Martin Haidn','Managment',DEFAULT);
INSERT INTO Mitarbeiter VALUES ('Dominik Scholz','IT',DEFAULT);

INSERT INTO Veranstaltung VALUES ('Halloween party',TO_DATE('31.10.2014', 'DD.MM.YYYY'),false,10,DEFAULT);
INSERT INTO Veranstaltung VALUES ('PPM Vortrag',TO_DATE('11.07.2014', 'DD.MM.YYYY'),true,0,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Herbstwanderung',TO_DATE('11.11.2014', 'DD.MM.YYYY'),false,5,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Party1',TO_DATE('04.01.2014', 'DD.MM.YYYY'),false,10,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Party2',TO_DATE('27.10.2014', 'DD.MM.YYYY'),false,10,DEFAULT);
INSERT INTO Veranstaltung VALUES ('ABC Konferenz',TO_DATE('31.10.2014', 'DD.MM.YYYY'),true,50,DEFAULT);
INSERT INTO Veranstaltung VALUES ('CCC',TO_DATE('31.10.2015', 'DD.MM.YYYY'),true,0,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),false,0,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Party3',TO_DATE('31.10.2014', 'DD.MM.YYYY'),false,25,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Vortrag Wichtig',TO_DATE('13.12.2014', 'DD.MM.YYYY'),true,0,DEFAULT);
INSERT INTO Veranstaltung VALUES ('Vortrag zach',TO_DATE('18.11.2014', 'DD.MM.YYYY'),false,20,DEFAULT);

INSERT INTO Besucher VALUES ('Martin Haidn','PPM Vortrag',TO_DATE('11.07.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Hannah Siegel','PPM Vortrag',TO_DATE('11.07.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Paul Adeyemi','Party1',TO_DATE('04.01.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Wolfram Soyka','Vortrag Wichtig',TO_DATE('13.12.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Jakob Saxinger','Vortrag zach',TO_DATE('18.11.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Philip Schwarzkopf','Herbstwanderung',TO_DATE('11.11.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Elias Frantar','CCC',TO_DATE('31.10.2015', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Gary Ye','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Aly Ahmed','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Martin Haidn','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),DEFAULT);
INSERT INTO Besucher VALUES ('Dominik Scholz','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),DEFAULT);

-----------------------------------------------------------------------------------------------------------------
-------------------------------------------------- MITARBEITER --------------------------------------------------
-----------------------------------------------------------------------------------------------------------------

-------------------------------------------- insert into mitarbeiter --------------------------------------------
CREATE OR REPLACE FUNCTION insert_into_Mitarbeiter() RETURNS TRIGGER AS $mitarbeiter$
    BEGIN
		IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'insert','Person','{}','{"name":"'||NEW.name||'","addresse":"","aname":"'||NEW.abteilung||'"}',current_timestamp);
		END IF;
		RETURN NEW;
    END;
$mitarbeiter$ LANGUAGE plpgsql;

CREATE TRIGGER insert_into_Mitarbeiter_trigger AFTER INSERT ON Mitarbeiter
FOR EACH ROW EXECUTE PROCEDURE insert_into_Mitarbeiter();


-------------------------------------------- update mitarbeiter --------------------------------------------

CREATE OR REPLACE FUNCTION update_Mitarbeiter() RETURNS TRIGGER AS $mitarbeiter2$
    BEGIN
		IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'update','Person','{"name":"'||OLD.name||'","addresse":"","aname":"'||OLD.abteilung||'"}','{"name":"'||NEW.name||'","addresse":"","abteilung":"'||NEW.abteilung||'"}',current_timestamp);
		END IF;
       
	   RETURN NULL;
	   -- RETURN NEW;
    END;
$mitarbeiter2$ LANGUAGE plpgsql;

CREATE TRIGGER update_Mitarbeiter_trigger BEFORE UPDATE ON Mitarbeiter
FOR EACH ROW EXECUTE PROCEDURE update_Mitarbeiter();


-------------------------------------------- delete from mitarbeiter --------------------------------------------

CREATE OR REPLACE FUNCTION delete_from_Mitarbeiter() RETURNS TRIGGER AS $mitarbeiter3$
    BEGIN
		-- IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'delete','Person','{"name":"'||OLD.name||'","addresse":"","aname":"'||OLD.abteilung||'"}','{}',current_timestamp);
		-- END IF;
       
	   RETURN NEW;
    END;
$mitarbeiter3$ LANGUAGE plpgsql;

CREATE TRIGGER delete_from_Mitarbeiter_trigger AFTER DELETE ON Mitarbeiter
FOR EACH ROW EXECUTE PROCEDURE delete_from_Mitarbeiter();

-----------------------------------------------------------------------------------------------------------------
-------------------------------------------------- VERANSTALTUNG --------------------------------------------------
-----------------------------------------------------------------------------------------------------------------


 --  '{"vname":"'||NEW.vname||'","date":"'||NEW.date||'","verpflichtend":'||NEW.verpflichtend||',"kosten":'||NEW.kosten||'}'              
 -- '{"vname":"'||OLD.vname||'","date":"'||OLD.date||'","verpflichtend":'||OLD.verpflichtend||',"kosten":'||OLD.kosten||'}'              



-------------------------------------------- insert into Veranstaltung --------------------------------------------
CREATE OR REPLACE FUNCTION insert_into_Veranstaltung() RETURNS TRIGGER AS $veranstaltung$
    BEGIN
		IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'insert','Veranstaltung','{}','{"vname":"'||NEW.vname||'","date":"'||NEW.date||'","verpflichtend":'||NEW.verpflichtend||',"kosten":'||NEW.kosten||'}',current_timestamp);
       	END IF;

		RETURN NEW;
    END;
$veranstaltung$ LANGUAGE plpgsql;

CREATE TRIGGER insert_into_Veranstaltung_trigger AFTER INSERT ON Veranstaltung
FOR EACH ROW EXECUTE PROCEDURE insert_into_Veranstaltung();


-------------------------------------------- update Veranstaltung --------------------------------------------

CREATE OR REPLACE FUNCTION update_Veranstaltung() RETURNS TRIGGER AS $veranstaltung2$
    BEGIN
        IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'update','Veranstaltung','{"vname":"'||OLD.vname||'","date":"'||OLD.date||'","verpflichtend":'||OLD.verpflichtend||',"kosten":'||OLD.kosten||'}','{"vname":"'||NEW.vname||'","date":"'||NEW.date||'","verpflichtend":'||NEW.verpflichtend||',"kosten":'||NEW.kosten||'}' ,current_timestamp);
        END IF;

		RETURN NULL;
    END;
$veranstaltung2$ LANGUAGE plpgsql;

CREATE TRIGGER update_Veranstaltung_trigger BEFORE UPDATE ON Veranstaltung
FOR EACH ROW EXECUTE PROCEDURE update_Veranstaltung();


-------------------------------------------- delete from Veranstaltung --------------------------------------------

CREATE OR REPLACE FUNCTION delete_from_Veranstaltung() RETURNS TRIGGER AS $veranstaltung3$
    BEGIN
        --IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'delete','Veranstaltung','{"vname":"'||OLD.vname||'","date":"'||OLD.date||'","verpflichtend":'||OLD.verpflichtend||',"kosten":'||OLD.kosten||'}','{}',current_timestamp);        
		-- END IF;
		RETURN NEW;	
	END;
$veranstaltung3$ LANGUAGE plpgsql;

CREATE TRIGGER delete_from_Veranstaltung_trigger AFTER DELETE ON Veranstaltung
FOR EACH ROW EXECUTE PROCEDURE delete_from_Veranstaltung();



-----------------------------------------------------------------------------------------------------------------
-------------------------------------------------- BESUCHER --------------------------------------------------
-----------------------------------------------------------------------------------------------------------------


 --  '{"vname":"'||NEW.vname||'","date":"'||NEW.date||'","name":'||NEW.name||'}'              
  --  '{"vname":"'||OLD.vname||'","date":"'||OLD.date||'","name":'||OLD.name||'}'              



-------------------------------------------- insert into Besucher --------------------------------------------
CREATE OR REPLACE FUNCTION insert_into_Besucher() RETURNS TRIGGER AS $besucher$
    BEGIN
		IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'insert','Teilnehmer','{}','{"vname":"'||NEW.vname||'","date":"'||NEW.date||'","name":"'||NEW.name||'"}',current_timestamp);
       	END IF;

		RETURN NEW;
    END;
$besucher$ LANGUAGE plpgsql;

CREATE TRIGGER insert_into_Besucher_trigger AFTER INSERT ON Besucher
FOR EACH ROW EXECUTE PROCEDURE insert_into_Besucher();


-------------------------------------------- update Veranstaltung --------------------------------------------

CREATE OR REPLACE FUNCTION update_Besucher() RETURNS TRIGGER AS $besucher2$
    BEGIN
		IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'update','Teilnehmer','{"vname":"'||OLD.vname||'","date":"'||OLD.date||'","name":"'||OLD.name||'"}','{"vname":"'||NEW.vname||'","date":"'||NEW.date||'","name":"'||NEW.name||'"}' ,current_timestamp);
        END IF;

		RETURN NULL;
    END;
$besucher2$ LANGUAGE plpgsql;

CREATE TRIGGER update_Besucher_trigger BEFORE UPDATE ON Besucher
FOR EACH ROW EXECUTE PROCEDURE update_Besucher();


-------------------------------------------- delete from Veranstaltung --------------------------------------------

CREATE OR REPLACE FUNCTION delete_from_Besucher() RETURNS TRIGGER AS $besucher3$
    BEGIN
		-- IF NEW.sync = 'new' THEN
			INSERT INTO Logged(id,action,tableName,old_values,new_values,date_done) VALUES (DEFAULT,'delete','Teilnehmer','{"vname":"'||OLD.vname||'","date":"'||OLD.date||'","name":"'||OLD.name||'"}','{}',current_timestamp);
    	-- END IF;

		RETURN NEW;
    END;
$besucher3$ LANGUAGE plpgsql;

CREATE TRIGGER delete_from_Besucher_trigger AFTER DELETE ON Besucher
FOR EACH ROW EXECUTE PROCEDURE delete_from_Besucher();





-- Testdaten

-- INSERT INTO Mitarbeiter VALUES('vorname1 mittelname1 nachname1','abteilung3');
-- INSERT INTO Veranstaltung VALUES ('Tolle Konferenz',TO_DATE('31.10.2014', 'DD.MM.YYYY'),false,50);
-- INSERT INTO Besucher VALUES ('vorname1 mittelname1 nachname1','Tolle Konferenz',TO_DATE('31.10.2014', 'DD.MM.YYYY'));

-- UPDATE Mitarbeiter SET abteilung = 'abteilungNEU' WHERE name = 'vorname1 mittelname1 nachname1';
-- UPDATE Veranstaltung SET verpflichtend = true WHERE vname = 'Tolle Konferenz';
-- UPDATE Besucher SET name = 'Hannah Siegel' WHERE vname = 'CCC';

-- DELETE FROM Besucher WHERE name = 'vorname1 mittelname1 nachname1';
-- DELETE FROM Mitarbeiter WHERE name = 'vorname1 mittelname1 nachname1';
-- DELETE FROM Veranstaltung WHERE vname = 'Tolle Konferenz';

SELECT * FROM Logged;