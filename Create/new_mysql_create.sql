DROP DATABASE vsdb_03;
CREATE DATABASE vsdb_03;
USE vsdb_03;

CREATE TABLE Abteilung (
 aname VARCHAR(255) NOT NULL,
  sync_state ENUM('current', 'old', 'new','syncing','deleting') DEFAULT 'new' NOT NULL
);

ALTER TABLE Abteilung ADD CONSTRAINT PK_Abteilung PRIMARY KEY (aname,sync_state);


CREATE TABLE Person (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 aname VARCHAR(255) NOT NULL,
 addresse VARCHAR(255) DEFAULT NULL,
 sync_state ENUM('current', 'old', 'new','syncing') DEFAULT 'new' NOT NULL
);

ALTER TABLE Person ADD CONSTRAINT PK_Person PRIMARY KEY (vorname,nachname,sync_state);


CREATE TABLE Veranstaltung (
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
 verpflichtend BOOLEAN,
 kosten INT,
 sync_state ENUM('current', 'old', 'new','syncing') DEFAULT 'new' NOT NULL

);

ALTER TABLE Veranstaltung ADD CONSTRAINT PK_Veranstaltung PRIMARY KEY (vname,date,sync_state);


CREATE TABLE Teilnehmer (
 vorname VARCHAR(255) NOT NULL,
 nachname VARCHAR(255) NOT NULL,
 vname VARCHAR(255) NOT NULL,
 date DATE NOT NULL,
  sync_state ENUM('current', 'old', 'new','syncing') DEFAULT 'new' NOT NULL
);

ALTER TABLE Teilnehmer ADD CONSTRAINT PK_Teilnehmer PRIMARY KEY (vorname,nachname,vname,date,sync_state);


ALTER TABLE Person ADD CONSTRAINT FK_Person_0 FOREIGN KEY (aname) REFERENCES Abteilung (aname)  ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_0 FOREIGN KEY (vorname,nachname) REFERENCES Person (vorname,nachname)  ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE Teilnehmer ADD CONSTRAINT FK_Teilnehmer_1 FOREIGN KEY (vname,date) REFERENCES Veranstaltung (vname,date) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO Abteilung VALUES ('HR','current');
INSERT INTO Abteilung VALUES ('IT','current');
INSERT INTO Abteilung VALUES ('Facility Management','current');
INSERT INTO Abteilung VALUES ('Kueche','current');
INSERT INTO Abteilung VALUES ('Finance','current');
INSERT INTO Abteilung VALUES ('Managment','current');
INSERT INTO Abteilung VALUES ('Abteilung Google','current');
INSERT INTO Abteilung VALUES ('Kindergarten','current');
INSERT INTO Abteilung VALUES ('Sales','current');
INSERT INTO Abteilung VALUES ('Analysten','current');
INSERT INTO Abteilung VALUES ('Sportabteilung','current');
INSERT INTO Abteilung VALUES ('Buffet','current');

INSERT INTO Person VALUES ('Hannah','Siegel','HR','Max Kahrer gasse','current');
INSERT INTO Person VALUES ('Nikolaus','Schrack','Analysten','Max Ernst gasse','current');
INSERT INTO Person VALUES ('Paul','Adeyemi','Sportabteilung','Max Posch gasse','current');
INSERT INTO Person VALUES ('Wolfram','Soyka','Finance','Max Lehrer gasse','current');
INSERT INTO Person VALUES ('Jakob','Saxinger','Kueche','Max Soundso gasse','current');
INSERT INTO Person VALUES ('Philip','Schwarzkopf','Sales','Jaegergasse','current');
INSERT INTO Person VALUES ('Elias','Frantar','Kindergarten','Heiligenstadt gasse','current');
INSERT INTO Person VALUES ('Gary','Ye','Putzfrauenabteilung','Einfache gasse','current');
INSERT INTO Person VALUES ('Aly','Ahmed','Facility Management','Doppelte gasse','current');
INSERT INTO Person VALUES ('Martin','Haidn','Managment','Gruene gasse','current');
INSERT INTO Person VALUES ('Dominik','Scholz','IT','Schwarze gasse','current');

 INSERT INTO Veranstaltung VALUES ('Halloween party','2014-10-31',0,10,'current');
 INSERT INTO Veranstaltung VALUES ('PPM Vortrag','2014-11-07',1,0,'current');
 INSERT INTO Veranstaltung VALUES ('Herbstwanderung','2014-11-11',0,5,'current');
 INSERT INTO Veranstaltung VALUES ('Party1','2015-01-04',0,10,'current');
 INSERT INTO Veranstaltung VALUES ('Party2','2014-10-27',0,10,'current');
 INSERT INTO Veranstaltung VALUES ('ABC Konferenz','2014-10-31',1,50,'current');
 INSERT INTO Veranstaltung VALUES ('CCC','2015-10-31',1,0,'current');
 INSERT INTO Veranstaltung VALUES ('Weihnachtsfeier','2014-12-23',0,0,'current');
 INSERT INTO Veranstaltung VALUES ('Party3','2014-10-31',0,25,'current');
 INSERT INTO Veranstaltung VALUES ('Vortrag Wichtig','2014-12-13',1,0,'current');
 INSERT INTO Veranstaltung VALUES ('Vortrag zach','2014-11-18',0,20,'current');

INSERT INTO Teilnehmer VALUES ('Martin','Haidn','PPM Vortrag','2014-11-07','current');
INSERT INTO Teilnehmer VALUES ('Hannah','Siegel','PPM Vortrag','2014-11-07','current');
INSERT INTO Teilnehmer VALUES ('Paul','Adeyemi','Party1','2015-01-04','current');
INSERT INTO Teilnehmer VALUES ('Wolfram','Soyka','Vortrag Wichtig','2014-12-13','current');
INSERT INTO Teilnehmer VALUES ('Jakob','Saxinger','Vortrag zach','2014-11-18','current');
INSERT INTO Teilnehmer VALUES ('Philip','Schwarzkopf','Herbstwanderung','2014-11-11','current');
INSERT INTO Teilnehmer VALUES ('Elias','Frantar','CCC','2015-10-31','current');
INSERT INTO Teilnehmer VALUES ('Gary','Ye','Weihnachtsfeier','2014-12-23','current');
INSERT INTO Teilnehmer VALUES ('Aly','Ahmed','Weihnachtsfeier','2014-12-23','current');
INSERT INTO Teilnehmer VALUES ('Martin','Haidn','Weihnachtsfeier','2014-12-23','current');
INSERT INTO Teilnehmer VALUES ('Dominik','Scholz','Weihnachtsfeier','2014-12-23','current');


CREATE TABLE Logged (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 action ENUM('insert', 'update', 'delete') NOT NULL,
 tableName VARCHAR(255) NOT NULL,
 old_values VARCHAR(255) NOT NULL,
 new_values VARCHAR(500),
 date_done TIMESTAMP
) ENGINE=MYISAM;

-- insert teilnehmer
delimiter //
CREATE TRIGGER insertteilnehmer AFTER INSERT ON Teilnehmer FOR EACH ROW
BEGIN
	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('insert','Teilnehmer','{}',CONCAT('{"name":"',NEW.vorname,' ',NEW.nachname,'","vname":"',NEW.vname,'","date":"',NEW.date,'"}'),NOW());
		-- DELETE FROM Teilnehmer WHERE name = NEW.name; 
	END IF;
END;//
delimiter ;

-- update teilnehmer
delimiter //
CREATE TRIGGER updateteilnehmer AFTER UPDATE ON Teilnehmer FOR EACH ROW
BEGIN
	-- declare msg varchar(255);

	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('update','Teilnehmer',CONCAT('{"name":"',OLD.vorname,' ',OLD.nachname,'","vname":"',OLD.vname,'","date":"',OLD.date,'"}'),CONCAT('{"name":"',NEW.vorname,' ',NEW.nachname,'","vname":"',NEW.vname,'","date":"',NEW.date,'"}'),NOW());
		-- set msg = 'Your update has been staged and sent to the syncserver';
        -- signal sqlstate '45000' set message_text = msg;		
	END IF;
END;//
delimiter ;

-- delete teilnehmer
delimiter //
CREATE TRIGGER deleteteilnehmer AFTER DELETE ON Teilnehmer FOR EACH ROW
BEGIN
	-- IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('delete','Teilnehmer',CONCAT('{"name":"',OLD.vorname,' ',OLD.nachname,'","vname":"',OLD.vname,'","date":"',OLD.date,'"}'),'{}',NOW());
	-- END IF;
END;//
delimiter ;


-- insert Veranstaltung
delimiter //
CREATE TRIGGER insertveranstaltung AFTER INSERT ON Veranstaltung FOR EACH ROW
BEGIN

	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('insert','Veranstaltung','{}',CONCAT('{"vname":"',NEW.vname,'","date":"',NEW.date,'","verpflichtend":',NEW.verpflichtend,',"kosten":',NEW.kosten,'}'),NOW());
	END IF;

END;//
delimiter ;

-- update Veranstaltung
delimiter //
CREATE TRIGGER updateveranstaltung AFTER UPDATE ON Veranstaltung FOR EACH ROW
BEGIN
	-- declare msg varchar(255);

	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('update','Veranstaltung',CONCAT('{"vname":"',OLD.vname,'","date":"',OLD.date,'","verpflichtend":',OLD.verpflichtend,',"kosten":',OLD.kosten,'}'),CONCAT('{"vname":"',NEW.vname,'","date":"',NEW.date,'","verpflichtend":',NEW.verpflichtend,',"kosten":',NEW.kosten,'}'),NOW());
		--  set msg = 'Your update has been staged and sent to the syncserver';
        --  signal sqlstate '45000' set message_text = msg;		
	END IF;

END;//
delimiter ;

-- delete Veranstaltung
delimiter //
CREATE TRIGGER deleteveranstaltung AFTER DELETE ON Veranstaltung FOR EACH ROW
BEGIN
	-- IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('delete','Veranstaltung',CONCAT('{"vname":"',OLD.vname,'","date":"',OLD.date,'"}'),'{}',NOW());
	-- END IF;
END;//
delimiter ;



-- insert Person
delimiter //
CREATE TRIGGER insertperson AFTER INSERT ON Person FOR EACH ROW
BEGIN
	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('insert','Person','{}',CONCAT('{"name":"',NEW.vorname,' ',NEW.nachname,'","aname":"',NEW.aname,'","addresse":"',NEW.addresse,'"}'),NOW());
		-- DELETE FROM Teilnehmer WHERE vorname = NEW.vorname AND nachname = NEW.nachname; 
	END IF;
END;//
delimiter ;



-- update Person
delimiter //
CREATE TRIGGER updateperson AFTER UPDATE ON Person FOR EACH ROW
BEGIN
    -- declare msg varchar(255);

	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('update','Person',CONCAT('{"name":"',OLD.vorname,' ',OLD.nachname,'","aname":"',OLD.aname,'","addresse":"',OLD.addresse,'"}'),CONCAT('{"name":"',NEW.vorname,' ',NEW.nachname,'","aname":"',NEW.aname,'","addresse":"',NEW.addresse,'"}'),NOW());
		-- set msg = 'Your update has been staged and sent to the syncserver';
        -- signal sqlstate '45000' set message_text = msg;		
	END IF;
END;//
delimiter ;

-- delimiter //
-- CREATE PROCEDURE test (IN oldvorname VARCHAR(255), IN oldnachname VARCHAR(255),IN oldaname VARCHAR(255),IN oldaddresse VARCHAR(255), IN newvorname VARCHAR(255), IN newnachname VARCHAR(255))
-- BEGIN
--	UPDATE Person SET sync_state='current', vorname = oldvorname, nachname = oldnachname, aname = oldaname, addresse=oldaddresse WHERE vorname = newvorname AND nachname = newnachname;
-- END//
-- delimiter ;

-- delete Veranstaltung
delimiter //
CREATE TRIGGER deleteperson AFTER DELETE ON Person FOR EACH ROW
BEGIN
	-- IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('delete','Person',CONCAT('{"name":"',OLD.vorname,' ',OLD.nachname,'"}'),'{}',NOW());
	-- END IF;

END;//
delimiter ;


-- insert Abteilung
delimiter //
CREATE TRIGGER insertabteilung AFTER INSERT ON Abteilung FOR EACH ROW
BEGIN
	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('insert','Abteilung','{}',CONCAT('{"aname":"',NEW.aname,'"}'),NOW());
	END IF;

END;//
delimiter ;

-- update Veranstaltung
delimiter //
CREATE TRIGGER updateabteilung AFTER UPDATE ON Abteilung FOR EACH ROW
BEGIN
	-- declare msg varchar(255);
	IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('update','Abteilung',CONCAT('{"aname":"',OLD.aname,'"}'),CONCAT('{"aname":"',NEW.aname,'"}'),NOW());
		-- set msg = 'Your update has been staged and sent to the syncserver';
		-- signal sqlstate '45000' set message_text = msg;		
	END IF;
END;//
delimiter ;

-- delete Veranstaltung
delimiter //
CREATE TRIGGER deleteabteilung AFTER DELETE ON Abteilung FOR EACH ROW
BEGIN
	-- IF NEW.sync_state = 'new' THEN
		INSERT INTO Logged(action,tableName,old_values,new_values,date_done) VALUES ('delete','Abteilung',CONCAT('{"aname":"',OLD.aname,'"}'),'{}',NOW());
	-- END IF;

END;//
delimiter ;


  INSERT INTO Teilnehmer VALUES ('Hannah','Siegel','Weihnachtsfeier','2014-12-23',DEFAULT);
    UPDATE Teilnehmer SET  vorname='Wolfram', sync_state='new', nachname='Soyka' WHERE  vorname='Hannah' AND nachname='Siegel' AND vname='Weihnachtsfeier' AND date='2014-12-23';
    DELETE FROM Teilnehmer WHERE vorname='Gary' AND nachname='Ye' AND vname='Weihnachtsfeier';

    INSERT INTO Veranstaltung VALUES ('Party20','2014-12-26',1,5,DEFAULT);
    DELETE FROM Veranstaltung WHERE vname='Party3' AND date='2014-10-31';
    UPDATE Veranstaltung SET  vname='Party200', sync_state='new' WHERE  vname='Party20' AND date='2014-12-26';

    INSERT INTO Person VALUES ('Max','Mustermann','HR','adresse Max Mustermann',DEFAULT);
    UPDATE Person SET  addresse='neue addresse', sync_state='new' WHERE vorname='Nikolaus' AND nachname='Schrack';
    DELETE FROM Person WHERE vorname='Nikolaus' AND nachname='Schrack';

   INSERT INTO Abteilung VALUES ('Ganz Neue Abteilung',DEFAULT);
   UPDATE Abteilung SET  aname='Neue Abteilung', sync_state='new' WHERE aname='Ganz Neue Abteilung';
    DELETE FROM Abteilung WHERE aname='Buffet';

 
 
SELECT action, tableName from Logged;
