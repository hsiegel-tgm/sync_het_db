package postgres;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import remoteInterfaces.Mapper;
import start.De;

/**
 * The PostgresMapper is mapping from the global Schema into the pg Database
 * 
 * @author Hannah Siegel
 * @version 2014-11-14
 *
 */
public class PostgresMapper implements Mapper{
	//connection
	PostgresConnection m_connection;
	
	/**
	 * Constructor of the class
	 * 
	 * @param connection
	 */
	public PostgresMapper(PostgresConnection connection){
		m_connection = connection;
	}
	
	/* (non-Javadoc)
	 * @see remoteInterfaces.Mapper#execute(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	public boolean execute(String caller, int id, String action, String table, String json_primaryKeys, String json_values, Date date) throws RemoteException {
		boolean ret = false;
		
		//fetching values
		JsonReader jsonReader_keys = Json.createReader(new StringReader(json_primaryKeys));
		JsonObject primary_keys = jsonReader_keys.readObject();
		jsonReader_keys.close();

		//fetching values
		JsonReader jsonReader_values = Json.createReader(new StringReader(json_values));
		JsonObject values = jsonReader_values.readObject();
		jsonReader_values.close();
		
		//calling the right 'table'
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
	
	/**
	 * The method executeTeilnehmer is mapping every action on the Teilnehmer table
	 * 
	 * @param action
	 * @param old_values
	 * @param new_values
	 * @return true if mapping has worked, otherwise if not
	 */
	public boolean executeTeilnehmer(String action, JsonObject old_values, JsonObject new_values){
		String old_name="", old_vname="",old_date="";
		String new_name="", new_vname="",new_date="";

		if (old_values.size() != 0){
			 old_name = old_values.getString("name");
			 old_vname = old_values.getString("vname");
			 old_date = (old_values.getString("date"));
		}
		if (new_values.size() != 0){
			new_name = new_values.getString("name");
			new_vname = new_values.getString("vname");
			new_date = (new_values.getString("date"));
		}
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Besucher VALUES('"+new_name+"','"+new_vname+"',TO_DATE('"+new_date+"','YYYY-MM-DD'),'current')";
		else if(action.equalsIgnoreCase("update"))	
			sql_string = "UPDATE Besucher SET sync = 'current', name = '"+ new_name +"', vname =  '"+ new_vname +"', date =  '"+ new_date +"' WHERE name = '"+ old_name +"' AND vname = '"+ old_vname +"' AND date = '"+ old_date +"' ";
		else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Besucher WHERE name = '"+ old_name +"' AND vname = '"+ old_vname +"' AND date = '"+ old_date +"'"; 
		
		return m_connection.execUpdate(sql_string);
	} 
	
	/**
	 * The method executeVeranstaltung is mapping every action on the Veranstaltung table
	 * 
	 * @param action
	 * @param old_values
	 * @param new_values
	 * @return true if mapping has worked, otherwise if not
	 */
	public boolean executeVeranstaltung(String action, JsonObject old_values, JsonObject new_values){
		String old_vname="",old_date="";
		boolean new_verpflichtend=false;
		String new_vname="",new_date="";
		int new_kosten=0;
		
		if (old_values.size() != 0){
			 old_vname = old_values.getString("vname");
			 old_date = (old_values.getString("date"));
		}
		
		if (new_values.size() != 0){
			new_verpflichtend = convert_to_postgres_boolean(new_values, "verpflichtend");
			De.bug("postgres bool:"+new_verpflichtend);
			new_kosten = new_values.getInt("kosten");
			new_vname = new_values.getString("vname");
			new_date = (new_values.getString("date"));
		}
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Veranstaltung VALUES('"+new_vname+"',TO_DATE('"+new_date+"','YYYY-MM-DD'),"+new_verpflichtend+","+new_kosten+",'current')";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Veranstaltung SET sync = 'current', vname = '"+ new_vname +"', kosten = "+ new_kosten +", date =  TO_DATE('"+ new_date +"','DD.MM.YYYY'), verpflichtend =  "+ new_verpflichtend +" WHERE vname = '"+ old_vname +"' AND date = TO_DATE('"+ old_date +"','DD.MM.YYYY')";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "DELETE FROM Veranstaltung WHERE vname = '"+ old_vname +"' AND date = '"+ old_date +"'"; 
		
		return m_connection.execUpdate(sql_string);

	} 
	
	/**
	 * The method executePerson is mapping every action on the Mitarbeiter table
	 * 
	 * @param action
	 * @param old_values
	 * @param new_values
	 * @return true if mapping has worked, otherwise if not
	 */
	public boolean executePerson(String action, JsonObject old_values, JsonObject new_values){
		String old_name="";
		String new_name="",new_abteilung="",new_addresse="";
		
		if (old_values.size() != 0){
			old_name = old_values.getString("name");
		}
		
		if (new_values.size() != 0){
			new_name = new_values.getString("name");
			new_abteilung = new_values.getString("aname");
			new_addresse = new_values.getString("addresse");
		}
		
		String sql_string="";
		
		if(action.equalsIgnoreCase("insert"))
			sql_string = "INSERT INTO Mitarbeiter VALUES('"+new_name+"','"+new_abteilung+"','current')";
		else if(action.equalsIgnoreCase("update"))	{
			sql_string = "UPDATE Mitarbeiter SET sync = 'current', name = '"+ new_name+"', abteilung =  '"+ new_abteilung +"' WHERE name = '"+ old_name+"'";
		}else if(action.equalsIgnoreCase("delete"))	
			sql_string = "UPDATE Mitarbeiter SET sync = 'deleting' WHERE name = '"+ old_name +"';DELETE FROM Mitarbeiter WHERE name = '"+ old_name +"'"; 
		
		return m_connection.execUpdate(sql_string);
	} 
	
	public boolean executeAbteilung(String action, JsonObject pks, JsonObject values){
		System.out.println("Abteilung can not be synchronised");
		return true;
	}
	
	/**
	 * The method is fetching the right type of value from the global schema and is converting it into the postgres format.
	 * 
	 * @param values
	 * @param name
	 * @return
	 */
	public boolean convert_to_postgres_boolean(JsonObject values, String name){
		try{
			return values.getBoolean(name);
		}catch(Exception e){
			try{
				int v = values.getInt(name);
				if(v == 1)
					return true;
				else
					return false;
			}
			catch(Exception e2){
				return false;
			}
		}
	}

}
