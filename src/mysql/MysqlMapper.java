package mysql;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import remoteInterfaces.Mapper;
import start.De;

public class MysqlMapper implements Mapper{
	private MysqlConnection m_connection;
	HashMap<Integer,DBUpdate> m_updates = new HashMap<Integer,DBUpdate>();
	
	public MysqlMapper(MysqlConnection connection){
		m_connection = connection;
	}
	
	public boolean execute(String caller,int id, String action, String table, String old_values, String new_values, Date date) throws RemoteException {
		boolean ret = false;
		JsonReader jsonReader_keys = Json.createReader(new StringReader(old_values));
		JsonObject json_old_values = jsonReader_keys.readObject();
		jsonReader_keys.close();

		JsonReader jsonReader_values = Json.createReader(new StringReader(new_values));
		JsonObject json_new_values = jsonReader_values.readObject();
		jsonReader_values.close();
		
		if(table.equalsIgnoreCase("Teilnehmer")){
			ret = executeTeilnehmer(action, json_old_values, json_new_values);
		}
		
		else if (table.equalsIgnoreCase("Veranstaltung")){
			ret = executeVeranstaltung(action,json_old_values,json_new_values);
		}
		
		else if (table.equalsIgnoreCase("Person")){
			ret = executePerson(action,json_old_values,json_new_values);
		}
		else if (table.equalsIgnoreCase("Abteilung")){
			ret = executeAbteilung(action,json_old_values,json_new_values);
		}

		return ret; 
	}
	
	/**
	 * The method processName is cutting a name string into firstname and lastname:
	 * 		E.g:
	 * 			Hannah Katharina Siegel -> {Siegel, Hannah Katharina}
	 * 			Susi Muster				->  {Muster, Susi}
	 * 
	 * 
	 * @param str - String which should be cutted
	 * @returns String[] like {lastname, firstname}
	 */
	public String[] processName(String str){
		String [] ret = new String[2];
		String[] splited = str.split("\\s+");
		ret[0] = splited[(splited.length-1)].trim();
		ret[1] = str.substring(0,(str.length()-ret[0].length())).trim();
		return ret;
	}
	
	public boolean executeTeilnehmer(String action, JsonObject pks, JsonObject values){
		String old_vorname="",old_nachname="", old_vname="",old_date="";
		String new_vorname="",new_nachname="",  new_vname="",new_date="";

		if (pks.size() != 0){
			String name = pks.getString("name");
			String[] splited = processName(name);
			old_nachname = splited[0];
			old_vorname = splited[1];

			 old_vname = pks.getString("vname");
			 old_date = (pks.getString("date"));
		}
		
		if (values.size() != 0){
			String name = values.getString("name");
			String[] splited = processName(name);
			new_nachname = splited[0];
			new_vorname = splited[1];
			new_vname = values.getString("vname");
			new_date = (values.getString("date"));
		}
		
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Teilnehmer VALUES('"+new_vorname+"','"+new_nachname+"','"+new_vname+"','"+new_date+"','current')";
		else if(action.equalsIgnoreCase("update"))
			sql_string = "UPDATE Teilnehmer SET sync_state = 'current', vorname = '"+ new_vorname +"', nachname = '" +new_nachname+"' , vname =  '"+ new_vname +"', date =  '"+ new_date +"' WHERE vorname = '"+ old_vorname +"' AND nachname = '"+old_nachname+"' AND vname = '"+ old_vname +"' AND date = '"+ old_date +"' ";
		else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Teilnehmer WHERE vorname = '"+ old_vorname +"' AND nachname = '"+old_nachname+ "' AND vname = '"+ old_vname +"' AND date = '"+ old_date +"'"; 
		
		if(action.equalsIgnoreCase("update")){
			String old_sql_string = "INSERT INTO Teilnehmer VALUES('"+old_vorname+"','"+old_nachname+"','"+old_vname+"','"+old_date+"','old')";
			m_connection.execUpdate(old_sql_string);
		}
		
		De.bug(sql_string);
		return m_connection.execUpdate(sql_string);
	} 
	
	public boolean executeVeranstaltung(String action, JsonObject pks, JsonObject values){
		String old_vname="",old_date="";
		
		boolean new_verpflichtend=false,old_verpflichtend=false;
		String new_vname="",new_date="";
		int new_kosten=0, old_kosten=0;
		
		if (pks.size() != 0){
			 old_vname = pks.getString("vname");
			 old_date = (pks.getString("date"));
			 old_verpflichtend = pks.getBoolean("verpflichtend");
			old_kosten = values.getInt("kosten");

		}
		
		if (values.size() != 0){
			new_verpflichtend = values.getBoolean("verpflichtend");
			new_kosten = values.getInt("kosten");
			new_vname = values.getString("vname");
			new_date = (values.getString("date"));
		}
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Veranstaltung VALUES('"+new_vname+"','"+new_date+"',"+new_verpflichtend+","+new_kosten+",'current')";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Veranstaltung SET sync_state = 'current', vname = '"+ new_vname +"', kosten = "+ new_kosten +", date = '"+ new_date +"', verpflichtend =  "+ new_verpflichtend +" WHERE vname = '"+ old_vname +"' AND date = '"+ old_date +"' AND sync_state='current'";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Veranstaltung WHERE vname = '"+ old_vname +"' AND date = '"+ old_date +"'"; 
		
		if(action.equalsIgnoreCase("update")){
			String old_sql_string = "INSERT INTO Veranstaltung VALUES('"+old_vname+"','"+old_date+"','"+old_verpflichtend+"','"+old_kosten+"','old')";
			m_connection.execUpdate(old_sql_string);
		}
		
		De.bug(sql_string);
		return m_connection.execUpdate(sql_string);
	} 

	public boolean executePerson(String action, JsonObject pks, JsonObject values){
		String old_vorname="",old_nachname="",old_abteilung="",old_addresse="";
		String new_vorname="",new_nachname="",new_abteilung="",new_addresse="";
		
		if (pks.size() != 0){
			try{
				String name = pks.getString("name");
				String[] splited = processName(name);
				old_nachname = splited[0];
				old_vorname = splited[1];	
				old_abteilung =  pks.getString("aname");
				old_addresse = pks.getString("addresse");
			}catch(Exception e){
				System.out.println("hier:"+pks.toString());
			}
		}
		
		if (values.size() != 0){
			String[] splited=null;
			try{
				String name = values.getString("name");
				splited = processName(name);
				new_nachname = splited[0];
				new_vorname = splited[1];
				new_abteilung = values.getString("aname");
				new_addresse = values.getString("addresse");
			}catch(java.lang.NullPointerException npe){
				System.out.println("hier:"+values.toString());
				De.bug(splited.toString());
				//TODO
			}
		}
		
		if(!(m_connection.isInDB("Abteilung", "aname" , new_abteilung))){
			m_connection.execUpdate("INSERT INTO Abteilung VALUES ('"+new_abteilung+"','current')");
		}
		
		
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Person VALUES('"+new_vorname+"','"+new_nachname+"','"+new_abteilung+"','"+new_addresse+"','current')";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Person SET sync_state = 'syncing', vorname = '"+ new_vorname +"', nachname = '"+ new_nachname +"', aname =  '"+ new_abteilung +"', addresse =  '"+ new_addresse +"' WHERE vorname = '"+ old_vorname +"' AND nachname = '"+ old_nachname+"' AND sync_state='current'";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Person WHERE vorname = '"+ old_vorname +"' AND nachname = '"+ old_nachname +"'"; 
		
		
		if(action.equalsIgnoreCase("update")){
			String old_sql_string = "INSERT INTO Person VALUES('"+old_vorname+"','"+old_nachname+"','"+old_abteilung+"','"+old_addresse+"','old')";
			m_connection.execUpdate(old_sql_string);
			m_updates.put(4, new DBUpdate("Person", "vorname ='"+old_vorname+"' AND nachname= '"+old_nachname+"'", "vorname ='"+new_vorname+"' AND nachname= '"+new_nachname+"'"));

		}
		
		De.bug(sql_string);
		return m_connection.execUpdate(sql_string);
	} 
	
	public boolean executeAbteilung(String action, JsonObject pks, JsonObject values){
		String old_aname="";
		String new_aname="";
		
		if (pks.size() != 0){
			old_aname = pks.getString("name");
		}
		
		if (values.size() != 0){
			new_aname = values.getString("aname");
		}
		
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Abteilung VALUES('"+new_aname+"','current')";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Abteilung SET sync_state = 'current', aname = '"+ new_aname +"' WHERE aname = '"+ old_aname +"')";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Abteilung WHERE aname = '"+ old_aname +"'"; 
		
		De.bug(sql_string);
		return m_connection.execUpdate(sql_string);
	} 
	
	

	
	public boolean revertUpdate(int id){
		String sql = m_updates.get(id).revertUpdate();
		return m_connection.execUpdate(sql);
	}
	
	public boolean approveUpdate(int id){
		String sql = m_updates.get(id).approveUpdate();
		return m_connection.execUpdate(sql);
	}
}
