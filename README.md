sync_het_db
===========

SYNCHRONISATION VON HETEROGENEN DATENBANKEN

ssh connection: (password: schueler)
	ssh schueler@192.168.181.128 

psql login: (password: vsdb_03)
	psql -h 127.0.0.1 -d vsdb_03 -U vsdb_03
psql create ausführen: (Man muss im Desktop Ordner sein oder Pfad dort hin setzten)
	nach dem einloggen:
	.\i psql_create.sql
 
msql login: (password: vsdb_03)
	 mysql -u vsdb_03 -p
mysql create ausführen: 
	mysql -u vsdb_03 -p  < mysql_create.sql