package mysql;

import interfaces.Mapper;

import java.io.StringReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class ChangeListenerMysql implements Runnable, Mapper {
	private Connection m_connection;
	private MysqlConnection m_connectionobj;
	private String QUERY = "Select * from logged";
	private Mapper m_mapper;
	
	public ChangeListenerMysql(String db,String host,String user,String pw){
		m_connectionobj = new MysqlConnection(db,host,user,pw);
		m_connection = m_connectionobj.getConnection();
		
	//	try{
	//		Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
	//		m_mapper = (Mapper) registry.lookup("Postgres");
			this.run();
	//	} catch (RemoteException e) {
	//		De.bug("\n bb \n");
	//		e.printStackTrace();
	//	} catch (NotBoundException e) {
	//		De.bug("\n aa \n \n");
	//		e.printStackTrace();
	//	}
	}
	
	public void run() {
		while(true){
			int num = m_connectionobj.countExecQuery(QUERY);
			
			if(num > 0){
				ResultSet rs =m_connectionobj.RSExecQuery(QUERY);
				try {
					//TODO implement something like processing and finished to handle exeptions!
					while(rs.next()){
						
						int id = rs.getInt(1); 
						String action = rs.getString(2); 
						String table = rs.getString(3); 
						String pks = rs.getString(4); 
						String values = rs.getString(5); 
						java.sql.Date date = rs.getDate(6); 

						//TODO
						// boolean worked = m_mapper.execute(id, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getDate(6));
						//normally the remote obj should have this method! (but the rmi reg is not working)
						boolean worked = this.executeFAKE(id, action, table, pks, values, date);
						
						if(worked){
							m_connectionobj.RSExecUpdate("delete from logged where id ="+id);							
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} 
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean execute(int id, String action, String table, String pks,
			String values, Date date) throws RemoteException {
		return false;
	}
	
	public boolean executeFAKE(int id, String action, String table, String json_primaryKeys, String json_values, Date date) throws RemoteException {
		boolean ret = false;
		
		JsonReader jsonReader_keys = Json.createReader(new StringReader(json_primaryKeys));
		JsonObject primary_keys = jsonReader_keys.readObject();
		jsonReader_keys.close();

		JsonReader jsonReader_values = Json.createReader(new StringReader(json_values));
		JsonObject values = jsonReader_values.readObject();
		jsonReader_values.close();
		
		if(table.equalsIgnoreCase("Teilnehmer")){
			ret = executeTeilnehmer(action, primary_keys, values);
		}else if (table.equalsIgnoreCase("Veranstaltung")){
			ret = executeVeranstaltung(action,primary_keys,values);
		}else if (table.equalsIgnoreCase("Person")){
			ret = executePerson(action,primary_keys,values);
		}else if (table.equalsIgnoreCase("Abteilung")){
			ret = executeAbteilung(action,primary_keys,values);
		}
		
		return ret;
	}
	
	public boolean executeTeilnehmer(String action, JsonObject pks, JsonObject values){
		String old_name="", old_vname="",old_date="";
		String new_name="", new_vname="",new_date="";

		if (pks.size() != 0){
			 old_name = pks.getString("vorname") +" "+  pks.getString("nachname");
			 old_vname = pks.getString("vname");
			 old_date = (pks.getString("date"));
		}
		if (values.size() != 0){
			new_name = values.getString("vorname") +" "+  values.getString("nachname");
			new_vname = values.getString("vname");
			new_date = (values.getString("date"));
		}
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Besucher VALUES('"+new_name+"','"+new_vname+"',TO_DATE('"+new_date+"','DD.MM.YYYY'))";
		else if(action.equalsIgnoreCase("update"))	
			sql_string = "UPDATE Besucher SET name = '"+ new_name +"', vname =  '"+ new_vname +"', date =  '"+ new_date +"' WHERE name = '"+ old_name +"' AND vname = '"+ old_vname +"' AND date = '"+ old_date +"' ";
		else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Besucher WHERE name = '"+ old_name +"' AND vname = '"+ old_vname +"' AND date = '"+ old_date +"'"; 
		
		De.bug(sql_string);
		return true;
	} 
	
	
	public boolean executeVeranstaltung(String action, JsonObject pks, JsonObject values){
		String old_vname="",old_date="";
		int new_verpflichtend=0;
		String new_vname="",new_date="";
		int new_kosten=0;
		
		if (pks.size() != 0){
			 old_vname = pks.getString("vname");
			 old_date = (pks.getString("date"));
		}
		
		if (values.size() != 0){
			new_verpflichtend = values.getInt("verpflichtend");
			new_kosten = values.getInt("kosten");
			new_vname = values.getString("vname");
			new_date = (values.getString("date"));
		}
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Veranstaltung VALUES('"+new_vname+"',TO_DATE('"+new_date+"','DD.MM.YYYY'),"+new_verpflichtend+","+new_kosten+")";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Veranstaltung SET vname = '"+ new_vname +"', kosten = '"+ new_kosten +"', date =  TO_DATE('"+ new_date +"','DD.MM.YYYY'), verpflichtend =  '"+ new_verpflichtend +"' WHERE vname = '"+ old_vname +"' AND date = TO_DATE('"+ old_date +"','DD.MM.YYYY')";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Veranstaltung WHERE vname = '"+ old_vname +"' AND date = '"+ old_date +"'"; 
		
		De.bug(sql_string);
		return true;
	} 
	

	public boolean executePerson(String action, JsonObject pks, JsonObject values){
		String old_vorname="",old_nachname="",old_abteilung="",old_addresse="";
		String new_vorname="",new_nachname="",new_abteilung="",new_addresse="";
		
		if (pks.size() != 0){
			old_vorname = pks.getString("vorname");
			old_nachname = (pks.getString("nachname"));
		}
		
		if (values.size() != 0){
			new_vorname = values.getString("vorname");
			new_nachname = values.getString("nachname");
			new_abteilung = values.getString("aname");
			new_addresse = values.getString("addresse");
		}
		
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Person VALUES('"+new_vorname+"','"+new_nachname+"','"+new_abteilung+"','"+new_addresse+"')";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Person SET vorname = '"+ new_vorname +"', nachname = '"+ new_nachname +"', aname =  '"+ new_abteilung +"', addresse =  '"+ new_addresse +"' WHERE vorname = '"+ old_vorname +"' AND nachname = '"+ old_nachname+"')";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Person WHERE vorname = '"+ old_vorname +"' AND nachname = '"+ old_nachname +"'"; 
		
		De.bug(sql_string);
		return true;
	} 
	public boolean executeAbteilung(String action, JsonObject pks, JsonObject values){
		System.out.println("Abteilung can not be synchronised");
		return true;
	} 

}
